package prop.hex.presentacio;

import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.ModesInici;
import prop.hex.presentacio.auxiliars.JPanelImatge;
import prop.hex.presentacio.auxiliars.VistaDialeg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vista de preferències d'usuari del joc Hex.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */
public final class ConfiguracioVista extends BaseVista
{

	// Panells
	
	/**
     * Panell central de la vista.
     */
	private JPanel panell_central;
	
	/**
     * Panell amb els botons de la vista.
     */
	private JPanel panell_botons;

	// Botons d'opcions i grups associats que restringeixen la selecció múltiple
	
	/**
     * Botó d'opció de colors vermell/blau.
     */
	private JRadioButton colors_vermell_blau;
	
	/**
     * Botó d'opció de colors negre/blanc.
     */
	private JRadioButton colors_negre_blanc;
	
	/**
     * Botó d'opció de mode d'inici estàndard.
     */
	private JRadioButton mode_inici_estandard;
	
	/**
     * Botó d'opció de mode d'inici regla del pastís.
     */
	private JRadioButton mode_inici_pastis;
	
	/**
     * Grup de botons de colors.
     */
	private ButtonGroup grup_colors;
	
	/**
     * Botó de botons de modes d'inici.
     */
	private ButtonGroup grup_modes_inici;

	// Etiquetes de text
	
	/**
     * Etiqueta de colors.
     */
	private JLabel colors;
	
	/**
     * Etiqueta de mode d'inici.
     */
	private JLabel modes_inici;

	// Botons
	
	/**
     * Botó de reinicia les estadístiques.
     */
	private JButton reinicia_estadistiques;
	
	/**
     * Botó de canvia la contrasenya.
     */
	private JButton canvia_contrasenya;
	
	/**
     * Botó d'elimina usuari.
     */
	private JButton elimina_usuari;
	
	/**
     * Botó d'accepta.
     */
	private JButton accepta;
	
	/**
     * Botó de torna al menú principal.
     */
	private JButton torna;

	/**
	 * Constructor que crea una vista passant-li quin és el frame sobre el qual s'haurà de treballar.
	 *
	 * @param frame_principal Frame principal sobre el que s'hauran d'afegir els diferents components.
	 */
	public ConfiguracioVista( JFrame frame_principal )
	{
		super( frame_principal );

		titol = new JLabel( "Configuració" );
		panell_central = new JPanel();
		panell_botons = new JPanel();
		colors_vermell_blau = new JRadioButton( "Vermell/Blau" );
		colors_negre_blanc = new JRadioButton( "Negre/Blanc" );
		mode_inici_estandard = new JRadioButton( "Estàndard" );
		mode_inici_pastis = new JRadioButton( "Regla del pastís" );
		grup_colors = new ButtonGroup();
		grup_modes_inici = new ButtonGroup();
		colors = new JLabel( "Combinació de colors:" );
		modes_inici = new JLabel( "Mode d'inici de la partida:" );
		reinicia_estadistiques = new JButton( "Reinicia les estadístiques" );
		canvia_contrasenya = new JButton( "Canvia la contrasenya" );
		elimina_usuari = new JButton( "Elimina el teu usuari" );
		accepta = new JButton( "Accepta" );
		torna = new JButton( "Torna al menú principal" );

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
		panell_principal.add( Box.createHorizontalStrut( 65 ), propietats_panel );
	}

	@Override
	protected void inicialitzaPanellCentral()
	{
		panell_central.setLayout( new GridLayout( 3, 1, 10, 10 ) );
		panell_central.setOpaque( false );
		JPanel panell_colors = new JPanelImatge( getClass().getResource("/prop/img/caixa.png") );
		panell_colors.setBorder( BorderFactory.createRaisedBevelBorder() );
		panell_colors.setLayout( new GridLayout( 3, 1, 10, 10 ) );
		grup_colors.add( colors_vermell_blau );
		grup_colors.add( colors_negre_blanc );
		colors_vermell_blau.setOpaque( false );
		colors_negre_blanc.setOpaque( false );

		if ( PresentacioCtrl.getInstancia().obteCombinacioDeColorsJugadorPrincipal() ==
		     CombinacionsColors.VERMELL_BLAU )
		{
			colors_vermell_blau.setSelected( true );
		}
		else
		{
			colors_negre_blanc.setSelected( true );
		}

		panell_colors.add( colors );
		panell_colors.add( colors_vermell_blau );
		panell_colors.add( colors_negre_blanc );
		JPanel panell_modes_inici = new JPanelImatge( getClass().getResource("/prop/img/caixa.png") );
		panell_modes_inici.setBorder( BorderFactory.createRaisedBevelBorder() );
		panell_modes_inici.setLayout( new GridLayout( 3, 1, 10, 10 ) );
		grup_modes_inici.add( mode_inici_estandard );
		grup_modes_inici.add( mode_inici_pastis );
		mode_inici_estandard.setOpaque( false );
		mode_inici_pastis.setOpaque( false );

		if ( PresentacioCtrl.getInstancia().obteModeIniciJugadorPrincipal() == ModesInici.ESTANDARD )
		{
			mode_inici_estandard.setSelected( true );
		}
		else
		{
			mode_inici_pastis.setSelected( true );
		}

		panell_modes_inici.add( modes_inici );
		panell_modes_inici.add( mode_inici_estandard );
		panell_modes_inici.add( mode_inici_pastis );
		panell_central.add( panell_colors );
		panell_central.add( panell_modes_inici );
		JPanel panell_botons_opcions = new JPanel();
		if ( PresentacioCtrl.getInstancia().getEsConvidat() )
		{
			reinicia_estadistiques.setEnabled( false );
			canvia_contrasenya.setEnabled( false );
			elimina_usuari.setEnabled( false );
		}
		panell_botons_opcions.add( reinicia_estadistiques );
		panell_botons_opcions.add( canvia_contrasenya );
		panell_botons_opcions.add( elimina_usuari );
		panell_botons_opcions.setOpaque( false );
		panell_central.add( panell_botons_opcions );
	}

	@Override
	protected void inicialitzaPanellPeu()
	{
		panell_botons.setLayout( new FlowLayout() );

		panell_botons.add( accepta );
		panell_botons.add( torna );
		panell_botons.setOpaque( false );
	}

	@Override
	protected void assignaListeners()
	{
		super.assignaListeners();

		reinicia_estadistiques.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoReiniciaEstadistiques();
			}
		} );

		canvia_contrasenya.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoCanviaContrasenya();
			}
		} );

		elimina_usuari.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoEliminaUsuari();
			}
		} );

		accepta.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoAccepta();
			}
		} );

		torna.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoTorna();
			}
		} );
	}

	/**
	 * Defineix el comportament del botó de reiniciar estadístiques quan sigui pitjat.
	 */
	public void accioBotoReiniciaEstadistiques()
	{
		VistaDialeg dialeg = new VistaDialeg();
		String[] botons = {
				"Sí",
				"No"
		};
		String valor_seleccionat = dialeg.setDialeg( "Reinicia les estadístiques", "Estàs segur que vols reiniciar " +
		                                                                           "" +
		                                                                           "les teves estadístiques? Aquesta acció no es podrà desfer.",
				botons, JOptionPane.QUESTION_MESSAGE );
		if ( valor_seleccionat == "Sí" )
		{
			PresentacioCtrl.getInstancia().reiniciaEstadistiquesJugadorPrincipal();
		}
	}

	/**
	 * Defineix el comportament del botó de canviar la contrasenya quan sigui pitjat.
	 */
	public void accioBotoCanviaContrasenya()
	{
		if ( PresentacioCtrl.getInstancia().getEsConvidat() )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Els usuaris convidats no tenen contrasenya.", botons,
					JOptionPane.ERROR_MESSAGE );
		}
		else
		{
			PresentacioCtrl.getInstancia().vistaPreferenciesACanviaContrasenya();
		}
	}

	/**
	 * Defineix el comportament del botó d'eliminar usuari quan sigui pitjat.
	 */
	public void accioBotoEliminaUsuari()
	{
		VistaDialeg dialeg = new VistaDialeg();
		String[] botons = {
				"Sí",
				"No"
		};
		String valor_seleccionat = dialeg.setDialeg( "Confirma eliminació usuari",
				"Estàs segur que vols eliminar el teu usuari? Aquesta acció no es podrà desfer.", botons,
				JOptionPane.QUESTION_MESSAGE );

		if ( valor_seleccionat == "Sí" )
		{
			PresentacioCtrl.getInstancia().eliminaUsuariJugadorPrincipal();
			PresentacioCtrl.getInstancia().vistaMenuPrincipalAIniciaSessio();
		}
	}

	/**
	 * Defineix el comportament del botó d'acceptar quan sigui pitjat.
	 */
	public void accioBotoAccepta()
	{
		try
		{
			ModesInici mode_inici = ModesInici.ESTANDARD;
			CombinacionsColors combinacio_colors = CombinacionsColors.VERMELL_BLAU;

			if ( mode_inici_pastis.isSelected() )
			{
				mode_inici = ModesInici.PASTIS;
			}

			if ( colors_negre_blanc.isSelected() )
			{
				combinacio_colors = CombinacionsColors.NEGRE_BLANC;
			}

			PresentacioCtrl.getInstancia().modificaPreferenciesJugadorPrincipal( mode_inici, combinacio_colors );
			PresentacioCtrl.getInstancia().guardaJugadorPrincipal();
			PresentacioCtrl.getInstancia().vistaPreferenciesAMenuPrincipal();
		}
		catch ( Exception e )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Error al guardar el fitxer d'usuari.", botons,
					JOptionPane.ERROR_MESSAGE );
		}
	}

	/**
	 * Defineix el comportament del botó de tornar quan sigui pitjat.
	 */
	public void accioBotoTorna()
	{
		PresentacioCtrl.getInstancia().vistaPreferenciesAMenuPrincipal();
	}
}
