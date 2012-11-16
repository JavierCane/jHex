package prop.hex.domini.controladors;

import java.io.*;

import prop.hex.domini.models.UsuariHex;
import prop.hex.gestors.UsuariGstr;
import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.ModesInici;

public class UsuariCtrl
{

	/**
	 * Crea una instància d'UsuariHex amb el nom d'usuari i la contrasenya donats, sempre i quan no existeixi ja un
	 * usuari amb el mateix identificador al sistema,
	 *
	 * @param nom         Nom de l'usuari nou que es vol instanciar.
	 * @param contrasenya Contrasenya de l'usuari nou que es vol instanciar.
	 * @param registrat   Indica si es vol instanciar un usuari registrat o un intern del sistema.
	 * @return Un nou UsuariHex amb el nom i la contrasenya donats.
	 * @throws IllegalArgumentException Si el nom d'usuari ja existeix al sistema,
	 *                                  si conté caràcters il·legals o si es tracta d'un nom no permès.
	 */
	public UsuariHex creaUsuari( String nom, String contrasenya, boolean registrat ) throws IllegalArgumentException
	{
		UsuariGstr gestor_usuari = new UsuariGstr();

		if ( gestor_usuari.existeixElement( nom ) )
		{
			throw new IllegalArgumentException( "[KO]\tEl nom d'usuari ja existeix." );
		}
		if ( !nom.matches( UsuariHex.getCaractersPermesos() ) )
		{
			throw new IllegalArgumentException( "[KO]\tEl nom d'usuari conté caràcters il·legals." );
		}
		if ( registrat && UsuariHex.getNomsNoPermesos().contains( nom ) )
		{
			throw new IllegalArgumentException( "[KO]\tNo es permet utilitzar aquest nom d'usuari." );
		}
		else
		{
			UsuariHex usuari_hex = new UsuariHex( nom, contrasenya );
			return usuari_hex;
		}
	}

	/**
	 * Elimina un usuari existent al sistema.
	 *
	 * @param nom Nom de l'usuari que es vol eliminar.
	 * @return Cert, si s'ha pogut eliminar l'usuari del sistema. Fals, altrament.
	 * @throws IllegalArgumentException
	 */
	public boolean eliminaUsuari( String nom ) throws IllegalArgumentException
	{
		UsuariGstr gestor_usuari = new UsuariGstr();

		if ( !gestor_usuari.existeixElement( nom ) )
		{
			throw new IllegalArgumentException( "[KO]\tL'usuari no existeix." );
		}

		return gestor_usuari.eliminaElement( nom );
	}

	/**
	 * Carrega un usuari existent al sistema.
	 *
	 * @param nom         Nom de l'usuari que es vol carregar.
	 * @param contrasenya Contrasenya de l'usuari que es vol carregar.
	 * @return L'usuari corresponent al nom i la contrasenya donats.
	 * @throws IllegalArgumentException Si l'usuari identificat pel nom no existeix o si la contrasenya no
	 * coincideix amb l'usuari.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public UsuariHex carregaUsuari( String nom, String contrasenya ) throws IllegalArgumentException,
			FileNotFoundException, IOException, ClassNotFoundException
	{
		UsuariGstr gestor_usuari = new UsuariGstr();

		if ( !gestor_usuari.existeixElement( nom ) )
		{
			throw new IllegalArgumentException( "[KO]\tL'usuari no existeix." );
		}
		else
		{
			UsuariHex usuari = gestor_usuari.carregaElement( nom );
			if ( usuari.getContrasenya() != contrasenya )
			{
				throw new IllegalArgumentException( "[KO]\tLa contrasenya no és correcta." );
			}
			return usuari;
		}
	}

	/**
	 * Guarda un usuari al sistema.
	 *
	 * @param usuari Usuari que es vol guardar al sistema.
	 * @return Cert, si l'usuari s'han pogut guardar correctament. Fals, altrament.
	 * @throws IOException
	 */
	public boolean guardaUsuari( UsuariHex usuari ) throws IOException
	{
		UsuariGstr model_usuari = new UsuariGstr();

		return model_usuari.guardaElement( usuari, usuari.getIdentificadorUnic() );
	}

	/**
	 * Modifica la contrasenya d'un usuari.
	 *
	 * @param usuari      Usuari al que es vol modificar la contrasenya,
	 * @param contrasenya Contrasenya antiga de l'usuari que es vol modificar.
	 * @return Cert, si s'ha modificat la contrasenya. Fals, altrament.
	 * @throws IllegalArgumentException Si la contrasenya passada com a paràmetre no coincideix amb la contrasenya
	 * de l'usuari.
	 */
	public boolean modificaContrasenya( UsuariHex usuari, String contrasenya ) throws IllegalArgumentException
	{
		if ( usuari.getContrasenya() != contrasenya )
		{
			throw new IllegalArgumentException( "[KO]\tLa contrasenya introduïda no correspon a l'actual." );
		}

		return usuari.setContrasenya( contrasenya );
	}

	/**
	 * Modifica les preferències d'un usuari.
	 *
	 * @param usuari            Usuari al que es volen modificar les preferències.
	 * @param mode_inici        Mode d'inici que es vol donar a l'usuari.
	 * @param combinacio_colors Combinació de colors que es vol donar a l'usuari.
	 * @return Cert, si les preferències s'han modificat correctament. Fals, altrament.
	 */
	public boolean modificaPreferencies( UsuariHex usuari, ModesInici mode_inici,
	                                     CombinacionsColors combinacio_colors )
	{
		return ( usuari.setModeInici( mode_inici ) && usuari.setCombinacionsColors( combinacio_colors ) );
	}

	/**
	 * Modifica el temps mínim en una partida d'un usuari.
	 *
	 * @param usuari      Usuari al que es vol modificar el temps mínim.
	 * @param temps_minim Temps mínim d'una partida que es vol donar a l'usuari.
	 * @return Cert, si el temps mínim s'ha actualitzat. Fals, altrament.
	 */
	public boolean modificaTempsMinim( UsuariHex usuari, long temps_minim )
	{
		return usuari.setTempsMinim( temps_minim );
	}

	/**
	 * Modifica el nombre mínim de fitxes en una partida d'un usuari.
	 *
	 * @param usuari         Usuari al que es vol modificar nombre mínim de fitxes.
	 * @param fitxes_minimes Nombre mínim de fitxes en una partida que es vol donar a l'usuari.
	 * @return Cert, si el nombre mínim de fitxes s'ha actualitzat. Fals, altrament.
	 */
	public boolean modificaFitxesMinimes( UsuariHex usuari, int fitxes_minimes )
	{
		return usuari.setFitxesMinimes( fitxes_minimes );
	}

	/**
	 * Modifica el nombre de partides jugades d'un usuari.
	 *
	 * @param usuari           Usuari al que es vol modificar el temps mínim.
	 * @param partides_jugades Nombre de partides jugades que es vol donar a l'usuari.
	 * @return Cert, si el nombre de partides jugades s'ha actualitzat. Fals, altrament.
	 */
	public boolean modificaPartidesJugades( UsuariHex usuari, int partides_jugades )
	{
		return usuari.setPartidesJugades( partides_jugades );
	}

	/**
	 * Modifica el nombre de partides guanyades d'un usuari.
	 *
	 * @param usuari             Usuari al que es vol modificar el temps mínim.
	 * @param partides_guanyades Nombre de partides guanyades que es vol donar a l'usuari.
	 * @return Cert, si el nombre de partides guanyades s'ha actualitzat. Fals, altrament.
	 */
	public boolean modificaPartidesGuanyades( UsuariHex usuari, int partides_guanyades )
	{
		return usuari.setPartidesGuanyades( partides_guanyades );
	}
}
