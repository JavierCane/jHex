package prop.hex.presentacio;

import prop.hex.presentacio.auxiliars.ModelTaula;
import prop.hex.presentacio.auxiliars.ModelTaulaClassificacio;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vista del rànquing.
 * Inclou la visualització de la calssificació i la del del Hall of Fame
 *
 * @author Javier Ferrer Gonzalez (Grup 7.3, Hex)
 */
public final class RanquingVista extends BaseVista
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

	// Botons

	/**
	 * Botó de torna al menú principal.
	 */
	private JButton torna;

	// Dades de les taules

	/**
	 * Dades de la taula de la classificació.
	 */
	private String[][] dades_classificacio;

	/**
	 * Dades del Hall of Fame.
	 */
	private String[][] dades_hall_of_fame;

	// Taules

	/**
	 * Taula que conté la classificació.
	 */
	private JTable taula_classificacio;

	/**
	 * Taula que conté el Hall of Fame.
	 */
	private JTable taula_hall_of_fame;

	/**
	 * Model de la taula de classificació, que afegeix característiques específiques d'aquesta taula.
	 */
	private ModelTaulaClassificacio model_taula_classificacio;

	/**
	 * Constructor que crea una vista passant-li quin és el frame sobre el qual s'haurà de treballar.
	 *
	 * @param frame_principal Frame principal sobre el que s'hauran d'afegir els diferents components.
	 */
	public RanquingVista( JFrame frame_principal )
	{
		super( frame_principal );

		titol = new JLabel( "Rànquing" );
		panell_central = new JPanel();
		panell_botons = new JPanel();
		torna = new JButton( "Torna al menú principal" );

		// Construeixo les taules del rànquing (classificació)
		dades_classificacio = PresentacioCtrl.getInstancia().getClassificacioFormatejada();
		model_taula_classificacio = new ModelTaulaClassificacio( dades_classificacio, new String[] {
				"Nom d'usuari",
				"Partides jugades",
				"Percentatge de victòries",
				"Puntuació global"
		} );
		taula_classificacio = new JTable( model_taula_classificacio );

		// Afegeixo atributs per poder ordenar la taula
		TableRowSorter<ModelTaulaClassificacio> ordenacio =
				new TableRowSorter<ModelTaulaClassificacio>( model_taula_classificacio );
		taula_classificacio.setRowSorter( ordenacio );

		// Construeixo les taules del rànquing (hall of fame)
		dades_hall_of_fame = PresentacioCtrl.getInstancia().getHallOfFameFormatejat();
		taula_hall_of_fame = new JTable( new ModelTaula( dades_hall_of_fame, new String[] {
				"Fita aconseguida",
				"Jugador",
				"Rècord"
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
		panell_principal.add( Box.createHorizontalStrut( 65 ), propietats_panel );
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

		// Separació de taules
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
				accioBotoTorna();
			}
		} );
	}

	/**
	 * Defineix el comportament del botó de tornar al menú principal quan sigui pitjat.
	 */
	public void accioBotoTorna()
	{
		PresentacioCtrl.getInstancia().vistaRanquingAMenuPrincipal();
	}
}
