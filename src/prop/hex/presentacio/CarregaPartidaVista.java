package prop.hex.presentacio;

import prop.hex.presentacio.auxiliars.ModelTaulaPartides;
import prop.hex.presentacio.auxiliars.VistaDialeg;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
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
     * Botó de carrega partida.
     */
	private JButton carrega;
	
	/**
     * Botó de torna al menú principal.
     */
	private JButton torna;
	
	/**
     * Botó d'elimina partida.
     */
	private JButton elimina;

	// Dades associades a la taula
	
	/**
     * Dades de la taula de les partides per carregar.
     */
	private String[] id_partides;

	// Taula de partides a carregar
	
	/**
     * Taula que conté les partides per carregar.
     */
	private JTable taula_partides;
	
	/**
     * Model de la taula de partides per carregar, que afegeix característiques específiques d'aquesta taula.
     */
	private ModelTaulaPartides model_taula_partides;

	/**
	 * Constructor que crea una vista passant-li quin és el frame sobre el qual s'haurà de treballar.
	 *
	 * @param frame_principal Frame principal sobre el que s'hauran d'afegir els diferents components.
	 */
	public CarregaPartidaVista( JFrame frame_principal )
	{
		super( frame_principal );

		titol = new JLabel( "Carrega una partida" );
		panell_central = new JPanel();
		panell_botons = new JPanel();
		carrega = new JButton( "Carrega" );
		torna = new JButton( "Torna al menú principal" );
		elimina = new JButton( "Elimina" );
		String[][] dades = PresentacioCtrl.getInstancia().obteLlistaPartides();
		Object[][] dades_taula = new String[dades.length][3];
		id_partides = new String[dades.length];
		for ( int i = 0; i < dades.length; i++ )
		{
			id_partides[i] = dades[i][0];
			System.arraycopy( dades[i], 1, dades_taula[i], 0, 3 );
		}

		model_taula_partides = new ModelTaulaPartides( dades_taula, new String[] {
				"Nom de la partida",
				"Oponent",
				"Data i hora"
		} );
		taula_partides = new JTable( model_taula_partides );

		// Afegeixo atributs per poder ordenar la taula
		TableRowSorter<ModelTaulaPartides> ordenacio = new TableRowSorter<ModelTaulaPartides>( model_taula_partides );
		taula_partides.setRowSorter( ordenacio );

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
		panell_botons.add( torna );
		panell_botons.add( elimina );
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
				accioBotoCarrega();
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

		elimina.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoElimina();
			}
		} );
	}

	/**
	 * Defineix el comportament del botó de carregar quan sigui pitjat.
	 */
	public void accioBotoCarrega()
	{
		if ( taula_partides.getSelectedRowCount() == 0 )
		{
			VistaDialeg dialeg_error = new VistaDialeg();
			String[] botons_error = { "Accepta" };
			dialeg_error.setDialeg( "Error", "Has de seleccionar almenys una partida a carregar.", botons_error,
					JOptionPane.WARNING_MESSAGE );
		}
		else
		{
			try
			{
				String id_partida = id_partides[taula_partides.getSelectedRow()];
				String usuari = PresentacioCtrl.getInstancia().usuariSenseAutenticarAPartida( id_partida );
				if ( usuari == null )
				{
					PresentacioCtrl.getInstancia().carregaPartida( id_partida, "" );
					PresentacioCtrl.getInstancia().vistaCarregaPartidaAPartida();
				}
				else
				{
					PresentacioCtrl.getInstancia().vistaCarregaPartidaAIdentificaCarregaPartida( usuari, id_partida );
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
	 * Defineix el comportament del botó de tornar quan sigui pitjat.
	 */
	public void accioBotoTorna()
	{
		PresentacioCtrl.getInstancia().vistaCarregaPartidaAMenuPrincipal();
	}

	/**
	 * Defineix el comportament del botó de eliminar quan sigui pitjat.
	 */
	public void accioBotoElimina()
	{
		if ( taula_partides.getSelectedRowCount() == 0 )
		{
			VistaDialeg dialeg_error = new VistaDialeg();
			String[] botons_error = { "Accepta" };
			dialeg_error.setDialeg( "Error", "Has de seleccionar almenys una partida a eliminar.", botons_error,
					JOptionPane.WARNING_MESSAGE );
		}
		else
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = {
					"Sí",
					"No"
			};
			String valor_seleccionat = dialeg.setDialeg( "Confirma eliminació partida",
					"Estàs segur que vols eliminar aquesta partida? Aquesta acció no es podrà desfer.", botons,
					JOptionPane.QUESTION_MESSAGE );

			if ( valor_seleccionat == "Sí" )
			{
				int partida_seleccionada = taula_partides.getSelectedRow();
				PresentacioCtrl.getInstancia().eliminaPartida( id_partides[partida_seleccionada] );
				model_taula_partides.removeRow( partida_seleccionada );
			}
		}
	}
}
