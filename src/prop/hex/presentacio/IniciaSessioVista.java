package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IniciaSessioVista extends BaseVista
{
	private JPanel panel_dades = new JPanelImatge( "img/caixa.png" );
	private JPanel panel_botons = new JPanel();
	private JButton accepta = new JButton( "Accepta" );
	private JButton registra = new JButton( "Registra't" );
	private JButton convidat = new JButton( "Entra com a convidat" );
	private JTextField usuari = new JTextField();
	private JPasswordField contrasenya = new JPasswordField();
	private JLabel text_usuari = new JLabel( "Nom d'usuari:" );
	private JLabel text_contrasenya = new JLabel( "Contrasenya:" );

	public IniciaSessioVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );
		titol = new JLabel("Inicia sessió");
		inicialitzaVista();
	}

	protected void inicialitzaVista()
	{
		inicialitzaPanelPrincipal();
		inicialitzaPanelTitol();
		inicialitzaPanelDades();
		inicialitzaPanelBotons();
		inicialitzaPanelSortida();
		assignaListeners();
	}

	private void inicialitzaPanelDades()
	{
		panel_dades.setBorder( BorderFactory.createRaisedBevelBorder() );
		panel_dades.setLayout( new BoxLayout( panel_dades, BoxLayout.PAGE_AXIS ) );
		JPanel panel_camps = new JPanel();
		panel_camps.setLayout( new GridLayout( 2, 2, 10, 10 ) );
		panel_camps.add( text_usuari );
		panel_camps.add( usuari );
		panel_camps.add( text_contrasenya );
		panel_camps.add( contrasenya );
		panel_camps.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		panel_camps.setOpaque( false );
		panel_dades.add( panel_camps );
		accepta.setAlignmentX( Component.CENTER_ALIGNMENT );
		panel_dades.add( accepta );
		panel_dades.add( Box.createVerticalStrut( 10 ) );
	}

	private void inicialitzaPanelBotons()
	{
		panel_botons.setLayout( new BoxLayout( panel_botons, BoxLayout.PAGE_AXIS ) );
		registra.setAlignmentX( Component.CENTER_ALIGNMENT );
		panel_botons.add( registra );
		panel_botons.add( Box.createVerticalStrut( 10 ) );
		convidat.setAlignmentX( Component.CENTER_ALIGNMENT );
		panel_botons.add( convidat );
		panel_botons.setOpaque( false );
	}

	protected void inicialitzaPanelPrincipal()
	{
		panel_principal.setLayout( new GridBagLayout() );
		panel_principal.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		GridBagConstraints propietats_panel = new GridBagConstraints();
		propietats_panel.fill = GridBagConstraints.HORIZONTAL;
		propietats_panel.anchor = GridBagConstraints.CENTER;
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 0;
		propietats_panel.weightx = 0.5;
		propietats_panel.weighty = 0.1;
		panel_principal.add( panel_titol, propietats_panel );
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.5;
		panel_principal.add( panel_dades, propietats_panel );
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 2;
		propietats_panel.weighty = 0.4;
		panel_principal.add( panel_botons, propietats_panel );
		propietats_panel.fill = GridBagConstraints.NONE;
		propietats_panel.gridx = 2;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.25;
		propietats_panel.weighty = 0.1;
		propietats_panel.anchor = GridBagConstraints.SOUTHEAST;
		panel_principal.add( panel_sortida, propietats_panel );
		propietats_panel.gridx = 0;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.25;
		propietats_panel.anchor = GridBagConstraints.SOUTHWEST;
		panel_principal.add( titol_baix, propietats_panel );
	}

	public void accioBotoAccepta( ActionEvent event )
	{
		try
		{
			presentacio_ctrl.setJugadorPrincipal( usuari.getText(), new String( contrasenya.getPassword() ) );
			presentacio_ctrl.vistaIniciaSessioAMenuPrincipal();
		}
		catch ( IllegalArgumentException excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat =
					dialeg.setDialeg( "Error", "Nom d'usuari o contrasenya incorrectes.", botons, JOptionPane.WARNING_MESSAGE );
		}
		catch ( Exception excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Error inesperat.", botons, JOptionPane.ERROR_MESSAGE );
		}
	}

	public void accioBotoConvidat( ActionEvent event )
	{
		try
		{
			presentacio_ctrl.entraConvidat();
			presentacio_ctrl.vistaIniciaSessioAMenuPrincipal();
		}
		catch ( IllegalArgumentException excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat =
					dialeg.setDialeg( "Error", "Nom d'usuari o contrasenya incorrectes.", botons, JOptionPane.WARNING_MESSAGE );
		}
		catch ( Exception excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Error inesperat.", botons, JOptionPane.ERROR_MESSAGE );
		}
	}

	public void accioBotoRegistra( ActionEvent event )
	{
		presentacio_ctrl.vistaIniciaSessioARegistra();
	}

	protected void assignaListeners()
	{
		accepta.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoAccepta( event );
			}
		} );

		convidat.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoConvidat( event );
			}
		} );

		registra.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoRegistra( event );
			}
		} );

		surt.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoSurt( event );
			}
		} );
	}
}
