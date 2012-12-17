package prop.hex.presentacio;

import prop.hex.presentacio.auxiliars.JPanelImatge;
import prop.hex.presentacio.auxiliars.VistaDialeg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Vista base del joc Hex.
 * Conté l'estructura bàsica de totes les vistes: imatge de fons, títol, botó de sortida...
 * És una classe abstracta, les vistes hauran de ser herència d'aqeusta, redefinint algunes operacions.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */
public abstract class BaseVista
{

	/**
	 * Frame principal de la vista.
	 */
	protected JFrame frame_vista;

	/**
	 * Panell principal de la vista.
	 */
	protected JPanelImatge panell_principal;

	/**
	 * Panell superior de la vista, pensat per a contenir el títol.
	 */
	protected JPanel panell_titol;

	/**
	 * Panell inferior de la vista, pensat per a contenir els botons de sortida i ajuda.
	 */
	protected JPanel panell_sortida;

	/**
	 * Botó d'ajuda.
	 */
	protected JButton ajuda;

	/**
	 * Botó de sortida.
	 */
	protected JButton surt;

	/**
	 * Títol de la vista.
	 */
	protected static JLabel titol;

	/**
	 * Constructor que crea una vista passant-li quin és el frame sobre el qual s'haurà de treballar i el
	 * controlador de presentació al qual haurà de demanar certes operacions.
	 *
	 * @param frame_vista Frame principal sobre el que s'hauran d'afegir els diferents components.
	 */
	public BaseVista( JFrame frame_vista )
	{
		this.frame_vista = frame_vista;
		panell_principal = new JPanelImatge( getClass().getResource( "/prop/img/fons.png" ) );
		panell_titol = new JPanel();
		panell_sortida = new JPanel();
		ajuda = new JButton( "", new ImageIcon( getClass().getResource( "/prop/img/ajuda.png" ) ) );
		surt = new JButton( "", new ImageIcon( getClass().getResource( "/prop/img/surt.png" ) ) );
	}

	/**
	 * Fa visible la vista al frame.
	 */
	public void fesVisible()
	{
		frame_vista.setContentPane( panell_principal );
		frame_vista.pack();
		frame_vista.setVisible( true );
	}

	/**
	 * Fa que la vista deixi de ser visible al frame.
	 */
	public void fesInvisible()
	{
		frame_vista.setVisible( false );
	}

	/**
	 * Activa la vista.
	 */
	public void activa()
	{
		frame_vista.setEnabled( true );
	}

	/**
	 * Desactiva la vista.
	 */
	public void desactiva()
	{
		frame_vista.setEnabled( false );
	}

	/**
	 * Inicialitza els diferents components de les vistes.
	 */
	protected void inicialitzaVista()
	{
		inicialitzaPanellPrincipal(); // Mètode abstracte, a implementar a cada classe
		inicialitzaPanellTitol(); // Mètode comú a totes les vistes

		inicialitzaPanellCentral(); // Mètode abstracte, a implementar a cada classe
		inicialitzaPanellPeu(); // Mètode abstracte, a implementar a cada classe

		inicialitzaPanellSortida(); // Mètode comú a totes les vistes
		assignaListeners(); // Mètode comú a totes les vistes
	}

	/**
	 * Inicialitza el panell principal de les vistes. Cada classe l'haurà d'implementar,
	 * ja que depèn del contingut de la vista.
	 */
	protected abstract void inicialitzaPanellPrincipal();

	/**
	 * Inicialitza el panell del títol.
	 */
	protected void inicialitzaPanellTitol()
	{
		panell_titol.add( titol );
		panell_titol.setOpaque( false );
	}

	/**
	 * Inicialitza el panell central de les vistes. Cada classe l'haurà d'implementar,
	 * ja que depèn del contingut de la vista.
	 */
	protected abstract void inicialitzaPanellCentral();

	/**
	 * Inicialitza el panell inferior de les vistes. Cada classe l'haurà d'implementar,
	 * ja que depèn del contingut de la vista.
	 */
	protected abstract void inicialitzaPanellPeu();

	/**
	 * Inicialitza el panell amb els botons de sortida i ajuda de les vistes.
	 */
	protected void inicialitzaPanellSortida()
	{
		panell_sortida.setLayout( new GridLayout( 2, 1, 10, 10 ) );
		panell_sortida.add( ajuda );
		panell_sortida.add( surt );
		panell_sortida.setOpaque( false );
	}

	/**
	 * Assigna els listeners als diferents botons.
	 */
	protected void assignaListeners()
	{
		surt.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoSurt();
			}
		} );

		ajuda.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoAjuda();
			}
		} );
	}

	/**
	 * Defineix el comportament del botó de sortida quan sigui pitjat.
	 */
	public void accioBotoSurt()
	{
		VistaDialeg dialeg = new VistaDialeg();

		String[] botons = {
				"Sí",
				"No"
		};

		String valor_seleccionat = dialeg.setDialeg( "Confirmació de la sortida", "Estàs segur que vols sortir del " +
		                                                                          "" + "programa?", botons,
				JOptionPane.QUESTION_MESSAGE );

		if ( "Sí" == valor_seleccionat )
		{
			System.exit( 0 );
		}
	}

	/**
	 * Defineix el comportament del botó d'ajuda quan sigui pitjat.
	 */
	public void accioBotoAjuda()
	{
		try
		{
			Desktop desktop = null;
			if ( Desktop.isDesktopSupported() )
			{
				desktop = Desktop.getDesktop();
			}

			desktop.open( new File( "doc/ajuda.pdf" ) );
		}
		catch ( Exception e )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat =
					dialeg.setDialeg( "Error", "Error al obrir el fitxer d'ajuda.", botons, JOptionPane.ERROR_MESSAGE );
		}
	}
}
