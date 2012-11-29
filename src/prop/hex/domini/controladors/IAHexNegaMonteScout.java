package prop.hex.domini.controladors;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IA.ElementTaulaTransposicions;
import prop.hex.domini.controladors.IA.enums.FitesDePoda;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Isaac
 * Date: 25/11/12
 * Time: 10:19
 * To change this template use File | Settings | File Templates.
 */
public class IAHexNegaMonteScout extends IAHexQueenBeeCtrl implements MouFitxaIA
{

	private PartidaHex partida;
	private TaulerHex tauler;
	private static int profunditat_maxima = 1;
	private static HashMap<Integer, ElementTaulaTransposicions> taula_transposicio;

	/**
	 * Constructor per defecte. Genera la taula de transposició si no existeix.
	 */
	public IAHexNegaMonteScout()
	{
		if ( taula_transposicio == null )
		{
			taula_transposicio = new HashMap<Integer, ElementTaulaTransposicions>();
		}
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
	 * Configura la instància de partida per a la intel·ligència artificial.
	 *
	 * @param partida Partida on es vol jugar amb la intel·ligència artificial.
	 * @return Cert si s'ha canviat de partida. Fals altrament.
	 */
	public boolean setPartida( PartidaHex partida )
	{
		if ( partida != null )
		{
			this.partida = partida;
			return true;
		}
		else
		{
			return false;
		}
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
	int negaMonteScout( Casella casella, EstatCasella jugador, EstatCasella contrincant, int alfa, int beta,
	                    int profunditat, EstatPartida estat_iteracio )
	{
		int beta_2, puntuacio;
		if ( profunditat == profunditat_maxima || estat_iteracio != EstatPartida.NO_FINALITZADA )
		{
			puntuacio = funcioAvaluacio( tauler, estat_iteracio, profunditat, jugador );
			taula_transposicio.put( tauler.hashCode(),
					new ElementTaulaTransposicions( profunditat, FitesDePoda.VALOR_EXACTE, puntuacio, jugador ) );
			return puntuacio;
		}

		if ( taula_transposicio.containsKey( tauler.hashCode() ) )
		{
			ElementTaulaTransposicions element = taula_transposicio.get( tauler.hashCode() );
			if ( element.getProfunditat() >= profunditat )
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

		Random generador = new Random();

		int caselles_restants = tauler.getMida() * tauler.getMida() - tauler.getTotalFitxes();
		int max_moviments =
				Math.max( ( caselles_restants / ( ( int ) ( Math.sqrt( tauler.getMida() ) * 0.45 ) ) ) - profunditat,
						7 - profunditat );
		Set<Casella> explorades = new HashSet<Casella>( max_moviments );

		beta_2 = beta;
		boolean primer_fill = true;
		FitesDePoda fita = FitesDePoda.FITA_SUPERIOR;
		while ( explorades.size() < max_moviments && explorades.size() < caselles_restants )
		{
			Casella actual =
					new Casella( generador.nextInt( tauler.getMida() ), generador.nextInt( tauler.getMida() ) );
			if ( tauler.esMovimentValid( contrincant, actual ) && !explorades.contains( actual ) )
			{
				explorades.add( actual );
				tauler.mouFitxa( contrincant, actual );
				estat_iteracio = partida.comprovaEstatPartida( actual.getFila(), actual.getColumna() );
				puntuacio = -negaMonteScout( actual, contrincant, jugador, -beta_2, -alfa, profunditat + 1,
						estat_iteracio );

				if ( alfa < puntuacio && puntuacio < beta && !primer_fill )
				{
					fita = FitesDePoda.VALOR_EXACTE;
					puntuacio = -negaMonteScout( actual, contrincant, jugador, -beta, -alfa, profunditat + 1,
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
							new ElementTaulaTransposicions( profunditat, FitesDePoda.FITA_INFERIOR, alfa, jugador ) );
					return alfa;
				}

				beta_2 = alfa + 1;
				primer_fill = false;
			}
		}

		taula_transposicio.put( tauler.hashCode(), new ElementTaulaTransposicions( profunditat, fita, alfa, jugador ) );

		return alfa;
	}

	/**
	 * Retorna un moviment adequat al tauler actual per al jugador indicat per fitxa.
	 * <p/>
	 * Fa servir un algorisme negaScout amb taules de transposició i elecció d'un subconjunt de nodes aleatoris
	 * (pseudo-montecarlo).
	 *
	 * @param fitxa Fitxa que vol col·locar-se al tauler de la partida del paràmetre implícit.
	 * @return La casella on es mouria la fitxa.
	 */
	public Casella mouFitxa( EstatCasella fitxa )
	{
		tauler = ( TaulerHex ) partida.getTauler();

		int puntuacio_millor = Integer.MIN_VALUE + 1;
		Casella millor_moviment = null;

		for ( int fila = 0; fila < tauler.getMida(); fila++ )
		{
			for ( int columna = 0; columna < tauler.getMida(); columna++ )
			{
				Casella actual = new Casella( fila, columna );

				if ( tauler.esMovimentValid( fitxa, actual ) )
				{
					tauler.mouFitxa( fitxa, actual );
					int puntuacio_actual =
							negaMonteScout( actual, fitxa, fitxaContraria( fitxa ), Integer.MIN_VALUE + 1,
									Integer.MAX_VALUE - 1, 0, partida.comprovaEstatPartida( fila, columna ) );

					tauler.treuFitxa( actual );

					if ( puntuacio_actual > puntuacio_millor )
					{
						puntuacio_millor = puntuacio_actual;
						millor_moviment = actual;
					}
				}
			}
		}

		return millor_moviment;
	}
}
