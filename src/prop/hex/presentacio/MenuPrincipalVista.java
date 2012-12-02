package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class MenuPrincipalVista extends BaseVista
{

	private JPanel panell_botons = new JPanel();
	private JPanel panell_tanca_sessio = new JPanel();
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
		propietats_panel.weighty = 0.2;
		panell_principal.add( panell_titol, propietats_panel );
		propietats_panel.fill = GridBagConstraints.BOTH;
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.6;
		panell_principal.add( panell_botons, propietats_panel );
		propietats_panel.fill = GridBagConstraints.NONE;
		propietats_panel.gridy = 2;
		propietats_panel.weighty = 0.2;
		panell_principal.add( panell_tanca_sessio, propietats_panel );
		propietats_panel.gridx = 2;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.25;
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
		panell_botons.setLayout( new GridLayout( 4, 1, 20, 20 ) );
		panell_botons.add( juga );
		panell_botons.add( carrega );
		panell_botons.add( preferencies );
		panell_botons.add( ranquing );
		panell_botons.setOpaque( false );
	}

	@Override
	protected void inicialitzaPanellPeu()
	{
		nom_jugador_principal = new JLabel( "Has iniciat sessió com a " + presentacio_ctrl.obteNomJugadorPrincipal() );
		panell_tanca_sessio.add( nom_jugador_principal );
		panell_tanca_sessio.add( tanca_sessio );
		panell_tanca_sessio.setOpaque( false );
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

	@Override
	protected void assignaListeners()
	{
		super.assignaListeners();

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
	}
}
