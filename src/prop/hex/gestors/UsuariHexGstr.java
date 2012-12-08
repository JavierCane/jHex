package prop.hex.gestors;

import prop.hex.domini.models.UsuariHex;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Classe per la gestió de la persistència d'UsuariHexs en disc, com s'estén de BaseGstr,
 * unicament fa falta especificar la subcarpeta on guardar les dades dels usuaris.
 */
public final class UsuariHexGstr extends BaseGstr<UsuariHex>
{

	/**
	 * Constructora, simplement especifica la subcarpeta a on guardar els elements de tipus Usuari
	 */
	public UsuariHexGstr()
	{
		subcarpeta_dades = "usuaris";
	}

	/**
	 * Guarda el fitxer de la partida
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
