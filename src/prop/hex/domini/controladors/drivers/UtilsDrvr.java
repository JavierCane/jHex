package prop.hex.domini.controladors.drivers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Classe d'utilitats varies usades desde els drivers. Classe completament estàtica.
 * <p/>
 * Date: 06/11/12
 */
public final class UtilsDrvr
{

	// Eliminem la possibilitat d'instanciacio ja que es tracta d'una classe d'utilitats/metodes estatics
	private UtilsDrvr()
	{
		throw new AssertionError();
	}

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

	public static int llegeixEnter( String capcalera )
	{
		System.out.println( capcalera );
		return llegeixEnter();
	}

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

	public static String llegeixParaula( String capcalera )
	{
		System.out.println( capcalera );
		return llegeixParaula();
	}
}
