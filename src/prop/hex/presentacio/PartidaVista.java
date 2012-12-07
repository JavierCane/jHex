package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class PartidaVista extends BaseVista
{

	private JPanelTauler panell_central;
	private JPanel panell_botons;
	private JButton abandona;

	public PartidaVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "jHex" );
		panell_central = new JPanelTauler( true );
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
		try {
			presentacio_ctrl.tancaPartida();
		}
		catch ( Exception excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", excepcio.getMessage(),
					botons, JOptionPane.WARNING_MESSAGE );
		}

		presentacio_ctrl.vistaPartidaAMenuPrincipal();
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
