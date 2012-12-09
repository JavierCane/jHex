package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vista de definir una situació inicial d'una partida del joc Hex.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */
public class DefineixSituacioVista extends BaseVista
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
	 * @param presentacio_ctrl Controlador de presentació.
	 * @param frame_principal  Frame principal sobre el que s'hauran d'afegir els diferents components.
	 */
	public DefineixSituacioVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "jHex" );
		panell_central = new JPanelTauler( false, presentacio_ctrl );
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
				accioBotoIniciaPartida( event );
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
	 * Defineix el comportament del botó d'abandonar partida quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoAbandona( ActionEvent event )
	{
		VistaDialeg dialeg = new VistaDialeg();

		String[] botons = {
				"Sí", "No"
		};

		String valor_seleccionat = dialeg.setDialeg( "Confirmació de sortida de la partida", "Estàs segur de que " +
				"vols sortir de la partida?", botons, JOptionPane.QUESTION_MESSAGE );

		if ( "Sí" == valor_seleccionat )
		{
			presentacio_ctrl.vistaDefineixSituacioAMenuPrincipal();
		}

	}

	/**
	 * Defineix el comportament del botó d'iniciar partida quan sigui pitjat.
	 *
	 * @param event Event que activarà el botó.
	 */
	public void accioBotoIniciaPartida( ActionEvent event )
	{
		presentacio_ctrl.vistaDefineixSituacioAPartida();
	}


}
