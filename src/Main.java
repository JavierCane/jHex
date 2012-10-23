import Dominio.Usuario;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Main
{

	/**
	 * Método principal
	 *
	 * @param args
	 */
	public static void main( String[] args )
	{
		testCrearUsuarios();

		testGuardarUsuarios();

		testCargarUsuarios();
	}

	private static void testCrearUsuarios()
	{
		Usuario main_user = new Usuario( "nombre", "pass" );

		System.out.println( main_user.getUsername() );

		try
		{
			Usuario guest_user = new Usuario( "Maquina_1", "passwd" );
		}
		catch ( IllegalArgumentException e )
		{
			System.err.println( "[ERROR]: " + e.getMessage() );
		}
	}

	private static void testGuardarUsuarios()
	{
		try
		{
			//
			// Create instances of FileOutputStream and ObjectOutputStream.
			//
			FileOutputStream usuarios_fos = new FileOutputStream( "dat/usuarios.dat" );
			ObjectOutputStream usuarios_oos = new ObjectOutputStream( usuarios_fos );

			//
			// Create a Book instance. This book object then will be stored in
			// the file.
			//
			Usuario main_user = new Usuario( "nombre", "pass" );

			//
			// By using writeObject() method of the ObjectOutputStream we can
			// make the book object persistent on the books.dat file.
			//
			usuarios_oos.writeObject( main_user );

			//
			// Flush and close the ObjectOutputStream.
			//
			usuarios_oos.flush();
			usuarios_oos.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	private static void testCargarUsuarios()
	{
		try
		{
			//
			// We have the book saved. Now it is time to read it back and display
			// its detail information.
			//
			FileInputStream usuarios_fis = new FileInputStream( "dat/usuarios.dat" );
			ObjectInputStream usuarios_ois = new ObjectInputStream( usuarios_fis );

			//
			// To read the Book object use the ObjectInputStream.readObject() method.
			// This method return Object type data so we need to cast it back the its
			// origin class, the Book class.
			//
			Usuario main_user = ( Usuario ) usuarios_ois.readObject();

			System.out.println( "Usuario cargado desde fichero usuarios.dat: " + main_user.toString() );

			usuarios_ois.close();
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
