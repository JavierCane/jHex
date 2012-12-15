package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IA.auxiliars.ResistenciaCasella;
import prop.hex.domini.controladors.IA.auxiliars.TwoDistance;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

/**
 * Potencials
 */
public final class IAHexPotencialsCtrl extends InteligenciaArtificialHexCtrl
{

	private TaulerHex tauler;

	private Casella movimentFactible( EstatCasella fitxa_jugador )
	{
		TreeSet<ResistenciaCasella> moviments_ordenats = new TreeSet<ResistenciaCasella>();

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

		Iterator<ResistenciaCasella> resistencies = moviments_ordenats.iterator();
		ResistenciaCasella actual = resistencies.next();
		int resistencia_actual = actual.getResistencia();
		int resistencia_minima = resistencia_actual;
		ArrayList<Casella> caselles = new ArrayList<Casella>( moviments_ordenats.size() );
		caselles.add( actual.getCasella() );

		int visitades_max =
				1 + Math.min( ( tauler.getMida() * tauler.getMida() - tauler.getTotalFitxes() ) / tauler.getMida(), 7 );

		while ( resistencies.hasNext() && resistencia_actual == resistencia_minima && caselles.size() < visitades_max )
		{
			actual = resistencies.next();
			resistencia_actual = actual.getResistencia();
			if ( resistencia_actual == resistencia_minima )
			{
				caselles.add( actual.getCasella() );
			}
		}

		return caselles.get( new Random().nextInt( caselles.size() ) );
	}

	@Override
	public int funcioAvaluacio( Tauler tauler, EstatPartida estat_moviment, int profunditat,
	                            EstatCasella fitxa_jugador )
	{
		return 0;
	}

	/**
	 * Retorna un moviment adequat al tauler actual per al jugador indicat per fitxa.
	 * <p/>
	 * Fa servir un algorisme negaScout amb taules de transposició i elecció d'un subconjunt de nodes aleatoris
	 * (Monte Carlo amb elecció equiprobable).
	 *
	 * @param fitxa Fitxa que vol col·locar-se al tauler de la partida del paràmetre implícit.
	 * @return La casella on es mouria la fitxa.
	 * @see InteligenciaArtificialHexCtrl
	 */
	public Casella obteMoviment( EstatCasella fitxa )
	{
		if ( partida.getTornsJugats() == 1 )
		{
			return obertura();
		}

		tauler = partida.getTauler();

		return movimentFactible( fitxa );
	}
}
