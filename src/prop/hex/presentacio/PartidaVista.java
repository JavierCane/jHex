package prop.hex.presentacio;

import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.enums.*;
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
	 * @param presentacio_ctrl Controlador de presentació.
	 * @param frame_principal  Frame principal sobre el que s'hauran d'afegir els diferents components.
	 */
	public PartidaVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "jHex" );
		panell_central = new JPanelTauler( true, presentacio_ctrl );
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
		panell_central.afegeixBotons( mou_ia, demana_pista, abandona, intercanvia, null );
	}

	@Override
	protected void inicialitzaPanellPeu()
	{
		JPanel botons = new JPanel();
		Object[] elements_de_control_partida = presentacio_ctrl.getElementsDeControlPartida();
		if ( elements_de_control_partida[4] == ModesInici.PASTIS )
		{
			botons.add( intercanvia );
		}
		if ( elements_de_control_partida[4] == ModesInici.PASTIS )
		{
			botons.setLayout( new GridLayout( 2, 2, 10, 10 ) );
			botons.add( intercanvia );
		}
		else
		{
			botons.setLayout( new FlowLayout( FlowLayout.CENTER, 10, 0 ) );
		}
		botons.add( mou_ia );
		botons.add( demana_pista );
		botons.add( abandona );
		botons.setOpaque( false );
		panell_botons.setLayout( new FlowLayout( FlowLayout.CENTER, 100, 0 ) );
		panell_botons.add( titol_baix );
		panell_botons.add( botons );
		panell_botons.add( panell_sortida );
		panell_botons.setOpaque( false );
		mou_ia.setEnabled( false );
		demana_pista.setEnabled( false );
	}

	@Override
	protected void inicialitzaPanellSortida()
	{
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
				accioBotoIntercanviaFitxa( event );
			}
		} );

		mou_ia.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoMouIA( event );
			}
		} );

		demana_pista.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoDemanaPista( event );
			}
		} );

		abandona.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoAbandona( event );
			}
		} );
	}

	/**
	 * Defineix el comportament del botó d'intercanvia fitxa quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoIntercanviaFitxa( ActionEvent event )
	{
		intercanvia.setEnabled( false );
		panell_central.intercanviaFitxa();
	}

	/**
	 * Defineix el comportament del botó de mou IA quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoMouIA( ActionEvent event )
	{
		mou_ia.setEnabled( false );
		panell_central.mouIAOMostraPista();
	}

	/**
	 * Defineix el comportament del botó de demanar pista quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoDemanaPista( ActionEvent event )
	{
		demana_pista.setEnabled( false );
		panell_central.mouIAOMostraPista();
	}

	/**
	 * Defineix el comportament del botó d'abandonar partida quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoAbandona( ActionEvent event )
	{
		// Primer preguntem si vol guardar la partida a disc abans de sortir
		if ( presentacio_ctrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA )
		{
			Object[][] elements_de_control_jugadors = presentacio_ctrl.getElementsDeControlJugadors();
			boolean es_partida_ia = ( ( TipusJugadors ) elements_de_control_jugadors[0][0] ) != TipusJugadors
					.JUGADOR &&
					( ( TipusJugadors ) elements_de_control_jugadors[0][0] ) != TipusJugadors.CONVIDAT &&
					( ( TipusJugadors ) elements_de_control_jugadors[0][1] ) != TipusJugadors.JUGADOR &&
					( ( TipusJugadors ) elements_de_control_jugadors[0][1] ) != TipusJugadors.CONVIDAT;
			boolean es_partida_convidat = ( ( TipusJugadors ) elements_de_control_jugadors[0][0] ) == TipusJugadors
					.CONVIDAT || ( ( TipusJugadors ) elements_de_control_jugadors[0][1] ) == TipusJugadors.CONVIDAT;

			if ( es_partida_convidat || es_partida_ia )
			{
				VistaDialeg dialeg = new VistaDialeg();
				String[] botons = { "Sí", "No" };

				String valor_seleccionat = dialeg.setDialeg( "Confirmació de sortida de la partida",
						"Estàs segur de que vols sortir de la partida?", botons, JOptionPane.QUESTION_MESSAGE );

				if ( "Sí" == valor_seleccionat )
				{
					presentacio_ctrl.vistaPartidaAMenuPrincipal();
				}
			}
			else
			{
				VistaDialeg dialeg_guardar_partida = new VistaDialeg();
				String[] botons_guardar_partida = { "Sí", "No", "Cancel·la" };

				String valor_seleccionat = dialeg_guardar_partida.setDialeg( "Guardar abans de sortir?",
						"Vols guardar la partida abans de sortir per poder-la carregar després?",
						botons_guardar_partida, JOptionPane.QUESTION_MESSAGE );

				// Si vol guardar la partida, la guardem a disc i sortim al menú principal
				if ( "Sí" == valor_seleccionat )
				{
					try
					{
						presentacio_ctrl.guardaPartida();
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

					presentacio_ctrl.netejaParametresPartidaActual();
					presentacio_ctrl.vistaPartidaAMenuPrincipal();

				}
				else if ( "No" == valor_seleccionat )
				{
					// Si no vol guardar la partida, retornem a menu principal reinicialitzant els paràmetres de la
					// partida
					// en joc a valors buits
					presentacio_ctrl.netejaParametresPartidaActual();
					presentacio_ctrl.vistaPartidaAMenuPrincipal();
				}
				// Si selecciona l'opció de cancel·lar la sortida, simplement no fem res :)
			}
		}
		else
		{
			presentacio_ctrl.vistaPartidaAMenuPrincipal();
		}
	}
}
