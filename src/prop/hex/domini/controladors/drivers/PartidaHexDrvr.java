package prop.hex.domini.controladors.drivers;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;

import java.util.ArrayList;

import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixEnter;
import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixParaula;

/**
 * Proves dels mètodes de la classe PartidaHex.
 */
public final class PartidaHexDrvr
{

	/**
	 * Llistat de partides
	 */
	private static ArrayList<PartidaHex> partides = new ArrayList<PartidaHex>();

	/**
	 * Conté l'índex de la partida actual
	 */
	private static int actual;

	/**
	 * Fa una nova instància de PartidaHex amb la mida i el nom especificats. Canvia la partida actual a la nova.
	 */
//	public static void testInstanciaPartida()
//	{
//		int mida = llegeixEnter( "Introdueix la mida del tauler a utilitzar en la partida:" );
//		String nom = llegeixParaula( "Introdueix el nom de la partida:" );
//		partides.add( new PartidaHex( new UsuariHex( "Nom_jugador_a", "Contrasenya_jugador_a", TipusJugadors.JUGADOR ),
//				new UsuariHex( "Nom_jugador_b", "Contrasenya_jugador_b", TipusJugadors.JUGADOR ), new TaulerHex( mida ), nom ) );
//		actual = partides.size() - 1;
//		System.out.println( "[OK]\tS'ha instanciat la partida amb èxit.\n\tLa partida actual és: " + actual + "" +
//		                    ".\n\tLes seves característiques són:\n" + partides.get( actual ).toString() );
//	}

	/**
	 * Canvia el valor de l'índex de la partida a un altre
	 */
	public static void canviaPartida()
	{
		if ( partides.size() > 0 )
		{
			System.out.println(
					"Escriu el número de la partida que vols (entre 0 i " + Integer.toString( partides.size() - 1 ) +
					"):" );
			int num = llegeixEnter();

			if ( num < 0 || num >= partides.size() )
			{
				System.out.println( "[KO]\tLa partida seleccionada no existeix. No es canvia de partida.\n\tLa " +
				                    "partida actual és: " + actual );
			}
			else
			{
				actual = num;
				System.out.println( "[OK]\tS'ha canviat de partida amb èxit.\n\tLa partida actual és: " + actual +
				                    ".\n\tLes seves característiques són:\n" + partides.get( actual ).toString() );
			}
		}
		else
		{
			System.out.println( "[KO]\tNo existeix cap partida!" );
		}
	}

	/**
	 * Seleciona un tauler per a la partida d'entre tres taulers predefinits.
	 */
	public static void seleccionaTauler()
	{
		if ( partides.size() > 0 )
		{
			System.out.println( "Escriu el número de tauler que vols (entre 0 i 2):" );

			int num = llegeixEnter();

			if ( num < 0 || num > 2 )
			{
				System.out.println(
						"[KO]\tEl tauler seleccionat no existeix. No es modifica el tauler de la " + "partida." );
			}
			else
			{
				switch ( num )
				{
					case 0:
						int mida = partides.get( actual ).getTauler().getMida();
						for ( int columna = 0; columna < mida; columna++ )
						{
							for ( int fila = 0; fila < mida; fila++ )
							{
								if ( partides.get( actual ).getTauler().getEstatCasella( fila, columna ) !=
								     EstatCasella.BUIDA )
								{
									partides.get( actual ).getTauler().treuFitxa( fila, columna );
								}
							}
						}
						break;
					case 1:
						mida = partides.get( actual ).getTauler().getMida();
						for ( int columna = 0; columna < mida; columna++ )
						{
							for ( int fila = 0; fila < mida; fila++ )
							{
								if ( partides.get( actual ).getTauler().getEstatCasella( fila, columna ) !=
								     EstatCasella.BUIDA )
								{
									partides.get( actual ).getTauler().treuFitxa( fila, columna );
								}
							}
						}
						for ( int columna = 0; columna < mida; columna++ )
						{
							for ( int fila = 0; fila < mida; fila++ )
							{
								if ( fila == 0 )
								{
									partides.get( actual ).getTauler()
											.mouFitxa( EstatCasella.JUGADOR_A, fila, columna );
								}
								else
								{
									if ( columna == 0 )
									{
										partides.get( actual ).getTauler()
												.mouFitxa( EstatCasella.JUGADOR_B, fila, columna );
									}
								}
							}
						}
						break;
					case 2:
						mida = partides.get( actual ).getTauler().getMida();
						for ( int columna = 0; columna < mida; columna++ )
						{
							for ( int fila = 0; fila < mida; fila++ )
							{
								if ( partides.get( actual ).getTauler().getEstatCasella( fila, columna ) !=
								     EstatCasella.BUIDA )
								{
									partides.get( actual ).getTauler().treuFitxa( fila, columna );
								}
							}
						}
						for ( int columna = 0; columna < mida; columna++ )
						{
							for ( int fila = 0; fila < mida; fila++ )
							{
								if ( columna == 0 )
								{
									partides.get( actual ).getTauler()
											.mouFitxa( EstatCasella.JUGADOR_B, fila, columna );
								}
								else
								{
									if ( fila == 0 )
									{
										partides.get( actual ).getTauler()
												.mouFitxa( EstatCasella.JUGADOR_A, fila, columna );
									}
								}
							}
						}
						break;
				}
				System.out.println( "[OK]\tS'ha modificat correctament el tauler de la partida. El tauler actual " +
				                    "és:\n" + ( ( TaulerHex ) ( partides.get( actual ).getTauler() ) ).toString() );
			}
			;
		}
		else
		{
			System.out.println( "[KO]\tNo existeix cap partida!" );
		}
	}

	/**
	 * Comprova l'estat de la partida actual.
	 */
	public static void testEstatPartida()
	{
		if ( partides.size() > 0 )
		{
			int fila = llegeixEnter( "Escriu la fila de la casella:" );

			int columna = llegeixEnter( "Escriu la columna de la casella:" );

			try
			{
				EstatPartida estat_partida = partides.get( actual ).comprovaEstatPartida( fila, columna );
				System.out.println( "[OK]\tS'ha calculat correctament l'estat de la partida, " +
				                    "que és: " + estat_partida + "." );
			}
			catch ( IndexOutOfBoundsException excepcio )
			{
				System.out.println( "[KO]\tNo s'ha pogut calcular l'estat de la partida: " + excepcio.getMessage() );
			}
		}
		else
		{
			System.out.println( "[KO]\tNo existeix cap partida!" );
		}
	}

	/**
	 * Comprova el funcionament dels getters i els setters.
	 */
	public static void testGettersSetters()
	{
		if ( partides.size() > 0 )
		{
			System.out.println(
					"L'identificador únic de la partida és: " + partides.get( actual ).getIdentificadorUnic() +
					"\nEl nombre màxim de pistes és: " + partides.get( actual ).getMaxNumPistes() + "." );
			System.out.println( "Els caràcters permesos als noms de les partides són els que compleixen l'expressió " +
			                    partides.get( actual ).getCaractersPermesos() );
			System.out.println( "La combinació de colores de la partida és " +
			                    partides.get( actual ).getCombinacioColors().toString() + " i el mode d'inici és " +
			                    partides.get( actual ).getModeInici().toString() + "." );
			int jugador = llegeixEnter(
					"Escriu el número del jugador al que vols modificar els " + "paràmetres:" );
			int pistes = llegeixEnter( "Escriu la quantitat de pistes que vols donar al jugador:" );
			try
			{
				partides.get( actual ).incrementaPistesUsades( jugador, pistes );
				System.out.println( "[OK]\tS'han modificat correctament les pistes usades del jugador amb " +
				                    "identificador únic " + jugador + "\nEl nombre actual de pistes és " +
				                    partides.get( actual ).getPistesUsades( jugador ) + "." );
				int quantitat = llegeixEnter( "Escriu la quantitat de pistes que vols afegir:" );
				try
				{
					partides.get( actual ).incrementaPistesUsades( jugador, quantitat );
					System.out.println( "[OK]\tS'han incrementat correctament les pistes usades del jugador amb " +
					                    "identificador únic " + jugador + ", afegint " + quantitat +
					                    " pistes.\nEl nombre " +
					                    "actual de pistes és " + partides.get( actual ).getPistesUsades( jugador ) +
					                    "." );
					int temps = llegeixEnter( "Escriu el temps de joc que vols assignar a l'usuari:" );
					try
					{
						partides.get( actual ).setTempsDeJoc( jugador, temps );
						System.out.println( "[OK]\tS'ha modificat correctament el temps de joc del jugador amb" +
						                    "identificador únic " + jugador + "." );
						int quantitat_temps = llegeixEnter( "Escriu el temps de joc que vols afegir a l'usuari:" );
						try
						{
							partides.get( actual ).incrementaTempsDeJoc( jugador, quantitat_temps );
							System.out.println( "[OK]\tS'ha incrementat correctament el temps de joc del jugador " +
							                    "amb identificador únic " + jugador + ", " +
							                    "afegint " + quantitat_temps + ".\nEl temps de joc actual és " +
							                    partides.get( actual ).getTempsDeJoc( jugador ) + "." );
						}
						catch ( IllegalArgumentException excepcio )
						{
							System.out.println( "[KO]\tNo s'ha pogut incrementar correctament el temps de joc del" +
							                    "jugador amb identificador únic " + jugador + ": " +
							                    excepcio.getMessage() );
						}
					}
					catch ( IllegalArgumentException excepcio )
					{
						System.out.println( "[KO]\tNo s'ha pogut modificar correctament el temps de joc del jugador" +
						                    " amb identificador únic " + jugador + ": " + excepcio.getMessage() );
					}
				}
				catch ( IllegalArgumentException excepcio )
				{
					System.out.println( "[KO]\tNo s'han pogut incrementar correctament les pistes del jugador amb " +
					                    "identificador únic " + jugador + ": " + excepcio.getMessage() );
				}
				System.out.println( "\tEl nombre de pistes actuals del jugador amb identificador únic " + jugador +
				                    " és " + partides.get( actual ).getPistesUsades( jugador ) +
				                    " i el temps de joc és " +
				                    partides.get( actual ).getTempsDeJoc( jugador ) );
			}
			catch ( IllegalArgumentException excepcio )
			{
				System.out.println( "[KO]\tNo s'han pogut incrementar correctament les pistes del jugador amb " +
				                    "identificador únic " + jugador + ": " + excepcio.getMessage() );
			}
		}
		else
		{
			System.out.println( "[KO]\tNo existeix cap partida!" );
		}
	}
}
