package prop.hex.domini.controladors.drivers;

import prop.hex.domini.models.UsuariHex;
import prop.hex.gestors.UsuariGstr;

import java.io.FileNotFoundException;
import java.io.IOException;

public class UsuariGstrDrvr
{

	/**
	 * Método principal
	 *
	 * @param args
	 */
	public static void main( String[] args )
	{
		testGuardarUsuario();

		testCargarUsuarios();
	}

	private static void testGuardarUsuario()
	{
		UsuariGstr model_usuari = new UsuariGstr();

		String nom_usuari = "nom1";

		UsuariHex main_user = new UsuariHex( nom_usuari, "pass1" );

		try
		{
			if ( model_usuari.guardaElement( main_user, main_user.getIdentificadorUnic() ) )
			{
				System.out.println( "[OK] S'ha guardat correctament el fitxer de l'usuari " + nom_usuari +
				                    ", les seves dades son: \n\t" + main_user.toString() );
			}
			else
			{
				System.out
						.println( "[KO] S'ha produit un error intentant guardar al fitxer de l'usuari " + nom_usuari );
			}
		}
		catch ( IOException e )
		{
			System.out.println( "[KO] S'ha produit un error intentant accedir al fitxer de l'usuari " + nom_usuari );
			e.printStackTrace();
		}
	}

	private static void testCargarUsuarios()
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
