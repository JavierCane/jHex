package prop.hex.domini.controladors.drivers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixEnter;

public class BaseDrvr
{

	private static Class driver_a_provar;
	private static Method[] metodes_a_provar;

	private static boolean ha_seleccionat_driver = false;

	/**
	 * Método principal
	 *
	 * @param args
	 */
	public static void main( String[] args )
	{
		seleccionaDriverAProvar();

		if ( ha_seleccionat_driver )
		{
			seleccionaMetodesAProvar();
		}
	}

	private static void seleccionaDriverAProvar()
	{
		System.out.println();
		System.out.println( "------------------------------------------------------" );
		System.out.println( "Escull un driver a executar: " );
		System.out.println( "1.- TaulerHexDrvr" );
		System.out.println( "2.- UsuariGstrDrvr" );
		System.out.println( "\n9.- Sortir del programa" );

		int opcio = llegeixEnter();

		switch ( opcio )
		{
			case 1:
				try
				{
					driver_a_provar = Class.forName( "prop.hex.domini.controladors.drivers.TaulerHexDrvr" );
					ha_seleccionat_driver = true;
				}
				catch ( ClassNotFoundException excepcio )
				{
					System.out.println(
							"[KO]\tNo s'ha trobat la classe prop.hex.domini.controladors.drivers.TaulerHexDrvr" );
				}
				break;
			case 2:
				try
				{
					driver_a_provar = Class.forName( "prop.hex.domini.controladors.drivers.UsuariGstrDrvr" );
					ha_seleccionat_driver = true;
				}
				catch ( ClassNotFoundException excepcio )
				{
					System.out.println(
							"[KO]\tNo s'ha trobat la classe prop.hex.domini.controladors.drivers.UsuariGstrDrvr" );
				}
				break;
			case 9:
				break;
			default:
				System.out.println( "L'opcio " + opcio + " no és una opció vàlida.\n" );
		}
	}

	private static void seleccionaMetodesAProvar()
	{
		int iterador_dopcio = 0;

		metodes_a_provar = driver_a_provar.getDeclaredMethods();

		System.out.println();
		System.out.println( "------------------------------------------------------" );
		System.out.println( "Escull una prova a realitzar: " );

		for ( Method metode_a_provar : metodes_a_provar )
		{
			if ( 0 != iterador_dopcio ) // No imprimeixo l'opcio del métode main
			{
				System.out.println( iterador_dopcio + ".- " + metode_a_provar.getName() );
			}

			iterador_dopcio++;
		}
		System.out.println( "\n9.- Sortir del programa" );

		int num_metode_a_provar = llegeixEnter();

		try
		{
			metodes_a_provar[num_metode_a_provar].setAccessible( true );
			metodes_a_provar[num_metode_a_provar].invoke( null, null );
		}
		catch ( IllegalAccessException e )
		{
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		catch ( InvocationTargetException e )
		{
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}
}
