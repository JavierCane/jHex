package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PartidaVista extends BaseVista
{

	private JPanelTauler panel_central = new JPanelTauler();
	private JPanel panel_botons = new JPanel();
	private JButton abandona = new JButton( "Abandona la partida" );

	public PartidaVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );
		inicialitzaVista();
	}

	protected void inicialitzaVista()
	{
		inicialitzaPanelPrincipal();
		inicialitzaPanelCentral();
		inicialitzaPanelBotons();
		assignaListeners();
	}

	private void inicialitzaPanelCentral()
	{
		panel_central.setOpaque( false );
	}

	private void inicialitzaPanelBotons()
	{
		panel_botons.setLayout( new FlowLayout( FlowLayout.CENTER, 230, 0 ) );
		panel_botons.add( titol_baix );
		panel_botons.add( abandona );
		panel_botons.add( surt );
		panel_botons.setOpaque( false );
	}

	protected void inicialitzaPanelPrincipal()
	{
		panel_principal.add( panel_central );
		panel_principal.add( panel_botons );
	}

	public void accioBotoAbandona( ActionEvent event )
	{
		presentacio_ctrl.vistaPartidaAMenuPrincipal();
	}

	protected void assignaListeners()
	{

		abandona.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoAbandona( event );
			}
		} );

		surt.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoSurt( event );
			}
		} );
	}

}
