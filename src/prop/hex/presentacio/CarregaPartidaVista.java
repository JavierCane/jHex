package prop.hex.presentacio;

import prop.hex.presentacio.auxiliars.ModelTaula;
import prop.hex.presentacio.auxiliars.VistaDialeg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vista de carregar partides del joc Hex.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */
public final class CarregaPartidaVista extends BaseVista
{

	// Panells
	private JPanel panell_central;
	private JPanel panell_botons;

	// Botons
	private JButton carrega;
	private JButton descarta;

	// Dades associades a la taula
	private String[] id_partides;

	// Taula de partides a carregar
	private JTable taula_partides;

	/**
	 * Constructor que crea una vista passant-li quin és el frame sobre el qual s'haurà de treballar i el
	 * controlador de presentació al qual haurà de demanar certes operacions.
	 *
	 * @param presentacio_ctrl Controlador de presentació.
	 * @param frame_principal  Frame principal sobre el que s'hauran d'afegir els diferents components.
	 */
	public CarregaPartidaVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "Carrega una partida" );
		panell_central = new JPanel();
		panell_botons = new JPanel();
		carrega = new JButton( "Carrega" );
		descarta = new JButton( "Descarta" );
		String[][] dades = presentacio_ctrl.obteLlistaPartides();
		Object[][] dades_taula = new String[dades.length][3];
		id_partides = new String[dades.length];
		for ( int i = 0; i < dades.length; i++ )
		{
			id_partides[i] = dades[i][0];
			System.arraycopy( dades[i], 1, dades_taula[i], 0, 3 );
		}

		taula_partides = new JTable( new ModelTaula( dades_taula, new String[] {
				"Nom de la partida",
				"Oponent",
				"Data i hora"
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
        taula_partides.getTableHeader().setReorderingAllowed( false );
		taula_partides.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
	}

	@Override
	protected void inicialitzaPanellPeu()
	{
		panell_botons.add( carrega );
		panell_botons.add( descarta );
		panell_botons.setOpaque( false );
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

	/**
	 * Defineix el comportament del botó de carregar quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoCarrega( ActionEvent event )
	{
		if ( taula_partides.getSelectedRowCount() == 0 )
		{
			VistaDialeg dialeg_error = new VistaDialeg();
			String[] botons_error = { "Accepta" };
			dialeg_error.setDialeg( "Error", "Has de seleccionar almenys una partida.", botons_error,
					JOptionPane.WARNING_MESSAGE );
		}
		else
		{
			try
			{
				String id_partida = id_partides[taula_partides.getSelectedRow()];
				String usuari = presentacio_ctrl.usuariSenseAutenticarAPartida( id_partida );
				if ( usuari == null )
				{
					presentacio_ctrl.carregaPartida( id_partida, "" );
					presentacio_ctrl.vistaCarregaPartidaAPartida();
				}
				else
				{
					presentacio_ctrl.vistaCarregaPartidaAIdentificaCarregaPartida( usuari, id_partida );
				}
			}
			catch ( Exception excepcio )
			{
				VistaDialeg dialeg_error = new VistaDialeg();
				String[] botons_error = { "Accepta" };
				dialeg_error.setDialeg( "Error", excepcio.getMessage(), botons_error, JOptionPane.ERROR_MESSAGE );
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
		presentacio_ctrl.vistaCarregaPartidaAMenuPrincipal();
	}
}
