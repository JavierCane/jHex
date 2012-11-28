package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPrincipalVista extends BaseVista
{

	private JPanel panel_botons = new JPanel();
	private JPanel panel_tanca_sessio = new JPanel();
	private JLabel nom_jugador_principal;
	private JButton tanca_sessio = new JButton( "Tanca la sessió" );
	private JButton juga = new JButton( "Juga una partida" );
	private JButton carrega = new JButton( "Carrega una partida" );
	private JButton preferencies = new JButton( "Preferències" );
	private JButton ranquing = new JButton( "Rànquing" );

	public MenuPrincipalVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );
		titol = new JLabel( "Menú principal" );
		inicialitzaVista();
	}

	protected void inicialitzaVista()
	{
		inicialitzaPanelPrincipal();
		inicialitzaPanelTitol();
		inicialitzaPanelBotons();
		inicialitzaPanelTancaSessio();
		inicialitzaPanelSortida();
		assignaListeners();
	}

	private void inicialitzaPanelBotons()
	{
		panel_botons.setLayout( new GridLayout( 4, 1, 20, 20 ) );
		panel_botons.add( juga );
		panel_botons.add( carrega );
		panel_botons.add( preferencies );
		panel_botons.add( ranquing );
		panel_botons.setOpaque( false );
	}

	private void inicialitzaPanelTancaSessio()
	{
		nom_jugador_principal = new JLabel( "Has iniciat sessió com a " + presentacio_ctrl.obteNomJugadorPrincipal() );
		panel_tanca_sessio.add( nom_jugador_principal );
		panel_tanca_sessio.add( tanca_sessio );
		panel_tanca_sessio.setOpaque( false );
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
		propietats_panel.weighty = 0.2;
		panel_principal.add( panel_titol, propietats_panel );
		propietats_panel.fill = GridBagConstraints.BOTH;
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.6;
		panel_principal.add( panel_botons, propietats_panel );
		propietats_panel.fill = GridBagConstraints.NONE;
		propietats_panel.gridy = 2;
		propietats_panel.weighty = 0.2;
		panel_principal.add( panel_tanca_sessio, propietats_panel );
		propietats_panel.gridx = 2;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.25;
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
		presentacio_ctrl.vistaMenuPrincipalAIniciaPartida();
	}

	public void accioBotoCarrega( ActionEvent event )
	{
		presentacio_ctrl.vistaMenuPrincipalACarregaPartida();
	}

	public void accioBotoPreferencies( ActionEvent event )
	{
		presentacio_ctrl.vistaMenuPrincipalAPreferencies();
	}

	public void accioBotoRanquing( ActionEvent event )
	{
		presentacio_ctrl.vistaMenuPrincipalARanquing();
	}

	public void accioBotoTancaSessio( ActionEvent event )
	{
		presentacio_ctrl.tancaSessio();
		presentacio_ctrl.vistaMenuPrincipalAIniciaSessio();
	}

	protected void assignaListeners()
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

		tanca_sessio.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoTancaSessio( event );
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
