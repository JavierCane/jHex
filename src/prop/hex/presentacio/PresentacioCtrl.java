package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import prop.hex.domini.controladors.PartidaCtrl;
import prop.hex.domini.controladors.UsuariCtrl;
import prop.hex.domini.models.UsuariHex;
import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.ModesInici;
import prop.hex.domini.models.enums.TipusJugadors;

public class PresentacioCtrl
{

	private JFrame frame_principal = new JFrame( "jHex" );
	private UsuariHex jugador_principal;
	private String nom_jugador_principal;
	private boolean es_convidat = false;
	private IniciaSessioVista inicia_sessio_vista = new IniciaSessioVista( this, frame_principal );
	private RegistraVista registra_vista;
	private MenuPrincipalVista menu_principal_vista;
	private PreferenciesVista preferencies_vista;
	private IniciaPartidaVista inicia_partida_vista;

	public void inicialitzaPresentacio()
	{
		frame_principal.setMinimumSize( new Dimension( 800, 600 ) );
		frame_principal.setPreferredSize( frame_principal.getMinimumSize() );
		frame_principal.setResizable( false );
		frame_principal.setLocationRelativeTo( null );
		frame_principal.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		inicia_sessio_vista.fesVisible();
	}

	public void setJugadorPrincipal( String nom, String contrasenya )
			throws IllegalArgumentException, FileNotFoundException, IOException, ClassNotFoundException,
			       NullPointerException
	{
		jugador_principal = UsuariCtrl.carregaUsuari( nom, contrasenya, TipusJugadors.JUGADOR );
		nom_jugador_principal = nom;
	}

	public void entraConvidat() throws IllegalArgumentException, IOException
	{
		jugador_principal = UsuariCtrl.creaUsuari( "Convidat", "", TipusJugadors.JUGADOR, false );
		nom_jugador_principal = "Convidat";
		es_convidat = true;
	}

	public void registraUsuari( String nom, String contrasenya ) throws IllegalArgumentException, IOException
	{
		UsuariCtrl.creaUsuari( nom, contrasenya, TipusJugadors.JUGADOR, true );
	}

	public void guardaJugadorPrincipal() throws IOException, FileNotFoundException
	{
		if ( !es_convidat )
		{
			UsuariCtrl.guardaUsuari( jugador_principal );
		}
	}

	public void tancaSessio()
	{
		jugador_principal = null;
		es_convidat = false;
	}

	public String obteNomJugadorPrincipal()
	{
		return nom_jugador_principal;
	}

	public ModesInici obteModeIniciJugadorPrincipal()
	{
		return UsuariCtrl.obteModeInici( jugador_principal );
	}

	public CombinacionsColors obteCombinacioDeColorsJugadorPrincipal()
	{
		return UsuariCtrl.obteCombinacioDeColors( jugador_principal );
	}

	public void modificaPreferenciesJugadorPrincipal( ModesInici mode_inici, CombinacionsColors combinacio_colors )
	{
		UsuariCtrl.modificaPreferencies( jugador_principal, mode_inici, combinacio_colors );
	}

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

	public void vistaMenuPrincipalAIniciaPartida()
	{
		if ( inicia_partida_vista == null )
		{
			inicia_partida_vista = new IniciaPartidaVista( this, frame_principal );
		}
		menu_principal_vista = null;
		inicia_partida_vista.fesVisible();
	}

	public void vistaIniciaPartidaAMenuPrincipal()
	{
		if ( menu_principal_vista == null )
		{
			menu_principal_vista = new MenuPrincipalVista( this, frame_principal );
		}
		inicia_partida_vista = null;
		menu_principal_vista.fesVisible();
	}
}
