package prop.hex.gestors;

import prop.hex.domini.models.PartidaHex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Classe per la gestió de la persistència de PartidaHex en disc, com s'estén de BaseGstr,
 * únicament fa falta especificar la subcarpeta on guardar les dades de les partides.
 */
public final class PartidaHexGstr extends BaseGstr<PartidaHex>
{

	/**
	 * Constructora, simplement especifica la subcarpeta a on guardar els elements de tipus PartidaHex.
	 */
	public PartidaHexGstr()
	{
		subcarpeta_dades = "partides";
	}

	/**
	 * Guarda el fitxer de la partida
	 *
	 * @return boolean true si s'ha pogut guardar correctament i el fitxer resultant es contempla com correcte,
	 *         false altrament
	 * @throws java.io.FileNotFoundException Si el fitxer no s'ha generat i no s'hi poden escriure les dades.
	 * @throws java.io.IOException           Si ha ocorregut un error d'entrada/sortida inesperat.
	 */
	public boolean guardaElement( PartidaHex partida_hex ) throws FileNotFoundException, IOException
	{
		return super.guardaElement( partida_hex, partida_hex.getIdentificadorUnic() );
	}

	/**
	 * Llista els identificadors de les partides jugades per l'usuari amb identificador únic id_usuari.
	 *
	 * @return El conjunt d'identificadors de partides jugades per l'usuari amb identificador únic id_usuari
	 *         ordenades per data de creació.
	 */
	public Set<String> llistaPartidesUsuari( String id_usuari )
	{
		File carpeta = new File( carpeta_dades + '/' + subcarpeta_dades + '/' );
		File[] llista_arxius = carpeta.listFiles();

		Set<String> noms_elements = new TreeSet<String>();
		if ( llista_arxius != null )
		{
			for ( File arxiu : llista_arxius )
			{
				String nom = arxiu.getName();
				if ( nom.endsWith( '.' + extensio_fitxers ) )
				{
					String id_partida = nom.substring( 0, nom.length() - ( 1 + extensio_fitxers.length() ) );
					String[] identificadors = id_partida.split( "@" );

					// Comprovem si l'usuari juga a la partida
					if ( identificadors[identificadors.length - 1].equals( id_usuari ) ||
					     identificadors[identificadors.length - 2].equals( id_usuari ) )
					{
						// Afegim el nom de l'element sense l'extensió al conjunt d'identificadors
						noms_elements.add( id_partida );
					}
				}
			}
		}

		return noms_elements;
	}
}
