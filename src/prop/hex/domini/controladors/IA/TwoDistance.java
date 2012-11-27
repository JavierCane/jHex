package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.List;

/**
 *
 *
 */
public class TwoDistance
{

	private TaulerHex tauler;
	private EstatCasella jugador;
	private int[][] distancies_a, distancies_b;
	private int infinit = 1000000;

	public TwoDistance( TaulerHex tauler, EstatCasella jugador )
	{
		this.tauler = tauler;
		this.jugador = jugador;
		distancies_a = new int[tauler.getMida()][tauler.getMida()];
		distancies_b = new int[tauler.getMida()][tauler.getMida()];

		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				distancies_a[i][j] = infinit;
				distancies_b[i][j] = infinit;
			}
		}

		if ( jugador == EstatCasella.JUGADOR_A )
		{
			for ( int fila = 0; fila < tauler.getMida(); fila++ )
			{
				distancies_a[fila][0] = resistenciaCasella( new Casella( fila, 0 ) );
				distancies_b[fila][tauler.getMida() - 1] =
						resistenciaCasella( new Casella( fila, tauler.getMida() - 1 ) );
			}
		}
		else
		{
			for ( int columna = 0; columna < tauler.getMida(); columna++ )
			{
				distancies_a[0][columna] = resistenciaCasella( new Casella( 0, columna ) );
				distancies_b[tauler.getMida() - 1][columna] =
						resistenciaCasella( new Casella( tauler.getMida() - 1, columna ) );
			}
		}

		followTheWhiteRabbit(distancies_a);
		followTheWhiteRabbit(distancies_b);

	}

	private void followTheWhiteRabbit( int[][] distancia )
	{

		LinkedList<Casella> pendents = new LinkedList<Casella>();
		int contador = 0;
		int maxim = tauler.getMida()*tauler.getMida()*tauler.getMida();

		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				Casella actual = new Casella( i, j );
				//Si la distancia es infinit (casella no chequejada) i la casella esta buida, l'afegim a pendents.
				if ( distancia[i][j] >= infinit && tauler.getEstatCasella( actual ) == EstatCasella.BUIDA )
				{
					pendents.add( actual );
				}
			}
		}

		while ( !pendents.isEmpty() && contador < maxim )
		{
			Casella actual = pendents.poll();
			contador++;

			int min_u = infinit;
			int min_dos = infinit;
			ArrayList<Casella> veins = getVeins( actual );
			for ( Casella vei : veins )
			{
				if ( distancia[vei.getFila()][vei.getColumna()] < min_u )
				{
					min_dos = min_u;
					min_u = distancia[vei.getFila()][vei.getColumna()];
				}
				else if ( distancia[vei.getFila()][vei.getColumna()] < min_dos )
				{
					min_dos = distancia[vei.getFila()][vei.getColumna()];
				}
			}

			if(min_dos < infinit) {
				distancia[actual.getFila()][actual.getColumna()] = min_dos + 1;
			}
			else {
				pendents.add(actual);
			}
		}
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
			return infinit;
		}
	}

	ArrayList<Casella> getVeins( Casella casella )
	{
		tauler.mouFitxa( jugador, casella );
		GrupCaselles grup = new GrupCaselles( tauler );
		grup.estendre( casella );
		ArrayList<Casella> grup_veins = grup.getVeins().getGrup();
		tauler.treuFitxa( casella );
		return grup_veins;
	}

	public void shout()
	{
		System.out.println( "OUTPUT A" );
		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				System.out.print( distancies_a[i][j] + " " );
			}
			System.out.print( "\n" );
		}

		System.out.println( "OUTPUT B" );
		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				System.out.print( distancies_b[i][j] + " " );
			}
			System.out.print( "\n" );
		}
	}
}
