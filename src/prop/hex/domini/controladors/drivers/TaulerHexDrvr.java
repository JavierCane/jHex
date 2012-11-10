package prop.hex.domini.controladors.drivers;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.TaulerHex;

import java.io.IOException;
import java.util.ArrayList;

import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixEnter;
import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixParaula;

public final class TaulerHexDrvr
{

	private static ArrayList<TaulerHex> taulers = new ArrayList<TaulerHex>();
	private static int actual;

	public static void testInstanciaTauler()
	{
		int mida = llegeixEnter( "Introdueix la mida del tauler (més gran que 0):" );

		taulers.add( new TaulerHex( mida ) );
		actual = taulers.size() - 1;
		System.out.println( "[OK]\tS'ha instanciat el tauler amb èxit.\n\tEl tauler actual és: " + actual + ".\n\tLes" +
		                    " seves característiques són:\n" + taulers.get( actual ).toString() );
	}

	public static void testCopiaTauler()
	{
		if ( taulers.size() > 0 )
		{
			System.out.println( taulers.size() );
			int num = llegeixEnter(
					"Escriu el número del tauler per copiar (entre 0 i " + Integer.toString( taulers.size() - 1 ) +
					"):" );

			taulers.add( new TaulerHex( taulers.get( num ) ) );
			actual = taulers.size() - 1;
			System.out.println( "[OK]\tS'ha copiat el tauler amb èxit.\n\tEl tauler actual és: " + actual + ".\n\tLes" +
			                    " seves característiques són:\n" + taulers.get( actual ).toString() );
		}
		else
		{
			System.out.println( "[KO]\tNo existeix cap tauler!" );
		}
	}

	public static void canviaTauler()
	{
		if ( taulers.size() > 0 )
		{
			System.out.println(
					"Escriu el número del tauler que vols (entre 0 i " + Integer.toString( taulers.size() - 1 ) + ")" +
					":" );
			int num = llegeixEnter();

			if ( num < 0 || num >= taulers.size() )
			{
				System.out.println(
						"[KO]\tEl tauler seleccionat no existeix. No es canvia de tauler.\n\tEl tauler actual és: " +
						actual );
			}
			else
			{
				actual = num;
				System.out
						.println( "[OK]\tS'ha canviat de tauler amb èxit.\n\tEl tauler actual és: " + actual + ".\n\t" +
						          "Les seves característiques són:\n" + taulers.get( actual ).toString() );
			}
		}
		else
		{
			System.out.println( "[KO]\tNo existeix cap tauler!" );
		}

	}

	public static void testMouFitxa() throws IOException
	{
		if ( taulers.size() > 0 )
		{
			String jugador = llegeixParaula( "Escull un jugador (A, B, cap):" );
			EstatCasella fitxa;
			if ( jugador.equalsIgnoreCase( "A" ) )
			{
				fitxa = EstatCasella.JUGADOR_A;
			}
			else if ( jugador.equalsIgnoreCase( "B" ) )
			{
				fitxa = EstatCasella.JUGADOR_B;
			}
			else
			{
				fitxa = EstatCasella.BUIDA;
			}

			int fila = llegeixEnter( "Escriu la fila de la casella:" );

			int columna = llegeixEnter( "Escriu la columna de la casella:" );

			try
			{
				taulers.get( actual ).mouFitxa( fitxa, fila, columna );
				System.out.println( "[OK]\tLa fitxa s'ha mogut correctament." );
			}
			catch ( IndexOutOfBoundsException excepcio )
			{
				System.out.println( "[KO]\tNo s'ha pogut moure la fitxa: " + excepcio.getMessage() );
			}
			catch ( IllegalArgumentException excepcio )
			{
				System.out.println( "[KO]\tNo s'ha pogut moure la fitxa: " + excepcio.getMessage() );
			}

			System.out.println( "\tEl tauler actual és: " + actual + ".\n\t" + "Les seves característiques són:\n" +
			                    taulers.get( actual ).toString() );
		}
		else
		{
			System.out.println( "[KO]\tNo existeix cap tauler!" );
		}
	}

	public static void testTreuFitxa()
	{
		if ( taulers.size() > 0 )
		{
			int fila = llegeixEnter( "Escriu la fila de la casella:" );

			int columna = llegeixEnter( "Escriu la columna de la casella:" );

			try
			{
				taulers.get( actual ).treuFitxa( fila, columna );
				System.out.println( "[OK]\tLa fitxa s'ha tret correctament." );
			}
			catch ( IndexOutOfBoundsException excepcio )
			{
				System.out.println( "[KO]\tNo s'ha pogut treure la fitxa: " + excepcio.getMessage() );
			}
			catch ( IllegalArgumentException excepcio )
			{
				System.out.println( "[KO]\tNo s'ha pogut treure la fitxa: " + excepcio.getMessage() );
			}

			System.out.println( "\tEl tauler actual és: " + actual + ".\n\t" + "Les seves característiques són:\n" +
			                    taulers.get( actual ).toString() );
		}
		else
		{
			System.out.println( "[KO]\tNo existeix cap tauler!" );
		}
	}

	public static void testIntercanviaFitxa()
	{
		if ( taulers.size() > 0 )
		{
			int fila = llegeixEnter( "Escriu la fila de la casella:" );

			int columna = llegeixEnter( "Escriu la columna de la casella:" );

			try
			{
				taulers.get( actual ).intercanviaFitxa( fila, columna );
				System.out.println( "[OK]\tLa fitxa s'ha intercanviat." );
			}
			catch ( IndexOutOfBoundsException excepcio )
			{
				System.out.println( "[KO]\tNo s'ha pogut intercanviar la fitxa: " + excepcio.getMessage() );
			}
			catch ( IllegalArgumentException excepcio )
			{
				System.out.println( "[KO]\tNo s'ha pogut intercanviar la fitxa: " + excepcio.getMessage() );
			}

			System.out.println( "\tEl tauler actual és: " + actual + ".\n\t" + "Les seves característiques són:\n" +
			                    taulers.get( actual ).toString() );
		}
		else
		{
			System.out.println( "[KO]\tNo existeix cap tauler!" );
		}
	}
}
