package prop.hex.presentacio;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPrincipalVista
{
	private PresentacioCtrl presentacio_ctrl;
	private JFrame frame_vista;
	private JPanelImatge panel_principal = new JPanelImatge( "img/fons.png" );
	private JPanel panel_titol = new JPanel();
	private JPanel panel_botons = new JPanel();
	private JPanel panel_sortida = new JPanel();
	private JButton juga = new JButton( "Juga una partida" );
	private JButton carrega = new JButton( "Carrega una partida" );
	private JButton preferencies = new JButton( "Preferències" );
	private JButton ranquing = new JButton( "Rànquing" );
	private JButton surt = new JButton( "", new ImageIcon( "img/surt.png" ) );
	private JLabel titol = new JLabel( "Menú principal" );
	private JLabel titol_baix = new JLabel( "jHex v1.0" );

	public MenuPrincipalVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
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
		inicialitzaPanelBotons();
		inicialitzaPanelSortida();
		assignaListeners();
	}

	private void inicialitzaPanelTitol()
	{
		panel_titol.add( titol );
		panel_titol.setOpaque( false );
	}

	private void inicialitzaPanelBotons()
	{
		panel_botons.setLayout( new GridLayout( 4, 1, 20, 20 ) );
		panel_botons.add(juga);
		panel_botons.add(carrega);
		panel_botons.add(preferencies);
		panel_botons.add(ranquing);
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
		propietats_panel.weighty = 0.2;
		panel_principal.add( panel_titol, propietats_panel );
		propietats_panel.fill = GridBagConstraints.BOTH;
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.6;
		panel_principal.add( panel_botons, propietats_panel );
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

	public void accioBotoJuga( ActionEvent event )
	{

	}

	public void accioBotoCarrega( ActionEvent event )
	{

	}

	public void accioBotoPreferencies( ActionEvent event )
	{

	}

	public void accioBotoRanquing( ActionEvent event )
	{

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
		juga.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoJuga( event );
			}
		} );


		carrega.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoCarrega( event );
			}
		} );

		preferencies.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoPreferencies( event );
			}
		} );

		ranquing.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoRanquing( event );
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
