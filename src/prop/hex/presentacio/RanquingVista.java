package prop.hex.presentacio;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class RanquingVista extends JHexVista
{

	private JPanel panel_central = new JPanel();
	private JPanel panel_botons = new JPanel();
	private JButton torna = new JButton( "Torna al menú principal" );
	private Object[][] dades_taula = new Object[][] { { "Test", 15, 10, 25 } };
	private Object[][] dades_hall_of_fame = new Object[][] {
			{ "Test", "Test", "Test", "Test" }, {
			"27 s", "10", "10", "10"
	}
	};
	private JTable taula_ranquing = new JTable( dades_taula, new String[] {
			"Nom d'usuari", "Partides jugades", "Partides guanyades", "Puntuació global"
	} );
	private JTable taula_hall_of_fame = new JTable( dades_hall_of_fame, new String[] {
			"Victòria amb temps mínim", "Victòria amb més fitxes", "Més partides jugades", "Més victòries"
	} );

	public RanquingVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );
		titol = new JLabel( "Rànquing" );
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
		panel_central.setLayout( new BoxLayout( panel_central, BoxLayout.PAGE_AXIS ) );
		panel_central.add( new JScrollPane( taula_ranquing ) );
		panel_central.add( Box.createVerticalStrut( 10 ) );
		panel_central.add( taula_hall_of_fame.getTableHeader() );
		panel_central.add( taula_hall_of_fame );
		panel_central.setOpaque( false );
		taula_ranquing.setFillsViewportHeight( true );
		taula_ranquing.setEnabled( false );
		taula_hall_of_fame.setFillsViewportHeight( true );
		taula_hall_of_fame.setEnabled( false );
	}

	private void inicialitzaPanelBotons()
	{
		panel_botons.add( torna );
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
		propietats_panel.weightx = 0.8;
		propietats_panel.weighty = 0.2;
		panel_principal.add( panel_titol, propietats_panel );
		propietats_panel.fill = GridBagConstraints.BOTH;
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.6;
		panel_principal.add( panel_central, propietats_panel );
		propietats_panel.fill = GridBagConstraints.HORIZONTAL;
		propietats_panel.gridy = 2;
		propietats_panel.weighty = 0.2;
		panel_principal.add( panel_botons, propietats_panel );
		propietats_panel.fill = GridBagConstraints.NONE;
		propietats_panel.gridx = 2;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.10;
		propietats_panel.anchor = GridBagConstraints.SOUTHEAST;
		panel_principal.add( panel_sortida, propietats_panel );
		propietats_panel.gridx = 0;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.10;
		propietats_panel.anchor = GridBagConstraints.SOUTHWEST;
		panel_principal.add( titol_baix, propietats_panel );
	}

	public void accioBotoTorna( ActionEvent event )
	{
		presentacio_ctrl.vistaRanquingAMenuPrincipal();
	}

	protected void assignaListeners()
	{
		torna.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoTorna( event );
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
