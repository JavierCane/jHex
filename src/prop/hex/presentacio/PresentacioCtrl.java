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
	 * Frame principal de la interfície del joc.
	 */
	private JFrame frame_principal = new JFrame( "jHex - Joc de Taula Hex" );

	/**
	 * Indica si l'usuari identificat al sistema és o no un convidat.
	 */
	private boolean es_convidat = false;

	/**
	 * Vista d'inici de sessió.
	 */
	private IniciaSessioVista inicia_sessio_vista = new IniciaSessioVista( this, frame_principal );

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
	private PreferenciesVista preferencies_vista;

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
			aplicacio.getClass().getMethod( "setDockIconImage", Image.class ).invoke( aplicacio,
					new ImageIcon( "img/logo-120_120.png" ).getImage() );
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

	public void setUsuariActual( String nom, String contrasenya ) throws IllegalArgumentException,
			FileNotFoundException, IOException, ClassNotFoundException, NullPointerException
	{
		UsuariCtrl.getInstancia().setUsuariPrincipal( nom, contrasenya );
	}

	public boolean getEsConvidat()
	{
		return es_convidat;
	}

	public void entraConvidat() throws IllegalArgumentException, IOException
	{
		UsuariCtrl.getInstancia().entraConvidat();
		es_convidat = true;
	}

	public void registraUsuari( String nom, String contrasenya ) throws IllegalArgumentException, IOException
	{
		UsuariCtrl.getInstancia().creaUsuari( nom, contrasenya, TipusJugadors.JUGADOR );
	}

	public void guardaJugadorPrincipal() throws IOException, FileNotFoundException
	{
		if ( !es_convidat )
		{
			UsuariCtrl.getInstancia().guardaUsuari();
		}
	}

	public void tancaSessio()
	{
		es_convidat = false;
	}

	public String obteNomJugadorPrincipal()
	{
		return UsuariCtrl.getInstancia().obteNom();
	}

	public ModesInici obteModeIniciJugadorPrincipal()
	{
		return UsuariCtrl.getInstancia().obteModeInici();
	}

	public CombinacionsColors obteCombinacioDeColorsJugadorPrincipal()
	{
		return UsuariCtrl.getInstancia().obteCombinacioColors();
	}

	public void modificaPreferenciesJugadorPrincipal( ModesInici mode_inici, CombinacionsColors combinacio_colors )
	{
		UsuariCtrl.getInstancia().modificaPreferencies( mode_inici, combinacio_colors );
	}

	public void reiniciaEstadistiquesJugadorPrincipal()
	{
		UsuariCtrl.getInstancia().reiniciaEstadistiques();
	}

	public void canviaContrasenyaJugadorPrincipal( String contrasenya_actual, String contrasenya_nova ) throws
			IllegalArgumentException
	{
		UsuariCtrl.getInstancia().modificaContrasenya( contrasenya_actual, contrasenya_nova );
	}

	// Mètodes ConfiguraPartidaVista
	// ----------------------------------------------------------------------------------------------------------------

	public void preInicialitzaUsuariPartida( int num_jugador, TipusJugadors tipus_jugador, String nom_usuari,
	                                         String contrasenya_usuari ) throws IllegalArgumentException,
			FileNotFoundException, IOException, ClassNotFoundException, NullPointerException
	{
		PartidaCtrl.getInstancia().preInicialitzaUsuariPartida( num_jugador, tipus_jugador, nom_usuari,
				contrasenya_usuari );
	}

	public void iniciaPartida( int mida_tauler, String nom_partida, boolean situacio_inicial ) throws
			ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException
	{
		PartidaCtrl.getInstancia().inicialitzaPartida( mida_tauler, nom_partida, situacio_inicial );
	}

	// Mètodes RanquingVista
	// ----------------------------------------------------------------------------------------------------------------

	public Object[][] getClassificacioFormatejada()
	{
		List<UsuariHex> classificacio = Ranquing.getInstancia().getClassificacio();

		Object[][] classificacio_formatejada = new Object[classificacio.size()][4];

		int i = 0;
		for ( UsuariHex usuari_classificat : classificacio )
		{
			classificacio_formatejada[i][0] = usuari_classificat.getNom();
			classificacio_formatejada[i][1] = usuari_classificat.getPartidesJugades();
			classificacio_formatejada[i][2] = usuari_classificat.getPartidesGuanyades();
			classificacio_formatejada[i][3] = usuari_classificat.getPuntuacioGlobal();
			i++;
		}

		return classificacio_formatejada;
	}

	// Mètodes CarregaPartidaVista
	// ----------------------------------------------------------------------------------------------------------------

	public String[][] obteLlistaPartides()
	{
		return PartidaCtrl.getInstancia().llistaPartidesUsuari();
	}

	// Metodes JPanelTauler
	// --------------------------------------------------------------------------------

	public EstatPartida consultaEstatPartida()
	{
		return PartidaCtrl.getInstancia().consultaEstatPartida();
	}

	public void mouFitxa( int fila, int columna ) throws UnsupportedOperationException
	{
		PartidaCtrl.getInstancia().mouFitxa(fila, columna);
	}

	public EstatCasella getEstatCasella( int fila, int columna )
	{
		return PartidaCtrl.getInstancia().getEstatCasella(fila, columna);
	}

	public boolean esPartidaAmbSituacioInicial()
	{
		return PartidaCtrl.getInstancia().esPartidaAmbSituacioInicial();
	}

	public boolean esPartidaAmbSituacioInicialAcabadaDeDefinir()
	{
		return PartidaCtrl.getInstancia().esPartidaAmbSituacioInicialAcabadaDeDefinir();
	}

	public void executaMovimentIA()
	{
		PartidaCtrl.getInstancia().executaMovimentIA();
	}

	public boolean esTornHuma()
	{
		return PartidaCtrl.getInstancia().esTornHuma();
	}

	public Casella obtePista()
	{
		return PartidaCtrl.getInstancia().obtePista();
	}

	public int[] getElementsDeControlPartida()
	{
		return PartidaCtrl.getInstancia().getElementsDeControlPartida();
	}

	public Object[][] getElementsDeControlJugadors()
	{
		return PartidaCtrl.getInstancia().getElementsDeControlJugadors();
	}

	// Mètodes PartidaVista
	// ----------------------------------------------------------------------------------------------------------------

	public void finalitzaPartida() throws IOException
	{
		PartidaCtrl.getInstancia().finalitzaPartida();
	}

	public void guardaPartida() throws IOException
	{
		PartidaCtrl.getInstancia().guardaPartida();
	}

	public void netejaParametresPartidaActual()
	{
		PartidaCtrl.getInstancia().netejaParametresPartidaActual();
	}

	// Mètodes per intercanviar vistes
	// ----------------------------------------------------------------------------------------------------------------

	public void vistaIniciaSessioARegistra()
	{
		if ( registra_vista == null )
		{
			registra_vista = new RegistraVista( this, frame_principal );
		}
		inicia_sessio_vista = null;
		registra_vista.fesVisible();
	}

	public void vistaRegistraAIniciaSessio()
	{
		if ( inicia_sessio_vista == null )
		{
			inicia_sessio_vista = new IniciaSessioVista( this, frame_principal );
		}
		registra_vista = null;
		inicia_sessio_vista.fesVisible();
	}

	public void vistaRegistraAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( this, frame_principal );
		}
		registra_vista = null;
		menu_principal_vista.fesVisible();
	}

	public void vistaIniciaSessioAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( this, frame_principal );
		}

		inicia_sessio_vista = null;
		menu_principal_vista.fesVisible();
	}

	public void vistaMenuPrincipalAIniciaSessio()
	{
		if ( inicia_sessio_vista == null )
		{
			inicia_sessio_vista = new IniciaSessioVista( this, frame_principal );
		}
		menu_principal_vista = null;
		inicia_sessio_vista.fesVisible();
	}

	public void vistaMenuPrincipalAPreferencies()
	{
		if ( preferencies_vista == null )
		{
			preferencies_vista = new PreferenciesVista( this, frame_principal );
		}
		menu_principal_vista = null;
		preferencies_vista.fesVisible();
	}

	public void vistaPreferenciesAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( this, frame_principal );
		}
		preferencies_vista = null;
		menu_principal_vista.fesVisible();
	}

	public void vistaCanviaContrasenyaAPreferencies()
	{
		if ( preferencies_vista == null )
		{
			preferencies_vista = new PreferenciesVista( this, frame_principal );
		}
		canvia_contrasenya_vista = null;
		preferencies_vista.fesVisible();
	}

	public void vistaCanviaContrasenyaAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( this, frame_principal );
		}
		canvia_contrasenya_vista = null;
		menu_principal_vista.fesVisible();
	}

	public void vistaPreferenciesACanviaContrasenya()
	{
		if ( canvia_contrasenya_vista == null )
		{
			canvia_contrasenya_vista = new CanviaContrasenyaVista( this, frame_principal );
		}
		preferencies_vista = null;
		canvia_contrasenya_vista.fesVisible();
	}

	public void vistaMenuPrincipalAConfiguraPartida()
	{
		if ( configura_partida_vista == null )
		{
			configura_partida_vista = new ConfiguraPartidaVista( this, frame_principal );
		}
		menu_principal_vista = null;
		configura_partida_vista.fesVisible();
	}

	public void vistaConfiguraPartidaAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( this, frame_principal );
		}
		configura_partida_vista = null;
		menu_principal_vista.fesVisible();
	}

	public void vistaMenuPrincipalARanquing()
	{
		if ( ranquing_vista == null )
		{
			ranquing_vista = new RanquingVista( this, frame_principal );
		}
		menu_principal_vista = null;
		ranquing_vista.fesVisible();
	}

	public void vistaRanquingAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( this, frame_principal );
		}
		ranquing_vista = null;
		menu_principal_vista.fesVisible();
	}

	public void vistaMenuPrincipalACarregaPartida()
	{
		if ( carrega_partida_vista == null )
		{
			carrega_partida_vista = new CarregaPartidaVista( this, frame_principal );
		}
		menu_principal_vista = null;
		carrega_partida_vista.fesVisible();
	}

	public void vistaCarregaPartidaAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( this, frame_principal );
		}
		carrega_partida_vista = null;
		menu_principal_vista.fesVisible();
	}

	public void vistaConfiguraPartidaAPartida()
	{
		if ( partida_vista == null )
		{
			partida_vista = new PartidaVista( this, frame_principal );
		}
		configura_partida_vista = null;
		partida_vista.fesVisible();
	}

	public void vistaConfiguraPartidaADefineixSituacio()
	{
		if ( defineix_situacio_vista == null )
		{
			defineix_situacio_vista = new DefineixSituacioVista( this, frame_principal );
		}
		configura_partida_vista = null;
		defineix_situacio_vista.fesVisible();
	}

	public void vistaPartidaAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( this, frame_principal );
		}
		partida_vista = null;
		menu_principal_vista.fesVisible();
	}

	public void vistaDefineixSituacioAPartida()
	{
		if ( partida_vista == null )
		{
			partida_vista = new PartidaVista( this, frame_principal );
		}
		defineix_situacio_vista = null;

		PartidaCtrl.getInstancia().acabaDeDefinirSituacioInicial();
		partida_vista.fesVisible();
	}

	public void vistaDefineixSituacioAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( this, frame_principal );
		}
		defineix_situacio_vista = null;
		menu_principal_vista.fesVisible();
	}
}
