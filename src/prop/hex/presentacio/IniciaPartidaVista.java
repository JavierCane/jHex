package prop.hex.presentacio;

import prop.hex.domini.models.enums.TipusJugadors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IniciaPartidaVista extends BaseVista implements ItemListener
{

	private JPanel panel_central = new JPanel();
	private JPanel panel_botons = new JPanel();
	private JPanel seleccio_jugador_1 = new JPanel();
	private JPanel seleccio_jugador_2 = new JPanel();
	private JButton inicia_partida = new JButton( "Inicia la partida" );
	private JButton situacio_inicial = new JButton( "Defineix la situació inicial" );
	private JButton descarta = new JButton( "Descarta" );
	private JComboBox tipus_jugador_1 = new JComboBox( new String[] {
			"Humà", "Màquina"
	} );
	private JComboBox tipus_maquina_1 = new JComboBox( new String[] {
			"Fàcil", "Difícil"
	} );
	private JComboBox tipus_jugador_2 = new JComboBox( new String[] {
			"Humà", "Màquina"
	} );
	private JComboBox tipus_maquina_2 = new JComboBox( new String[] {
			"Fàcil", "Difícil"
	} );
	private JTextField usuari = new JTextField();
	private JPasswordField contrasenya = new JPasswordField();
	private JLabel nom_jugador_principal = new JLabel( "Nom d'usuari: " + presentacio_ctrl.obteNomJugadorPrincipal
			() );
	private JLabel text_usuari = new JLabel( "Nom d'usuari:" );
	private JLabel text_contrasenya = new JLabel( "Contrasenya:" );
	private JLabel text_jugador_1 = new JLabel( "Jugador 1:" );
	private JLabel text_jugador_2 = new JLabel( "Jugador 2:" );

	public IniciaPartidaVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );
		titol = new JLabel( "Juga una partida" );
		inicialitzaVista();
	}

	protected void inicialitzaVista()
	{
		inicialitzaPanelPrincipal();
		inicialitzaPanelTitol();
		inicialitzaPanelCentral();
		inicialitzaPanelBotons();
		inicialitzaPanelSortida();
		assignaListeners();
	}

	private void inicialitzaPanelCentral()
	{
		panel_central.setLayout( new GridLayout( 2, 1, 10, 10 ) );
		panel_central.setOpaque( false );
		JPanel panel_jugador_1 = new JPanelImatge( "img/caixa.png" );
		panel_jugador_1.setBorder( BorderFactory.createRaisedBevelBorder() );
		panel_jugador_1.setLayout( new BoxLayout( panel_jugador_1, BoxLayout.PAGE_AXIS ) );
		panel_jugador_1.setOpaque( false );
		text_jugador_1.setAlignmentX( Component.CENTER_ALIGNMENT );
		panel_jugador_1.add( text_jugador_1 );
		JPanel principal_jugador_1 = new JPanel();
		principal_jugador_1.setLayout( new GridLayout( 1, 2, 10, 10 ) );
		principal_jugador_1.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		principal_jugador_1.setOpaque( false );
		seleccio_jugador_1.setLayout( new CardLayout() );
		seleccio_jugador_1.setOpaque( false );
		JPanel maquina_jugador_1 = new JPanel();
		maquina_jugador_1.add( tipus_maquina_1 );
		maquina_jugador_1.setOpaque( false );
		JPanel huma_jugador_1 = new JPanel();
		huma_jugador_1.add( nom_jugador_principal );
		huma_jugador_1.setOpaque( false );
		seleccio_jugador_1.add( huma_jugador_1, "Humà" );
		seleccio_jugador_1.add( maquina_jugador_1, "Màquina" );
		principal_jugador_1.add( tipus_jugador_1 );
		principal_jugador_1.add( seleccio_jugador_1 );
		panel_jugador_1.add( principal_jugador_1 );
		JPanel panel_jugador_2 = new JPanelImatge( "img/caixa.png" );
		panel_jugador_2.setBorder( BorderFactory.createRaisedBevelBorder() );
		panel_jugador_2.setLayout( new BoxLayout( panel_jugador_2, BoxLayout.PAGE_AXIS ) );
		panel_jugador_2.setOpaque( false );
		text_jugador_2.setAlignmentX( Component.CENTER_ALIGNMENT );
		panel_jugador_2.add( text_jugador_2 );
		JPanel principal_jugador_2 = new JPanel();
		principal_jugador_2.setLayout( new GridLayout( 1, 2, 10, 10 ) );
		principal_jugador_2.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		principal_jugador_2.setOpaque( false );
		seleccio_jugador_2.setLayout( new CardLayout() );
		seleccio_jugador_2.setOpaque( false );
		JPanel maquina_jugador_2 = new JPanel();
		maquina_jugador_2.add( tipus_maquina_2 );
		maquina_jugador_2.setOpaque( false );
		JPanel huma_jugador_2 = new JPanel();
		huma_jugador_2.setLayout( new GridLayout( 2, 2, 10, 10 ) );
		huma_jugador_2.setOpaque( false );
		huma_jugador_2.add( text_usuari );
		huma_jugador_2.add( usuari );
		huma_jugador_2.add( text_contrasenya );
		huma_jugador_2.add( contrasenya );
		seleccio_jugador_2.add( huma_jugador_2, "Humà" );
		seleccio_jugador_2.add( maquina_jugador_2, "Màquina" );
		principal_jugador_2.add( tipus_jugador_2 );
		principal_jugador_2.add( seleccio_jugador_2 );
		panel_jugador_2.add( principal_jugador_2 );
		panel_central.add( panel_jugador_1 );
		panel_central.add( panel_jugador_2 );
	}

	private void inicialitzaPanelBotons()
	{
		panel_botons.setLayout( new FlowLayout() );
		panel_botons.add( inicia_partida );
		panel_botons.add( situacio_inicial );
		panel_botons.add( descarta );
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
		propietats_panel.weighty = 0.2;
		panel_principal.add( panel_titol, propietats_panel );
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.6;
		panel_principal.add( panel_central, propietats_panel );
		propietats_panel.gridy = 2;
		propietats_panel.weighty = 0.2;
		panel_principal.add( panel_botons, propietats_panel );
		propietats_panel.fill = GridBagConstraints.NONE;
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

	public void accioBotoIniciaPartida( ActionEvent event )
	{
		TipusJugadors jugador_A;
		TipusJugadors jugador_B;
		if ( tipus_jugador_1.getSelectedItem() == "Humà" )
		{
			jugador_A = TipusJugadors.JUGADOR;
		}
		else
		{
			if (tipus_maquina_1.getSelectedItem() == "Fàcil")
			{
				jugador_A = TipusJugadors.IA_FACIL;
			}
			else
			{
				jugador_A = TipusJugadors.IA_DIFICIL;
			}
		}
		if ( tipus_jugador_2.getSelectedItem() == "Humà" )
		{
			jugador_B = TipusJugadors.JUGADOR;
		}
		else
		{
			if (tipus_maquina_2.getSelectedItem() == "Fàcil")
			{
				jugador_B = TipusJugadors.IA_FACIL;
			}
			else
			{
				jugador_B = TipusJugadors.IA_DIFICIL;
			}
		}
		try
		{
			presentacio_ctrl.configuraUsuarisPartida( jugador_A, usuari.getText(),
					new String( contrasenya.getPassword() ), jugador_B );
			presentacio_ctrl.iniciaPartida(7, "AAA");
			presentacio_ctrl.vistaIniciaPartidaAPartida();
		}
		catch ( Exception excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Error inesperat.", botons,
					JOptionPane.ERROR_MESSAGE );
		}

	}

	public void accioBotoDescarta( ActionEvent event )
	{
		presentacio_ctrl.vistaIniciaPartidaAMenuPrincipal();
	}

	public void accioSeleccionaTipusJugador1( ActionEvent event )
	{

	}

	public void itemStateChanged( ItemEvent event )
	{
		if ( event.getItemSelectable() == tipus_jugador_1 )
		{
			CardLayout c = ( CardLayout ) ( seleccio_jugador_1.getLayout() );
			c.show( seleccio_jugador_1, ( String ) event.getItem() );
		}
		else
		{
			CardLayout c = ( CardLayout ) ( seleccio_jugador_2.getLayout() );
			c.show( seleccio_jugador_2, ( String ) event.getItem() );
		}
	}

	protected void assignaListeners()
	{
		inicia_partida.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoIniciaPartida( event );
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

		tipus_jugador_1.addItemListener( this );
		tipus_jugador_2.addItemListener( this );
	}
}
