package prop.hex.gestors;

import prop.hex.domini.models.UsuariHex;

/**
 * Classe per la gestió de la persistència d'UsuariHexs en disc, com s'estén de BaseGstr,
 * unicament fa falta especificar la subcarpeta on guardar les dades dels usuaris.
 * <p/>
 * Date: 22/10/12
 */
public final class UsuariGstr extends BaseGstr<UsuariHex>
{

	/**
	 * Constructora, simplement especifica la subcarpeta a on guardar els elements de tipus Usuari
	 */
	public UsuariGstr()
	{
		this.subcarpeta_dades = "usuaris";
	}
}
