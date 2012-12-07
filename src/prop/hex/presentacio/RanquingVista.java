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
	private Object[][] dades_taula;
	private Object[][] dades_hall_of_fame;
	private JTable taula_ranquing;
	private JTable taula_hall_of_fame;

	public RanquingVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "Rànquing" );
		panell_central = new JPanel();
		panell_botons = new JPanel();
		torna = new JButton( "Torna al menú principal" );
		dades_taula = presentacio_ctrl.getClassificacioFormatejada();
		dades_hall_of_fame = new Object[][] {
				{
						"Test", "Test", "Test", "Test"
				}, {
				"27 s", "10", "10", "10"
		}
		};
		taula_ranquing = new JTable( dades_taula, new String[] {
				"Nom d'usuari", "Partides jugades", "Partides guanyades", "Puntuació global"
		} );
		taula_hall_of_fame = new JTable( dades_hall_of_fame, new String[] {
				"Victòria amb temps mínim", "Victòria amb més fitxes", "Més partides jugades", "Més victòries"
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
		panell_central.add( new JScrollPane( taula_ranquing ) );
		panell_central.add( Box.createVerticalStrut( 10 ) );
		panell_central.add( taula_hall_of_fame.getTableHeader() );
		panell_central.add( taula_hall_of_fame );
		panell_central.setOpaque( false );
		taula_ranquing.setFillsViewportHeight( true );
		taula_ranquing.setEnabled( false );
		taula_hall_of_fame.setFillsViewportHeight( true );
		taula_hall_of_fame.setEnabled( false );
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
