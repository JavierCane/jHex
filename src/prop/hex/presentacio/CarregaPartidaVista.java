package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class CarregaPartidaVista extends BaseVista
{

	private JPanel panell_central;
	private JPanel panell_botons;
	private JButton carrega;
	private JButton descarta;
	private Object[][] dades_taula;
	private String[] id_partides;
	private JTable taula_partides;

	public CarregaPartidaVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "Carrega una partida" );
		panell_central = new JPanel();
		panell_botons = new JPanel();
		carrega = new JButton( "Carrega" );
		descarta = new JButton( "Descarta" );
		String[][] dades = presentacio_ctrl.obteLlistaPartides();
		dades_taula = new String[dades.length][3];
		id_partides = new String[dades.length];
		for ( int i = 0; i < dades.length; i++ )
		{
			id_partides[i] = dades[i][0];
			System.arraycopy( dades[i], 1, dades_taula[i], 0, 3 );
		}
		taula_partides = new JTable( dades_taula, new String[] {
				"Nom de la partida",
				"Oponent",
				"Data i hora"
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
		panell_central.add( new JScrollPane( taula_partides ) );
		panell_central.setOpaque( false );
		taula_partides.setFillsViewportHeight( true );
		taula_partides.setEnabled( false );
	}

	@Override
	protected void inicialitzaPanellPeu()
	{
		panell_botons.add( carrega );
		panell_botons.add( descarta );
		panell_botons.setOpaque( false );
	}

	public void accioBotoCarrega( ActionEvent event )
	{
	}

	public void accioBotoDescarta( ActionEvent event )
	{
		presentacio_ctrl.vistaCarregaPartidaAMenuPrincipal();
	}

	@Override
	protected void assignaListeners()
	{
		super.assignaListeners();

		carrega.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoCarrega( event );
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
	}
}
