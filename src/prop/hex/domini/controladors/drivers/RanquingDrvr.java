package prop.hex.domini.controladors.drivers;

import prop.hex.domini.models.Ranquing;
import prop.hex.domini.models.UsuariHex;
import prop.hex.domini.models.enums.TipusJugadors;

/**
 * Proves dels mètodes de la classe Ranquing.
 */
public final class RanquingDrvr
{

	public static void testCreaInstanciaRanquing()
	{
		Ranquing ranquing = Ranquing.getInstancia();

		System.out.println(
				"[OK]\tS'ha instanciat correctament el rànquing. Les seves dades son:\n\t\t" + ranquing.toString() );

		Ranquing ranquing_2 = Ranquing.getInstancia();

		System.out.println(
				"[OK]\tS'ha demanat una altra instància de rànquing, les seves haurien de ser les mateixes degut a " +
				"que és una classe Singleton, aquí estan:\n\t\t" + ranquing_2.toString() );
	}

	public static void testModificaRanquing()
	{
		Ranquing ranquing = Ranquing.getInstancia();

		System.out.println( "[OK]\tActulment el rànquing té les seguents dades:\n\t\t" + ranquing.toString() );

//		UsuariHex usuari = new UsuariHex( "Actualizado después", "Contraseña" );
//		ranquing.actualitzaUsuari( usuari );
//
//		UsuariHex usuari_0 = new UsuariHex( "Sin partidas", "Contraseña" );
//		ranquing.actualitzaUsuari( usuari_0 );

		UsuariHex usuari_1 = new UsuariHex( "Derrotado 1", "Contraseña" );
		usuari_1.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_FACIL, (long) 10.2, 10 );
		ranquing.actualitzaUsuari( usuari_1 );

//		UsuariHex usuari_2 = new UsuariHex( "Derrotado 2", "Contraseña" );
//
//		usuari_2.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_DIFICIL, 10L, 10 );
//
//		System.out.println( "partides jugades: " + usuari_2.getPartidesJugades() + ", derrotes difícil: " +
//		                    usuari_2.getDerrotes( TipusJugadors.IA_DIFICIL.getPosicioDificultat() ) + ", " +
//		                    "puntuació global: " + usuari_2.getPuntuacioGlobal() );
//
//		ranquing.actualitzaUsuari( usuari_2 );
//
//		UsuariHex usuari_3 = new UsuariHex( "Derrotado 3", "Contraseña" );
//		usuari_3.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_FACIL, (long) 10.2, 10 );
//		usuari_3.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_DIFICIL, (long) 10.2, 10 );
//		ranquing.actualitzaUsuari( usuari_3 );
//
//		UsuariHex usuari_4 = new UsuariHex( "Ganador 1", "Contraseña" );
//		usuari_4.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_FACIL, (long) 10.2, 10 );
//		ranquing.actualitzaUsuari( usuari_4 );
//
//		UsuariHex usuari_5 = new UsuariHex( "Ganador 2", "Contraseña" );
//		usuari_5.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_DIFICIL, (long) 10.2, 10 );
//		ranquing.actualitzaUsuari( usuari_5 );
//
//		UsuariHex usuari_6 = new UsuariHex( "Ganador 3", "Contraseña" );
//		usuari_6.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_FACIL, (long) 10.2, 10 );
//		usuari_6.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_DIFICIL, (long) 10.2, 10 );
//		ranquing.actualitzaUsuari( usuari_6 );
//
//		UsuariHex usuari_7 = new UsuariHex( "Derrotado-Ganador 1", "Contraseña" );
//		usuari_7.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_FACIL, (long) 10.2, 10 );
//		usuari_7.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_FACIL, (long) 10.2, 10 );
//		ranquing.actualitzaUsuari( usuari_7 );
//
//		UsuariHex usuari_8 = new UsuariHex( "Derrotado-Ganador 2", "Contraseña" );
//		usuari_8.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_DIFICIL, (long) 10.2, 10 );
//		usuari_8.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_DIFICIL, (long) 10.2, 10 );
//		ranquing.actualitzaUsuari( usuari_8 );
//
//		UsuariHex usuari_9 = new UsuariHex( "Derrotado-Ganador 2", "Contraseña" );
//		usuari_9.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_FACIL, (long) 10.2, 10 );
//		usuari_9.recalculaDadesUsuariPartidaFinalitzada( false, TipusJugadors.IA_DIFICIL, (long) 10.2, 10 );
//		usuari_9.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_FACIL, (long) 10.2, 10 );
//		usuari_9.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_DIFICIL, (long) 10.2, 10 );
//		ranquing.actualitzaUsuari( usuari_9 );
//
//		usuari.recalculaDadesUsuariPartidaFinalitzada( true, TipusJugadors.IA_FACIL, (long) 10.2, 10 );
//		ranquing.actualitzaUsuari( usuari );

		System.out.println( "[OK]\tDesprés de la bateria de modificacions de dades, el rànquing queda així:\n\t\t" +
		                    ranquing.toString() );
	}

	/**
	 * Primerament crida a testInstanciaUsuariHex per després intentar guardar aquest UsuariHex a disc.
	 */
//	public static void testGuardaUsuariHex()
//	{
//		UsuariGstr gestor_usuari = new UsuariGstr();
//
//		UsuariHex usuari_hex = testInstanciaUsuariHex();
//
//		try
//		{
//			if ( gestor_usuari.guardaElement( usuari_hex, usuari_hex.getIdentificadorUnic() ) )
//			{
//				System.out.println( "[OK]\tS'ha guardat correctament el fitxer de l'usuari amb identificador: " +
//				                    usuari_hex.getIdentificadorUnic() );
//			}
//			else
//			{
//				System.out.println(
//						"[KO]\tS'ha produit un error intentant guardar al fitxer de l'usuari amb identificador: " +
//						usuari_hex.getIdentificadorUnic() );
//			}
//		}
//		catch ( IOException e )
//		{
//			System.out.println(
//					"[KO]\tS'ha produit un error intentant accedir al fitxer de l'usuari amb identificador: " +
//					usuari_hex.getIdentificadorUnic() );
//			e.printStackTrace();
//		}
//	}

	/**
	 * Carrega un UsuariHex desde disc. Tracta totes les possibles excepcions.
	 */
//	public static void testCarregaUsuariHex()
//	{
//		UsuariGstr model_usuari = new UsuariGstr();
//
//		String nom_usuari = llegeixParaula( "Escriu el nom de l'usuari a carregar de disc:" );
//
//		try
//		{
//			UsuariHex main_user = model_usuari.carregaElement( nom_usuari );
//
//			System.out.println( "[OK]\tS'ha carregat correctament el fitxer de l'usuari " + nom_usuari + ", " +
//			                    "les seves dades son: \n\t\t" + main_user.toString() );
//		}
//		catch ( FileNotFoundException excepcio )
//		{
//			System.out.println( "[OK]\tL'usuari que s'intenta carregar (" + nom_usuari + "), " +
//			                    "no existeix al sistema: " + excepcio.getMessage() );
//		}
//		catch ( IOException excepcio )
//		{
//			System.out.println( "[KO]\tS'ha produit un error intentant accedir al fitxer de l'usuari " + nom_usuari );
//		}
//		catch ( ClassNotFoundException excepcio )
//		{
//			System.out.println(
//					"[KO]\tError no esperat de tipus ClassNotFoundException intentant accedir al fitxer de l'usuari " +
//					nom_usuari );
//		}
//	}

	/**
	 * Elimina un UsuariHex desde disc.
	 */
//	public static void testEliminaUsuariHex()
//	{
//		UsuariGstr model_usuari = new UsuariGstr();
//
//		String nom_usuari = llegeixParaula( "Escriu el nom de l'usuari a eliminar de disc:" );
//
//		if ( model_usuari.eliminaElement( nom_usuari ) )
//		{
//			System.out.println( "[OK]\tS'ha eliminat correctament el fitxer de l'usuari " + nom_usuari + "." );
//		}
//		else
//		{
//			System.out.println( "[KO]\tNo s'ha pogut eliminar el fitxer de l'usuari " + nom_usuari + "." );
//		}
//	}
}
