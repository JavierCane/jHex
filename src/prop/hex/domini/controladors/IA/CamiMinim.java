package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Busca el camí mínim per arribar d'una punta a una altra del tauler,
 * horitzontal o verticalment depenent de quin jugador sigui (resistencia mínima del tauler).
 */
public class CamiMinim
{

	private TaulerHex tauler;
	private EstatCasella jugador;
	/**
	 * Guardem els valors parcials del cami mínim. Li diem resistencies per mantenir el concepte
	 * on cada casella es una resistencia el valor de la qual varia segons el seu contingut.
	 */
	private int[][] resistencies_parcials;

	/**
	 * @param tauler  Tauler sobre el que buscar
	 * @param jugador Per a quin jugador volem buscar
	 */
	public CamiMinim( TaulerHex tauler, EstatCasella jugador )
	{
		this.tauler = tauler;
		this.jugador = jugador;
		resistencies_parcials = new int[tauler.getMida()][tauler.getMida()];
		for ( int fila = 0; fila < tauler.getMida(); fila++ )
		{
			for ( int columna = 0; columna < tauler.getMida(); columna++ )
			{
				resistencies_parcials[fila][columna] = Integer.MAX_VALUE;
			}
		}
	}

	/**
	 * Busca el camí mínim aplicant l'algoritme de Dijkstra
	 *
	 * @return retorna el camí de cost mínim (Resistencia mínima).
	 */
	public int evalua()
	{
		Comparator<ResistenciaCasella> comparador = new ResistenciaCasellaComparator();
		PriorityQueue<ResistenciaCasella> cua_caselles = new PriorityQueue<ResistenciaCasella>( 10, comparador );

		/**
		 * Omplim la cua_caselles amb les caselles d'un canto, el superior si és jugador_B o l'esquerra si és A.
		 */
		if ( jugador == EstatCasella.JUGADOR_A )
		{
			for ( int fila = 0; fila < tauler.getMida(); fila++ )
			{
				resistencies_parcials[fila][0] = resistenciaCasella( new Casella( fila, 0 ) );
				cua_caselles.add( new ResistenciaCasella( new Casella( fila, 0 ), resistencies_parcials[fila][0] ) );
			}
		}
		else
		{
			for ( int columna = 0; columna < tauler.getMida(); columna++ )
			{
				resistencies_parcials[0][columna] = resistenciaCasella( new Casella( 0, columna ) );
				cua_caselles.add( new ResistenciaCasella( new Casella( 0, columna ), resistencies_parcials[0][columna] ) );
			}
		}

		/**
		 * Fem Dijkstra sobre la cua_caselles.
		 */
		while ( !cua_caselles.isEmpty() )
		{
			ResistenciaCasella actual = cua_caselles.poll();

			List<Casella> veins = tauler.getVeins( actual.getCasella() );
			for ( Casella vei : veins )
			{
				if ( resistencies_parcials[vei.getFila()][vei.getColumna()] > actual.getResistencia() + resistenciaCasella( vei ) )
				{
					resistencies_parcials[vei.getFila()][vei.getColumna()] = actual.getResistencia() + resistenciaCasella( vei );
					cua_caselles.add( new ResistenciaCasella( vei, resistencies_parcials[vei.getFila()][vei.getColumna()] ) );
				}
			}
		}

		/**
		 * Mirem la cantonada inferior o dreta (depenent del jugador) i ens quedem amb el mínim valor de totes les
		 * caselles que hi ha, aquest serà el mínim valor per creuar el tauler.
		 */
		int min = 100000;

		if ( jugador == EstatCasella.JUGADOR_A )
		{
			for ( int fila = 0; fila < tauler.getMida(); fila++ )
			{
				if ( resistencies_parcials[fila][tauler.getMida() - 1] < min )
				{
					min = resistencies_parcials[fila][tauler.getMida() - 1];
				}
			}
		}
		else
		{
			for ( int columna = 0; columna < tauler.getMida(); columna++ )
			{
				if ( resistencies_parcials[tauler.getMida() - 1][columna] < min )
				{
					min = resistencies_parcials[tauler.getMida() - 1][columna];
				}
			}
		}

		return min;
	}

	/**
	 * Retorna la resistencia d'una casella donada.
	 *
	 * @param casella
	 * @return
	 */
	private int resistenciaCasella( Casella casella )
	{
		EstatCasella estat = tauler.getEstatCasella( casella );
		if ( estat == jugador )
		{
			return 0;
		}
		else if ( estat == EstatCasella.BUIDA )
		{
			return 1;
		}
		else
		{
			return 100000;
		}
	}

	/**
	 * Classe que vincula una casella amb la seva resistencia.
	 * Necessaria com a auxiliar a Dijkstra.
	 */
	private class ResistenciaCasella
	{

		int resistencia;
		Casella casella;

		ResistenciaCasella( Casella casella, int resistencia )
		{
			this.casella = casella;
			this.resistencia = resistencia;
		}

		int getResistencia()
		{
			return resistencia;
		}

		Casella getCasella()
		{
			return casella;
		}
	}

	/**
	 * Classe comparadora de ResistenciaCasella per a poder utilitzar priority queues.
	 */
	public class ResistenciaCasellaComparator implements Comparator<ResistenciaCasella>
	{

		@Override
		public int compare( ResistenciaCasella x, ResistenciaCasella y )
		{
			if ( x.getResistencia() < y.getResistencia() )
			{
				return -1;
			}
			if ( x.getResistencia() > y.getResistencia() )
			{
				return 1;
			}
			return 0;
		}
	}
}
