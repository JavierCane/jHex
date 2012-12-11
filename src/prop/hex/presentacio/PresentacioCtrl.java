package prop.hex.presentacio;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.PartidaCtrl;
import prop.hex.domini.controladors.UsuariCtrl;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.Ranquing;
import prop.hex.domini.models.UsuariHex;
import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.ModesInici;
import prop.hex.domini.models.enums.TipusJugadors;
import prop.hex.presentacio.auxiliars.VistaDialeg;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Controlador de presentació del joc Hex.
 * Inicialitza totes les estructures relacionades amb la interfície i s'encarrega de la comunicació entre les vistes
 * i la capa de domini.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */

public final class PresentacioCtrl
{

	/**
	 * Instància del controlador.
	 */
	private static PresentacioCtrl instancia;

	/**
	 * Constructor per defecte. Declarat privat perquè és una classe singleton
	 */
	private PresentacioCtrl()
	{
	}

	/**
	 * Consultora de l'instancia actual del controlador de partida.
	 * Si encara no s'ha inicialitzat l'objecte, crida el constructor. Si ja s'ha instanciat prèviament,
	 * simplement retorna l'instància ja creada.
	 *
	 * @return L'objecte singleton amb el el controlador de presentació.
	 */
	public static synchronized PresentacioCtrl getInstancia()
	{
		if ( instancia == null )
		{
			instancia = new PresentacioCtrl();
		}

		return instancia;
	}

	/**
	 * Frame principal de la interfície del joc.
	 */
	private JFrame frame_principal = new JFrame( "jHex - Joc de Taula Hex" );

	/**
	 * Vista d'inici de sessió.
	 */
	private IniciaSessioVista inicia_sessio_vista = new IniciaSessioVista( frame_principal );

	/**
	 * Vista de registre d'un usuari.
	 */
	private RegistraVista registra_vista;

	/**
	 * Vista del menú principal.
	 */
	private MenuPrincipalVista menu_principal_vista;

	/**
	 * Vista de les preferències de l'usuari.
	 */
	private ConfiguracioVista preferencies_vista;

	/**
	 * Vista del menú de configuració d'una nova partida.
	 */
	private ConfiguraPartidaVista configura_partida_vista;

	/**
	 * Vista del rànquing.
	 */
	private RanquingVista ranquing_vista;

	/**
	 * Vista de la pantalla de carregar una partida guardada.
	 */
	private CarregaPartidaVista carrega_partida_vista;

	/**
	 * Vista de la pantalla d'identificar-se per a carregar una partida guardada.
	 */
	private IdentificaCarregaPartidaVista identifica_carrega_partida_vista;

	/**
	 * Vista de la pantalla de definició d'una situació inicial.
	 */
	private DefineixSituacioVista defineix_situacio_vista;

	/**
	 * Vista del visualitzador de partides.
	 */
	private PartidaVista partida_vista;

	/**
	 * Vista del menú de canvi de la contrasenya.
	 */
	private CanviaContrasenyaVista canvia_contrasenya_vista;

	/**
	 * Inicialitza el controlador de presentació, establint la mida de la finestra, la icona, i la primera vista a mostrar.
	 */
	public void inicialitzaPresentacio()
	{
		// Mida fixa i sense opció a redimensionar per evitar que hi hagi errors en el posicionament dels botons.
		frame_principal.setMinimumSize( new Dimension( 800, 600 ) );
		frame_principal.setPreferredSize( frame_principal.getMinimumSize() );
		frame_principal.setResizable( false );
		frame_principal.setLocationRelativeTo( null );
		frame_principal.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame_principal.setIconImage( new ImageIcon( "img/logo-36_36.png" ).getImage() ); // Icona finestra windows
		try // Icona dock Mac
		{
			Method metode = Class.forName( "com.apple.eawt.Application" ).getMethod( "getApplication" );
			Object aplicacio = metode.invoke( null );
			aplicacio.getClass().getMethod( "setDockIconImage", Image.class )
					.invoke( aplicacio, new ImageIcon( "img/logo-120_120.png" ).getImage() );
		}
		catch ( Exception excepcio )
		{
			// no estem a Mac OS X.
		}

		try
		{
			PartidaCtrl.comprovaConsistenciaFitxersIDades();
		}
		catch ( Exception e )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Error inicialitzant les dades del joc.", botons,
					JOptionPane.ERROR_MESSAGE );

			e.printStackTrace(); // Imprimeixo l'error per consola per poder-ho debugar
		}

		inicia_sessio_vista.fesVisible();
	}

	/**
	 * Estableix l'usuari identificat al joc com l'usuari corresponent al nom i la contrasenya que passem com a
	 * paràmetre.
	 *
	 * @param nom         Nom de l'usuari que es vol carregar.
	 * @param contrasenya Contrasenya de l'usuari que es vol carregar.
	 * @return Cert, si l'usuari es carrega correctament. Fals, altrament.
	 * @throws IllegalArgumentException Si l'usuari identificat pel nom no existeix
	 *                                  i, si es vol carregar un jugador, si la contrasenya no coincideix amb
	 *                                  l'usuari.
	 * @throws FileNotFoundException    Si el fitxer no s'ha generat i no s'han pogut escriure les dades.
	 * @throws IOException              IOException Si ha succeït un error d'entrada/sortida inesperat.
	 * @throws ClassNotFoundException   Si hi ha un problema de classes quan es carrega l'usuari.
	 * @throws NullPointerException     Es dona si el fitxer està buit.
	 */
	public void setUsuariActual( String nom, String contrasenya )
			throws IllegalArgumentException, FileNotFoundException, IOException, ClassNotFoundException,
			       NullPointerException
	{
		UsuariCtrl.getInstancia().setUsuariPrincipal( nom, contrasenya );
	}

	/**
	 * Indica si l'usuari identificat al joc és un convidat.
	 *
	 * @return Cert, si l'usuari identificat al joc és un convidat. Fals, altrament.
	 */
	public boolean getEsConvidat()
	{
		return UsuariCtrl.getInstancia().esConvidat();
	}

	/**
	 * Identifica un usuari convidat al sistema.
	 */
	public void entraConvidat()
	{
		UsuariCtrl.getInstancia().entraConvidat();
	}

	/**
	 * Registra un usuari amb el nom d'usuari i la contrasenya donats als sistema, guardant les seves dades a memòria
	 * principal i afegint-lo al rànquing.
	 *
	 * @param nom           Nom de l'usuari nou que es vol registrar al sistema.
	 * @param contrasenya   Contrasenya de l'usuari nou que es vol registrar al sistema.
	 * @throws IllegalArgumentException Si el nom d'usuari ja existeix al sistema,
	 *                                  si conté caràcters il·legals o si es tracta d'un nom no permès.
	 * @throws IOException              Si ha succeït un error d'entrada/sortida inesperat.
	 */
	public void registraUsuari( String nom, String contrasenya ) throws IllegalArgumentException, IOException
	{
		UsuariCtrl.getInstancia().creaUsuari( nom, contrasenya, TipusJugadors.JUGADOR );
	}

	/**
	 * Guarda les dades de l'usuari identificat al sistema en memòria secundària.
	 *
	 * @throws IOException           Si ha succeït un error d'entrada/sortida inesperat.
	 * @throws FileNotFoundException Si el fitxer no s'ha generat i no s'han pogut escriure les dades.
	 */
	public void guardaJugadorPrincipal() throws IOException, FileNotFoundException
	{
		if ( !getEsConvidat() )
		{
			UsuariCtrl.getInstancia().guardaUsuari();
		}
	}

	/**
	 * Obté el nom de l'usuari identificat al sistema.
	 *
	 * @return El nom de l'usuari identificat al sistema.
	 */
	public String obteNomJugadorPrincipal()
	{
		return UsuariCtrl.getInstancia().obteNom();
	}

	// Mètodes ConfiguracioVista
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * Obté el mode d'inici de l'usuari identificat al sistema.
	 *
	 * @return El mode d'inici de l'usuari identificat al sistema.
	 */
	public ModesInici obteModeIniciJugadorPrincipal()
	{
		return UsuariCtrl.getInstancia().obteModeInici();
	}

	/**
	 * Obté la combinació de colors de l'usuari identificat al sistema.
	 *
	 * @return La combinació de colors de l'usuari identificat al sistema.
	 */
	public CombinacionsColors obteCombinacioDeColorsJugadorPrincipal()
	{
		return UsuariCtrl.getInstancia().obteCombinacioColors();
	}

	/**
	 * Modifica les preferències de l'usuari identificat al sistema.
	 *
	 * @param mode_inici Mode d'inici que es vol donar a l'usuari.
	 * @param combinacio_colors Combinació de colors que es vol donar a l'usuari.
	 */
	public void modificaPreferenciesJugadorPrincipal( ModesInici mode_inici, CombinacionsColors combinacio_colors )
	{
		UsuariCtrl.getInstancia().modificaPreferencies( mode_inici, combinacio_colors );
	}

	/**
	 * Reinicia les estadístiques de l'usuari identificat al sistema.
	 */
	public void reiniciaEstadistiquesJugadorPrincipal()
	{
		UsuariCtrl.getInstancia().reiniciaEstadistiques();
	}

	/**
	 * Modifica la contrasenya de l'usuari identificat al sistema.
	 *
	 * @param contrasenya_actual Contrasenya actual de l'usuari identificat al sistema.
	 * @param contrasenya_nova Contrasenya nova que es vol donar a l'usuari.
	 * @throws IllegalArgumentException Si la contrasenya actual passada com a paràmetre no coincideix amb la
	 *                                  contrasenya de l'usuari.
	 */
	public void canviaContrasenyaJugadorPrincipal( String contrasenya_actual, String contrasenya_nova )
			throws IllegalArgumentException
	{
		UsuariCtrl.getInstancia().modificaContrasenya( contrasenya_actual, contrasenya_nova );
	}

	/**
	 * Elimina l'usuari identificat al sistema de memòria secundària.
	 */
	public void eliminaUsuariJugadorPrincipal()
	{
		UsuariCtrl.getInstancia().eliminaUsuariJugadorPrincipal();
	}

	// Mètodes ConfiguraPartidaVista
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * Preinicialitza els usuaris que disputaran la partida.
	 * <p/>
	 * Carrega l'usuari corresponent al tipus de jugador seleccionat comprovant les credencials en cas que toqui,
	 * carregant de disc la versió corresponent de la IA o instanciant un usuari convidat temporal si és el cas.
	 *
	 * @param num_jugador        Número de jugador que es vol preinicialitzar (0 si és el jugador A, 1 si és el B)
	 * @param tipus_jugador      Tipus de jugador que es vol preinicialitzar
	 * @param nom_usuari         Nom de l'usuari d'aquest jugador
	 * @param contrasenya_usuari Contrasenya de l'usuari associat a aquest jugador
	 * @throws IllegalArgumentException Si la contrasenya de l'usuari és incorrecta.
	 * @throws FileNotFoundException    Si no es troba el fitxer amb les dades de l'usuari.
	 * @throws IOException              Si hi ha algun problema d'entrada/sortida quan intentem carregar l'usuari de disc.
	 * @throws ClassNotFoundException   Si no es troba la classe UsuariHex quan s'intenta carregar l'usuari de disc.
	 * @throws NullPointerException     Si el fitxer de l'usuari és buit.
	 */
	public void preInicialitzaUsuariPartida( int num_jugador, TipusJugadors tipus_jugador, String nom_usuari,
	                                         String contrasenya_usuari )
			throws IllegalArgumentException, FileNotFoundException, IOException, ClassNotFoundException,
			       NullPointerException
	{
		PartidaCtrl.getInstancia()
				.preInicialitzaUsuariPartida( num_jugador, tipus_jugador, nom_usuari, contrasenya_usuari );
	}

	/**
	 * Inicialitza una partida nova. Li assigna les preferències de l'usuari identificat al sistema.
	 *
	 * @param mida_tauler      Mida del tauler de la partida.
	 * @param nom_partida      Nom de la partida.
	 * @param situacio_inicial Indica si la partida ve definida amb una situació inicial
	 * @throws NullPointerException     Si no s'han preinicialitzat els usuaris de la partida prèviament.
	 * @throws IllegalArgumentException Si no s'ha especificat un nom de partida o si ja existeix una partida amb
	 *                                  aquest identificador
	 * @throws ClassNotFoundException   Si no es pot carregar la classe de les intel·ligències artificials.
	 * @throws InstantiationException   Si hi ha problemes amb la instanciació de les intel·ligències artificials.
	 * @throws IllegalAccessException   Si s'intenta accedir a un lloc no permès quan es carreguen les
	 *                                  intel·ligències artificials.
	 */
	public void inicialitzaPartida( int mida_tauler, String nom_partida, boolean situacio_inicial )
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException
	{
		PartidaCtrl.getInstancia().inicialitzaPartida( mida_tauler, nom_partida, situacio_inicial );
	}

	/**
	 * Indica si un nom d'usuari ja està en ús.
	 *
	 * @param nom Nom d'usuari que es vol comprovar.
	 * @return Cert, si el nom d'usuari ja està en ús. Fals, altrament.
	 */
	public boolean existeixUsuari( String nom )
	{
		return UsuariCtrl.getInstancia().existeixUsuari( nom );
	}

	// Mètodes RanquingVista
	// ----------------------------------------------------------------------------------------------------------------

	public String[][] getClassificacioFormatejada()
	{
		List<UsuariHex> classificacio = Ranquing.getInstancia().getClassificacio();

		String[][] classificacio_formatejada = new String[classificacio.size()][4];

		int i = 0;
		for ( UsuariHex usuari_classificat : classificacio )
		{
			classificacio_formatejada[i][0] = usuari_classificat.getNom();

			Integer partides_jugades = usuari_classificat.getPartidesJugades();
			classificacio_formatejada[i][1] = partides_jugades.toString();

			// Càlcul del percentatge de victòries
			Float percentatge_victories = new Float( ( usuari_classificat.getPartidesGuanyades().floatValue() /
			                                     partides_jugades.floatValue() ) * 100 );
			// Redondeig a dos decimals
			percentatge_victories = ( float ) ( Math.round( percentatge_victories * 100.0 ) / 100.0 );

			// Per poder ordenar :)
			if ( percentatge_victories > 99.99 )
			{
				percentatge_victories = ( float ) 99.99;
			}

			classificacio_formatejada[i][2] = percentatge_victories.toString() + "%";
			classificacio_formatejada[i][3] = usuari_classificat.getPuntuacioGlobal().toString();
			i++;
		}

		return classificacio_formatejada;
	}

	public String[][] getHallOfFameFormatejat()
	{
		Ranquing ranquing = Ranquing.getInstancia();

		String[][] hall_of_fame_formatejat = new String[4][3];

		hall_of_fame_formatejat[0] = getFitaHallOfFameFormatejada(
				"Victòria amb menys fitxes",
				ranquing.getUsuariFitxesMinimes(),
				ranquing.getFitxesMinimes().toString()
		);

		hall_of_fame_formatejat[1] = getFitaHallOfFameFormatejada(
				"Més victòries",
				ranquing.getUsuariMesPartidesGuanyades(),
				ranquing.getMesPartidesGuanyades().toString()
		);

		hall_of_fame_formatejat[2] = getFitaHallOfFameFormatejada(
				"Més partides jugades",
				ranquing.getUsuariMesPartidesJugades(),
				ranquing.getMesPartidesJugades().toString()
		);

		hall_of_fame_formatejat[3] = getFitaHallOfFameFormatejada(
				"Victòria en menys temps",
				ranquing.getUsuariTempsMinim(),
				String.valueOf( ranquing.getTempsMinim() / 1000L ) + "." +
				( ranquing.getTempsMinim() % 1000L ) / 100 + " segs."
		);

		return hall_of_fame_formatejat;
	}

	private String[] getFitaHallOfFameFormatejada( String nom_fita, String nom_usuari, String record )
	{
		String[] dades_fita = new String[3];

		dades_fita[0] = nom_fita;

		if ( nom_usuari != null )
		{
			dades_fita[1] = nom_usuari;
			dades_fita[2] = record;
		}
		else
		{
			dades_fita[1] = "-";
			dades_fita[2] = "(Per aconseguir)";
		}

		return dades_fita;
	}

	// Mètodes CarregaPartidaVista
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * Obté la llista de les partides guardades on participa l'usuari identificat al sistema.
	 *
	 * @return Una array amb les dades de les partides guardades on participa l'usuari identificat al sistema.
	 */
	public String[][] obteLlistaPartides()
	{
		return PartidaCtrl.getInstancia().llistaPartidesUsuari();
	}

	/**
	 * Consulta quin usuari no ha iniciat sessió (si és el cas) per poder jugar a la partida actual.
	 *
	 * @param id_partida Identificador únic de la partida
	 * @return El nom de l'usuari que no ha iniciat sessió (si és el cas). Si no n'hi ha, retorna null.
	 * @throws IOException            Si no es pot carregar la partida
	 * @throws ClassNotFoundException Si no existeix la classe PartidaHex.
	 */
	public String usuariSenseAutenticarAPartida( String id_partida ) throws IOException, ClassNotFoundException
	{
		return PartidaCtrl.getInstancia().usuariSenseAutenticarAPartida( id_partida );
	}

	/**
	 * Carrega de memòria secundària la partida identificada per identificador_partida i la estableix com la partida
	 * en joc.
	 *
	 * @param id_partida              Identificador de la partida que es vol carregar
	 * @param contrasenya_contrincant Contrasenya de l'usuari contrincant
	 * @throws IOException            Si no es pot carregar la partida
	 * @throws ClassNotFoundException Si no existeix la classe PartidaHex o la de la intel·ligència artificial.
	 * @throws IllegalAccessException Si hi ha un problema d'accés al fitxer amb la partida
	 * @throws InstantiationError     Si hi ha un problema de classes a la instanciació de la partida que es vol
	 *                                carregar
	 */
	public void carregaPartida( String id_partida, String contrasenya_contrincant )
			throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException
	{
		PartidaCtrl.getInstancia().carregaPartida( id_partida, contrasenya_contrincant );
	}

	/**
	 * Elimina de memòria secundària la partida identificada per id_partida.
	 *
	 * @param id_partida Identificador de la partida que es vol eliminar.
	 */
	public void eliminaPartida( String id_partida )
	{
		PartidaCtrl.getInstancia().eliminaPartida( id_partida );
	}

	// Metodes JPanelTauler
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * Obté l'estat de la partida en curs.
	 *
	 * @return L'estat de la partida en curs.
	 */
	public EstatPartida consultaEstatPartida()
	{
		return PartidaCtrl.getInstancia().consultaEstatPartida();
	}

	/**
	 * Col·loca una fitxa a la casella de la qual es passa la posició com a paràmetre.
	 *
	 * @param fila Fila de la casella en la qual es vol col·locar una fitxa.
	 * @param columna Columna de la casella en la qual es vol col·locar una fitxa.
	 * @throws UnsupportedOperationException Si la partida ja ha finalitzat.
	 */
	public void mouFitxa( int fila, int columna ) throws UnsupportedOperationException
	{
		PartidaCtrl.getInstancia().mouFitxa( fila, columna );
	}

	/**
	 * Obté l'estat d'una casella determinada.
	 *
	 * @param fila Fila de la casella que es vol consultar.
	 * @param columna Columna de la casella que es vol consultar.
	 * @return L'estat de la casella demanada (buida, del jugador A, del jugador B).
	 */
	public EstatCasella getEstatCasella( int fila, int columna )
	{
		return PartidaCtrl.getInstancia().getEstatCasella( fila, columna );
	}

	/**
	 * Demana a la intel·ligència artificial que executi el proper moviment de la partida.
	 */
	public void executaMovimentIA()
	{
		PartidaCtrl.getInstancia().executaMovimentIA();
	}

	/**
	 * Indica si el torn actual de la partida correspon a un usuari humà.
	 *
	 * @return Cert, si el torn actual és d'un usuari humà. Fals, altrament.
	 */
	public boolean esTornHuma()
	{
		return PartidaCtrl.getInstancia().esTornHuma();
	}

	/**
	 * Indica si una certa posició del tauler és o no la casella central.
	 *
	 * @param fila Fila de la posició que es vol comprovar.
	 * @param columna Columna de la posició que es vol comprovar.
	 * @return Cert, si és una casella central. Fals, altrament.
	 */
	public boolean esCasellaCentral( int fila, int columna )
	{
		return PartidaCtrl.getInstancia().esCasellaCentral( fila, columna );
	}

	/**
	 * Obté la casella recomanada per la intel·ligència artificial per a col·locar la propera fitxa de la partida.
	 *
	 * @return La Casella con la intel·ligència artificial col·locaria la propera fitxa de la partida.
	 */
	public Casella obtePista()
	{
		return PartidaCtrl.getInstancia().obtePista();
	}

	/**
	 * Obté els elements de control de la partida en curs.
	 *
	 * @return Un array amb els elements de control de la partida en curs.
	 */
	public Object[] getElementsDeControlPartida()
	{
		return PartidaCtrl.getInstancia().getElementsDeControlPartida();
	}

	/**
	 * Obté els elements de control dels jugadors de la partida en curs.
	 *
	 * @return Un array amb els elements de control dels jugadors de la partida en curs.
	 */
	public Object[][] getElementsDeControlJugadors()
	{
		return PartidaCtrl.getInstancia().getElementsDeControlJugadors();
	}

	/**
	 * Mostra el diàleg de partida finalitzada si s'està jugant una partida.
	 *
	 * @param missatge Missatge que es vol mostrar al diàleg.
	 */
    public void mostraDialegVictoriaPartida( String missatge )
    {
        partida_vista.mostraDialegVictoria( missatge );
    }

	/**
	 * Mostra el diàleg de partida finalitzada si s'està definint una situació inicial.
	 *
	 * @param missatge Missatge que es vol mostrar al diàleg.
	 */
    public void mostraDialegVictoriaDefineixSituacio( String missatge )
    {
        defineix_situacio_vista.mostraDialegVictoria( missatge );
    }

	// Mètodes PartidaVista
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * Activa o desactiva el botó d'intercanvia fitxa a la vista del visualitzador de partides.
	 *
	 * @param estat Cert, is volem activar el botó. Fals, altrament.
	 */
	public void estatBotoIntercanviaFitxa( boolean estat )
	{
		partida_vista.estatBotoIntercanviaFitxa( estat );
	}

	/**
	 * Activa o desactiva el botó de demana pista a la vista del visualitzador de partides.
	 *
	 * @param estat Cert, is volem activar el botó. Fals, altrament.
	 */
	public void estatBotoDemanaPista( boolean estat )
	{
		partida_vista.estatBotoDemanaPista( estat );
	}

	/**
	 * Canvia de jugador propietari l'última fitxa col·locada al tauler.
	 */
	public void intercanviaFitxa()
	{
		PartidaCtrl.getInstancia().intercanviaDarreraFitxa();
	}

	/**
	 * Finalitza la partida actual, actualitzant les estadístiques dels usuaris participants, si cal.
	 *
	 * @throws IOException Si ha ocorregut un error d'entrada/sortida inesperat.
	 */
	public void finalitzaPartida() throws IOException
	{
		PartidaCtrl.getInstancia().finalitzaPartida();
	}

	/**
	 * Guarda la partida actual en memòria secundària.
	 *
	 * @return Cert si s'ha guardat correctament. Fals altrament.
	 * @throws IOException                   Si hi ha un error d'entrada/sortida.
	 * @throws UnsupportedOperationException Si es vol guardar una partida ja finalitzada o cap dels usuaris és
	 *                                       registrat.
	 */
	public void guardaPartida() throws IOException, UnsupportedOperationException
	{
		PartidaCtrl.getInstancia().guardaPartida();
	}

	public void netejaParametresPartidaActual()
	{
		PartidaCtrl.getInstancia().netejaParametresPartidaActual();
	}

	// Mètodes per intercanviar vistes
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * Canvia la vista d'iniciar sessió per la de registrar-se.
	 */
	public void vistaIniciaSessioARegistra()
	{
		if ( registra_vista == null )
		{
			registra_vista = new RegistraVista( frame_principal );
		}
		inicia_sessio_vista = null;
		registra_vista.fesVisible();
	}

	/**
	 * Canvia la vista de registrar-se per la d'iniciar sessió.
	 */
	public void vistaRegistraAIniciaSessio()
	{
		if ( inicia_sessio_vista == null )
		{
			inicia_sessio_vista = new IniciaSessioVista( frame_principal );
		}
		registra_vista = null;
		inicia_sessio_vista.fesVisible();
	}

	/**
	 * Canvia la vista de registrar-se per la del menú principal.
	 */
	public void vistaRegistraAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( frame_principal );
		}
		registra_vista = null;
		menu_principal_vista.fesVisible();
	}

	/**
	 * Canvia la vista d'iniciar sessió per la del menú principal.
	 */
	public void vistaIniciaSessioAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( frame_principal );
		}

		inicia_sessio_vista = null;
		menu_principal_vista.fesVisible();
	}

	/**
	 * Canvia la vista del menú principal per la d'iniciar sessió.
	 */
	public void vistaMenuPrincipalAIniciaSessio()
	{
		if ( inicia_sessio_vista == null )
		{
			inicia_sessio_vista = new IniciaSessioVista( frame_principal );
		}
		menu_principal_vista = null;
		inicia_sessio_vista.fesVisible();
	}

	/**
	 * Canvia la vista del menú principal per la de modificar les preferències.
	 */
	public void vistaMenuPrincipalAPreferencies()
	{
		if ( preferencies_vista == null )
		{
			preferencies_vista = new ConfiguracioVista( frame_principal );
		}
		menu_principal_vista = null;
		preferencies_vista.fesVisible();
	}

	/**
	 * Canvia la vista de modificar les preferències per la del menú principal.
	 */
	public void vistaPreferenciesAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( frame_principal );
		}
		preferencies_vista = null;
		menu_principal_vista.fesVisible();
	}

	/**
	 * Canvia la vista de canviar la contrasenya per la de modificar les preferències.
	 */
	public void vistaCanviaContrasenyaAPreferencies()
	{
		if ( preferencies_vista == null )
		{
			preferencies_vista = new ConfiguracioVista( frame_principal );
		}
		canvia_contrasenya_vista = null;
		preferencies_vista.fesVisible();
	}

	/**
	 * Canvia la vista de canviar la contrasenya per la del menú principal.
	 */
	public void vistaCanviaContrasenyaAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( frame_principal );
		}
		canvia_contrasenya_vista = null;
		menu_principal_vista.fesVisible();
	}

	/**
	 * Canvia la vista de modificar les preferències per la de canviar la contrasenya.
	 */
	public void vistaPreferenciesACanviaContrasenya()
	{
		if ( canvia_contrasenya_vista == null )
		{
			canvia_contrasenya_vista = new CanviaContrasenyaVista( frame_principal );
		}
		preferencies_vista = null;
		canvia_contrasenya_vista.fesVisible();
	}

	/**
	 * Canvia la vista del menú principal per la de configurar la partida.
	 */
	public void vistaMenuPrincipalAConfiguraPartida()
	{
		if ( configura_partida_vista == null )
		{
			configura_partida_vista = new ConfiguraPartidaVista( frame_principal );
		}
		menu_principal_vista = null;
		configura_partida_vista.fesVisible();
	}

	/**
	 * Canvia la vista de configurar la partida per la del menú principal.
	 */
	public void vistaConfiguraPartidaAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( frame_principal );
		}
		configura_partida_vista = null;
		menu_principal_vista.fesVisible();
	}

	/**
	 * Canvia la vista del menú principal per la del rànquing.
	 */
	public void vistaMenuPrincipalARanquing()
	{
		if ( ranquing_vista == null )
		{
			ranquing_vista = new RanquingVista( frame_principal );
		}
		menu_principal_vista = null;
		ranquing_vista.fesVisible();
	}

	/**
	 * Canvia la vista del rànquing per la del menú principal.
	 */
	public void vistaRanquingAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( frame_principal );
		}
		ranquing_vista = null;
		menu_principal_vista.fesVisible();
	}

	/**
	 * Canvia la vista del menú principal per la de carregar partida.
	 */
	public void vistaMenuPrincipalACarregaPartida()
	{
		if ( carrega_partida_vista == null )
		{
			carrega_partida_vista = new CarregaPartidaVista( frame_principal );
		}
		menu_principal_vista = null;
		carrega_partida_vista.fesVisible();
	}

	/**
	 * Canvia la vista de carregar partida per la del menú principal.
	 */
	public void vistaCarregaPartidaAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( frame_principal );
		}
		carrega_partida_vista = null;
		menu_principal_vista.fesVisible();
	}

	/**
	 * Canvia la vista de carregar partida per la del visualitzador de partides.
	 */
	public void vistaCarregaPartidaAPartida()
	{
		if ( partida_vista == null )
		{
			partida_vista = new PartidaVista( frame_principal );
		}
		carrega_partida_vista = null;
		partida_vista.fesVisible();
	}

	/**
	 * Canvia la vista de carregar partida per la de identificar-se per a carregar una partida.
	 *
	 * @param usuari Usuari que s'ha d'identificar.
	 * @param id_partida Identificador de la partida que es vol carregar.
	 */
	public void vistaCarregaPartidaAIdentificaCarregaPartida( String usuari, String id_partida )
	{
		if ( identifica_carrega_partida_vista == null )
		{
			identifica_carrega_partida_vista =
					new IdentificaCarregaPartidaVista( frame_principal, usuari, id_partida );
		}
		carrega_partida_vista = null;
		identifica_carrega_partida_vista.fesVisible();
	}

	/**
	 * Canvia la vista d'identificar-se per a carregar una partida per la del visualitzador de partides.
	 */
	public void vistaIdentificaCarregaPartidaAPartida()
	{
		if ( partida_vista == null )
		{
			partida_vista = new PartidaVista( frame_principal );
		}
		identifica_carrega_partida_vista = null;
		partida_vista.fesVisible();
	}

	/**
	 * Canvia la vista d'identificar-se per a carregar una partida per la del menú principal.
	 */
	public void vistaIdentificaCarregaPartidaAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( frame_principal );
		}
		identifica_carrega_partida_vista = null;
		menu_principal_vista.fesVisible();
	}

	/**
	 * Canvia la vista d'identificar-se per a carregar una partida per la de carregar una partida.
	 */
	public void vistaIdentificaCarregaPartidaACarregaPartida()
	{
		if ( carrega_partida_vista == null )
		{
			carrega_partida_vista = new CarregaPartidaVista( frame_principal );
		}
		identifica_carrega_partida_vista = null;
		carrega_partida_vista.fesVisible();
	}

	/**
	 * Canvia la vista de configurar una partida per la del visualitzador de partides.
	 */
	public void vistaConfiguraPartidaAPartida()
	{
		if ( partida_vista == null )
		{
			partida_vista = new PartidaVista( frame_principal );
		}
		configura_partida_vista = null;
		partida_vista.fesVisible();
	}

	/**
	 * Canvia la vista de configurar una partida per la de definir una situació inicial.
	 */
	public void vistaConfiguraPartidaADefineixSituacio()
	{
		if ( defineix_situacio_vista == null )
		{
			defineix_situacio_vista = new DefineixSituacioVista( frame_principal );
		}
		configura_partida_vista = null;
		defineix_situacio_vista.fesVisible();
	}

	/**
	 * Canvia la vista del visualitzador de partides per la del menú principal.
	 */
	public void vistaPartidaAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( frame_principal );
		}
		partida_vista = null;
		menu_principal_vista.fesVisible();
	}

	/**
	 * Canvia la vista de definir una situació inicial per la del visualitzador de partides.
	 */
	public void vistaDefineixSituacioAPartida()
	{
		if ( partida_vista == null )
		{
			partida_vista = new PartidaVista( frame_principal );
		}
		defineix_situacio_vista = null;

		PartidaCtrl.getInstancia().acabaDeDefinirSituacioInicial();
		partida_vista.fesVisible();
	}

	/**
	 * Canvia la vista de definir una situació inicial per la del menú principal.
	 */
	public void vistaDefineixSituacioAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( frame_principal );
		}
		defineix_situacio_vista = null;
		menu_principal_vista.fesVisible();
	}
}
