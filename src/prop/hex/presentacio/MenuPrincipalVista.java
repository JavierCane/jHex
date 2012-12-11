package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vista del menú principal del joc Hex.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */
public final class MenuPrincipalVista extends BaseVista
{

	// Panell
	private JPanel panell_botons;
	private JPanel panell_tanca_sessio;

	// Botons
	private JButton tanca_sessio;
	private JButton juga;
	private JButton carrega;
	private JButton configuracio;
	private JButton ranquing;

	// Etiquetes de text
	private JLabel nom_jugador_principal;

	/**
	 * Constructor que crea una vista passant-li quin és el frame sobre el qual s'haurà de treballar i el
	 * controlador de presentació al qual haurà de demanar certes operacions.
	 *
	 * @param frame_principal  Frame principal sobre el que s'hauran d'afegir els diferents components.
	 */
	public MenuPrincipalVista( JFrame frame_principal )
	{
		super( frame_principal );

		titol = new JLabel( "Menú principal" );
		panell_botons = new JPanel();
		panell_tanca_sessio = new JPanel();
		tanca_sessio = new JButton( "Tanca la sessió" );
		juga = new JButton( "Juga una partida" );
		carrega = new JButton( "Carrega una partida" );
		configuracio = new JButton( "Configuració" );
		ranquing = new JButton( "Rànquing" );

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
		propietats_panel.fill = GridBagConstraints.BOTH;
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.6;
		panell_principal.add( panell_botons, propietats_panel );
		propietats_panel.fill = GridBagConstraints.NONE;
		propietats_panel.gridy = 2;
		propietats_panel.weighty = 0.2;
		panell_principal.add( panell_tanca_sessio, propietats_panel );
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
		panell_botons.setLayout( new GridLayout( 4, 1, 20, 20 ) );
		panell_botons.add( juga );
		if ( PresentacioCtrl.getInstancia().getEsConvidat() )
		{
			carrega.setEnabled( false );
		}
		panell_botons.add( carrega );
		panell_botons.add( configuracio );
		panell_botons.add( ranquing );
		panell_botons.setOpaque( false );
	}

	@Override
	protected void inicialitzaPanellPeu()
	{
		nom_jugador_principal = new JLabel( "Has iniciat sessió com a " + PresentacioCtrl.getInstancia().obteNomJugadorPrincipal() );

		panell_tanca_sessio.add( nom_jugador_principal );
		panell_tanca_sessio.add( tanca_sessio );
		panell_tanca_sessio.setOpaque( false );
	}

	@Override
	protected void assignaListeners()
	{
		super.assignaListeners();

		juga.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoJuga( event );
			}
		} );

		carrega.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoCarrega( event );
			}
		} );

		configuracio.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoPreferencies( event );
			}
		} );

		ranquing.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoRanquing( event );
			}
		} );

		tanca_sessio.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoTancaSessio( event );
			}
		} );
	}

	/**
	 * Defineix el comportament del botó de jugar una partida quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoJuga( ActionEvent event )
	{
		PresentacioCtrl.getInstancia().vistaMenuPrincipalAConfiguraPartida();
	}

	/**
	 * Defineix el comportament del botó de carregar una partida quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoCarrega( ActionEvent event )
	{
		PresentacioCtrl.getInstancia().vistaMenuPrincipalACarregaPartida();
	}

	/**
	 * Defineix el comportament del botó de preferències quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoPreferencies( ActionEvent event )
	{
		PresentacioCtrl.getInstancia().vistaMenuPrincipalAPreferencies();
	}

	/**
	 * Defineix el comportament del botó de rànquing quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoRanquing( ActionEvent event )
	{
		PresentacioCtrl.getInstancia().vistaMenuPrincipalARanquing();
	}

	/**
	 * Defineix el comportament del botó de tancar sessió quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoTancaSessio( ActionEvent event )
	{
		PresentacioCtrl.getInstancia().vistaMenuPrincipalAIniciaSessio();
	}
}
