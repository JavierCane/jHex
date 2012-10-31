import Dominio.Tablero;
import Dominio.Usuario;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JFrame;

public class Main
{
	/**
	 * Método principal
	 *
	 * @param args
	 */

	public static Tablero tabla = new Tablero(7);

	public static void main( String[] args )
	{
		JFrame mainFrame = new JFrame("Graphics demo");

		tabla.colocarFicha(1, 2, 2);
		tabla.colocarFicha(0, 2, 3);

		mainFrame.getContentPane().add( new DrawTablero() );
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

}
