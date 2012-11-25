package prop.hex.gestors;

import prop.hex.domini.models.Ranquing;

/**
 * Classe per la gestió de la persistència de Ranquing en disc, com s'estén de BaseGstr,
 * unicament fa falta especificar la subcarpeta on guardar les dades del rànquing.
 */
public final class RanquingGstr extends BaseGstr<Ranquing>
{

	/**
	 * Constructora, simplement especifica la subcarpeta a on guardar els elements de tipus Ranquing
	 */
	public RanquingGstr()
	{
		subcarpeta_dades = "ranquing";
	}
}
