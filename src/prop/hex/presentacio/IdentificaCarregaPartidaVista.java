package prop.hex.presentacio;

import prop.cluster.domini.models.estats.EstatPartida;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class IdentificaCarregaPartidaVista extends BaseVista
{

	private JPanel panell_dades;
	private JPanel panell_botons;
	private JButton accepta;
	private JButton llista_partides;
	private JButton menu_principal;
	private JPasswordField contrasenya;
	private JLabel usuari;
	private JLabel text_usuari;
	private JLabel text_contrasenya;
	private String id_partida;

	public IdentificaCarregaPartidaVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal, String usuari,
	                                      String id_partida )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "Carrega partida - Identificar-se" );
		panell_dades = new JPanelImatge( "img/caixa.png" );
		panell_botons = new JPanel();
		accepta = new JButton( "Accepta" );
		llista_partides = new JButton( "Torna a la llista de partides" );
		menu_principal = new JButton( "Torna al menú principal" );
		this.usuari = new JLabel( usuari );
		contrasenya = new JPasswordField();
		text_usuari = new JLabel( "Nom d'usuari:" );
		text_contrasenya = new JLabel( "Contrasenya:" );
		this.id_partida = id_partida;

		inicialitzaVista();
	}

	@Override
	protected void inicialitzaPanellPrincipal()
	{
		panell_principal.setLayout( new GridBagLayout() );
		panell_principal.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		GridBagConstraints propietats_panel = new GridBagConstraints();
		propietats_panel.fill = GridBagConstraints.HORIZONTAL;
		propietats_panel.anchor = GridBagConstraints.CENTER;
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 0;
		propietats_panel.weightx = 0.5;
		propietats_panel.weighty = 0.1;
		panell_principal.add( panell_titol, propietats_panel );
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.5;
		panell_principal.add( panell_dades, propietats_panel );
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 2;
		propietats_panel.weighty = 0.4;
		panell_principal.add( panell_botons, propietats_panel );
		propietats_panel.fill = GridBagConstraints.NONE;
		propietats_panel.gridx = 2;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.25;
		propietats_panel.weighty = 0.1;
		propietats_panel.anchor = GridBagConstraints.SOUTHEAST;
		panell_principal.add( panell_sortida, propietats_panel );
		propietats_panel.gridx = 0;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.25;
		propietats_panel.anchor = GridBagConstraints.SOUTHWEST;
		panell_principal.add( titol_baix, propietats_panel );
	}

	@Override
	protected void inicialitzaPanellCentral()
	{
		panell_dades.setBorder( BorderFactory.createRaisedBevelBorder() );
		panell_dades.setLayout( new BoxLayout( panell_dades, BoxLayout.PAGE_AXIS ) );
		JPanel panell_camps = new JPanel();
		panell_camps.setLayout( new GridLayout( 2, 2, 10, 10 ) );
		panell_camps.add( text_usuari );
		panell_camps.add( usuari );
		panell_camps.add( text_contrasenya );
		panell_camps.add( contrasenya );
		panell_camps.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		panell_camps.setOpaque( false );
		panell_dades.add( panell_camps );
		accepta.setAlignmentX( Component.CENTER_ALIGNMENT );
		panell_dades.add( accepta );
		panell_dades.add( Box.createVerticalStrut( 10 ) );
	}

	@Override
	protected void inicialitzaPanellPeu()
	{
		panell_botons.setLayout( new BoxLayout( panell_botons, BoxLayout.PAGE_AXIS ) );
		llista_partides.setAlignmentX( Component.CENTER_ALIGNMENT );
		panell_botons.add( llista_partides );
		panell_botons.add( Box.createVerticalStrut( 10 ) );
		menu_principal.setAlignmentX( Component.CENTER_ALIGNMENT );
		panell_botons.add( menu_principal );
		panell_botons.setOpaque( false );
	}

	public void accioBotoAccepta( ActionEvent event )
	{
		try
		{
			presentacio_ctrl.carregaPartida( id_partida, new String( contrasenya.getPassword() ) );
			presentacio_ctrl.vistaIdentificaCarregaPartidaAPartida();
		}
		catch ( Exception excepcio )
		{
			VistaDialeg dialeg_error = new VistaDialeg();
			String[] botons_error = { "Accepta" };
			dialeg_error.setDialeg( "Error", excepcio.getMessage(), botons_error, JOptionPane.ERROR_MESSAGE );
		}
	}

	public void accioBotoLlistaPartides( ActionEvent event )
	{
		presentacio_ctrl.vistaIdentificaCarregaPartidaACarregaPartida();
	}

	public void accioBotoMenuPrincipal( ActionEvent event )
	{
		presentacio_ctrl.vistaIdentificaCarregaPartidaAMenuPrincipal();
	}

	@Override
	protected void assignaListeners()
	{
		super.assignaListeners();

		accepta.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoAccepta( event );
			}
		} );

		llista_partides.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoLlistaPartides( event );
			}
		} );

		menu_principal.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoMenuPrincipal( event );
			}
		} );
	}
}
