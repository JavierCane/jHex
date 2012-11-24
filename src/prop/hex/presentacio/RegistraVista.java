package prop.hex.presentacio;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RegistraVista
{

	private PresentacioCtrl presentacio_ctrl;
	private JFrame frame_vista;
	private JPanelImatge panel_principal = new JPanelImatge( "img/fons.png" );
	private JPanel panel_titol = new JPanel();
	private JPanel panel_dades = new JPanelImatge( "img/caixa.png" );
	private JPanel panel_sortida = new JPanel();
	private JButton accepta = new JButton( "Accepta" );
	private JButton descarta = new JButton( "Descarta" );
	private JButton surt = new JButton( "", new ImageIcon( "img/surt.png" ) );
	private JTextField usuari = new JTextField();
	private JPasswordField contrasenya = new JPasswordField();
	private JPasswordField confirma_contrasenya = new JPasswordField();
	private JLabel titol = new JLabel( "Registra't" );
	private JLabel text_usuari = new JLabel( "Nom d'usuari:" );
	private JLabel text_contrasenya = new JLabel( "Contrasenya:" );
	private JLabel text_confirma_contrasenya = new JLabel( "Confirma la contrasenya:" );
	private JLabel titol_baix = new JLabel( "jHex v1.0" );

	public RegistraVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		this.presentacio_ctrl = presentacio_ctrl;
		frame_vista = frame_principal;
		inicialitzaVista();
	}

	public void fesVisible()
	{
		frame_vista.setContentPane( panel_principal );
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
		panel_camps.setLayout( new GridLayout( 3, 2, 10, 10 ) );
		panel_camps.add( text_usuari );
		panel_camps.add( usuari );
		panel_camps.add( text_contrasenya );
		panel_camps.add( contrasenya );
		panel_camps.add( text_confirma_contrasenya );
		panel_camps.add( confirma_contrasenya );
		panel_camps.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		panel_camps.setOpaque( false );
		JPanel panel_botons = new JPanel();
		panel_botons.setLayout( new FlowLayout() );
		panel_botons.add( accepta );
		panel_botons.add( descarta );
		panel_botons.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		panel_botons.setOpaque( false );
		panel_dades.add( panel_camps );
		panel_dades.add( panel_botons );
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
		propietats_panel.weighty = 0.2;
		panel_principal.add( panel_titol, propietats_panel );
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.6;
		panel_principal.add( panel_dades, propietats_panel );
		propietats_panel.fill = GridBagConstraints.NONE;
		propietats_panel.gridx = 2;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.25;
		propietats_panel.weighty = 0.2;
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
			String contrasenya_introduida = new String( contrasenya.getPassword() );
			if ( !contrasenya_introduida.equals( new String( confirma_contrasenya.getPassword() ) ) )
			{
				VistaDialeg dialeg = new VistaDialeg();
				String[] botons = { "Accepta" };
				String valor_seleccionat = dialeg.setDialeg( "Error", "Les dues contrasenyes no coincideixen.",
						botons, 2 );
			}
			else
			{
				presentacio_ctrl.registraUsuari( usuari.getText(), contrasenya_introduida );
				presentacio_ctrl.vistaRegistraAIniciaSessio();
			}
		}
		catch ( IllegalArgumentException excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Nom d'usuari no vàlid.", botons, 2 );
		}
		catch ( Exception excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Error inesperat.", botons, 0 );
		}
	}

	public void accioBotoDescarta( ActionEvent event )
	{
		presentacio_ctrl.vistaRegistraAIniciaSessio();
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


		descarta.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoDescarta( event );
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
