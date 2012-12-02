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
	 * Instància del controlador.
	 */
	private static UsuariCtrl instancia;

	/**
	 * Instància amb l'usuari identificat actualment al joc.
	 */
	private UsuariHex usuari_actual;

	private UsuariHex usuari_jugador_a;

	private UsuariHex usuari_jugador_b;

	/**
	 * Instància del gestor d'usuaris en disc.
	 */
	private static UsuariGstr gestor_usuari = new UsuariGstr();

	/**
	 * Constructor per defecte. Declarat privat perquè és una classe singleton
	 */
	private UsuariCtrl()
	{
		gestor_usuari = new UsuariGstr();
	}

	private String getIdentificadorUnic( String nom )
	{
		return nom.replace( ' ', '-' );
	}

	/**
	 * Consultora de l'instancia actual del controlador de partida.
	 * Si encara no s'ha inicialitzat l'objecte, crida el constructor. Si ja s'ha instanciat prèviament,
	 * simplement retorna l'instància ja creada.
	 *
	 * @return L'objecte singleton amb el el controlador de partida.
	 */
	public static synchronized UsuariCtrl getInstancia()
	{
		if ( instancia == null )
		{
			instancia = new UsuariCtrl();
		}

		return instancia;
	}

	public UsuariHex getUsuariActual()
	{
		return usuari_actual;
	}

	public UsuariHex getUsuariJugadorA()
	{
		return usuari_jugador_a;
	}

	public UsuariHex getUsuariJugadorB()
	{
		return usuari_jugador_b;
	}

	/**
	 * Guarda a memòria secundària el fitxer associat a l'UsuariHex d'un jugador amb el nom d'usuari i la
	 * contrasenya donats o un
	 * usuari de la IA, sempre i quan no existeixi ja un usuari amb el mateix identificador al sistema.
	 *
	 * @param nom           Nom de l'usuari nou que es vol instanciar.
	 * @param contrasenya   Contrasenya de l'usuari nou que es vol instanciar.
	 * @param tipus_jugador Tipus de l'usuari que es vol instanciar.
	 * @return Cert, si s'ha creat correctament l'usuari. Fals, altrament.
	 * @throws IllegalArgumentException Si el nom d'usuari ja existeix al sistema,
	 *                                  si conté caràcters il·legals o si es tracta d'un nom no permès.
	 * @throws IOException              Si ha succeït un error d'entrada/sortida inesperat.
	 */
	public boolean creaUsuari( String nom, String contrasenya, TipusJugadors tipus_jugador ) throws
			IllegalArgumentException, IOException
	{
		if ( tipus_jugador == TipusJugadors.JUGADOR )
		{
			if ( !nom.matches( UsuariHex.getCaractersPermesos() ) )
			{
				throw new IllegalArgumentException( "[KO]\tEl nom d'usuari conté caràcters il·legals. Només " +
						"s'accepten caràcters alfanumèris (sense accents), espais i guions baixos." );
			}

			if ( UsuariHex.getNomsNoPermesos().contains( nom ) )
			{
				throw new IllegalArgumentException( "[KO]\tNo es permet utilitzar aquest nom d'usuari. Els noms no " +
						"permesos són " + UsuariHex.getNomsNoPermesos().toString() );
			}

			UsuariHex usuari_hex = new UsuariHex( nom, contrasenya );
			if ( gestor_usuari.existeixElement( usuari_hex.getIdentificadorUnic() ) )
			{
				throw new IllegalArgumentException( "[KO]\tEl nom d'usuari ja existeix." );
			}
			else
			{
				if ( !gestor_usuari.guardaElement( usuari_hex, usuari_hex.getIdentificadorUnic() ) )
				{
					throw new IOException( "[KO]\tNo s'ha pogut guardar el jugador." );
				}
			}
		}
		else
		{
			UsuariIAHex usuari_ia = new UsuariIAHex( tipus_jugador );
			if ( !gestor_usuari.guardaElement( usuari_ia, usuari_ia.getIdentificadorUnic() ) )
			{
				throw new IOException( "[KO]\tNo s'ha pogut guardar el jugador." );
			}
		}
		return true;
	}

	public boolean entraConvidat()
	{
		usuari_actual = new UsuariHex( "Convidat", "" );
		return true;
	}

	/**
	 * Elimina un usuari existent al sistema.
	 *
	 * @param usuari Usuari que es vol eliminar.
	 * @return Cert, si s'ha pogut eliminar l'usuari del sistema. Fals, altrament.
	 * @throws IllegalArgumentException Si l'usuari que passem con a paràmetre no existeix al sistema.
	 */
	public boolean eliminaUsuari( UsuariHex usuari ) throws IllegalArgumentException
	{
		if ( !gestor_usuari.existeixElement( usuari.getIdentificadorUnic() ) )
		{
			throw new IllegalArgumentException( "[KO]\tL'usuari no existeix." );
		}
		boolean es_eliminat = gestor_usuari.eliminaElement( usuari.getIdentificadorUnic() );
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
	 * @return Cert, si l'usuari s'ha carregat correctament. Fals, altrament.
	 * @throws IllegalArgumentException Si l'usuari identificat pel nom no existeix
	 *                                  i, si es vol carregar un jugador, si la contrasenya no coincideix amb
	 *                                  l'usuari.
	 * @throws FileNotFoundException    Si el fitxer no s'ha generat i no s'han pogut escriure les dades.
	 * @throws IOException              IOException Si ha succeït un error d'entrada/sortida inesperat.
	 * @throws ClassNotFoundException   Si hi ha un problema de classes quan es carrega l'usuari.
	 * @throws NullPointerException     Es dona si el fitxer està buit.
	 */
	public boolean carregaUsuari( String nom, String contrasenya, TipusJugadors tipus_jugador ) throws
			IllegalArgumentException, FileNotFoundException, IOException, ClassNotFoundException, NullPointerException
	{
		if ( !gestor_usuari.existeixElement( getIdentificadorUnic( nom ) ) )
		{
			throw new IllegalArgumentException( "[KO]\tL'usuari no existeix." );
		}
		else
		{
			UsuariHex usuari = gestor_usuari.carregaElement( getIdentificadorUnic( nom ) );
			if ( tipus_jugador == TipusJugadors.JUGADOR )
			{
				if ( UsuariHex.getNomsNoPermesos().contains( nom ) )
				{
					throw new IllegalArgumentException( "[KO]\tL'usuari demanat és intern del sistema." );
				}
				if ( !usuari.getContrasenya().equals( contrasenya ) )
				{
					throw new IllegalArgumentException( "[KO]\tLa contrasenya no és correcta." );
				}
			}
			usuari_actual = usuari;
		}
		return true;
	}

	public boolean carregaJugadorsPartida( TipusJugadors jugador_a, String nom, String contrasenya,
	                                       TipusJugadors jugador_b ) throws IllegalArgumentException,
			FileNotFoundException, IOException, ClassNotFoundException, NullPointerException
	{
		if ( TipusJugadors.JUGADOR == jugador_a )
		{
			usuari_jugador_a = usuari_actual;
		}
		else
		{
			usuari_jugador_a = gestor_usuari.carregaElement( getIdentificadorUnic( jugador_a.getNomUsuari() ) );
		}

		if ( TipusJugadors.JUGADOR == jugador_b )
		{
			if ( !gestor_usuari.existeixElement( getIdentificadorUnic( nom ) ) )
			{
				throw new IllegalArgumentException( "L'usuari introduit com Jugador 2 no està registrat." );
			}
			else
			{
				UsuariHex usuari = gestor_usuari.carregaElement( getIdentificadorUnic( nom ) );

				if ( UsuariHex.getNomsNoPermesos().contains( nom ) )
				{
					throw new IllegalArgumentException( "L'usuari introduit com Jugador 2 és intern del sistema." );
				}
				else
				{
					if ( !usuari.getContrasenya().equals( contrasenya ) )
					{
						throw new IllegalArgumentException( "La contrasenya introduïda per al Jugador 2 no és correcta." );
					}
					else
					{
						usuari_jugador_b = usuari;
					}
				}
			}
		}
		else
		{
			usuari_jugador_b = gestor_usuari.carregaElement( getIdentificadorUnic( jugador_b.getNomUsuari() ) );
		}

		return true;
	}

	/**
	 * Guarda un l'usuari actual a memòria secundària.
	 *
	 * @return Cert, si l'usuari s'han pogut guardar correctament. Fals, altrament.
	 * @throws IOException           Si ha succeït un error d'entrada/sortida inesperat.
	 * @throws FileNotFoundException Si el fitxer no s'ha generat i no s'han pogut escriure les dades.
	 */
	public boolean guardaUsuari() throws IOException, FileNotFoundException
	{

		return gestor_usuari.guardaElement( usuari_actual, usuari_actual.getIdentificadorUnic() );
	}

	/**
	 * Modifica la contrasenya de l'usuari actual.
	 *
	 * @param contrasenya_antiga Contrasenya antiga de l'usuari que es vol modificar.
	 * @param contrasenya_nova   Contrasenya nova de l'usuari que es vol modificar.
	 * @return Cert, si s'ha modificat la contrasenya. Fals, altrament.
	 * @throws IllegalArgumentException Si la contrasenya antiga passada com a paràmetre no coincideix amb la
	 *                                  contrasenya de l'usuari.
	 */
	public boolean modificaContrasenya( String contrasenya_antiga, String contrasenya_nova ) throws
			IllegalArgumentException
	{
		if ( !usuari_actual.getContrasenya().equals( contrasenya_antiga ) )
		{
			throw new IllegalArgumentException( "[KO]\tLa contrasenya actual introduïda no correspon a l'actual de " +
					"" + "l'usuari." );
		}
		else
		{
			return usuari_actual.setContrasenya( contrasenya_nova );
		}
	}

	/**
	 * Modifica les preferències d'un usuari.
	 *
	 * @param mode_inici        Mode d'inici que es vol donar a l'usuari.
	 * @param combinacio_colors Combinació de colors que es vol donar a l'usuari.
	 * @return Cert, si les preferències s'han modificat correctament. Fals, altrament.
	 */
	public boolean modificaPreferencies( ModesInici mode_inici, CombinacionsColors combinacio_colors )
	{
		return ( usuari_actual.setModeInici( mode_inici ) && usuari_actual.setCombinacionsColors( combinacio_colors
		) );
	}

	public ModesInici obteModeInici()
	{
		return usuari_actual.getModeInici();
	}

	public CombinacionsColors obteCombinacioDeColors()
	{
		return usuari_actual.getCombinacionsColors();
	}

	public String obteNom()
	{
		return usuari_actual.getNom();
	}

	public Object[] obteEstadistiquesUsuari( UsuariHex usuari )
	{
		return new Object[] {
				usuari.getNom(), usuari.getPartidesJugades(), usuari.getPartidesGuanyades(),
				usuari.getPuntuacioGlobal()
		};
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
	public void actualitzaEstadistiques( UsuariHex usuari, boolean ha_guanyat, TipusJugadors jugador_contrari,
	                                     Long temps_emprat, Integer fitxes_usades )
	{
		usuari.recalculaDadesUsuariPartidaFinalitzada( ha_guanyat, jugador_contrari, temps_emprat, fitxes_usades );
	}

	/**
	 * Reinicia les estadístiques de l'usuari actual.
	 *
	 * @return Cert, si s'han reiniciat les estadístiques. Fals, altrament.
	 */
	public boolean reiniciaEstadistiques()
	{
		usuari_actual.reiniciaEstadistiques();
		Ranquing.getInstancia().actualitzaUsuari( usuari_actual );
		return true;
	}
}
