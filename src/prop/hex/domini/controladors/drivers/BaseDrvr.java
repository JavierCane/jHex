package prop.hex.domini.controladors.drivers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixEnter;

/**
 * Classe principal per realitzar tots els tests dels drivers. Només s'ha d'executar aquesta classe i (mitjançant la
 * tècnica de reflexió) ja s'encarrega d'anar a buscar tots els drivers que n'hi hagin a
 * la carpeta corresponent (carpeta_drivers) i llistar els seus mètodes
 * <p/>
 * Date: 06/11/12
 */
public final class BaseDrvr
{

	private static final String carpeta_drivers = "./src/prop/hex/domini/controladors/drivers/";
	private static final String package_drivers = "prop.hex.domini.controladors.drivers.";

	private static Class driver_a_provar;
	private static String nom_driver_a_provar;
	private static LinkedList<Method> metodes_a_provar;

	private static boolean ha_seleccionat_driver = false;
	private static boolean vol_sortir_del_programa = false;
	private static boolean vol_sortir_al_menu_principal = false;

	/**
	 * Mètode principal, encarregat de mantenir el bucle d'execució
	 *
	 * @param args
	 */
	public static void main( String[] args )
	{
		while ( !vol_sortir_del_programa )
		{
			seleccionaDriverAProvar();

			if ( ha_seleccionat_driver && !vol_sortir_del_programa )
			{
				while ( !vol_sortir_al_menu_principal )
				{
					seleccionaMetodesAProvar();
				}
				vol_sortir_al_menu_principal = false;
			}
		}
	}

	/**
	 * Obté tots els fitxers de la carpeta_drivers que cumpleixin amb el filtrat de la classe
	 * FiltratLlistatDrivers, els llista per pantalla i estableix nom_driver_a_provar i driver_a_provar amb el valor
	 * selecionat.
	 */
	private static final void seleccionaDriverAProvar()
	{
		File[] llistat_drivers = obteLlistatFitxers();
		int iterador_dopcio = 1;

		System.out.println( "\n   _____________________________________________________________________________" );
		System.out.println( " /\n| Escull un driver a executar: \n|" );

		for ( File driver : llistat_drivers )
		{
			System.out.println( "| " + iterador_dopcio + ".- " +
			                    driver.getName().substring( 0, driver.getName().length() - 5 ) );

			iterador_dopcio++;
		}
		System.out.println( "|\n| 0.- Sortir del programa" );
		System.out.println( " \\ _____________________________________________________________________________\n" );

		int num_driver_a_provar = llegeixEnter();

		if ( num_driver_a_provar != 0 )
		{
			nom_driver_a_provar = llistat_drivers[num_driver_a_provar - 1].getName();
			nom_driver_a_provar = nom_driver_a_provar.substring( 0, nom_driver_a_provar.length() - 5 );

			String path_driver_a_provar = package_drivers + nom_driver_a_provar;

			try
			{
				driver_a_provar = Class.forName( path_driver_a_provar );
				ha_seleccionat_driver = true;
			}
			catch ( ClassNotFoundException excepcio )
			{
				System.out.println( "[KO]\tNo s'ha trobat la classe " + path_driver_a_provar );
				ha_seleccionat_driver = false;
			}
		}
		else
		{
			System.out.println( "Deu!" );
			vol_sortir_del_programa = true;
		}
	}

	/**
	 * Obté tots el mètodes de la classe driver_a_provar, llista els que siguin públics i executa el que l'usuari
	 * indiqui
	 */
	private static final void seleccionaMetodesAProvar()
	{
		int iterador_dopcio = 1;

		Method[] array_metodes_a_provar = driver_a_provar.getDeclaredMethods();

		// Els fico a una LinkedList per poder treure els que no siguin publics
		metodes_a_provar = new LinkedList<Method>( Arrays.asList( array_metodes_a_provar ) );

		netejaConsola();

		System.out.println( "   _____________________________________________________________________________" );
		System.out.println( " /\n| Escull una prova de la classe " + nom_driver_a_provar + " a realitzar: \n|" );

		// En aquest cas, faig ús d'un iterador perquè si no, mentre estic recorrent la LinkedList no podria fer el
		// remove()
		Iterator iterador_metodes = metodes_a_provar.iterator();
		while ( iterador_metodes.hasNext() )
		{
			Method metode_a_provar = ( Method ) iterador_metodes.next();

			if ( Modifier.isPublic( metode_a_provar.getModifiers() ) )
			{
				System.out.println( "| " + iterador_dopcio + ".- " + metode_a_provar.getName() );

				iterador_dopcio++;
			}
			else
			{
				iterador_metodes.remove();
			}
		}

		System.out.println( "|\n| 0.- Sortir al menú principal" );
		System.out.println( " \\ _____________________________________________________________________________\n" );

		int num_metode_a_provar = llegeixEnter();

		if ( num_metode_a_provar != 0 )
		{
			try
			{
				metodes_a_provar.get( num_metode_a_provar - 1 ).invoke( new Object() );
				System.out.println( "\nExecució del test finalitzada, pulsa \'intro\' per continuar." );
				System.in.read();
			}
			catch ( IllegalAccessException excepcio )
			{
				excepcio.printStackTrace();
			}
			catch ( InvocationTargetException excepcio )
			{
				excepcio.printStackTrace();
			}
			catch ( IOException excepcio )
			{
				excepcio.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}
		else
		{
			vol_sortir_al_menu_principal = true;
		}
	}

	/**
	 * Obté el llistat de fitxers del directori carpeta_drivers. Únicament retorna els fitxers que cumpleixen les
	 * normes establertes a FiltratLlistatDrivers.
	 *
	 * @return Array de File
	 */
	private static final File[] obteLlistatFitxers()
	{
		File directori_actual = new File( carpeta_drivers );

		return directori_actual.listFiles( new FiltratLlistatDrivers() );
	}

	/**
	 * Intenta netejar la pantalla de la consola mitjançant les comandes de sistemes Windows i Unix (cls i clear
	 * respectivament), si a on s'hi está executant el programa no soporta cap de les comandes, no ho fa.
	 */
	private static final void netejaConsola()
	{
		try
		{
			Runtime.getRuntime().exec( "cls" );
		}
		catch ( IOException excepcio_no_windows )
		{
			try
			{
				Runtime.getRuntime().exec( "clear" );
			}
			catch ( IOException excepcio_no_unix )
			{
				// En aquest cas no netejo la pantalla, tampoc ve d'aquí ara...
			}
		}
	}
}
