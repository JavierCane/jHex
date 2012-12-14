package prop.hex.presentacio;

import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.enums.ModesInici;
import prop.hex.domini.models.enums.TipusJugadors;
import prop.hex.presentacio.auxiliars.JPanelTauler;
import prop.hex.presentacio.auxiliars.VistaDialeg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Vista d'una partida del joc Hex.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */
public final class PartidaVista extends BaseVista
{

	// Panell del tauler
	private JPanelTauler panell_central;

	// Panells
	private JPanel panell_botons;

	// Botons
	private JButton intercanvia;
	private JButton mou_ia;
	private JButton demana_pista;
	private JButton abandona;

	/**
	 * Constructor que crea una vista passant-li quin és el frame sobre el qual s'haurà de treballar i el
	 * controlador de presentació al qual haurà de demanar certes operacions.
	 *
	 * @param frame_principal Frame principal sobre el que s'hauran d'afegir els diferents components.
	 */
	public PartidaVista( JFrame frame_principal )
	{
		super( frame_principal );

		titol = new JLabel( "jHex" );
		panell_central = new JPanelTauler( true );
		panell_botons = new JPanel();
		intercanvia = new JButton( "Intercanvia fitxa" );
		mou_ia = new JButton( "Mou IA" );
		demana_pista = new JButton( "Demana pista" );
		abandona = new JButton( "Abandona la partida" );

		inicialitzaVista();
	}

	@Override
	protected void inicialitzaPanellPrincipal()
	{
		panell_principal.add( panell_central );
		panell_principal.add( panell_botons );
	}

	@Override
	protected void inicialitzaPanellCentral()
	{
		panell_central.setOpaque( false );
	}

	@Override
	protected void inicialitzaPanellPeu()
	{
		Object[][] elements_de_control_jugadors = PresentacioCtrl.getInstancia().getElementsDeControlJugadors();
		boolean es_partida_ia = ( ( TipusJugadors ) elements_de_control_jugadors[0][0] ) != TipusJugadors.JUGADOR &&
		                        ( ( TipusJugadors ) elements_de_control_jugadors[0][0] ) != TipusJugadors.CONVIDAT &&
		                        ( ( TipusJugadors ) elements_de_control_jugadors[0][1] ) != TipusJugadors.JUGADOR &&
		                        ( ( TipusJugadors ) elements_de_control_jugadors[0][1] ) != TipusJugadors.CONVIDAT;
		JPanel botons = new JPanel();
		if ( PresentacioCtrl.getInstancia().getElementsDeControlPartida()[4] == ModesInici.PASTIS &&
		     ( elements_de_control_jugadors[0][1] == TipusJugadors.CONVIDAT ||
		       elements_de_control_jugadors[0][1] == TipusJugadors.JUGADOR ) )
		{
			botons.add( intercanvia );
			intercanvia.setEnabled( false );
		}
		int num_jugador = ( Integer ) PresentacioCtrl.getInstancia().getElementsDeControlPartida()[2] % 2;
		if ( elements_de_control_jugadors[0][num_jugador] != TipusJugadors.CONVIDAT &&
		     elements_de_control_jugadors[0][num_jugador] != TipusJugadors.JUGADOR )
		{
			botons.add( mou_ia );
		}
		if ( !es_partida_ia )
		{
			botons.add( demana_pista );
			demana_pista.setEnabled( false );
		}
		botons.add( abandona );
		botons.setOpaque( false );
		panell_botons.setLayout( new FlowLayout( FlowLayout.CENTER, 100, 0 ) );
		panell_botons.add( titol_baix );
		panell_botons.add( botons );
		panell_botons.add( panell_sortida );
		panell_botons.setOpaque( false );
	}

	@Override
	protected void inicialitzaPanellSortida()
	{
		panell_sortida.setLayout( new FlowLayout( FlowLayout.CENTER, 10, 0 ) );
		panell_sortida.add( ajuda );
		panell_sortida.add( surt );
		panell_sortida.setOpaque( false );
	}

	@Override
	protected void assignaListeners()
	{
		super.assignaListeners();

		intercanvia.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoIntercanviaFitxa();
			}
		} );

		mou_ia.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoMouIA();
			}
		} );

		demana_pista.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoDemanaPista();
			}
		} );

		abandona.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoAbandona( false );
			}
		} );
	}

	/**
	 * Modifica l'estat del botó d'intercanvia fitxa.
	 *
	 * @param estat Cert, si es vol activar el botó. Fals, altrament.
	 */
	public void estatBotoIntercanviaFitxa( boolean estat )
	{
		intercanvia.setEnabled( estat );
	}

	/**
	 * Modifica l'estat del botó de demana pista.
	 *
	 * @param estat Cert, si es vol activar el botó. Fals, altrament.
	 */
	public void estatBotoDemanaPista( boolean estat )
	{
		demana_pista.setEnabled( estat );
	}

	/**
	 * Defineix el comportament del botó d'intercanvia fitxa quan sigui pitjat.
	 */
	public void accioBotoIntercanviaFitxa()
	{
		intercanvia.setEnabled( false );
		panell_central.intercanviaFitxa();
	}

	/**
	 * Defineix el comportament del botó de mou IA quan sigui pitjat.
	 */
	public void accioBotoMouIA()
	{
		mou_ia.setEnabled( false );
		panell_central.mouIAOMostraPista();
		if ( PresentacioCtrl.getInstancia().consultaEstatPartida() == EstatPartida.NO_FINALITZADA )
		{
			int num_jugador = ( Integer ) PresentacioCtrl.getInstancia().getElementsDeControlPartida()[2] % 2;
			if ( PresentacioCtrl.getInstancia().getElementsDeControlJugadors()[0][num_jugador] !=
			     TipusJugadors.CONVIDAT &&
			     PresentacioCtrl.getInstancia().getElementsDeControlJugadors()[0][num_jugador] !=
			     TipusJugadors.JUGADOR )
			{
				mou_ia.setEnabled( true );
			}
		}
	}

	/**
	 * Defineix el comportament del botó de demanar pista quan sigui pitjat.
	 */
	public void accioBotoDemanaPista()
	{
		demana_pista.setEnabled( false );
		panell_central.mouIAOMostraPista();
	}

	/**
	 * Defineix el comportament del botó d'abandonar partida quan sigui pitjat.
	 * Pot ser invocat també desde el botó de sortida del joc.
	 *
	 * @param sortir_del_programa Si es true sortirem del joc completament tancant el programa, si no,
	 *                            anirém a la pantalla principal
	 */
	public void accioBotoAbandona( boolean sortir_del_programa )
	{
		// Primer preguntem si vol guardar la partida a disc abans de sortir
		Object[][] elements_de_control_jugadors = PresentacioCtrl.getInstancia().getElementsDeControlJugadors();

		boolean es_partida_ia = ( ( TipusJugadors ) elements_de_control_jugadors[0][0] ) != TipusJugadors.JUGADOR &&
		                        ( ( TipusJugadors ) elements_de_control_jugadors[0][0] ) != TipusJugadors.CONVIDAT &&
		                        ( ( TipusJugadors ) elements_de_control_jugadors[0][1] ) != TipusJugadors.JUGADOR &&
		                        ( ( TipusJugadors ) elements_de_control_jugadors[0][1] ) != TipusJugadors.CONVIDAT;

		boolean es_partida_convidat =
				( ( TipusJugadors ) elements_de_control_jugadors[0][0] ) == TipusJugadors.CONVIDAT ||
				( ( TipusJugadors ) elements_de_control_jugadors[0][1] ) == TipusJugadors.CONVIDAT;

		if ( es_partida_convidat || es_partida_ia )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = {
					"Sí",
					"No"
			};

			String valor_seleccionat = dialeg.setDialeg( "Confirmació de sortida de la partida",
					"Estàs segur de que vols sortir de la partida?", botons, JOptionPane.QUESTION_MESSAGE );

			if ( "Sí" == valor_seleccionat )
			{
				if ( sortir_del_programa )
				{
					System.exit( 0 );
				}
				else
				{
					PresentacioCtrl.getInstancia().vistaPartidaAMenuPrincipal();
				}
			}
		}
		else
		{
			VistaDialeg dialeg_guardar_partida = new VistaDialeg();
			String[] botons_guardar_partida = {
					"Sí",
					"No",
					"Cancel·la"
			};

			String valor_seleccionat = dialeg_guardar_partida.setDialeg( "Guardar abans de sortir?",
					"Vols guardar la partida abans de sortir per poder-la carregar després?", botons_guardar_partida,
					JOptionPane.QUESTION_MESSAGE );

			// Si vol guardar la partida, la guardem a disc i sortim al menú principal
			if ( "Sí" == valor_seleccionat )
			{
				try
				{
					PresentacioCtrl.getInstancia().guardaPartida();

					PresentacioCtrl.getInstancia().netejaParametresPartidaActual();

					if ( sortir_del_programa )
					{
						System.exit( 0 );
					}
					else
					{
						PresentacioCtrl.getInstancia().vistaPartidaAMenuPrincipal();
					}
				}
				catch ( IOException excepcio )
				{
					VistaDialeg dialeg_error_guardant = new VistaDialeg();
					String[] botons_error_guardant = { "Accepta" };
					dialeg_error_guardant.setDialeg( "Error", excepcio.getMessage(), botons_error_guardant,
							JOptionPane.WARNING_MESSAGE );
				}
				catch ( UnsupportedOperationException excepcio )
				{
					VistaDialeg dialeg_error_guardant = new VistaDialeg();
					String[] botons_error_guardant = { "Accepta" };
					dialeg_error_guardant.setDialeg( "Error", excepcio.getMessage(), botons_error_guardant,
							JOptionPane.WARNING_MESSAGE );
				}
			}
			else if ( "No" == valor_seleccionat )
			{
				// Si no vol guardar la partida, retornem a menu principal reinicialitzant els paràmetres de la
				// partida en joc a valors buits
				PresentacioCtrl.getInstancia().netejaParametresPartidaActual();

				if ( sortir_del_programa )
				{
					System.exit( 0 );
				}
				else
				{
					PresentacioCtrl.getInstancia().vistaPartidaAMenuPrincipal();
				}
			}
			// Si selecciona l'opció de cancel·lar la sortida, simplement no fem res :)
		}
	}

	/**
	 * Mostra un diàleg al finalitzar la partida amb les dades de la mateixa. Realitza la finalització de la partida.
	 *
	 * @param missatge Missatge que es mostra en el diàleg.
	 */
	public void mostraDialegVictoria( String missatge )
	{
		try
		{
			PresentacioCtrl.getInstancia().finalitzaPartida();
		}
		catch ( Exception excepcio )
		{
			VistaDialeg dialeg_error = new VistaDialeg();
			String[] botons_error = { "Accepta" };
			dialeg_error.setDialeg( "Error", excepcio.getMessage(), botons_error, JOptionPane.WARNING_MESSAGE );
		}

		VistaDialeg dialeg = new VistaDialeg();
		String[] botons = {
				"Torna al menú principal",
				"Partida de revenja"
		};
		String opcio = dialeg.setDialeg( "Partida finalitzada", missatge, botons, JOptionPane.INFORMATION_MESSAGE );

		try
		{
			PresentacioCtrl.getInstancia().tancaPartida( opcio.equals( "Partida de revenja" ) );
		}
		catch ( Exception excepcio )
		{
			VistaDialeg dialeg_error = new VistaDialeg();
			String[] botons_error = { "Accepta" };
			dialeg_error.setDialeg( "Error", excepcio.getMessage(), botons_error, JOptionPane.ERROR_MESSAGE );
		}

		if ( opcio.equals( "Partida de revenja" ) )
		{
			PresentacioCtrl.getInstancia().regeneraPartidaVista();
		}
		else
		{
			PresentacioCtrl.getInstancia().vistaPartidaAMenuPrincipal();
		}
	}

	/**
	 * Defineix el comportament del botó de sortida quan sigui pitjat.
	 */
	@Override
	public void accioBotoSurt()
	{
		accioBotoAbandona( true );
	}
}
