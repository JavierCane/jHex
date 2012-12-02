package prop.hex.presentacio;

import prop.hex.domini.models.enums.TipusJugadors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public final class IniciaPartidaVista extends BaseVista implements ItemListener
{

	// Panells
	private JPanel panell_central = new JPanel();
	private JPanel panell_botons = new JPanel();
	private JPanel seleccio_jugador_a = new JPanel(); // Panell de tipus CardLayout (intercanviable)
	private JPanel seleccio_jugador_b = new JPanel(); // Panell de tipus CardLayout (intercanviable)

	// Botons
	private JButton inicia_partida = new JButton( "Inicia la partida" );
	private JButton situacio_inicial = new JButton( "Defineix la situació inicial" );
	private JButton descarta = new JButton( "Descarta" );

	// Camps de tipus combos
	private JComboBox combo_tipus_jugador_a = new JComboBox( new String[] {
			"Convidat 1",
			"Màquina"
	} );
	private JComboBox combo_tipus_maquina_a = new JComboBox( TipusJugadors.obteLlistatMaquines() );
	private JComboBox combo_tipus_jugador_b = new JComboBox( new String[] {
			"Usuari registrat",
			"Convidat 2",
			"Màquina"
	} );
	private JComboBox combo_tipus_maquina_b = new JComboBox( TipusJugadors.obteLlistatMaquines() );

	// Camps de tipus text/contrasenya
	private JTextField camp_nom_usuari_b = new JTextField();
	private JPasswordField camp_contrasenya_usuari_b = new JPasswordField();

	// Etiquetes de text
	private JLabel nom_jugador_principal = new JLabel( "Nom d'usuari: " + presentacio_ctrl.obteNomJugadorPrincipal() );
	private JLabel text_usuari = new JLabel( "Nom d'usuari:" );
	private JLabel text_contrasenya = new JLabel( "Contrasenya:" );
	private JLabel text_jugador_a = new JLabel( "Jugador 1:" );
	private JLabel text_jugador_b = new JLabel( "Jugador 2:" );

	public IniciaPartidaVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "Juga una partida" );

		inicialitzaVista();
	}

	@Override
	protected void inicialitzaPanellCentral()
	{
		// Panell central
		panell_central.setLayout( new GridLayout( 2, 1, 10, 10 ) );
		panell_central.setOpaque( false );

		// Panell jugador 1
		JPanel panell_jugador_1 = new JPanelImatge( "img/caixa.png" ); // Caixa i text "Jugador 1:"
		panell_jugador_1.setBorder( BorderFactory.createRaisedBevelBorder() );
		panell_jugador_1.setLayout( new BoxLayout( panell_jugador_1, BoxLayout.PAGE_AXIS ) );
		panell_jugador_1.setOpaque( false );
		text_jugador_a.setAlignmentX( Component.CENTER_ALIGNMENT );
		panell_jugador_1.add( text_jugador_a );

		JPanel principal_jugador_1 = new JPanel();
		principal_jugador_1.setLayout( new GridLayout( 1, 2, 10, 10 ) );
		principal_jugador_1.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		principal_jugador_1.setOpaque( false );

		// Seleccionable Jugador 1, CardLayout (mostra un dels n JPanels afegits
		seleccio_jugador_a.setLayout( new CardLayout() );
		seleccio_jugador_a.setOpaque( false );

		JPanel text_usuari_registrat = new JPanel(); // Etiqueta de text nom Jugador 1
		text_usuari_registrat.add( nom_jugador_principal );
		text_usuari_registrat.setOpaque( false );
		seleccio_jugador_a.add( text_usuari_registrat, "Usuari registrat" );

		// Combo/seleccionable Jugador 1, Convidat 1, Màquina 1
		JPanel opcions_maquina_seleccio_jugador_a = new JPanel();
		opcions_maquina_seleccio_jugador_a.add( combo_tipus_maquina_a );
		opcions_maquina_seleccio_jugador_a.setOpaque( false );
		seleccio_jugador_a.add( opcions_maquina_seleccio_jugador_a, "Màquina" );

		// Afegeixo a la vista el panell de selecció de tipus de jugador a
		principal_jugador_1.add( combo_tipus_jugador_a );
		principal_jugador_1.add( seleccio_jugador_a );
		panell_jugador_1.add( principal_jugador_1 );

		// Panell jugador 2
		JPanel panell_jugador_2 = new JPanelImatge( "img/caixa.png" ); // Caixa i text "Jugador 2:"
		panell_jugador_2.setBorder( BorderFactory.createRaisedBevelBorder() );
		panell_jugador_2.setLayout( new BoxLayout( panell_jugador_2, BoxLayout.PAGE_AXIS ) );
		panell_jugador_2.setOpaque( false );
		text_jugador_b.setAlignmentX( Component.CENTER_ALIGNMENT );
		panell_jugador_2.add( text_jugador_b );

		JPanel principal_jugador_2 = new JPanel();
		principal_jugador_2.setLayout( new GridLayout( 1, 2, 10, 10 ) );
		principal_jugador_2.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		principal_jugador_2.setOpaque( false );

		JPanel maquina_jugador_2 = new JPanel(); // Combo/seleccionable Convidat 2, Màquina 2
		maquina_jugador_2.add( combo_tipus_maquina_b );
		maquina_jugador_2.setOpaque( false );

		JPanel huma_jugador_2 = new JPanel(); // Etiqueta de text nom Jugador 2
		huma_jugador_2.setLayout( new GridLayout( 2, 2, 10, 10 ) );
		huma_jugador_2.setOpaque( false );

		huma_jugador_2.add( text_usuari );
		huma_jugador_2.add( camp_nom_usuari_b );
		huma_jugador_2.add( text_contrasenya );
		huma_jugador_2.add( camp_contrasenya_usuari_b );

		seleccio_jugador_b.setLayout( new CardLayout() );
		seleccio_jugador_b.setOpaque( false );
		seleccio_jugador_b.add( huma_jugador_2, "Humà" );
		seleccio_jugador_b.add( maquina_jugador_2, "Màquina" );

		principal_jugador_2.add( combo_tipus_jugador_b );
		principal_jugador_2.add( seleccio_jugador_b );
		panell_jugador_2.add( principal_jugador_2 );

		// Panel central
		panell_central.add( panell_jugador_1 );
		panell_central.add( panell_jugador_2 );
	}

	@Override
	protected void inicialitzaPanellPeu()
	{
		panell_botons.setLayout( new FlowLayout() );
		panell_botons.add( inicia_partida );
		panell_botons.add( situacio_inicial );
		panell_botons.add( descarta );
		panell_botons.setOpaque( false );
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
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.6;
		panell_principal.add( panell_central, propietats_panel );
		propietats_panel.gridy = 2;
		propietats_panel.weighty = 0.2;
		panell_principal.add( panell_botons, propietats_panel );
		propietats_panel.fill = GridBagConstraints.NONE;
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

	public void accioBotoIniciaPartida( ActionEvent event )
	{
		TipusJugadors tipus_jugador_a;
		TipusJugadors tipus_jugador_b;

		if ( combo_tipus_jugador_a.getSelectedItem() == "Humà" )
		{
			tipus_jugador_a = TipusJugadors.JUGADOR;
		}
		else
		{
			tipus_jugador_a = ( TipusJugadors ) combo_tipus_maquina_a.getSelectedItem();
		}

		if ( combo_tipus_jugador_b.getSelectedItem() == "Humà" )
		{
			tipus_jugador_b = TipusJugadors.JUGADOR;
		}
		else
		{
			tipus_jugador_b = ( TipusJugadors ) combo_tipus_maquina_b.getSelectedItem();
		}

		try
		{
			presentacio_ctrl.configuraUsuarisPartida( tipus_jugador_a, camp_nom_usuari_b.getText(),
					new String( camp_contrasenya_usuari_b.getPassword() ), tipus_jugador_b );

			presentacio_ctrl.iniciaPartida( 7, "AAA" );
			presentacio_ctrl.vistaIniciaPartidaAPartida();
		}
		catch ( Exception excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat =
					dialeg.setDialeg( "Error", "Error inesperat.", botons, JOptionPane.ERROR_MESSAGE );
		}
	}

	public void accioBotoDescarta( ActionEvent event )
	{
		presentacio_ctrl.vistaIniciaPartidaAMenuPrincipal();
	}

	public void accioSeleccionaTipusJugador1( ActionEvent event )
	{
	}

	/**
	 * Mètode per intercanviar els diferents panells afegits al CardLayout quan es cambiï el tipus de jugador
	 * seleccionat
	 *
	 * @param event
	 */
	@Override
	public void itemStateChanged( ItemEvent event )
	{
		if ( event.getItemSelectable() == combo_tipus_jugador_a )
		{
			CardLayout c = ( CardLayout ) ( seleccio_jugador_a.getLayout() );
			c.show( seleccio_jugador_a, ( String ) event.getItem() );
		}
		else
		{
			CardLayout c = ( CardLayout ) ( seleccio_jugador_b.getLayout() );
			c.show( seleccio_jugador_b, ( String ) event.getItem() );
		}
	}

	@Override
	protected void assignaListeners()
	{
		super.assignaListeners();

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

		combo_tipus_jugador_a.addItemListener( this );
		combo_tipus_jugador_b.addItemListener( this );
	}
}
