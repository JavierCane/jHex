package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IA.auxiliars.ElementTaulaTransposicions;
import prop.hex.domini.controladors.IA.auxiliars.ResistenciaCasella;
import prop.hex.domini.controladors.IA.auxiliars.TwoDistance;
import prop.hex.domini.controladors.IA.auxiliars.enums.FitesDePoda;
import prop.hex.domini.controladors.InteligenciaArtificialHex;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: isaacsanchezbarrera
 * Date: 02/12/2012
 * Time: 17:45
 * To change this template use File | Settings | File Templates.
 */
public final class IAHexSexSearchCtrl extends InteligenciaArtificialHex
{

	private TaulerHex tauler;
	private static int pressupost = 5;
	private static int profunditat_max = 5;
	private HashMap<Integer, ElementTaulaTransposicions> taula_transposicio;

	/**
	 * Constructor per defecte. Genera la taula de transposició si no existeix.
	 */
	public IAHexSexSearchCtrl()
	{
		taula_transposicio = new HashMap<Integer, ElementTaulaTransposicions>();
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

	private Set<ResistenciaCasella> movimentsOrdenats( EstatCasella fitxa_jugador )
	{
		Set<ResistenciaCasella> moviments_ordenats = new TreeSet<ResistenciaCasella>();
		TwoDistance two_distance = new TwoDistance( tauler, fitxa_jugador );
		int[][] potencials = two_distance.getPotencials();

		for ( int fila = 0; fila < tauler.getMida(); fila++ )
		{
			for ( int columna = 0; columna < tauler.getMida(); columna++ )
			{
				Casella casella = new Casella( fila, columna );

				if ( tauler.esMovimentValid( fitxa_jugador, casella ) )
				{
					int potencial_moviment = potencials[fila][columna] - two_distance.getPotencialMinim( casella );
					moviments_ordenats.add( new ResistenciaCasella( casella, potencial_moviment ) );
				}
			}
		}

		return moviments_ordenats;
	}

	/**
	 * Realitza un seguit d'iteracions de l'algorisme negaScout amb nodes aleatoris fent servir la funció d'avaluació
	 * de QueensBee.
	 *
	 * @param casella        Casella del darrer moviment
	 * @param jugador        Fitxa del jugador que ha efectuat el darrer moviment
	 * @param contrincant    Fitxa del jugador contrincant
	 * @param alfa           Valor del paràmetre alfa a maximitzar en la darrera iteració recursiva
	 * @param beta           Valor del paràmetre alfa a minimitzar en la darrera iteració recursiva
	 * @param profunditat    Profunditat a què s'ha arribat
	 * @param estat_iteracio Estat de la partida en la darrera iteració
	 * @return El valor avaluat del moviment a la casella per al jugador que efectua el moviment
	 */
	int sexSearch( Casella casella, EstatCasella jugador, EstatCasella contrincant, int alfa, int beta, int cost,
	               int profunditat, EstatPartida estat_iteracio )
	{
		int beta_2, puntuacio;
		if ( cost >= pressupost || profunditat >= profunditat_max || estat_iteracio != EstatPartida.NO_FINALITZADA )
		{
			puntuacio = funcioAvaluacio( tauler, estat_iteracio, profunditat, jugador );
			taula_transposicio.put( tauler.hashCode(),
					new ElementTaulaTransposicions( partida.getTornsJugats() + profunditat, FitesDePoda.VALOR_EXACTE,
							puntuacio, jugador ) );
			return puntuacio;
		}

		if ( taula_transposicio.containsKey( tauler.hashCode() ) )
		{
			ElementTaulaTransposicions element = taula_transposicio.get( tauler.hashCode() );
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
		int num_explorades = 0;
		Set<ResistenciaCasella> moviments_ordenats = movimentsOrdenats( contrincant );
		for ( ResistenciaCasella resistencia_actual : moviments_ordenats )
		{
			Casella actual = resistencia_actual.getCasella();
			int cost_actual = cost + resistencia_actual.getResistencia();
			if ( tauler.esMovimentValid( contrincant, actual ) )
			{
				tauler.mouFitxa( contrincant, actual );
				estat_iteracio = partida.comprovaEstatPartida( actual.getFila(), actual.getColumna() );
				puntuacio =
						-sexSearch( actual, contrincant, jugador, -beta_2, -alfa, cost + cost_actual, profunditat + 1,
								estat_iteracio );

				if ( alfa < puntuacio && puntuacio < beta && !primer_fill )
				{
					fita = FitesDePoda.VALOR_EXACTE;
					puntuacio =
							-sexSearch( actual, contrincant, jugador, -beta, -alfa, cost + cost_actual, profunditat + 1,
									estat_iteracio );
				}

				tauler.treuFitxa( actual );

				if ( alfa < puntuacio )
				{
					fita = FitesDePoda.VALOR_EXACTE;
					alfa = puntuacio;
				}

				if ( alfa >= beta )
				{
					taula_transposicio.put( tauler.hashCode(),
							new ElementTaulaTransposicions( partida.getTornsJugats() + profunditat,
									FitesDePoda.FITA_INFERIOR, alfa, jugador ) );
					return alfa;
				}

				beta_2 = alfa + 1;
				primer_fill = false;
			}
		}

		taula_transposicio.put( tauler.hashCode(),
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
/*
		if ( memoria.containsKey( ( ( TaulerHex ) tauler ).hashCode() ) )
		{
			retorn = memoria.get( ( ( TaulerHex ) tauler ).hashCode() ).getPuntuacio( fitxa_jugador );
		}
		else
		{*/
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

		if ( fitxa_jugador == EstatCasella.JUGADOR_A )
		{
			retorn = potencial_b - potencial_a;
		}
		else
		{
			retorn = potencial_a - potencial_b;
		}
		//		memoria.put( ( ( TaulerHex ) tauler ).hashCode(), new ElementTaulaTransposicions( retorn,
		//		fitxa_jugador ) );
		//	}

		return retorn;
	}

	public Casella obteMoviment( EstatCasella fitxa )
	{
		tauler = partida.getTauler();
		int puntuacio_millor = Integer.MIN_VALUE + 1;
		Casella millor_moviment = null;

		Set<ResistenciaCasella> moviments_ordenats = movimentsOrdenats( fitxa );
		for ( ResistenciaCasella resistencia_actual : moviments_ordenats )
		{
			Casella casella = resistencia_actual.getCasella();
			int cost = resistencia_actual.getResistencia();
			int resultat_parcial =
					sexSearch( casella, fitxa, fitxaContraria( fitxa ), Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1,
							cost, 1, partida.comprovaEstatPartida( casella.getFila(), casella.getColumna() ) );
			if ( resultat_parcial > puntuacio_millor )
			{
				millor_moviment = casella;
				puntuacio_millor = resultat_parcial;
			}
		}

		return millor_moviment;
	}
}
