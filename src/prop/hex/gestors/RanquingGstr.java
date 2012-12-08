package prop.hex.gestors;

import prop.hex.domini.models.Ranquing;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Classe per la gestió de la persistència de Ranquing en disc, com s'estén de BaseGstr,
 * unicament fa falta especificar la subcarpeta on guardar les dades del rànquing.
 */
public final class RanquingGstr extends BaseGstr<Ranquing>
{

	private static String nom_ranquing = "ranquing";

	/**
	 * Constructora, simplement especifica la subcarpeta a on guardar els elements de tipus Ranquing
	 */
	public RanquingGstr()
	{
		subcarpeta_dades = "ranquing";
	}

	/**
	 * Guarda el fitxer del rànquing
	 *
	 * @return boolean true si s'ha pogut guardar correctament i el fitxer resultant es contempla com correcte,
	 *         false altrament
	 * @throws java.io.FileNotFoundException Si el fitxer no s'ha generat i no s'hi poden escriure les dades.
	 * @throws java.io.IOException           Si ha ocorregut un error d'entrada/sortida inesperat.
	 */
	public boolean guardaElement() throws FileNotFoundException, IOException
	{
		return super.guardaElement( Ranquing.getInstancia(), nom_ranquing );
	}

	/**
	 * Carrega el fitxer del rànquing
	 *
	 * @return L'objecte de l'element carregat
	 * @throws IOException            Si hi ha un problema d'entrada/sortida.
	 * @throws ClassNotFoundException Si no es troba la classe de l'objecte materialitzat
	 * @throws NullPointerException   Si el fitxer que es vol llegir és buit.
	 */
	public Ranquing carregaElement() throws IOException, ClassNotFoundException, NullPointerException
	{
		return super.carregaElement( nom_ranquing );
	}

	/**
	 * Comprova si existeix el fitxer del rànquing
	 *
	 * @return boolean Dependenent de si existeix o no l'element buscat tornarà true o false.
	 */
	public boolean existeixElement()
	{
		return super.existeixElement( nom_ranquing );
	}
}
