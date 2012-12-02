package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class PartidaVista extends BaseVista
{

	private JPanelTauler panell_central = new JPanelTauler();
	private JPanel panell_botons = new JPanel();
	private JButton abandona = new JButton( "Abandona la partida" );

	public PartidaVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "jHex" );

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
		panell_botons.setLayout( new FlowLayout( FlowLayout.CENTER, 230, 0 ) );
		panell_botons.add( titol_baix );
		panell_botons.add( abandona );
		panell_botons.add( surt );
		panell_botons.setOpaque( false );
	}

	public void accioBotoAbandona( ActionEvent event )
	{
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
