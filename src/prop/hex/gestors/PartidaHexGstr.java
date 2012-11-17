package prop.hex.gestors;

import prop.hex.domini.models.PartidaHex;

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
}
