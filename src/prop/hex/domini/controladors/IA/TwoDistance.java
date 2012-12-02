package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.util.*;

/**
 * REVISAR QUE PASA CON EL BORDE CONTRARIO Y LONGITUD DE BUSQUEDA
 * FALTA DESEMPATE!
 */
public class TwoDistance
{

	private TaulerHex tauler;
	private EstatCasella jugador;
	private EstatCasella jugador_contrari;
	private int[][] distancies_a, distancies_b, potencials;
	private int infinit = 1000000;
	private int potencial;

	public TwoDistance( TaulerHex tauler, EstatCasella jugador )
	{
		this.tauler = tauler;
		this.jugador = jugador;
		if ( jugador == EstatCasella.JUGADOR_A )
		{
			jugador_contrari = EstatCasella.JUGADOR_B;
		}
		else
		{
			jugador_contrari = EstatCasella.JUGADOR_A;
		}
		distancies_a = new int[tauler.getMida()][tauler.getMida()];
		distancies_b = new int[tauler.getMida()][tauler.getMida()];
		potencials = new int[tauler.getMida()][tauler.getMida()];

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
				if ( tauler.getEstatCasella( fila, 0 ) == EstatCasella.BUIDA )
				{
					distancies_a[fila][0] = resistenciaCasella( new Casella( fila, 0 ) );
				}
				if ( tauler.getEstatCasella( fila, tauler.getMida() - 1 ) == EstatCasella.BUIDA )
				{
					distancies_b[fila][tauler.getMida() - 1] =
							resistenciaCasella( new Casella( fila, tauler.getMida() - 1 ) );
				}

				if ( tauler.getEstatCasella( fila, 0 ) == jugador )
				{
					GrupCaselles grup = new GrupCaselles( tauler );
					grup.estendre( new Casella( fila, 0 ) );
					ArrayList<Casella> grup_veins = grup.getVeins().getGrup();

					for ( Casella vei : grup_veins )
					{
						distancies_a[vei.getFila()][vei.getColumna()] = 1;
					}
				}

				if ( tauler.getEstatCasella( fila, tauler.getMida() - 1 ) == jugador )
				{
					GrupCaselles grup = new GrupCaselles( tauler );
					grup.estendre( new Casella( fila, tauler.getMida() - 1 ) );
					ArrayList<Casella> grup_veins = grup.getVeins().getGrup();

					for ( Casella vei : grup_veins )
					{
						distancies_b[vei.getFila()][vei.getColumna()] = 1;
					}
				}
			}
		}
		else
		{
			for ( int columna = 0; columna < tauler.getMida(); columna++ )
			{
				if ( tauler.getEstatCasella( 0, columna ) == EstatCasella.BUIDA )
				{
					distancies_a[0][columna] = resistenciaCasella( new Casella( 0, columna ) );
				}
				if ( tauler.getEstatCasella( tauler.getMida() - 1, columna ) == EstatCasella.BUIDA )
				{
					distancies_b[tauler.getMida() - 1][columna] =
							resistenciaCasella( new Casella( tauler.getMida() - 1, columna ) );
				}

				if ( tauler.getEstatCasella( 0, columna ) == jugador )
				{
					GrupCaselles grup = new GrupCaselles( tauler );
					grup.estendre( new Casella( 0, columna ) );
					ArrayList<Casella> grup_veins = grup.getVeins().getGrup();

					for ( Casella vei : grup_veins )
					{
						distancies_a[vei.getFila()][vei.getColumna()] = 1;
					}
				}

				if ( tauler.getEstatCasella( tauler.getMida() - 1, columna ) == jugador )
				{
					GrupCaselles grup = new GrupCaselles( tauler );
					grup.estendre( new Casella( tauler.getMida() - 1, columna ) );
					ArrayList<Casella> grup_veins = grup.getVeins().getGrup();

					for ( Casella vei : grup_veins )
					{
						distancies_b[vei.getFila()][vei.getColumna()] = 1;
					}
				}
			}
		}

		followTheWhiteRabbit( distancies_a );
		followTheWhiteRabbit( distancies_b );

		potencial = infinit;

		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				potencials[i][j] = distancies_a[i][j] + distancies_b[i][j];
				//I assginem a potencial el nombre més petit de potencials.
				if ( potencials[i][j] < potencial )
				{
					potencial = potencials[i][j];
				}
			}
		}
	}

	//Y iterar un numero fijo de veces por cada casilla? ? ? ? (done, but not totally working).
	//Fix tema veins, potser arregla lo anterior.
	//SILLA
	private void followTheWhiteRabbit( int[][] distancia )
	{
		ArrayList<Casella> pendents = new ArrayList<Casella>();
		int contador = 0;
		int maxim = tauler.getMida();// * tauler.getMida();

		//Afegim a pendents totes les caselles buides del tauler no checkejades (no estan a la cantonada).
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

		HashMap<Casella, ArrayList<Casella>> meta_veins = new HashMap<Casella, ArrayList<Casella>>();
		for(Casella actual : pendents) {
			ArrayList<Casella> veins = getVeins(actual);
			meta_veins.put(actual, veins);
		}

		//Iterem pendents si el contador es menor a un maxim establert.
		while ( contador < maxim )
		{
			boolean modificat = false;
			contador++;

			for ( Casella actual : pendents )
			{

				int min_u = infinit;
				int min_dos = infinit;
				ArrayList<Casella> veins = meta_veins.get(actual);
				for ( Casella vei : veins )
				{
					if ( distancia[vei.getFila()][vei.getColumna()] <= min_u )
					{
						min_dos = min_u;
						min_u = distancia[vei.getFila()][vei.getColumna()];
					}
					else if ( distancia[vei.getFila()][vei.getColumna()] < min_dos )
					{
						min_dos = distancia[vei.getFila()][vei.getColumna()];
					}
				}

				if ( min_dos < infinit )
				{
					distancia[actual.getFila()][actual.getColumna()] = min_dos + 1;
					modificat = true;
				}
			}

			//si en una iteració no es modifica cap valor, ja hem acabat.
			if ( !modificat )
			{
				break;
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

	private ArrayList<Casella> getVeins( Casella casella )
	{
		tauler.mouFitxa( jugador, casella );
		GrupCaselles grup = new GrupCaselles( tauler );
		grup.estendre( casella );
		ArrayList<Casella> grup_veins = grup.getVeins().getGrup();
		tauler.treuFitxa( casella );
		return grup_veins;
	}

	public int[][] getDistancies_a()
	{
		return distancies_a;
	}

	public int[][] getDistancies_b()
	{
		return distancies_b;
	}

	public int[][] getPotencials()
	{
		return potencials;
	}

	public int getPotencial()
	{
		return potencial;
	}

	public void shout()
	{
		System.out.println( "OUTPUT A" );
		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				if ( distancies_a[i][j] >= infinit )
				{
					System.out.print( "i " );
				}
				else
				{
					System.out.print( distancies_a[i][j] + " " );
				}
			}
			System.out.print( "\n" );
		}

		System.out.println( "OUTPUT B" );
		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				if ( distancies_b[i][j] >= infinit )
				{
					System.out.print( "i " );
				}
				else
				{
					System.out.print( distancies_b[i][j] + " " );
				}
			}
			System.out.print( "\n" );
		}

		System.out.println( "POTENCIAL" );
		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				if ( potencials[i][j] >= infinit )
				{
					System.out.print( "i " );
				}
				else
				{
					System.out.print( potencials[i][j] + " " );
				}
			}
			System.out.print( "\n" );
		}
	}
}
