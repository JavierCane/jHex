package prop.hex.domini.controladors.drivers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Classe d'utilitats varies usades desde els drivers. Classe completament estàtica.
 *
 * @author Isaac Sánchez Barrera (Grup 7.3, Hex)
 */
public final class UtilsDrvr
{

	/**
	 * Constructor per defecte. Amb visibilitat privada ja que es tracta d'una classe d'utilitats/metodes estàtics.
	 */
	private UtilsDrvr()
	{
		throw new AssertionError();
	}

	/**
	 * Llegeix un enter de la línia de comandes.
	 * <p/>
	 * Si hi ha problemes de lectura, mostra l'error per pantalla.
	 *
	 * @return L'enter que s'ha llegit. Si hi ha hagut errors, retorna un 0.
	 */
	public static int llegeixEnter()
	{
		BufferedReader entrada = new BufferedReader( new InputStreamReader( System.in ) );

		try
		{
			return Integer.parseInt( entrada.readLine() );
		}
		catch ( NumberFormatException excepcio )
		{
			System.err.println( "El nombre introduit no es vàlid." );
			return 0;
		}
		catch ( IOException excepcio )
		{
			System.err.println( "No s'ha pogut llegir l'enter per el buffer d'entrada." );
			return 0;
		}
	}

	/**
	 * Llegeix un enter de la línia de comandes.
	 *
	 * @param capcalera Frase descriptiva sobre l'enter que es vol llegir.
	 * @return L'enter que s'ha llegit. Si hi ha hagut errors, retorna un 0.
	 * @see #llegeixEnter()
	 */
	public static int llegeixEnter( String capcalera )
	{
		System.out.println( capcalera );
		return llegeixEnter();
	}

	/**
	 * Llegeix una paraula de la línia de comandes.
	 *
	 * @return La paraula que s'ha llegit. Si hi ha hagut errors, retorna una cadena buida ("").
	 */
	public static String llegeixParaula()
	{
		BufferedReader entrada = new BufferedReader( new InputStreamReader( System.in ) );

		try
		{
			return entrada.readLine();
		}
		catch ( IOException excepcio )
		{
			System.err.println( "No s'ha pogut llegir la paraula per el buffer d'entrada." );
			return "";
		}
	}

	/**
	 * Llegeix una paraula de la línia de comandes.
	 *
	 * @param capcalera Frase descriptiva sobre la paraula que es vol llegir.
	 * @return La paraula que s'ha llegit. Si hi ha hagut errors, retorna una cadena buida ("").
	 * @see #llegeixParaula()
	 */
	public static String llegeixParaula( String capcalera )
	{
		System.out.println( capcalera );
		return llegeixParaula();
	}
}
