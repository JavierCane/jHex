package prop.hex.gestors;

import prop.hex.domini.models.PartidaHex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Classe per la gestió de la persistència de PartidaHex en disc. Com que s'estén de BaseGstr,
 * únicament fa falta especificar la subcarpeta on guardar les dades de les partides.
 *
 * @author Isaac Sánchez Barrera (Grup 7.3, Hex)
 */
public final class PartidaHexGstr extends BaseGstr<PartidaHex>
{

	/**
	 * Atribut per mantenir l'instancia Singleton de la classe
	 */
	private static PartidaHexGstr instancia = null;

	/**
	 * Constructora, simplement especifica la subcarpeta a on guardar els elements de tipus PartidaHex.
	 */
	private PartidaHexGstr()
	{
		subcarpeta_dades = "partides";
	}

	/**
	 * Consultora de l'instancia actual del gestor de fitxers
	 * Si encara no s'ha inicialitzat l'objecte, crida a la constructora, si ja s'ha instanciat previament,
	 * simplement retorna l'instancia ja creada.
	 *
	 * @return L'instància de la classe Singleton.
	 */
	public static PartidaHexGstr getInstancia()
	{
		if ( null == instancia )
		{
			instancia = new PartidaHexGstr();
		}

		return instancia;
	}

	/**
	 * Guarda el fitxer de la partida. El nom del fitxer és l'identificador únic de la partida.
	 *
	 * @return Cert si s'ha pogut guardar correctament i el fitxer resultant es contempla com correcte. Fals altrament.
	 * @throws FileNotFoundException Si el fitxer no s'ha generat i no s'hi poden escriure les dades.
	 * @throws IOException           Si ha ocorregut un error d'entrada/sortida inesperat.
	 * @see BaseGstr#guardaElement(Object, String)
	 * @see PartidaHex#getIdentificadorUnic()
	 */
	public boolean guardaElement( PartidaHex partida_hex ) throws FileNotFoundException, IOException
	{
		return super.guardaElement( partida_hex, partida_hex.getIdentificadorUnic() );
	}

	/**
	 * Llista els identificadors de les partides jugades per un usuari.
	 *
	 * @return El conjunt d'identificadors de partides jugades per l'usuari amb identificador únic id_usuari,
	 *         ordenades per data de creació. Si no n'hi ha, retorna el conjunt buit.
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
