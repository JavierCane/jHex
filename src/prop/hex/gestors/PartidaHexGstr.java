package prop.hex.gestors;

import prop.hex.domini.models.PartidaHex;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe per la gestió de la persistència de PartidaHex en disc, com s'estén de BaseGstr,
 * únicament fa falta especificar la subcarpeta on guardar les dades de les partides.
 */
public class PartidaHexGstr extends BaseGstr<PartidaHex>
{

	/**
	 * Constructora, simplement especifica la subcarpeta a on guardar els elements de tipus PartidaHex.
	 */
	public PartidaHexGstr()
	{
		subcarpeta_dades = "partides";
	}

	/**
	 * Llista els identificadors de les partides jugades per l'usuari amb identificador únic id_usuari.
	 *
	 * @return El conjunt d'identificadors de partides jugades per l'usuari amb identificador únic id_usuari.
	 */
	public Set<String> llistaPartidesUsuari( String id_usuari )
	{
		File carpeta = new File( carpeta_dades + '/' + subcarpeta_dades + '/' );
		File[] llista_arxius = carpeta.listFiles();

		Set<String> noms_elements = new HashSet<String>();
		for ( File arxiu : llista_arxius )
		{
			String nom = arxiu.getName();
			if ( nom.endsWith( '.' + extensio_fitxers ) )
			{
				String id_partida = nom.substring( 0, nom.length() - ( 1 + extensio_fitxers.length() ) );
				String[] identificadors = id_partida.split( "@" );

				// Comprovem si l'usuari juga a la partida
				if ( identificadors[identificadors.length - 1] == id_usuari ||
				     identificadors[identificadors.length - 2] == id_usuari )
				{
					// Afegeixo el nom de l'element sense l'extensió al conjunt d'identificadors
					noms_elements.add( id_partida );
				}
			}
		}

		return noms_elements;
	}
}
