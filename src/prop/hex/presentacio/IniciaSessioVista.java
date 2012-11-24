package prop.hex.presentacio;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class IniciaSessioVista
{

	private PresentacioCtrl presentacio_ctrl;
	private JFrame frame_vista;
	private JPanelImatge panel_principal = new JPanelImatge( "img/fons.png" );
	private JPanel panel_titol = new JPanel();
	private JPanel panel_dades = new JPanelImatge( "img/caixa.png" );
	private JPanel panel_botons = new JPanel();
	private JPanel panel_sortida = new JPanel();
	private JButton accepta = new JButton( "Accepta" );
	private JButton registra = new JButton( "Registra't" );
	private JButton convidat = new JButton( "Entra com a convidat" );
	private JButton surt = new JButton( "", new ImageIcon( "img/surt.png" ) );
	private JTextField usuari = new JTextField();
	private JPasswordField contrasenya = new JPasswordField();
	private JLabel titol = new JLabel( "Inicia sessió" );
	private JLabel text_usuari = new JLabel( "Nom d'usuari:" );
	private JLabel text_contrasenya = new JLabel( "Contrasenya:" );
	private JLabel titol_baix = new JLabel( "jHex v1.0" );

	public IniciaSessioVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		this.presentacio_ctrl = presentacio_ctrl;
		frame_vista = frame_principal;
		inicialitzaVista();
	}

	public void fesVisible()
	{
		frame_vista.setContentPane(panel_principal);
		frame_vista.pack();
		frame_vista.setVisible( true );
	}

	public void fesInvisible()
	{
		frame_vista.setVisible( false );
	}

	public void activa()
	{
		frame_vista.setEnabled( true );
	}

	public void desactiva()
	{
		frame_vista.setEnabled( false );
	}

	private void inicialitzaVista()
	{
		inicialitzaPanelPrincipal();
		inicialitzaPanelTitol();
		inicialitzaPanelDades();
		inicialitzaPanelBotons();
		inicialitzaPanelSortida();
		assignaListeners();
	}

	private void inicialitzaPanelTitol()
	{
		panel_titol.add( titol );
		panel_titol.setOpaque( false );
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

	private void inicialitzaPanelSortida()
	{
		panel_sortida.add( surt );
		panel_sortida.setOpaque( false );
	}

	private void inicialitzaPanelPrincipal()
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
			String valor_seleccionat = dialeg.setDialeg( "Error", "Nom d'usuari o contrasenya incorrectes.",
					botons, 2 );
		}
		catch ( Exception excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Error inesperat.", botons, 0 );
		}
	}

	public void accioBotoConvidat( ActionEvent event )
	{
		try
		{
			presentacio_ctrl.entraConvidat();
		}
		catch ( IllegalArgumentException excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Nom d'usuari o contrasenya incorrectes.",
					botons, 2 );
		}
		catch ( Exception excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Error inesperat.", botons, 0 );
		}
	}

	public void accioBotoRegistra( ActionEvent event )
	{
		presentacio_ctrl.vistaIniciaSessioARegistra();
	}

	public void accioBotoSurt( ActionEvent event )
	{
		VistaDialeg dialeg = new VistaDialeg();
		String[] botons = { "Sí", "No" };
		String valor_seleccionat = dialeg.setDialeg( "Confirmació de la sortida", "Estàs segur de que vols sortir "
				+ "del programa?", botons, 3 );
		if ( valor_seleccionat == "Sí" )
		{
			System.exit( 0 );
		}
	}

	private void assignaListeners()
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
