package prop.hex.presentacio;

import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.presentacio.auxiliars.JPanelTauler;
import prop.hex.presentacio.auxiliars.VistaDialeg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vista de definir una situació inicial d'una partida del joc Hex.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */
public final class DefineixSituacioVista extends BaseVista
{

	// Panell del tauler
	private JPanelTauler panell_central;

	// Panells
	private JPanel panell_botons;

	// Botons
	private JButton inicia_partida;
	private JButton abandona;

	/**
	 * Constructor que crea una vista passant-li quin és el frame sobre el qual s'haurà de treballar i el
	 * controlador de presentació al qual haurà de demanar certes operacions.
	 *
	 * @param frame_principal Frame principal sobre el que s'hauran d'afegir els diferents components.
	 */
	public DefineixSituacioVista( JFrame frame_principal )
	{
		super( frame_principal );

		titol = new JLabel( "jHex" );
		panell_central = new JPanelTauler( false );
		panell_botons = new JPanel();
		inicia_partida = new JButton( "Inicia la partida" );
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
		panell_botons.setLayout( new FlowLayout( FlowLayout.CENTER, 120, 0 ) );
		panell_botons.add( titol_baix );
		panell_botons.add( inicia_partida );
		panell_botons.add( abandona );
		panell_botons.add( panell_sortida );
		panell_botons.setOpaque( false );
	}

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

		inicia_partida.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoIniciaPartida();
			}
		} );

		abandona.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoAbandona();
			}
		} );
	}

	/**
	 * Defineix el comportament del botó d'abandonar partida quan sigui pitjat.
	 */
	public void accioBotoAbandona()
	{
		if ( PresentacioCtrl.getInstancia().consultaEstatPartida() == EstatPartida.NO_FINALITZADA )
		{
			VistaDialeg dialeg = new VistaDialeg();

			String[] botons = {
					"Sí",
					"No"
			};

			String valor_seleccionat = dialeg.setDialeg( "Confirmació de sortida de la partida",
					"Estàs segur de que " + "vols sortir de la partida?", botons, JOptionPane.QUESTION_MESSAGE );

			if ( "Sí" == valor_seleccionat )
			{
				PresentacioCtrl.getInstancia().vistaDefineixSituacioAMenuPrincipal();
			}
		}
		else
		{
			PresentacioCtrl.getInstancia().vistaDefineixSituacioAMenuPrincipal();
		}
	}

	/**
	 * Defineix el comportament del botó d'iniciar partida quan sigui pitjat.
	 */
	public void accioBotoIniciaPartida()
	{
		PresentacioCtrl.getInstancia().vistaDefineixSituacioAPartida();
	}

	/**
	 * Mostra un diàleg al finalitzar la partida amb les dades de la mateixa. Realitza la finalització de la partida.
	 */
	public void mostraDialegVictoria( String missatge )
	{
		VistaDialeg dialeg = new VistaDialeg();
		String[] botons = { "Torna al menú principal" };
		dialeg.setDialeg( "Partida finalitzada", missatge, botons, JOptionPane.INFORMATION_MESSAGE );

		try
		{
			PresentacioCtrl.getInstancia().finalitzaPartida();
			PresentacioCtrl.getInstancia().vistaDefineixSituacioAMenuPrincipal();
		}
		catch ( Exception excepcio )
		{
			VistaDialeg dialeg_error = new VistaDialeg();
			String[] botons_error = { "Accepta" };
			dialeg_error.setDialeg( "Error", excepcio.getMessage(), botons_error, JOptionPane.WARNING_MESSAGE );
		}
	}
}
