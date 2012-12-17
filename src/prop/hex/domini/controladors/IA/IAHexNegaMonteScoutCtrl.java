package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IA.auxiliars.ElementTaulaTransposicions;
import prop.hex.domini.controladors.IA.auxiliars.FitesDePoda;
import prop.hex.domini.controladors.IA.auxiliars.TwoDistance;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;

import java.util.HashMap;
import java.util.Random;

/**
 * Intel·ligència artificial per al joc Hex que utilitza un mètode negaScout amb Monte Carlo.
 * <p/>
 * Utilitza un mètode NegaScout amb elecció de moviments a avaluar amb un mètode de
 * Monte Carlo equiprobable. A més, guarda els valors de les avaluacions de la partida en joc en una taula de
 * transposicions, on els resums (<em>hash</em> en anglès) de les avaluacions vénen donats per <a
 * href="http://en.wikipedia.org/wiki/Zobrist_hashing">claus Zobrist</a>.
 * <p/>
 * Les avaluacions estàtiques del tauler fan servir la funció d'avaluació
 * <a href="http://javhar.net/AreBeesBetterThanFruitfliesThesis.pdf">QueenBee</a>,
 * de <a href="http://javhar.net/">Jack van Rijswijck</a>.
 *
 * @author Isaac Sánchez Barrera (Grup 7.3, Hex)
 */
public final class IAHexNegaMonteScoutCtrl extends InteligenciaArtificialHexCtrl
{

	/**
	 * Tauler on s'està desenvolupant la partida.
	 */
	private TaulerHex tauler;

	/**
	 * Profunditat màxima de l'arbre de cerca.
	 */
	private int profunditat_maxima = 3;

	/**
	 * Taula de transposicions. No té en compte possibles col·lisions.
	 *
	 * @see TaulerHex#hashCode()
	 */
	private HashMap<Integer, ElementTaulaTransposicions> taula_transposicions;

	/**
	 * Generador de nombres pseudo-aleatoris per a l'elecció dels moviments a avaluar.
	 */
	private Random generador;

	/**
	 * Constructor per defecte. Inicialitza la taula de transposicions i el generador dels nombres pseudo-aleatoris.
	 */
	public IAHexNegaMonteScoutCtrl()
	{
		taula_transposicions = new HashMap<Integer, ElementTaulaTransposicions>();
		generador = new Random();
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
	 * Realitza un seguit d'iteracions de l'algorisme negaScout amb nodes aleatoris fent servir la funció d'avaluació
	 * de QueensBee.
	 * <p/>
	 * La quantitat de nodes que s'avaluen depèn de la mida del tauler i les caselles buides. Les caselles que
	 * s'acaben avaluant són escollides de manera aleatòria d'entre totes les buides. L'avaluació final s'acaba
	 * afegint a la taula de transposicions, amb la fita corresponent.
	 *
	 * @param jugador        Fitxa del jugador que ha efectuat el darrer moviment
	 * @param contrincant    Fitxa del jugador contrincant
	 * @param alfa           Valor del paràmetre alfa a maximitzar en la darrera iteració recursiva
	 * @param beta           Valor del paràmetre alfa a minimitzar en la darrera iteració recursiva
	 * @param profunditat    Profunditat a què s'ha arribat
	 * @param estat_iteracio Estat de la partida en la darrera iteració
	 * @return El valor avaluat del moviment a la casella per al jugador que efectua el moviment
	 * @see ElementTaulaTransposicions
	 * @see FitesDePoda
	 * @see PartidaHex#comprovaEstatPartida(int, int)
	 */
	private int negaMonteScout( EstatCasella jugador, EstatCasella contrincant, int alfa, int beta, int profunditat,
	                            EstatPartida estat_iteracio )
	{
		int beta_2, puntuacio;
		if ( profunditat == profunditat_maxima || estat_iteracio != EstatPartida.NO_FINALITZADA )
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

		int caselles_restants = tauler.getMida() * tauler.getMida() - tauler.getTotalFitxes();
		int max_moviments = Math.max( caselles_restants / ( int ) ( Math.sqrt( tauler.getMida() ) * 0.85 ), 7 );

		beta_2 = beta;
		boolean primer_fill = true;
		FitesDePoda fita = FitesDePoda.FITA_SUPERIOR;
		boolean[][] explorades = new boolean[tauler.getMida()][tauler.getMida()];
		int num_explorades = 0;
		while ( num_explorades < max_moviments && num_explorades < caselles_restants )
		{
			Casella actual =
					new Casella( generador.nextInt( tauler.getMida() ), generador.nextInt( tauler.getMida() ) );
			if ( tauler.esMovimentValid( contrincant, actual ) && !explorades[actual.getFila()][actual.getColumna()] )
			{
				explorades[actual.getFila()][actual.getColumna()] = true;
				num_explorades++;
				tauler.mouFitxa( contrincant, actual );
				estat_iteracio = partida.comprovaEstatPartida( actual.getFila(), actual.getColumna() );
				puntuacio = -negaMonteScout( contrincant, jugador, -beta_2, -alfa, profunditat + 1, estat_iteracio );

				if ( alfa < puntuacio && puntuacio < beta && !primer_fill )
				{
					fita = FitesDePoda.VALOR_EXACTE;
					puntuacio = -negaMonteScout( contrincant, jugador, -beta, -alfa, profunditat + 1, estat_iteracio );
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

		TwoDistance distancia_a = new TwoDistance( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );
		TwoDistance distancia_b = new TwoDistance( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );

		int potencial_a = distancia_a.getPotencial();
		int potencial_b = distancia_b.getPotencial();
		int desempat_a = distancia_a.getNombrePotencialsMinims();
		int desempat_b = distancia_b.getNombrePotencialsMinims();

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
	 * Fa servir un algorisme negaScout amb taules de transposició i elecció d'un subconjunt de nodes aleatoris
	 * (Monte Carlo amb elecció equiprobable).
	 *
	 * @param fitxa Fitxa que vol col·locar-se al tauler de la partida del paràmetre implícit.
	 * @return La casella on es mouria la fitxa.
	 * @see #negaMonteScout(EstatCasella, EstatCasella, int, int, int, EstatPartida)
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
		Casella millor_moviment = null;

		int caselles_restants = tauler.getMida() * tauler.getMida() - tauler.getTotalFitxes();
		if ( partida.getTornsJugats() < 2 )
		{
			caselles_restants--;
		}
		else if ( tauler.getTotalFitxes() % ( ( int ) ( 2.3 * tauler.getMida() ) ) == 0 &&
		          partida.getTornsJugats() != 0 )
		{
			profunditat_maxima++;
		}

		int max_moviments = Math.max( caselles_restants / ( int ) ( Math.sqrt( tauler.getMida() ) * 0.6 ), 7 );
		boolean[][] explorades = new boolean[tauler.getMida()][tauler.getMida()];
		int num_explorades = 0;
		while ( num_explorades < max_moviments && num_explorades < caselles_restants )
		{
			Casella actual =
					new Casella( generador.nextInt( tauler.getMida() ), generador.nextInt( tauler.getMida() ) );
			if ( tauler.esMovimentValid( fitxa, actual ) && !explorades[actual.getFila()][actual.getColumna()] )
			{
				explorades[actual.getFila()][actual.getColumna()] = true;
				num_explorades++;

				tauler.mouFitxa( fitxa, actual );
				int puntuacio_actual =
						negaMonteScout( fitxa, fitxaContraria( fitxa ), Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1, 1,
                                partida.comprovaEstatPartida( actual.getFila(), actual.getColumna() ) );

				tauler.treuFitxa( actual );

				if ( puntuacio_actual > puntuacio_millor )
				{
					puntuacio_millor = puntuacio_actual;
					millor_moviment = actual;
				}
			}
		}

		return millor_moviment;
	}
}
