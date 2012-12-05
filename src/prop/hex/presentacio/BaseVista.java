package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class BaseVista
{

	protected static PresentacioCtrl presentacio_ctrl;
	protected JFrame frame_vista;
	protected JPanelImatge panell_principal = new JPanelImatge( "img/fons.png" ); // Si ho posem com static, UI peta
	protected JPanel panell_titol = new JPanel();
	protected JPanel panell_sortida = new JPanel();
	protected JButton ajuda;
	protected JButton surt;
	protected JLabel titol_baix;

	// Atribut per sobreescriure
	protected static JLabel titol;

	public BaseVista( PresentacioCtrl presentacio_ctrl, JFrame frame_vista )
	{
		this.presentacio_ctrl = presentacio_ctrl;
		this.frame_vista = frame_vista;
		panell_principal = new JPanelImatge( "img/fons.png" );
		panell_titol = new JPanel();
		panell_sortida = new JPanel();
		ajuda = new JButton( "", new ImageIcon( "img/ajuda.png" ) );
		surt = new JButton( "", new ImageIcon( "img/surt.png" ) );
		titol_baix = new JLabel( "jHex v1.0" );
	}

	public void fesVisible()
	{
		frame_vista.setContentPane( panell_principal );
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
		inicialitzaPanellPrincipal(); // Mètode abstracte, a implementar a cada classe
		inicialitzaPanellTitol(); // Mètode comú a totes les vistes

		inicialitzaPanellCentral(); // Mètode abstracte, a implementar a cada classe
		inicialitzaPanellPeu(); // Mètode abstracte, a implementar a cada classe

		inicialitzaPanellSortida(); // Mètode comú a totes les vistes
		assignaListeners(); // Mètode comú a totes les vistes
	}

	protected abstract void inicialitzaPanellPrincipal();

	protected void inicialitzaPanellTitol()
	{
		panell_titol.add( titol );
		panell_titol.setOpaque( false );
	}

	protected abstract void inicialitzaPanellCentral();

	protected abstract void inicialitzaPanellPeu();

	protected void inicialitzaPanellSortida()
	{
		panell_sortida.setLayout( new GridLayout( 2, 1, 10, 10 ) );
		panell_sortida.add( ajuda );
		panell_sortida.add( surt );
		panell_sortida.setOpaque( false );
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

	public void accioBotoSurt( ActionEvent event )
	{
		VistaDialeg dialeg = new VistaDialeg();

		String[] botons = {
				"Sí", "No"
		};

		String valor_seleccionat = dialeg.setDialeg( "Confirmació de la sortida", "Estàs segur que vols sortir del " +
				"programa?", botons, JOptionPane.QUESTION_MESSAGE );

		if ( "Sí" == valor_seleccionat )
		{
			System.exit( 0 );
		}
	}
}
