package prop.hex.domini.controladors.drivers;

import prop.hex.domini.models.UsuariHex;
import prop.hex.gestors.UsuariGstr;

import java.io.FileNotFoundException;
import java.io.IOException;

import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixEnter;
import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixParaula;

public class UsuariGstrDrvr
{

	/**
	 * Método principal
	 *
	 * @param args
	 */
	public static void main( String[] args )
	{
		int opcio = 0;

		while ( opcio != 9 )
		{
			System.out.println();
			System.out.println( "------------------------------------------------------" );
			System.out.println( "Proves de la clase UsuariGstr i per tant d'UsuariHex" );
			System.out.println( "Escull una opció:" );
			System.out.println( "1 - Instanciació nou UsuariHex" );
			System.out.println( "2 - Guarda un UsuariHex a disc" );
			System.out.println( "3 - Carrega un UsuariHex de disc" );
			System.out.println();
			System.out.println( "9 - Surt del programa" );

			opcio = llegeixEnter();

			switch ( opcio )
			{
				case 1:
					testInstanciaUsuariHex();
					break;
				case 2:
					testGuardaUsuariHex();
					break;
				case 3:
					testCarregaUsuariHex();
					break;
				case 9:
					break;
				default:
					System.out.println( "No és una opció vàlida.\n" );
			}
		}
	}

	private static UsuariHex testInstanciaUsuariHex()
	{
		String nom = llegeixParaula( "Escriu el nom de l'usuari a instanciar:\nPer provar casos extrems, " +
		                             "recorda que els noms no permesos son: \n\t" +
		                             UsuariHex.getNomsNoPermesos().toString() );

		String contrasenya = llegeixParaula( "Escriu la contrasenya de l'usuari a instanciar:" );

		try
		{
			UsuariHex usuari_hex = new UsuariHex( nom, contrasenya );
			System.out.println(
					"[OK]\tS'ha instanciat correctament l'usuari. Les seves dades son:\n\t" + usuari_hex.toString() );
			return usuari_hex;
		}
		catch ( IllegalArgumentException e )
		{
			System.err.println( "[KO]\t" + e.getMessage() );
			return null;
		}
	}

	private static void testGuardaUsuariHex()
	{
		UsuariGstr model_usuari = new UsuariGstr();

		UsuariHex usuari_hex = testInstanciaUsuariHex();

		try
		{
			if ( model_usuari.guardaElement( usuari_hex, usuari_hex.getIdentificadorUnic() ) )
			{
				System.out.println( "[OK]\tS'ha guardat correctament el fitxer de l'usuari amb identificador: " +
				                    usuari_hex.getIdentificadorUnic() );
			}
			else
			{
				System.out.println(
						"[KO]\tS'ha produit un error intentant guardar al fitxer de l'usuari amb identificador: " +
						usuari_hex.getIdentificadorUnic() );
			}
		}
		catch ( IOException e )
		{
			System.out.println(
					"[KO]\tS'ha produit un error intentant accedir al fitxer de l'usuari amb identificador: " +
					usuari_hex.getIdentificadorUnic() );
			e.printStackTrace();
		}
	}

	private static void testCarregaUsuariHex()
	{
		UsuariGstr model_usuari = new UsuariGstr();

		String nom_usuari = "nom2";

		try
		{
			UsuariHex main_user = model_usuari.carregaElement( nom_usuari );

			System.out.println( "[OK] S'ha guardat correctament el fitxer de l'usuari " + nom_usuari + ", " +
			                    "les seves dades son: \n\t" + main_user.toString() );
		}
		catch ( FileNotFoundException excepcio )
		{
			System.out.println( "[KO] L'usuari que s'intenta carregar (" + nom_usuari + "), no existeix al sistema." );
		}
		catch ( IOException excepcio )
		{
			System.out.println( "[KO] S'ha produit un error intentant accedir al fitxer de l'usuari " + nom_usuari );
			excepcio.printStackTrace();
		}
		catch ( ClassNotFoundException excepcio )
		{
			System.out.println(
					"[KO] Error no esperat de tipus ClassNotFoundException intentant accedir al fitxer de l'usuari " +
					nom_usuari );
			excepcio.printStackTrace();
		}
	}
}
