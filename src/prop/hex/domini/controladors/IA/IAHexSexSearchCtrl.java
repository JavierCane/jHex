package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IA.auxiliars.ElementTaulaTransposicions;
import prop.hex.domini.controladors.IA.auxiliars.FitesDePoda;
import prop.hex.domini.controladors.IA.auxiliars.ResistenciaCasella;
import prop.hex.domini.controladors.IA.auxiliars.TwoDistance;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.util.*;

/**
 * Intel·ligència artificial per a Hex basat en negaScout amb ordenació de moviments segons la funció de cost de
 * QueenBee.
 * <p/>
 * La funció de cost s'utilitza com a heurística per a ordenar els moviments en l'arbre de cerca del negaScout
 * <p/>
 * La funció que ordena els moviments prové de
 * <a href="http://javhar.net/AreBeesBetterThanFruitfliesThesis.pdf">QueenBee</a>,
 * de <a href="http://javhar.net/">Jack van Rijswijck</a>.
 *
 * @author Isaac Sánchez Barrera (Grup 7.3, Hex)
 */
public final class IAHexSexSearchCtrl extends InteligenciaArtificialHexCtrl
{

	/**
	 * Tauler on es desenvolupa la partida
	 */
	private TaulerHex tauler;

	/**
	 * Pressupost que hi ha per defecte
	 */
	private static int pressupost_defecte = 70;

	/**
	 * Pressupost per a la crida al negaScout en curs
	 */
	private int pressupost;

	/**
	 * Profunditat per defecte. Cada \(1.3 \cdot Mida_{tauler}\) iteracions s'incrementa en una unitat.
	 */
	private int profunditat_defecte = 3;

	/**
	 * Profunditat màxima.
	 */
	private int profunditat_maxima;

	/**
	 * Taula de transposicions
	 */
	private HashMap<Integer, ElementTaulaTransposicions> taula_transposicions;

	/**
	 * <em>Two distance</em> per al jugador A. S'utilitza per aprofitar els càlculs entre la funció d'avaluació i
	 * l'heurística dels moviments.
	 */
	private TwoDistance two_distance_a;

	/**
	 * <em>Two distance</em> per al jugador B. S'utilitza per aprofitar els càlculs entre la funció d'avaluació i
	 * l'heurística dels moviments.
	 */
	private TwoDistance two_distance_b;

	/**
	 * Constructor per defecte. Genera la taula de transposicions.
	 */
	public IAHexSexSearchCtrl()
	{
		taula_transposicions = new HashMap<Integer, ElementTaulaTransposicions>();
	}

	/**
	 * Retorna un conjunt (ordenat) dels moviments possibles.
	 * <p/>
	 * Aquests moviments estan ordenats segons la fórmula de cost de QueenBee. Si \(p\) és una casella,
	 * el seu cost o resistència és
	 * \[
	 * c(p) = \left\{
	 * \begin{array}{ll}
	 * -V^{*} & \text{si } V(p) = 0 \\
	 * V(p)  & \text{altrament}
	 * \end{array}\right.
	 * \]
	 * on \(V^{*}\) és el potencial del segon millor moviment. Si hi ha més d'un moviment amb potencial nul,
	 * aleshores \(V^{*} = 0\).
	 *
	 * @param fitxa_jugador Fitxa del jugador per qui es vol calcular el moviment
	 * @return El conjunt de caselles amb potencial mínim
	 */
	private Set<ResistenciaCasella> movimentsOrdenats( EstatCasella fitxa_jugador )
	{
		Set<ResistenciaCasella> moviments_ordenats = new TreeSet<ResistenciaCasella>();

		TwoDistance two_distance = two_distance_a;
		if ( fitxa_jugador == EstatCasella.JUGADOR_B )
		{
			two_distance = two_distance_b;
		}

		if ( two_distance == null )
		{
			two_distance = new TwoDistance( tauler, fitxa_jugador );
		}

		int[][] potencials = two_distance.getPotencials();

		for ( int fila = 0; fila < tauler.getMida(); fila++ )
		{
			for ( int columna = 0; columna < tauler.getMida(); columna++ )
			{
				Casella casella = new Casella( fila, columna );

				if ( tauler.esMovimentValid( fitxa_jugador, casella ) )
				{
					int potencial_moviment = potencials[fila][columna];
					if ( potencial_moviment == 0 )
					{
						potencial_moviment -= two_distance.getPotencialMinim( casella );
					}
					moviments_ordenats.add( new ResistenciaCasella( casella, potencial_moviment ) );
				}
			}
		}

		return moviments_ordenats;
	}

	/**
	 * Consulta la fitxa de l'oponent
	 *
	 * @param original Fitxa de qui es vol consultar l'oponent
	 * @return La fitxa de l'oponent
	 */
	private static EstatCasella fitxaContraria( EstatCasella original )
	{
		if ( original == EstatCasella.JUGADOR_A )
		{
			return EstatCasella.JUGADOR_B;
		}
		else
		{
			return EstatCasella.JUGADOR_A;
		}
	}

	/**
	 * Realitza un seguit d'iteracions de l'algorisme negaScout amb nodes ordenats seguint l'heurística de cost de
	 * QueenBee.
	 * <p/>
	 * L'algorisme deixa de fer crides recursives quan se sobrepassa la profunditat màxima o el pressupost establerts.
	 * De tots els moviments possibles en una determinada profunditat, només visita els més propers al moviment amb
	 * potencial més petit.
	 *
	 * @param jugador        Fitxa del jugador que ha efectuat el darrer moviment
	 * @param contrincant    Fitxa del jugador contrincant
	 * @param alfa           Valor del paràmetre alfa a maximitzar en la darrera iteració recursiva
	 * @param beta           Valor del paràmetre alfa a minimitzar en la darrera iteració recursiva
	 * @param profunditat    Profunditat a què s'ha arribat
	 * @param cost           Cost de la darrera iteració recursiva
	 * @param estat_iteracio Estat de la partida en la darrera iteració
	 * @return El valor avaluat del moviment a la casella per al jugador que efectua el moviment
	 * @see #movimentsOrdenats(EstatCasella)
	 * @see ElementTaulaTransposicions
	 * @see FitesDePoda
	 */
	private int sexSearch( EstatCasella jugador, EstatCasella contrincant, int alfa, int beta, int profunditat,
	                       int cost, EstatPartida estat_iteracio )
	{
		int beta_2, puntuacio;
		if ( cost >= pressupost || profunditat >= profunditat_maxima || estat_iteracio != EstatPartida.NO_FINALITZADA )
		{
			puntuacio = funcioAvaluacio( tauler, estat_iteracio, profunditat, jugador );
			taula_transposicions.put( tauler.hashCode(),
					new ElementTaulaTransposicions( partida.getTornsJugats() + profunditat, FitesDePoda.VALOR_EXACTE,
							puntuacio, jugador ) );
			return puntuacio;
		}

		if ( taula_transposicions.containsKey( tauler.hashCode() ) )
		{
			ElementTaulaTransposicions element = taula_transposicions.get( tauler.hashCode() );
			if ( element.getProfunditat() >= partida.getTornsJugats() + profunditat )
			{
				switch ( element.getFita( jugador ) )
				{
					case FITA_INFERIOR:
						alfa = Math.max( alfa, element.getPuntuacio( jugador ) );
						break;
					case FITA_SUPERIOR:
						beta = Math.min( beta, element.getPuntuacio( jugador ) );
						break;
					case VALOR_EXACTE:
						return element.getPuntuacio( jugador );
				}

				if ( alfa >= beta )
				{
					return alfa;
				}
			}
		}

		beta_2 = beta;
		boolean primer_fill = true;
		FitesDePoda fita = FitesDePoda.FITA_SUPERIOR;
		Set<ResistenciaCasella> moviments_ordenats = movimentsOrdenats( contrincant );
		int resistencia_minima = moviments_ordenats.iterator().next().getResistencia();

		Iterator<ResistenciaCasella> moviments = moviments_ordenats.iterator();
		ResistenciaCasella resistencia_actual;

		while ( moviments.hasNext() &&
		        ( resistencia_actual = moviments.next() ).getResistencia() <= resistencia_minima + 2 )
		{
			Casella actual = resistencia_actual.getCasella();
			tauler.mouFitxa( contrincant, actual );
			estat_iteracio = partida.comprovaEstatPartida( actual.getFila(), actual.getColumna() );
			puntuacio = -sexSearch( contrincant, jugador, -beta_2, -alfa, profunditat + 1,
					cost + resistencia_actual.getResistencia(), estat_iteracio );

			if ( alfa < puntuacio && puntuacio < beta && !primer_fill )
			{
				fita = FitesDePoda.VALOR_EXACTE;
				puntuacio = -sexSearch( contrincant, jugador, -beta, -alfa, profunditat + 1,
						cost + resistencia_actual.getResistencia(), estat_iteracio );
			}

			tauler.treuFitxa( actual );

			if ( alfa < puntuacio )
			{
				fita = FitesDePoda.VALOR_EXACTE;
				alfa = puntuacio;
			}

			if ( alfa >= beta )
			{
				taula_transposicions.put( tauler.hashCode(),
						new ElementTaulaTransposicions( partida.getTornsJugats() + profunditat,
								FitesDePoda.FITA_INFERIOR, alfa, jugador ) );
				return alfa;
			}

			beta_2 = alfa + 1;
			primer_fill = false;
		}

		taula_transposicions.put( tauler.hashCode(),
				new ElementTaulaTransposicions( partida.getTornsJugats() + profunditat, fita, alfa, jugador ) );

		return alfa;
	}

	/**
	 * Funció d'avaluació del MiniMax, si estem en un estat termianl on ja ha guanyat un jugador retornem 1000000 o
	 * -1000000, que son valors prou grans, sinó, apliquem l'estratègia agresiva o la passiva en funció del valor de
	 * la variable tactica_agresiva.
	 *
	 * @param tauler         Objecte de la classe <code>Tauler</code> sobre el qual es disputa una partida.
	 * @param estat_moviment Descriu en quin estat ha quedat <em>tauler</em> en funció de l'últim moviment efectuat
	 *                       sobre aquest.
	 * @param profunditat    És la profunditat a la que s'ha arribat durant l'exploració de les diferents possibilitats de
	 *                       moviment. Cada unitat de <em>profunditat</em> representa un torn jugat de la partida.
	 * @param fitxa_jugador  Indica el jugador de la partida a partir del qual avaluar <em>tauler</em>.
	 * @return La puntuació de l'evaluació
	 */
	public int funcioAvaluacio( Tauler tauler, EstatPartida estat_moviment, int profunditat,
	                            EstatCasella fitxa_jugador )
	{

		int retorn;

		if ( estat_moviment == EstatPartida.GUANYA_JUGADOR_A )
		{
			if ( fitxa_jugador == EstatCasella.JUGADOR_A )
			{
				return 1000000;
			}
			else
			{
				return -1000000;
			}
		}
		else if ( estat_moviment == EstatPartida.GUANYA_JUGADOR_B )
		{
			if ( fitxa_jugador == EstatCasella.JUGADOR_B )
			{
				return 1000000;
			}
			else
			{
				return -1000000;
			}
		}

		two_distance_a = new TwoDistance( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );
		two_distance_b = new TwoDistance( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );
		int potencial_a = two_distance_a.getPotencial();
		int potencial_b = two_distance_b.getPotencial();
		int desempat_a = two_distance_a.getPotencialsMinims();
		int desempat_b = two_distance_b.getPotencialsMinims();

		if ( fitxa_jugador == EstatCasella.JUGADOR_A )
		{
			retorn = 100 * ( potencial_b - potencial_a ) + desempat_b - desempat_a;
		}
		else
		{
			retorn = 100 * ( potencial_a - potencial_b ) + desempat_a - desempat_b;
		}

		return retorn;
	}

	/**
	 * Retorna un moviment adequat al tauler actual per al jugador indicat per fitxa.
	 * <p/>
	 * Fa servir un algorisme negaScout amb taules de transposició i amb anàlisi dels moviments ordenats segons
	 * l'heurística de la funció de cost de QueenBee.
	 *
	 * @param fitxa Fitxa que vol col·locar-se al tauler de la partida del paràmetre implícit.
	 * @return La casella on es mouria la fitxa.
	 * @see #sexSearch(EstatCasella, EstatCasella, int, int, int, int, EstatPartida)
	 * @see #movimentsOrdenats(EstatCasella)
	 * @see InteligenciaArtificialHexCtrl
	 */
	public Casella obteMoviment( EstatCasella fitxa )
	{
		if ( partida.getTornsJugats() <= 1 )
		{
			Casella obertura = obertura();
			if ( obertura != null )
			{
				return obertura;
			}
		}

		tauler = partida.getTauler();

		int puntuacio_millor = Integer.MIN_VALUE + 1;

		Set<ResistenciaCasella> moviments_ordenats = movimentsOrdenats( fitxa );
		ArrayList<Casella> millors_moviments = new ArrayList<Casella>();

		if ( tauler.getTotalFitxes() % ( ( int ) ( 1.3 * tauler.getMida() ) ) == 0 && partida.getTornsJugats() != 0 )
		{
			profunditat_maxima++;
		}

		int resistencia_minima = moviments_ordenats.iterator().next().getResistencia();
		for ( ResistenciaCasella resistencia_actual : moviments_ordenats )
		{
			Casella actual = resistencia_actual.getCasella();
			tauler.mouFitxa( fitxa, actual );
			pressupost = Math.max( pressupost_defecte, resistencia_actual.getResistencia() + 1 );
			if ( partida.getTornsJugats() < 3 )
			{
				profunditat_maxima = 3;
			}
			else if ( resistencia_actual.getResistencia() >= resistencia_minima + 3 )
			{
				profunditat_maxima = 3;
			}
			else
			{
				profunditat_maxima = profunditat_defecte;
			}

			int puntuacio_actual =
					sexSearch( fitxa, fitxaContraria( fitxa ), Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1, 1,
							resistencia_actual.getResistencia(),
							partida.comprovaEstatPartida( actual.getFila(), actual.getColumna() ) );

			tauler.treuFitxa( actual );

			if ( puntuacio_actual > puntuacio_millor )
			{
				puntuacio_millor = puntuacio_actual;
				millors_moviments = new ArrayList<Casella>();
				millors_moviments.add( actual );
			}
			else if ( puntuacio_actual == puntuacio_millor )
			{
				millors_moviments.add( actual );
			}
		}

		two_distance_a = two_distance_b = null;

		return millors_moviments.get( new Random().nextInt( millors_moviments.size() ) );
	}
}
