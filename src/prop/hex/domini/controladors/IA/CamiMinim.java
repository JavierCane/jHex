package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 17/11/12
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
public class CamiMinim
{

	TaulerHex tauler;
	EstatCasella jugador;
	int[][] resistencies_parcials;

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

/*
	DIJKSTRA (Grafo G, nodo_fuente s)
		para u ∈ V[G] hacer
			distancia[u] = INFINITO
			padre[u] = NULL
		distancia[s] = 0
		adicionar (cola, (s,distance[s]))
		mientras que cola no es vacía hacer
			u = extraer_minimo(cola)
			para todos v ∈ adyacencia[u] hacer
				si distancia[v] > distancia[u] + peso (u, v) hacer
					distancia[v] = distancia[u] + peso (u, v)
					padre[v] = u
	                adicionar(cola,(v,distance[v]))
*/

	public int evalua()
	{
		Comparator<ResistenciaCasella> comparador = new ResistenciaCasellaComparator();
		PriorityQueue<ResistenciaCasella> cua_caselles = new PriorityQueue<ResistenciaCasella>( 10, comparador );

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

		int min = 100000;

		if ( jugador == EstatCasella.JUGADOR_A )
		{
			for ( int fila = 0; fila < tauler.getMida(); fila++ )
			{
			//	if (  )
				System.out.print(resistencies_parcials[fila][tauler.getMida()-1]);
			}
			System.out.print("\n");
		}
		else
		{
			for ( int columna = 0; columna < tauler.getMida(); columna++ )
			{
				System.out.print(resistencies_parcials[tauler.getMida()-1][columna]);
			}
			System.out.print("\n");
		}

		return min;
	}


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
