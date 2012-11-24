package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import prop.hex.domini.controladors.PartidaCtrl;
import prop.hex.domini.controladors.UsuariCtrl;
import prop.hex.domini.models.UsuariHex;
import prop.hex.domini.models.enums.TipusJugadors;

public class PresentacioCtrl
{

	private JFrame frame_principal = new JFrame( "jHex" );
	private UsuariHex jugador_principal;
	private IniciaSessioVista inicia_sessio_vista = new IniciaSessioVista( this, frame_principal );
	private RegistraVista registra_vista;
	private MenuPrincipalVista menu_principal_vista = new MenuPrincipalVista( this, frame_principal );


	public void inicialitzaPresentacio()
	{
		frame_principal.setMinimumSize( new Dimension( 800, 600 ) );
		frame_principal.setPreferredSize( frame_principal.getMinimumSize() );
		frame_principal.setResizable( false );
		frame_principal.setLocationRelativeTo( null );
		frame_principal.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		inicia_sessio_vista.fesVisible();
		//menu_principal_vista.fesVisible();
	}

	public void setJugadorPrincipal( String nom, String contrasenya ) throws IllegalArgumentException,
			FileNotFoundException, IOException, ClassNotFoundException, NullPointerException
	{
		jugador_principal = UsuariCtrl.carregaUsuari( nom, contrasenya, TipusJugadors.JUGADOR );
	}

	public void entraConvidat() throws IllegalArgumentException, IOException
	{
		jugador_principal = UsuariCtrl.creaUsuari( "Convidat", "", TipusJugadors.JUGADOR, false );
	}

	public void registraUsuari( String nom, String contrasenya ) throws IllegalArgumentException, IOException
	{
		UsuariCtrl.creaUsuari( nom, contrasenya, TipusJugadors.JUGADOR, true );
	}

	public void vistaIniciaSessioARegistra()
	{
		inicia_sessio_vista.fesInvisible();
		registra_vista = new RegistraVista( this, frame_principal );
		registra_vista.fesVisible();
	}

	public void vistaRegistraAIniciaSessio()
	{
		registra_vista.fesInvisible();
		inicia_sessio_vista.fesVisible();
	}

	public void vistaIniciaSessioAMenuPrincipal()
	{
		inicia_sessio_vista.desactiva();
		menu_principal_vista.fesVisible();
	}
}
