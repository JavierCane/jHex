package prop.hex.presentacio;

import prop.cluster.domini.models.estats.EstatPartida;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public final class PartidaVista extends BaseVista
{

	private JPanelTauler panell_central;
	private JPanel panell_botons;
	private JButton abandona;

	public PartidaVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "jHex" );
		panell_central = new JPanelTauler( true, presentacio_ctrl );
		panell_botons = new JPanel();
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
		panell_botons.setLayout( new FlowLayout( FlowLayout.CENTER, 240, 0 ) );
		panell_botons.add( titol_baix );
		panell_botons.add( abandona );
		panell_botons.add( panell_sortida );
		panell_botons.setOpaque( false );
	}

	protected void inicialitzaPanellSortida()
	{
		panell_sortida.add( surt );
		panell_sortida.setOpaque( false );
	}

	public void accioBotoAbandona( ActionEvent event )
	{
		// Primer preguntem si vol guardar la partida a disc abans de sortir
		if (presentacio_ctrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA)
		{
			VistaDialeg dialeg_guardar_partida = new VistaDialeg();
			String[] botons_guardar_partida = { "Si", "No", "Cancel·la" };

			String valor_seleccionat = dialeg_guardar_partida.setDialeg( "Guardar abans de sortir?",
					"Vols guardar la partida abans de sortir per poder-la carregar després?",
					botons_guardar_partida, JOptionPane.QUESTION_MESSAGE );

			// Si vol guardar la partida, la guardem a disc i sortim al menú principal
			if ( "Si" == valor_seleccionat )
			{
				try
				{
					presentacio_ctrl.guardaPartida();
				}
				catch ( IOException excepcio )
				{
					VistaDialeg dialeg_error_guardant = new VistaDialeg();
					String[] botons_error_guardant = { "Accepta" };
					dialeg_error_guardant.setDialeg( "Error", excepcio.getMessage(),
							botons_error_guardant, JOptionPane.WARNING_MESSAGE );
				}
				catch ( UnsupportedOperationException excepcio )
				{
					VistaDialeg dialeg_error_guardant = new VistaDialeg();
					String[] botons_error_guardant = { "Accepta" };
					dialeg_error_guardant.setDialeg( "Error", excepcio.getMessage(),
							botons_error_guardant, JOptionPane.WARNING_MESSAGE );
				}

				presentacio_ctrl.netejaParametresPartidaActual();
				presentacio_ctrl.vistaPartidaAMenuPrincipal();

			}
			else if ( "No" == valor_seleccionat )
			{
				// Si no vol guardar la partida, retornem a menu principal reinicialitzant els paràmetres de la partida
				// en joc a valors buits
				presentacio_ctrl.netejaParametresPartidaActual();
				presentacio_ctrl.vistaPartidaAMenuPrincipal();
			}
			// Si selecciona l'opció de cancel·lar la sortida, simplement no fem res :)
		}
		else
		{
			presentacio_ctrl.vistaPartidaAMenuPrincipal();
		}
	}

	@Override
	protected void assignaListeners()
	{
		super.assignaListeners();

		abandona.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoAbandona( event );
			}
		} );
	}
}
