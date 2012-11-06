package prop.hex.domini.controladors.drivers;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.TaulerHex;

import java.io.IOException;
import java.util.ArrayList;

import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixEnter;
import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixParaula;

public class TaulerHexDrvr
{

	private static ArrayList<TaulerHex> taulers = new ArrayList<TaulerHex>();
	private static int actual;

	public static void main( String[] args )
	{
		try
		{
			int opcio = 0;

			while ( opcio != 9 && taulers.size() == 0 )
			{
				System.out.println();
				System.out.println( "------------------------------------------------------" );
				System.out.println( "Proves de la clase TaulerHex" );
				System.out.println( "Escull una opció:" );
				System.out.println( "1 - Nou tauler" );
				System.out.println( "9 - Surt del programa" );
				try
				{
					opcio = llegeixEnter();
				}
				catch ( NumberFormatException excepcio )
				{
					System.err.println( "No s'ha introduït cap nombre" );
				}
				switch ( opcio )
				{
					case 1:
						creaNouTauler();
						break;
					case 9:
						break;
					default:
						System.out.println( "No és una opció vàlida." );
						System.out.println();
				}
			}

			while ( opcio != 9 )
			{
				System.out.println();
				System.out.println( "------------------------------------------------------" );
				System.out.println( "Proves de la clase TaulerHex" );
				System.out.println( "Tauler actual: " + actual );
				System.out.println( "Escull una opció:" );
				System.out.println();
				System.out.println( "1 - Nou tauler" );
				System.out.println( "2 - Copia tauler existent" );
				System.out.println( "3 - Canvia de tauler" );
				System.out.println( "4 - Col·loca (mou) una fitxa" );
				System.out.println( "5 - Treu una fitxa" );
				System.out.println( "6 - Intercanvia una fitxa" );
				System.out.println( "7 - Comprova propietats del tauler" );
				System.out.println( "9 - Surt del programa" );

				try
				{
					opcio = llegeixEnter();
				}
				catch ( NumberFormatException excepcio )
				{
					System.err.println( "No s'ha introduït cap nombre" );
				}

				switch ( opcio )
				{
					case 1:
						creaNouTauler();
						break;
					case 2:
						copiaTauler();
						break;
					case 3:
						canviaTauler();
						break;
					case 4:
						mouFitxa();
						break;
					case 5:
						treuFitxa();
						break;
					case 6:
						intercanviaFitxa();
						break;
					case 7:
						obtePropietats();
						break;
					case 9:
						break;
					default:
						System.out.println( "No és una opció vàlida." );
						System.out.println();
				}
			}

		}
		catch ( IOException excepcio )
		{
			System.err.println( "Hi ha hagut un error d'entrada/sortida, es tanca el programa." );
			excepcio.printStackTrace();
			System.exit( 1 );
		}
		catch ( Exception excepcio )
		{
			System.err.println( "Hi ha hagut un error inesperat; es tanca el programa." );
			excepcio.printStackTrace();
			System.exit( 2 );
		}
	}

	private static void creaNouTauler() throws IOException, NumberFormatException
	{
		System.out.println( "Introdueix la mida del taulers" );
		int mida = llegeixEnter();

		taulers.add( new TaulerHex( mida ) );
		actual = taulers.size() - 1;
	}

	private static void copiaTauler() throws IOException
	{
		try
		{
			System.out.println(
					"Escriu el número del tauler per copiar (entre 0 i " + Integer.toString( taulers.size() - 1 ) +
					"):" );
			int num = llegeixEnter();

			taulers.add( ( TaulerHex ) taulers.get( num ).clone() );
			actual = taulers.size() - 1;
		}
		catch ( NumberFormatException excepcio )
		{
			System.err.println( "No s'ha introduït cap nombre. No es canvia res" );
		}
	}

	private static void canviaTauler() throws IOException
	{
		try
		{
			System.out.println(
					"Escriu el número del tauler que vols (entre 0 i " + Integer.toString( taulers.size() - 1 ) + ")" +
					":" );
			int num = llegeixEnter();

			if ( num < 0 || num >= taulers.size() )
			{
				System.out.println( "El número de tauler no és vàlid. No es canvia res." );
			}
			else
			{
				actual = num;
			}
		}
		catch ( NumberFormatException excepcio )
		{
			System.err.println( "No s'ha introduït cap nombre. No es canvia res" );
		}
	}

	private static void mouFitxa() throws IOException
	{
		System.out.println( "Escull un jugador (A, B, cap):" );
		String jugador = llegeixParaula();
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

		System.out.println( "Escriu la fila de la casella:" );
		int fila = llegeixEnter();

		System.out.println( "Escriu la columna de la casella:" );
		int columna = llegeixEnter();

		try
		{
			taulers.get( actual ).mouFitxa( fitxa, fila, columna );
		}
		catch ( IndexOutOfBoundsException excepcio )
		{
			System.err.println( "Error:" );
			System.err.println( excepcio.getMessage() );
		}
		catch ( IllegalArgumentException excepcio )
		{
			System.err.println( "Error:" );
			System.err.println( excepcio.getMessage() );
		}
	}

	private static void treuFitxa() throws IOException
	{
		System.out.println( "Escriu la fila de la casella:" );
		int fila = llegeixEnter();

		System.out.println( "Escriu la columna de la casella:" );
		int columna = llegeixEnter();

		try
		{
			taulers.get( actual ).treuFitxa( fila, columna );
		}
		catch ( IndexOutOfBoundsException excepcio )
		{
			System.err.println( "Error:" );
			System.err.println( excepcio.getMessage() );
		}
		catch ( IllegalArgumentException excepcio )
		{
			System.err.println( "Error:" );
			System.err.println( excepcio.getMessage() );
		}
	}

	private static void intercanviaFitxa() throws IOException
	{
		System.out.println( "Escriu la fila de la casella:" );
		int fila = llegeixEnter();

		System.out.println( "Escriu la columna de la casella:" );
		int columna = llegeixEnter();

		try
		{
			taulers.get( actual ).intercanviaFitxa( fila, columna );
		}
		catch ( IndexOutOfBoundsException excepcio )
		{
			System.err.println( "Error:" );
			System.err.println( excepcio.getMessage() );
		}
		catch ( IllegalArgumentException excepcio )
		{
			System.err.println( "Error:" );
			System.err.println( excepcio.getMessage() );
		}
	}

	private static void obtePropietats() throws IOException
	{
		int opcio = 0;
		while ( opcio != 9 )
		{
			System.out.println();
			System.out.println( "------------------------------------------------------" );
			System.out.println( "Propietats del tauler " + actual );
			System.out.println( "Escull una opció:" );
			System.out.println();
			System.out.println( "1 - Mida" );
			System.out.println( "2 - Estat d'una casella" );
			System.out.println( "3 - Número de fitxes del jugador A" );
			System.out.println( "4 - Número de fitxes del jugador B" );
			System.out.println( "5 - Número total de fitxes" );
			System.out.println( "6 - El tauler és buit?" );
			System.out.println( "9 - Tanca les propietats" );

			try
			{
				opcio = llegeixEnter();
			}
			catch ( NumberFormatException excepcio )
			{
				System.err.println( "No s'ha introduït cap nombre" );
			}

			switch ( opcio )
			{
				case 1:
					obteMida();
					break;
				case 2:
					obteEstatCasella();
					break;
				case 3:
					obteNumFitxesA();
					break;
				case 4:
					obteNumFitxesB();
					break;
				case 5:
					obteTotalFitxes();
					break;
				case 6:
					esTaulerBuit();
					break;
				case 9:
					break;
				default:
					System.out.println( "No és una opció vàlida." );
					System.out.println();
			}
		}
	}

	private static void obteMida()
	{
		System.out.println( "Mida del tauler: " + taulers.get( actual ).getMida() );
	}

	private static void obteEstatCasella() throws IOException
	{
		System.out.println( "Escriu la fila de la casella:" );
		int fila = llegeixEnter();

		System.out.println( "Escriu la columna de la casella:" );
		int columna = llegeixEnter();

		try
		{
			EstatCasella estat = taulers.get( actual ).getEstatCasella( fila, columna );
			switch ( estat )
			{
				case JUGADOR_A:
					System.out.println( "La casella té una fitxa del jugador A" );
					break;
				case JUGADOR_B:
					System.out.println( "La casella té una fitxa del jugador B" );
					break;
				default:
					System.out.println( "La casella és buida" );
			}
		}
		catch ( IndexOutOfBoundsException excepcio )
		{
			System.err.println( "Error:" );
			System.err.println( excepcio.getMessage() );
		}
	}

	private static void obteNumFitxesA()
	{
		System.out.println( "Fitxes del jugador A: " + taulers.get( actual ).getNumFitxesA() );
	}

	private static void obteNumFitxesB()
	{
		System.out.println( "Fitxes del jugador B: " + taulers.get( actual ).getNumFitxesB() );
	}

	private static void obteTotalFitxes()
	{
		System.out.println( "Fitxes totals: " + taulers.get( actual ).getTotalFitxes() );
	}

	private static void esTaulerBuit()
	{
		if ( taulers.get( actual ).esBuit() )
		{
			System.out.println( "El tauler actual és buit" );
		}
		else
		{
			System.out.println( "El tauler actual no és buit" );
		}
	}
}
