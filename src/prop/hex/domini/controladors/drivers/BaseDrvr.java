package prop.hex.domini.controladors.drivers;

import java.io.File;
import java.io.FilenameFilter;
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
		File[] llistat_drivers = obteLlistatFitxers();
		int iterador_dopcio = 1;

		System.out.println();
		System.out.println( "------------------------------------------------------" );
		System.out.println( "Escull un driver a executar: " );

		for ( File driver : llistat_drivers )
		{
			System.out.println( iterador_dopcio + ".- " + driver.getName().substring( 0, driver.getName().length() - 5 ) );

			iterador_dopcio++;
		}
		System.out.println( "\n9.- Sortir del programa" );

		int num_driver_a_provar = llegeixEnter();

		if ( num_driver_a_provar != 9 )
		{
			String nom_driver_a_provar = llistat_drivers[num_driver_a_provar-1].getName();
			String path_driver_a_provar = "prop.hex.domini.controladors.drivers." + nom_driver_a_provar.substring(
					0, nom_driver_a_provar.length()-5 );

			try
			{
				driver_a_provar = Class.forName( path_driver_a_provar );
				ha_seleccionat_driver = true;
			}
			catch ( ClassNotFoundException excepcio )
			{
				System.out.println( "[KO]\tNo s'ha trobat la classe " + path_driver_a_provar );
			}
		}
	}

	private static File[] obteLlistatFitxers()
	{
		File directori_actual = new File( "./src/prop/hex/domini/controladors/drivers/" );

		return directori_actual.listFiles( new FiltratDrivers() );
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
