package prop.hex.domini.controladors;

import prop.hex.domini.models.Ranquing;
import prop.hex.domini.models.UsuariHex;
import prop.hex.domini.models.UsuariIAHex;
import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.ModesInici;
import prop.hex.domini.models.enums.TipusJugadors;
import prop.hex.gestors.UsuariGstr;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Controlador d'usuaris per al joc Hex.
 * Gestiona totes les operacions relacionades amb la creació i modificació, a més de la càrrega i l'emmagatzemament a
 * memòria secundària.
 */
public class UsuariCtrl
{

	/**
	 * Instància del gestor d'usuaris en disc.
	 */
	private static UsuariGstr gestor_usuari = new UsuariGstr();

	/**
	 * Crea una instància d'UsuariHex associada a un jugador amb el nom d'usuari i la contrasenya donats o un
	 * usuari de la IA,
	 * sempre i quan no existeixi ja un usuari amb el mateix identificador al sistema.
	 *
	 * @param nom           Nom de l'usuari nou que es vol instanciar.
	 * @param contrasenya   Contrasenya de l'usuari nou que es vol instanciar.
	 * @param tipus_jugador Tipus de l'usuari que es vol instanciar.
	 * @param registrat     Indica si es vol instanciar un usuari registrat o un usuari convidat.
	 * @return Un nou UsuariHex, i si es tracta d'un jugador, amb el nom i la contrasenya donats.
	 * @throws IllegalArgumentException Si el nom d'usuari ja existeix al sistema,
	 *                                  si conté caràcters il·legals o si es tracta d'un nom no permès.
	 */
	public static UsuariHex creaUsuari( String nom, String contrasenya, TipusJugadors tipus_jugador,
	                                    boolean registrat ) throws IllegalArgumentException
	{
		if ( tipus_jugador == TipusJugadors.JUGADOR )
		{
			if ( gestor_usuari.existeixElement( nom ) )
			{
				throw new IllegalArgumentException( "[KO]\tEl nom d'usuari ja existeix." );
			}

			if ( !nom.matches( UsuariHex.getCaractersPermesos() ) )
			{
				throw new IllegalArgumentException( "[KO]\tEl nom d'usuari conté caràcters il·legals. Només " +
						"s'accepten caràcters alfanumèris (sense accents), espais i guions baixos." );
			}

			if ( registrat && UsuariHex.getNomsNoPermesos().contains( nom ) )
			{
				throw new IllegalArgumentException( "[KO]\tNo es permet utilitzar aquest nom d'usuari. Els noms no " +
						"permesos són " + UsuariHex.getNomsNoPermesos().toString() );
			}
			else
			{
				UsuariHex usuari_hex = new UsuariHex( nom, contrasenya );
				return usuari_hex;
			}
		}
		else
		{
			return new UsuariIAHex( tipus_jugador );
		}
	}

	/**
	 * Elimina un usuari existent al sistema.
	 *
	 * @param usuari Usuari que es vol eliminar.
	 * @return Cert, si s'ha pogut eliminar l'usuari del sistema. Fals, altrament.
	 * @throws IllegalArgumentException Si l'usuari que passem con a paràmetre no existeix al sistema.
	 */
	public static boolean eliminaUsuari( UsuariHex usuari ) throws IllegalArgumentException
	{
		if ( !gestor_usuari.existeixElement( usuari.getNom() ) )
		{
			throw new IllegalArgumentException( "[KO]\tL'usuari no existeix." );
		}
		boolean es_eliminat = gestor_usuari.eliminaElement( usuari.getNom() );
		if ( es_eliminat )
		{
			Ranquing.getInstancia().eliminaUsuari( usuari );
		}
		return es_eliminat;
	}

	/**
	 * Carrega un usuari existent al sistema.
	 *
	 * @param nom           Nom de l'usuari que es vol carregar.
	 * @param contrasenya   Contrasenya de l'usuari que es vol carregar.
	 * @param tipus_jugador Tipus del jugador que es vol carregar.
	 * @return L'usuari corresponent al nom i la contrasenya donats.
	 * @throws IllegalArgumentException Si l'usuari identificat pel nom no existeix
	 *                                  i, si es vol carregar un jugador, si la contrasenya no coincideix amb
	 *                                  l'usuari.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static UsuariHex carregaUsuari( String nom, String contrasenya, TipusJugadors tipus_jugador ) throws
			IllegalArgumentException, FileNotFoundException, IOException, ClassNotFoundException
	{
		if ( !gestor_usuari.existeixElement( nom ) )
		{
			throw new IllegalArgumentException( "[KO]\tL'usuari no existeix." );
		}
		else
		{
			UsuariHex usuari = gestor_usuari.carregaElement( nom );
			if ( tipus_jugador == TipusJugadors.JUGADOR )
			{
				if ( UsuariHex.getNomsNoPermesos().contains( nom ) )
				{
					throw new IllegalArgumentException( "[KO]\tL'usuari demanat és intern del sistema." );
				}
				if ( usuari.getContrasenya() != contrasenya )
				{
					throw new IllegalArgumentException( "[KO]\tLa contrasenya no és correcta." );
				}
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
	public static boolean guardaUsuari( UsuariHex usuari ) throws IOException
	{

		return gestor_usuari.guardaElement( usuari, usuari.getIdentificadorUnic() );
	}

	/**
	 * Modifica la contrasenya d'un usuari.
	 *
	 * @param usuari             Usuari al que es vol modificar la contrasenya,
	 * @param contrasenya_antiga Contrasenya antiga de l'usuari que es vol modificar.
	 * @param contrasenya_nova   Contrasenya nova de l'usuari que es vol modificar.
	 * @return Cert, si s'ha modificat la contrasenya. Fals, altrament.
	 * @throws IllegalArgumentException Si la contrasenya antiga passada com a paràmetre no coincideix amb la
	 *                                  contrasenya de l'usuari.
	 */
	public static boolean modificaContrasenya( UsuariHex usuari, String contrasenya_antiga,
	                                           String contrasenya_nova ) throws IllegalArgumentException
	{
		if ( usuari.getContrasenya() != contrasenya_antiga )
		{
			throw new IllegalArgumentException( "[KO]\tLa contrasenya actual introduïda no correspon a l'actual de " +
					"l'usuari." );
		}
		else
		{
			return usuari.setContrasenya( contrasenya_nova );
		}
	}

	/**
	 * Modifica les preferències d'un usuari.
	 *
	 * @param usuari            Usuari al que es volen modificar les preferències.
	 * @param mode_inici        Mode d'inici que es vol donar a l'usuari.
	 * @param combinacio_colors Combinació de colors que es vol donar a l'usuari.
	 * @return Cert, si les preferències s'han modificat correctament. Fals, altrament.
	 */
	public static boolean modificaPreferencies( UsuariHex usuari, ModesInici mode_inici,
	                                            CombinacionsColors combinacio_colors )
	{
		return ( usuari.setModeInici( mode_inici ) && usuari.setCombinacionsColors( combinacio_colors ) );
	}

	/**
	 * Actualitza les estadístiques d'un usuari després de jugar una partida.
	 *
	 * @param usuari           Usuari al que es volen actualitzar les estadístiques.
	 * @param ha_guanyat       Indica si l'usuari ha guanyat la partida.
	 * @param jugador_contrari Dificultat del contrincant.
	 * @param temps_emprat     Temps de joc de l'usuari a la partida.
	 * @param fitxes_usades    Fitxes utilitzades per l'usuari a la partida.
	 */
	public static void actualitzaEstadistiques( UsuariHex usuari, boolean ha_guanyat,
	                                            TipusJugadors jugador_contrari, Long temps_emprat,
	                                            Integer fitxes_usades )
	{
		usuari.recalculaDadesUsuariPartidaFinalitzada( ha_guanyat, jugador_contrari, temps_emprat, fitxes_usades );
	}

	/**
	 * Reinicia les estadístiques d'un usuari en una certa dificultat.
	 *
	 * @param usuari Usuari al que es volen reiniciar les estadístiques.
	 * @return Cert, si s'han reiniciat les estadístiques. Fals, altrament.
	 */
	public static boolean reiniciaEstadistiques( UsuariHex usuari )
	{
		usuari.reiniciaEstadistiques();
		Ranquing.getInstancia().actualitzaUsuari( usuari );
		return true;
	}
}
