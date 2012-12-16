package prop.hex.domini.controladors.drivers;

import java.io.File;
import java.io.FileFilter;

/**
 * Classe auxiliar per poder filtrar el llistat de fitxers a mostrar a la llista de drivers a executar per part del
 * PrincipalDrvr.
 * <p/>
 * Implementa l'operació accept de FileFilter amb tal de restringir que únicament es llistin fitxers .java i que no
 * siguin el propi PrincipalDrvr, aquest fitxer de filtrat i el d'utilitats pels drivers.
 *
 * @author Javier Ferrer Gonzalez (Grup 7.3, Hex)
 */
public final class FiltratLlistatDrivers implements FileFilter
{

	/**
	 * Determina quins fitxers seran acceptats a l'hora d'implementar el filtrat.
	 * Simplement volem llistar tots els fitxers java que no siguin els de classes "auxiliars".
	 *
	 * @param fitxer Fitxer a mirar si acceptem o no
	 * @return true o false depenent de si acceptem o no el fitxer per ser llistat
	 */
	@Override
	public boolean accept( File fitxer )
	{
		String nom_fitxer = fitxer.getName();

		return !nom_fitxer.equals( "PrincipalDrvr.java" ) && !nom_fitxer.equals( "FiltratLlistatDrivers.java" ) &&
		       !nom_fitxer.equals( "UtilsDrvr.java" ) && nom_fitxer.endsWith( ".java" );
	}
}
