package prop.hex.gestors;

import prop.hex.domini.models.UsuariHex;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Classe per la gestió de la persistència d'UsuariHexs en disc, com s'estén de BaseGstr,
 * unicament fa falta especificar la subcarpeta on guardar les dades dels usuaris.
 *
 * @author Javier Ferrer Gonzalez (Grup 7.3, Hex)
 */
public final class UsuariHexGstr extends BaseGstr<UsuariHex>
{

	/**
	 * Atribut per mantenir l'instancia Singleton de la classe
	 */
	private static UsuariHexGstr instancia = null;

	/**
	 * Constructora, simplement especifica la subcarpeta a on guardar els elements de tipus Usuari
	 */
	private UsuariHexGstr()
	{
		subcarpeta_dades = "usuaris";
	}

	/**
	 * Consultora de l'instancia actual del gestor de fitxers
	 * Si encara no s'ha inicialitzat l'objecte, crida a la constructora, si ja s'ha instanciat previament,
	 * simplement retorna l'instancia ja creada.
	 *
	 * @return L'instància de la classe Singleton.
	 */
	public static UsuariHexGstr getInstancia()
	{
		if ( null == instancia )
		{
			instancia = new UsuariHexGstr();
		}

		return instancia;
	}

	/**
	 * Guarda el fitxer de l'usuari.
	 * Pels usuaris utilitzem aquest mètode que realment implementa guardaElement de BaseGstr però semre passant-li
	 * l'identificador únic de l'usuari
	 *
	 * @return boolean true si s'ha pogut guardar correctament i el fitxer resultant es contempla com correcte,
	 *         false altrament
	 * @throws java.io.FileNotFoundException Si el fitxer no s'ha generat i no s'hi poden escriure les dades.
	 * @throws java.io.IOException           Si ha ocorregut un error d'entrada/sortida inesperat.
	 */
	public boolean guardaElement( UsuariHex usuari_hex ) throws FileNotFoundException, IOException
	{
		return super.guardaElement( usuari_hex, usuari_hex.getIdentificadorUnic() );
	}
}
