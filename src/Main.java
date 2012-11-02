import Dades.UsuariModel;
import Domini.UsuariHex;

import java.io.IOException;

public class Main
{

	/**
	 * Método principal
	 *
	 * @param args
	 */
	public static void main( String[] args )
	{
//		testCrearUsuarios();

		testGuardarUsuario();

		testCargarUsuarios();
	}

	private static void testCrearUsuarios()
	{
		UsuariHex main_user = new UsuariHex( "nombre", "pass" );

		System.out.println( main_user.getNom() );

		try
		{
			UsuariHex guest_user = new UsuariHex( "Maquina_1", "passwd" );
		}
		catch ( IllegalArgumentException e )
		{
			System.err.println( "[ERROR]: " + e.getMessage() );
		}
	}

	private static void testGuardarUsuario()
	{
		UsuariModel model_usuari = new UsuariModel();

		UsuariHex main_user = new UsuariHex( "nombre1", "pass1" );

		try
		{
			model_usuari.guardaElement( main_user, main_user.getUsername() );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	private static void testCargarUsuarios()
	{
		UsuariModel model_usuari = new UsuariModel();

		try
		{
			UsuariHex main_user = model_usuari.carregaElement( "nombre1" );

			System.out.println( "[OK] Usuario cargado: " + main_user.toString() );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		catch ( ClassNotFoundException e )
		{
			e.printStackTrace();
		}
	}
}
