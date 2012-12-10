package prop.hex.presentacio;

import prop.hex.presentacio.auxiliars.ModelTaula;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vista del rànquing del joc Hex.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */
public final class RanquingVista extends BaseVista
{

	// Panells
	private JPanel panell_central;
	private JPanel panell_botons;

	// Botons
	private JButton torna;

	// Dades de les taules
	private String[][] dades_classificacio;
	private String[][] dades_hall_of_fame;

	// Taules
	private JTable taula_classificacio;
	private JTable taula_hall_of_fame;

	/**
	 * Constructor que crea una vista passant-li quin és el frame sobre el qual s'haurà de treballar i el
	 * controlador de presentació al qual haurà de demanar certes operacions.
	 *
	 * @param presentacio_ctrl Controlador de presentació.
	 * @param frame_principal  Frame principal sobre el que s'hauran d'afegir els diferents components.
	 */
	public RanquingVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "Rànquing" );
		panell_central = new JPanel();
		panell_botons = new JPanel();
		torna = new JButton( "Torna al menú principal" );

		// Construeixo les taules del rànquing (classificació i Hall of Fame)
		dades_classificacio = presentacio_ctrl.getClassificacioFormatejada();
		taula_classificacio = new JTable( new ModelTaula( dades_classificacio, new String[] {
				"Nom d'usuari", "Partides jugades", "Percentatge de victòries", "Puntuació global"
		} ) );

		dades_hall_of_fame = presentacio_ctrl.getHallOfFameFormatejat();
		taula_hall_of_fame = new JTable( new ModelTaula( dades_hall_of_fame, new String[] {
				"Fita aconseguida", "Jugador", "Rècord"
		} ) );

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
		propietats_panel.weightx = 0.8;
		propietats_panel.weighty = 0.2;
		panell_principal.add( panell_titol, propietats_panel );
		propietats_panel.fill = GridBagConstraints.BOTH;
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.6;
		panell_principal.add( panell_central, propietats_panel );
		propietats_panel.fill = GridBagConstraints.HORIZONTAL;
		propietats_panel.gridy = 2;
		propietats_panel.weighty = 0.2;
		panell_principal.add( panell_botons, propietats_panel );
		propietats_panel.fill = GridBagConstraints.NONE;
		propietats_panel.gridx = 2;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.10;
		propietats_panel.anchor = GridBagConstraints.SOUTHEAST;
		panell_principal.add( panell_sortida, propietats_panel );
		propietats_panel.gridx = 0;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.10;
		propietats_panel.anchor = GridBagConstraints.SOUTHWEST;
		panell_principal.add( titol_baix, propietats_panel );
	}

	@Override
	protected void inicialitzaPanellCentral()
	{
		panell_central.setLayout( new BoxLayout( panell_central, BoxLayout.PAGE_AXIS ) );

		// Taula de classificació
		taula_classificacio.setFillsViewportHeight( true );
        taula_classificacio.getTableHeader().setReorderingAllowed( false );
		// Defineixo la dimensió del panell com tot l'ample que pugui i 100 d'altura
		taula_classificacio.setPreferredScrollableViewportSize( new Dimension( 1000, 100 ) );
		panell_central.add( new JScrollPane( taula_classificacio, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER ) );
		panell_central.add( Box.createVerticalStrut( 10 ) );

		// Taula Hall of Fame
		taula_hall_of_fame.setFillsViewportHeight( true );
		taula_hall_of_fame.getTableHeader().setReorderingAllowed( false );
		// Defineixo la dimensió del panell com tot l'ample que pugui i 20 d'altura
		taula_hall_of_fame.setPreferredScrollableViewportSize( new Dimension( 1000, 20 ) );
		panell_central.add( new JScrollPane( taula_hall_of_fame, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER ) );

		panell_central.setOpaque( false );
	}

	@Override
	protected void inicialitzaPanellPeu()
	{
		panell_botons.add( torna );
		panell_botons.setOpaque( false );
	}

	@Override
	protected void assignaListeners()
	{
		super.assignaListeners();

		torna.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoTorna( event );
			}
		} );
	}

	/**
	 * Defineix el comportament del botó de tornar al menú principal quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoTorna( ActionEvent event )
	{
		presentacio_ctrl.vistaRanquingAMenuPrincipal();
	}
}
