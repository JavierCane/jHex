package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class RanquingVista extends BaseVista
{

	private JPanel panell_central;
	private JPanel panell_botons;
	private JButton torna;
	private String[][] dades_classificacio;
	private String[][] dades_hall_of_fame;
	private JTable taula_classificacio;
	private JTable taula_hall_of_fame;

	public RanquingVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "Rànquing" );
		panell_central = new JPanel();
		panell_botons = new JPanel();
		torna = new JButton( "Torna al menú principal" );

		// Construeixo les taules del rànquing (classificació i Hall of Fame)
		dades_classificacio = presentacio_ctrl.getClassificacioFormatejada();
		taula_classificacio = new JTable( dades_classificacio, new String[] {
				"Nom d'usuari", "Partides jugades", "Percentatge de victòries", "Puntuació global"
		} );

		dades_hall_of_fame = presentacio_ctrl.getHallOfFameFormatejat();
		taula_hall_of_fame = new JTable( dades_hall_of_fame, new String[] {
				"Fita aconseguida", "Jugador", "Rècord"
		} );

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
		taula_classificacio.setEnabled( false );
		// Defineixo la dimensió del panell com tot l'ample que pugui i 100 d'altura
		taula_classificacio.setPreferredScrollableViewportSize( new Dimension( 1000, 100 ) );
		panell_central.add( new JScrollPane( taula_classificacio, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER ) );
		panell_central.add( Box.createVerticalStrut( 10 ) );

		// Taula Hall of Fame
		taula_hall_of_fame.setFillsViewportHeight( true );
		taula_hall_of_fame.setEnabled( false );
		// Defineixo la dimensió del panell com tot l'ample que pugui i 7 d'altura
		taula_hall_of_fame.setPreferredScrollableViewportSize( new Dimension( 1000, 7 ) );
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

	public void accioBotoTorna( ActionEvent event )
	{
		presentacio_ctrl.vistaRanquingAMenuPrincipal();
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
}
