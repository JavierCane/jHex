package prop.hex.domini.controladors.drivers;

import java.io.File;
import java.io.FileFilter;

/**
 * Created with IntelliJ IDEA.
 * User: javierferrer
 * Date: 06/11/12
 * Time: 22:45
 * To change this template use File | Settings | File Templates.
 */
public class FiltratDrivers implements FileFilter
{

	@Override
	public boolean accept( File fitxer )
	{
		String nom_fitxer = fitxer.getName();

		return !nom_fitxer.equals( "BaseDrvr.java" ) && !nom_fitxer.equals( "FiltratDrivers.java" ) &&
				!nom_fitxer.equals( "UtilsDrvr.java" ) && nom_fitxer.endsWith( ".java" );
	}
}
