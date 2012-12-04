package prop.hex.domini.controladors.drivers;

import prop.hex.domini.models.Ranquing;
import prop.hex.domini.models.UsuariHex;
import prop.hex.domini.models.enums.TipusJugadors;
import prop.hex.gestors.RanquingGstr;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Proves dels mètodes de la classe Ranquing.
 */
public final class RanquingDrvr
{

	/**
	 * Usuaris de prova. Declarats con atributs de classe per poder anar-los actualitzant cada cop que s'executi el
	 * test "testModificaRanquing".
	 */
	private static UsuariHex usuari_0 = new UsuariHex( "Sin partidas", "Contraseña", TipusJugadors.JUGADOR );
	private static UsuariHex usuari_1 = new UsuariHex( "Derrotado 1", "Contraseña", TipusJugadors.JUGADOR );
	private static UsuariHex usuari_2 = new UsuariHex( "Derrotado 2", "Contraseña", TipusJugadors.JUGADOR );
	private static UsuariHex usuari_3 = new UsuariHex( "Derrotado 3", "Contraseña", TipusJugadors.JUGADOR );
	private static UsuariHex usuari_4 = new UsuariHex( "Ganador 1", "Contraseña", TipusJugadors.JUGADOR );
	private static UsuariHex usuari_5 = new UsuariHex( "Ganador 2", "Contraseña", TipusJugadors.JUGADOR );
	private static UsuariHex usuari_6 = new UsuariHex( "Ganador 3", "Contraseña", TipusJugadors.JUGADOR );
	private static UsuariHex usuari_7 = new UsuariHex( "Derrotado-Ganador 1", "Contraseña", TipusJugadors.JUGADOR );
	private static UsuariHex usuari_8 = new UsuariHex( "Derrotado-Ganador 2", "Contraseña", TipusJugadors.JUGADOR );
	private static UsuariHex usuari_9 = new UsuariHex( "Derrotado-Ganador 2", "Contraseña", TipusJugadors.JUGADOR );

	/**
	 * Mostra el rànquing actual
	 */
	public static void testConsultaRanquing()
	{
		Ranquing ranquing = Ranquing.getInstancia();

		System.out.println( "[OK]\tActualment el rànquing té les seguents dades:\n\t\t" + ranquing.toString() );
	}

	/**
	 * Modifica el rànquing simulant que els usuaris de la classe han jugat noves partides
	 */
	public static void testModificaRanquing()
	{
		Ranquing ranquing = Ranquing.getInstancia();

		testConsultaRanquing();

		ranquing.actualitzaUsuari( usuari_0 );

		usuari_1.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_FACIL, 10L, 10 );
		ranquing.actualitzaUsuari( usuari_1 );

		usuari_2.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_QUEENBEE, 10L, 10 );
		ranquing.actualitzaUsuari( usuari_2 );

		usuari_3.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_FACIL, 10L, 10 );
		usuari_3.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_QUEENBEE, 10L, 10 );
		ranquing.actualitzaUsuari( usuari_3 );

		usuari_4.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_FACIL, 10L, 10 );
		ranquing.actualitzaUsuari( usuari_4 );

		usuari_5.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_QUEENBEE, 10L, 10 );
		ranquing.actualitzaUsuari( usuari_5 );

		usuari_6.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_FACIL, 10L, 10 );
		usuari_6.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_QUEENBEE, 10L, 10 );
		ranquing.actualitzaUsuari( usuari_6 );

		usuari_7.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_FACIL, 10L, 10 );
		usuari_7.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_FACIL, 10L, 10 );
		ranquing.actualitzaUsuari( usuari_7 );

		usuari_8.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_QUEENBEE, 10L, 10 );
		usuari_8.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_QUEENBEE, 10L, 10 );
		ranquing.actualitzaUsuari( usuari_8 );

		usuari_9.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_FACIL, 10L, 10 );
		usuari_9.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_QUEENBEE, 10L, 10 );
		usuari_9.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_FACIL, 10L, 10 );
		usuari_9.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_QUEENBEE, 10L, 10 );
		ranquing.actualitzaUsuari( usuari_9 );

		System.out.println( "[OK]\tBateria de modificacions fetes correctament." );

		testConsultaRanquing();
	}

	/**
	 * Neteja el rànquing actual deixant-lo en blanc
	 */
	public static void testNetejaRanquing()
	{
		Ranquing ranquing = Ranquing.getInstancia();

		ranquing.netejaRanquing();

		if ( ranquing.getClasificacio().isEmpty() )
		{
			System.out.println( "[OK]\tS'ha netejat correctament el rànquing." );
		}
		else
		{
			System.out.println( "[KO]\tNo s'ha pogut netejar el rànquing." );
		}
	}

	/**
	 * Guarda l'instància de rànquing a disc.
	 */
	public static void testGuardaRanquing()
	{
		RanquingGstr gestor_ranquing = new RanquingGstr();

		try
		{
			if ( gestor_ranquing.guardaElement() )
			{
				System.out.println( "[OK]\tS'ha guardat correctament el fitxer del rànquing." );
			}
			else
			{
				System.out.println( "[OK]\tS'ha produit un error intentant guardar el fitxer del rànquing." );
			}
		}
		catch ( IOException e )
		{
			System.out.println( "[KO]\tS'ha produit un error intentant accedir al fitxer del rànquing." );
			e.printStackTrace();
		}
	}

	/**
	 * Carrega el rànquing de disc.
	 */
	public static void testCarregaRanquing()
	{
		RanquingGstr gestor_ranquing = new RanquingGstr();

		try
		{
			Ranquing ranquing = gestor_ranquing.carregaElement();

			System.out.println( "[OK]\tS'ha carregat correctament el fitxer del rànquing." );

			testConsultaRanquing();
		}
		catch ( FileNotFoundException excepcio )
		{
			System.out.println( "[OK]\tEl rànquing no existeix al sistema: " + excepcio.getMessage() );
		}
		catch ( IOException excepcio )
		{
			System.out.println( "[KO]\tS'ha produit un error intentant accedir al fitxer del rànquing." );
		}
		catch ( ClassNotFoundException excepcio )
		{
			System.out.println(
					"[KO]\tError no esperat de tipus ClassNotFoundException intentant accedir al fitxer del rànquing" +
					"." );
		}
	}

	/**
	 * Elimina el fitxer de rànquing de disc.
	 */
	public static void testEliminaRanquing()
	{
		RanquingGstr gestor_ranquing = new RanquingGstr();

		if ( gestor_ranquing.eliminaElement( ) )
		{
			System.out.println( "[OK]\tS'ha eliminat correctament el fitxer del rànquing." );
		}
		else
		{
			System.out.println( "[KO]\tNo s'ha pogut eliminar el fitxer del rànquing." );
		}
	}
}
