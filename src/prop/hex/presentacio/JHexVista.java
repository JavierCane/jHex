package prop.hex.presentacio;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public abstract class JHexVista
{

	protected PresentacioCtrl presentacio_ctrl;
	protected JFrame frame_vista;
	protected JPanelImatge panel_principal;
	protected JPanel panel_titol;
	protected JPanel panel_sortida;
	protected JButton surt;
	protected JLabel titol;
	protected JLabel titol_baix;

	public JHexVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		panel_principal = new JPanelImatge( "img/fons.png" );
		panel_titol = new JPanel();
		panel_sortida = new JPanel();
		surt = new JButton( "", new ImageIcon( "img/surt.png" ) );
		titol_baix = new JLabel( "jHex v1.0" );
		this.presentacio_ctrl = presentacio_ctrl;
		frame_vista = frame_principal;
	}

	public void fesVisible()
	{
		frame_vista.setContentPane( panel_principal );
		frame_vista.pack();
		frame_vista.setVisible( true );
	}

	public void fesInvisible()
	{
		frame_vista.setVisible( false );
	}

	public void activa()
	{
		frame_vista.setEnabled( true );
	}

	public void desactiva()
	{
		frame_vista.setEnabled( false );
	}

	protected void inicialitzaVista()
	{
		inicialitzaPanelPrincipal();
		inicialitzaPanelTitol();
		inicialitzaPanelSortida();
		assignaListeners();
	}

	protected void inicialitzaPanelTitol()
	{
		panel_titol.add( titol );
		panel_titol.setOpaque( false );
	}

	protected void inicialitzaPanelSortida()
	{
		panel_sortida.add( surt );
		panel_sortida.setOpaque( false );
	}

	protected abstract void inicialitzaPanelPrincipal();

	public void accioBotoSurt( ActionEvent event )
	{
		VistaDialeg dialeg = new VistaDialeg();
		String[] botons = {
				"Sí",
				"No"
		};
		String valor_seleccionat =
				dialeg.setDialeg( "Confirmació de la sortida", "Estàs segur que vols sortir " + "del programa?", botons,
						3 );
		if ( valor_seleccionat == "Sí" )
		{
			System.exit( 0 );
		}
	}

	protected void assignaListeners()
	{

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
