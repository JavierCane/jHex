package prop.hex.presentacio;

import prop.hex.domini.models.enums.TipusJugadors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

/**
 * Vista de configuració d'una partida del joc Hex.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */
public final class ConfiguraPartidaVista extends BaseVista implements ItemListener
{

	// Panells
	private JPanel panell_central;
	private JPanel panell_botons;
	private JPanel seleccio_jugador_a; // Panell de tipus CardLayout (intercanviable)
	private JPanel seleccio_jugador_b; // Panell de tipus CardLayout (intercanviable)

	// Botons
	private JButton inicia_partida;
	private JButton situacio_inicial;
	private JButton descarta;

	// Camps de tipus combos
	private JComboBox combo_tipus_jugador_a;
	private JComboBox combo_tipus_maquina_a;
	private JComboBox combo_tipus_jugador_b;
	private JComboBox combo_tipus_maquina_b;

	// Camps de tipus text/contrasenya
	private JTextField camp_nom_partida;
	private JTextField camp_nom_convidat_a;
	private JTextField camp_nom_convidat_b;
	private JTextField camp_nom_usuari_b;
	private JPasswordField camp_contrasenya_usuari_b;

	// Etiquetes de text
	private JLabel text_nom_partida;
	private JLabel text_convidat_a;
	private JLabel text_convidat_b;
	private JLabel text_usuari;
	private JLabel text_contrasenya;
	private JLabel text_jugador_a;
	private JLabel text_jugador_b;

	/**
	 * Constructor que crea una vista passant-li quin és el frame sobre el qual s'haurà de treballar i el
	 * controlador de presentació al qual haurà de demanar certes operacions.
	 *
	 * @param presentacio_ctrl Controlador de presentació.
	 * @param frame_principal  Frame principal sobre el que s'hauran d'afegir els diferents components.
	 */
	public ConfiguraPartidaVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "Juga una partida" );

		// Panells
		panell_central = new JPanel();
		panell_botons = new JPanel();
		seleccio_jugador_a = new JPanel(); // Panell de tipus CardLayout (intercanviable)
		seleccio_jugador_b = new JPanel(); // Panell de tipus CardLayout (intercanviable)

		// Botons
		inicia_partida = new JButton( "Inicia la partida" );
		situacio_inicial = new JButton( "Defineix la situació inicial" );
		descarta = new JButton( "Descarta" );

		// Camps de tipus combos
		combo_tipus_maquina_a = new JComboBox( TipusJugadors.obteLlistatMaquines() );
		combo_tipus_jugador_b = new JComboBox( new String[] {
				"Màquina", "Convidat", "Usuari registrat"
		} );
		combo_tipus_maquina_b = new JComboBox( TipusJugadors.obteLlistatMaquines() );

		// Camps de tipus text/contrasenya
		camp_nom_partida = new JTextField();
		camp_nom_convidat_a = new JTextField();
		camp_nom_convidat_b = new JTextField();
		camp_nom_usuari_b = new JTextField();
		camp_contrasenya_usuari_b = new JPasswordField();

		// Etiquetes de text
		text_nom_partida = new JLabel( "Nom de la partida:" );
		text_convidat_a = new JLabel( "Nom d'usuari convidat 1:" );
		text_convidat_b = new JLabel( "Nom d'usuari convidat 2:" );
		text_usuari = new JLabel( "Nom d'usuari:" );
		text_contrasenya = new JLabel( "Contrasenya:" );
		text_jugador_a = new JLabel( "Jugador 1:" );
		text_jugador_b = new JLabel( "Jugador 2:" );

		// Si l'usuari ha iniciat sessió com a convidat, únicament mostro l'opció de jugar com convidat o màquina
		if ( presentacio_ctrl.getEsConvidat() )
		{
			combo_tipus_jugador_a = new JComboBox( new String[] {
					"Convidat", "Màquina"
			} );
		}
		else // Si ha iniciat sessió como a usuari registrat, mostro les 3 opcions
		{
			combo_tipus_jugador_a = new JComboBox( new String[] {
					presentacio_ctrl.obteNomJugadorPrincipal(), "Convidat", "Màquina"
			} );
		}

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

	@Override
	protected void inicialitzaPanellCentral()
	{
		// Panell central
		// ---------------------------------------------------------------------------------------------
		panell_central.setLayout( new GridLayout( 3, 1, 10, 10 ) );
		panell_central.setOpaque( false );

		// Panell nom partida
		// -----------------------------------------------------------------------------------------

		JPanel panell_nom_partida = new JPanelImatge( "img/caixa.png" );
		panell_nom_partida.setBorder( BorderFactory.createRaisedBevelBorder() );
		panell_nom_partida.setLayout( new BoxLayout( panell_nom_partida, BoxLayout.PAGE_AXIS ) );
		JPanel camps_nom_partida = new JPanel();
		camps_nom_partida.setLayout( new GridLayout( 1, 2, 10, 10 ) );
		camps_nom_partida.setOpaque( false );
		camps_nom_partida.add( text_nom_partida );
		camps_nom_partida.add( camp_nom_partida );
		camps_nom_partida.setBorder( BorderFactory.createEmptyBorder( 25, 10, 25, 10 ) );
		panell_nom_partida.add( camps_nom_partida );
		panell_central.add( panell_nom_partida );

		// Panell jugador 1
		// -------------------------------------------------------------------------------------------
		JPanel panell_jugador_a = new JPanelImatge( "img/caixa.png" ); // Caixa i text "Jugador 1:"
		panell_jugador_a.setOpaque( false );
		panell_jugador_a.setBorder( BorderFactory.createRaisedBevelBorder() );
		panell_jugador_a.setLayout( new BoxLayout( panell_jugador_a, BoxLayout.PAGE_AXIS ) );
		text_jugador_a.setAlignmentX( Component.CENTER_ALIGNMENT );
		panell_jugador_a.add( text_jugador_a );

		JPanel principal_jugador_1 = new JPanel();
		principal_jugador_1.setOpaque( false );
		principal_jugador_1.setLayout( new GridLayout( 1, 2, 10, 10 ) );
		principal_jugador_1.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );

		// Layout de tipus CardLayout per canviar formulari en base a la selecció del tipus d'usuari
		seleccio_jugador_a.setLayout( new CardLayout() );
		seleccio_jugador_a.setOpaque( false );

		// Si l'usuari no ha iniciat sessió com a convidat, mostro l'opció de jugar com a registrat
		if ( !presentacio_ctrl.getEsConvidat() )
		{
			JPanel text_usuari_registrat = new JPanel();
			text_usuari_registrat.setOpaque( false );
			seleccio_jugador_a.add( text_usuari_registrat, presentacio_ctrl.obteNomJugadorPrincipal() );
		}

		// Formulari nom convidat jugador 1 per quan a seleccionat jugar com convidat
		JPanel formulari_nom_convidat_jugador_a = new JPanel();
		formulari_nom_convidat_jugador_a.setLayout( new GridLayout( 2, 1 ) );
		formulari_nom_convidat_jugador_a.setOpaque( false );
		formulari_nom_convidat_jugador_a.add( text_convidat_a );
		formulari_nom_convidat_jugador_a.add( camp_nom_convidat_a );
		seleccio_jugador_a.add( formulari_nom_convidat_jugador_a, "Convidat" );

		// Seleccionable tipus de màquina jugador 1 per quan a seleccionat jugar com una màquina
		JPanel seleccio_tipus_maquina_jugador_a = new JPanel();
		seleccio_tipus_maquina_jugador_a.setLayout( new GridLayout( 1, 1 ) );
		seleccio_tipus_maquina_jugador_a.setOpaque( false );
		seleccio_tipus_maquina_jugador_a.add( combo_tipus_maquina_a );
		seleccio_jugador_a.add( seleccio_tipus_maquina_jugador_a, "Màquina" );

		// Afegeixo a la vista el panell de selecció de tipus de jugador a
		principal_jugador_1.add( combo_tipus_jugador_a );
		principal_jugador_1.add( seleccio_jugador_a );

		panell_jugador_a.add( principal_jugador_1 );
		panell_central.add( panell_jugador_a );

		// Panell jugador 2
		// -------------------------------------------------------------------------------------------
		JPanel panell_jugador_b = new JPanelImatge( "img/caixa.png" ); // Caixa i text "Jugador 2:"
		panell_jugador_b.setBorder( BorderFactory.createRaisedBevelBorder() );
		panell_jugador_b.setLayout( new BoxLayout( panell_jugador_b, BoxLayout.PAGE_AXIS ) );
		panell_jugador_b.setOpaque( false );
		text_jugador_b.setAlignmentX( Component.CENTER_ALIGNMENT );
		panell_jugador_b.add( text_jugador_b );

		JPanel principal_jugador_b = new JPanel();
		principal_jugador_b.setLayout( new GridLayout( 1, 2, 10, 10 ) );
		principal_jugador_b.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		principal_jugador_b.setOpaque( false );

		// Layout de tipus CardLayout per canviar formulari en base a la selecció del tipus d'usuari
		seleccio_jugador_b.setLayout( new CardLayout() );
		seleccio_jugador_b.setOpaque( false );

		JPanel seleccio_tipus_maquina_jugador_b = new JPanel(); // Seleccionable tipus de màquina jugador 2
		seleccio_tipus_maquina_jugador_b.setOpaque( false );
		seleccio_tipus_maquina_jugador_b.setLayout( new GridLayout( 1, 1 ) );
		seleccio_tipus_maquina_jugador_b.add( combo_tipus_maquina_b );
		seleccio_jugador_b.add( seleccio_tipus_maquina_jugador_b, "Màquina" );

		JPanel formulari_nom_convidat_jugador_b = new JPanel(); // Formulari nom convidat jugador 2
		formulari_nom_convidat_jugador_b.setLayout( new GridLayout( 2, 1 ) );
		formulari_nom_convidat_jugador_b.setOpaque( false );
		formulari_nom_convidat_jugador_b.add( text_convidat_b );
		formulari_nom_convidat_jugador_b.add( camp_nom_convidat_b );
		seleccio_jugador_b.add( formulari_nom_convidat_jugador_b, "Convidat" );

		// Formulari inici sessió jugador 2 per quan a seleccionat iniciar sessió com usuari registrat
		JPanel formulari_inici_sessio_jugador_b = new JPanel();
		formulari_inici_sessio_jugador_b.setOpaque( false );
		formulari_inici_sessio_jugador_b.setLayout( new GridLayout( 2, 2 ) );
		formulari_inici_sessio_jugador_b.add( text_usuari );
		formulari_inici_sessio_jugador_b.add( camp_nom_usuari_b );
		formulari_inici_sessio_jugador_b.add( text_contrasenya );
		formulari_inici_sessio_jugador_b.add( camp_contrasenya_usuari_b );
		seleccio_jugador_b.add( formulari_inici_sessio_jugador_b, "Usuari registrat" );

		principal_jugador_b.add( combo_tipus_jugador_b );
		principal_jugador_b.add( seleccio_jugador_b );

		panell_jugador_b.add( principal_jugador_b );
		panell_central.add( panell_jugador_b );
	}

	@Override
	protected void inicialitzaPanellPeu()
	{
		panell_botons.setLayout( new GridLayout( 2, 1, 10, 10 ) );
		panell_botons.setOpaque( false );

		JPanel panell_inicia_partida = new JPanel();
		panell_inicia_partida.setLayout( new GridLayout( 1, 1, 20, 20 ) );
		panell_inicia_partida.setBorder( BorderFactory.createEmptyBorder( 0, 150, 0, 150 ) );
		panell_inicia_partida.setOpaque( false );
		panell_inicia_partida.add( inicia_partida );
		panell_botons.add( panell_inicia_partida );

		JPanel panell_situacio_inicial_descarta = new JPanel();
		panell_situacio_inicial_descarta.setLayout( new GridLayout( 1, 2, 10, 10 ) );
		panell_situacio_inicial_descarta.setBorder( BorderFactory.createEmptyBorder( 0, 100, 0, 100 ) );
		panell_situacio_inicial_descarta.setOpaque( false );
		panell_situacio_inicial_descarta.add( situacio_inicial );
		panell_situacio_inicial_descarta.add( descarta );
		panell_botons.add( panell_situacio_inicial_descarta );
	}

	/**
	 * Inicialitza un jugador de la partida cridant a preInicialitzaUsuariPartida de PresentacioCtrl per tal de que
	 * aquesta cridi a preInicialitzaUsuariPartida de PartidaCtrl en base a el tipus d'usuari seleccionat i les seves
	 * dades corresponents.
	 *
	 * @param num_jugador
	 * @param combo_tipus_jugador
	 * @param camp_nom_convidat
	 * @param combo_tipus_maquina
	 * @param nom_usuari
	 * @param contrasenya_usuari
	 */
	private void preInicialitzaUsuariPartida( int num_jugador, JComboBox combo_tipus_jugador,
	                                          JTextField camp_nom_convidat, JComboBox combo_tipus_maquina,
	                                          String nom_usuari, String contrasenya_usuari )
			throws IOException, ClassNotFoundException, IllegalArgumentException
	{
		// Si ha seleccionat jugador màquina, obtinc quin tipus de màquina exactament i la carrego
		if ( combo_tipus_jugador.getSelectedItem() == "Màquina" )
		{
			TipusJugadors tipus_jugador_a_inicialitzar = ( TipusJugadors ) combo_tipus_maquina.getSelectedItem();

			try
			{
				presentacio_ctrl.preInicialitzaUsuariPartida( num_jugador, tipus_jugador_a_inicialitzar, "", "" );
			}
			catch ( IOException e )
			{
				throw new IOException( "Error carregant el fitxer d'aquesta màquina de disc, " +
				                       "prova de nou o selecciona una altra." );
			}
			catch ( ClassNotFoundException e )
			{
				throw new ClassNotFoundException( "Error carregant el fitxer d'aquesta màquina de disc, " +
				                       "prova de nou o selecciona una altra." );
			}
		}
		// Si ha seleccionat convidat, creo una instància temporal d'usuari amb el nom escollit
		else if ( combo_tipus_jugador.getSelectedItem() == "Convidat" )
		{
			if ( presentacio_ctrl.existeixUsuari( camp_nom_convidat.getText() ) )
			{
				throw new IllegalArgumentException( "El nom d'usuari ja està en ús." );
			}
			else if ( num_jugador == 1 && combo_tipus_jugador_a.getSelectedItem() == "Convidat" &&
					camp_nom_convidat_a.getText().equals( camp_nom_convidat.getText() ) )
			{
				throw new IllegalArgumentException( "El nom dels dos jugadors ha de ser diferent." );
			}
			else
			{
				try
				{
					presentacio_ctrl.preInicialitzaUsuariPartida( num_jugador, TipusJugadors.CONVIDAT,
							camp_nom_convidat.getText(), "" );
				}
				catch ( IOException e ) // Aquest error no s'hauria de donar mai ja que si l'usuari es de tipus
				// convidat, no el guardem a disc
				{
					throw new ClassNotFoundException( "Error intentant guardar l'usuari de tipus convidat a disc, " +
					                                  "prova de nou o juga amb un usuari registrat." );
				}
				catch ( ClassNotFoundException e ) // Aquest error no s'hauria de donar mai ja que si l'usuari es
				// de tipus convidat, no el guardem a disc
				{
					throw new ClassNotFoundException( "Error intentant guardar l'usuari de tipus convidat a disc, " +
					                                  "prova de nou o juga amb un usuari registrat." );
				}
			}
		}
		else // Si ha seleccionat l'altra opció, es que vol jugar amb un usuari registrat
		{
			if ( num_jugador == 1 && combo_tipus_jugador_a.getSelectedItem() == presentacio_ctrl
					.obteNomJugadorPrincipal() && nom_usuari.equals( presentacio_ctrl.obteNomJugadorPrincipal() ) )
			{
				throw new IllegalArgumentException( "No pots jugar contra tu mateix." );
			}
			else if ( num_jugador == 1 && nom_usuari.isEmpty() )
			{
				throw new IllegalArgumentException( "Has d'identificar-te amb el teu nom i contrasenya per poder " +
				                                    "jugar com a registrat  .");
			}
			else
			{
				try
				{
					presentacio_ctrl.preInicialitzaUsuariPartida( num_jugador, TipusJugadors.JUGADOR, nom_usuari,
							contrasenya_usuari );
				}
				catch ( IllegalArgumentException e )
				{
					// Error de tipus IllegalArgumentException, pot set perque ha introduït un usuari que no existeix,
					// perque l'usuari sigui de tipus intern de sistema o perque la contrasenya no sigui la correcta.
					throw new IllegalArgumentException( "Error intentant carregar l'usuari demanat: " + e.getMessage() );
				}
				catch ( IOException e )
				{
					throw new IOException( "Error carregant el fitxer d'aquest usuari de disc, " +
					                       "prova de nou o selecciona un altra." );
				}
				catch ( ClassNotFoundException e )
				{
					throw new ClassNotFoundException( "Error carregant el fitxer d'aquest usuari de disc, " +
					                                  "prova de nou o selecciona un altra." );
				}
			}
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

		situacio_inicial.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoSituacioInicial( event );
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

	/**
	 * Defineix el comportament del botó d'iniciar partida quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoIniciaPartida( ActionEvent event )
	{
		// Inicialitzo els dos jugadors de la partida en base a les dades dels seus formularis
		if ( camp_nom_partida.getText().isEmpty() )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Has de definir un nom de partida, " +
					"això servirà per identificar-la quan per exemple la guardis i la vulguis tornar a carregar" +
					".", botons, JOptionPane.ERROR_MESSAGE );
		}
		else
		{
			// Si ha posat el nom de la partida, comprobo al mateix temps la validesa dels dos usuaris
			boolean usuaris_prinicialitzats_ok = false;

			try
			{
				preInicialitzaUsuariPartida( 0, combo_tipus_jugador_a, camp_nom_convidat_a, combo_tipus_maquina_a,
						"", "" );

				usuaris_prinicialitzats_ok = true;
			}
			catch ( ClassNotFoundException excepcio )
			{
				VistaDialeg dialeg = new VistaDialeg();
				String[] botons = { "Accepta" };
				String valor_seleccionat = dialeg.setDialeg( "Error amb l'usuari 1", excepcio.getMessage(), botons,
						JOptionPane.ERROR_MESSAGE );
			}
			catch ( IOException excepcio )
			{
				VistaDialeg dialeg = new VistaDialeg();
				String[] botons = { "Accepta" };
				String valor_seleccionat = dialeg.setDialeg( "Error amb l'usuari 1", excepcio.getMessage(), botons,
						JOptionPane.ERROR_MESSAGE );
			}
			catch ( IllegalArgumentException excepcio )
			{
				VistaDialeg dialeg = new VistaDialeg();
				String[] botons = { "Accepta" };
				String valor_seleccionat = dialeg.setDialeg( "Error amb l'usuari 1", excepcio.getMessage(), botons,
						JOptionPane.ERROR_MESSAGE );
			}

			try
			{
				preInicialitzaUsuariPartida( 1, combo_tipus_jugador_b, camp_nom_convidat_b, combo_tipus_maquina_b,
							camp_nom_usuari_b.getText(), new String( camp_contrasenya_usuari_b.getPassword() ) );
			}
			catch ( IOException excepcio )
			{
				usuaris_prinicialitzats_ok = false;

				VistaDialeg dialeg = new VistaDialeg();
				String[] botons = { "Accepta" };
				String valor_seleccionat = dialeg.setDialeg( "Error amb l'usuari 2", excepcio.getMessage(), botons,
						JOptionPane.ERROR_MESSAGE );
			}
			catch ( ClassNotFoundException excepcio )
			{
				usuaris_prinicialitzats_ok = false;

				VistaDialeg dialeg = new VistaDialeg();
				String[] botons = { "Accepta" };
				String valor_seleccionat = dialeg.setDialeg( "Error amb l'usuari 2", excepcio.getMessage(), botons,
						JOptionPane.ERROR_MESSAGE );
			}
			catch ( IllegalArgumentException excepcio )
			{
				usuaris_prinicialitzats_ok = false;

				VistaDialeg dialeg = new VistaDialeg();
				String[] botons = { "Accepta" };
				String valor_seleccionat = dialeg.setDialeg( "Error amb l'usuari 2", excepcio.getMessage(), botons,
						JOptionPane.ERROR_MESSAGE );
			}

			// Si els dos usuaris son correctes, trato d'inicilitzar la partida
			if ( usuaris_prinicialitzats_ok )
			{
				try
				{
					presentacio_ctrl.inicialitzaPartida( 7, camp_nom_partida.getText(), false );
					presentacio_ctrl.vistaConfiguraPartidaAPartida();
				}
				catch ( ClassNotFoundException excepcio )
				{
					VistaDialeg dialeg = new VistaDialeg();
					String[] botons = { "Accepta" };
					String valor_seleccionat = dialeg.setDialeg( "Error inicialitzant la partida", excepcio.getMessage(),
							botons, JOptionPane.ERROR_MESSAGE );
				}
				catch ( InstantiationException excepcio )
				{
					VistaDialeg dialeg = new VistaDialeg();
					String[] botons = { "Accepta" };
					String valor_seleccionat = dialeg.setDialeg( "Error inicialitzant la partida", excepcio.getMessage(),
							botons, JOptionPane.ERROR_MESSAGE );
				}
				catch ( IllegalAccessException excepcio )
				{
					VistaDialeg dialeg = new VistaDialeg();
					String[] botons = { "Accepta" };
					String valor_seleccionat = dialeg.setDialeg( "Error inicialitzant la partida", excepcio.getMessage(),
							botons, JOptionPane.ERROR_MESSAGE );
				}
			}
		}
	}

	/**
	 * Defineix el comportament del botó de definir situació inicial quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoSituacioInicial( ActionEvent event )
	{
		// Inicialitzo els dos jugadors de la partida en base a les dades dels seus formularis
		if ( camp_nom_partida.getText().isEmpty() )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Has de definir un nom de partida, " +
					"això servirà per identificar-la quan per exemple la guardis i la vulguis tornar a carregar" +
					".", botons, JOptionPane.ERROR_MESSAGE );
		}
		else
		{
			try
			{
				preInicialitzaUsuariPartida( 0, combo_tipus_jugador_a, camp_nom_convidat_a, combo_tipus_maquina_a,
						"", "" );
				preInicialitzaUsuariPartida( 1, combo_tipus_jugador_b, camp_nom_convidat_b, combo_tipus_maquina_b,
						camp_nom_usuari_b.getText(), new String( camp_contrasenya_usuari_b.getPassword() ) );
				presentacio_ctrl.inicialitzaPartida( 7, camp_nom_partida.getText(), true );
				presentacio_ctrl.vistaConfiguraPartidaADefineixSituacio();
			}
			catch ( Exception excepcio )
			{
				VistaDialeg dialeg = new VistaDialeg();
				String[] botons = { "Accepta" };
				String valor_seleccionat = dialeg.setDialeg( "Error", excepcio.getMessage(), botons,
						JOptionPane.ERROR_MESSAGE );
			}
		}
	}

	/**
	 * Defineix el comportament del botó de descartar quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoDescarta( ActionEvent event )
	{
		presentacio_ctrl.vistaConfiguraPartidaAMenuPrincipal();
	}

	/**
	 * Mètode per intercanviar els diferents panells afegits al CardLayout quan es canviï el tipus de jugador
	 * seleccionat
	 *
	 * @param event Event que activarà el canvi de panell.
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
}
