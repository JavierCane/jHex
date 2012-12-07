package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class RegistraVista extends BaseVista
{

	private JPanel panell_dades;
	private JButton accepta;
	private JButton descarta;
	private JTextField usuari;
	private JPasswordField contrasenya;
	private JPasswordField confirma_contrasenya;
	private JLabel text_usuari;
	private JLabel text_contrasenya;
	private JLabel text_confirma_contrasenya;

	public RegistraVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "Registra't" );
		panell_dades = new JPanelImatge( "img/caixa.png" );
		accepta = new JButton( "Accepta" );
		descarta = new JButton( "Descarta" );
		usuari = new JTextField();
		contrasenya = new JPasswordField();
		confirma_contrasenya = new JPasswordField();
		text_usuari = new JLabel( "Nom d'usuari:" );
		text_contrasenya = new JLabel( "Contrasenya:" );
		text_confirma_contrasenya = new JLabel( "Confirma la contrasenya:" );

		inicialitzaVista();
	}

	@Override
	protected void inicialitzaPanellPrincipal()
	{
		panell_principal.setLayout( new GridBagLayout() );
		panell_principal.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		GridBagConstraints propietats_panel = new GridBagConstraints();
		propietats_panel.fill = GridBagConstraints.HORIZONTAL;
		propietats_panel.anchor = GridBagConstraints.CENTER;
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 0;
		propietats_panel.weightx = 0.5;
		propietats_panel.weighty = 0.2;
		panell_principal.add( panell_titol, propietats_panel );
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.6;
		panell_principal.add( panell_dades, propietats_panel );
		propietats_panel.fill = GridBagConstraints.NONE;
		propietats_panel.gridx = 2;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.25;
		propietats_panel.weighty = 0.2;
		propietats_panel.anchor = GridBagConstraints.SOUTHEAST;
		panell_principal.add( panell_sortida, propietats_panel );
		propietats_panel.gridx = 0;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.25;
		propietats_panel.anchor = GridBagConstraints.SOUTHWEST;
		panell_principal.add( titol_baix, propietats_panel );
	}

	@Override
	protected void inicialitzaPanellCentral()
	{
		panell_dades.setBorder( BorderFactory.createRaisedBevelBorder() );
		panell_dades.setLayout( new BoxLayout( panell_dades, BoxLayout.PAGE_AXIS ) );
		JPanel panell_camps = new JPanel();
		panell_camps.setLayout( new GridLayout( 3, 2, 10, 10 ) );
		panell_camps.add( text_usuari );
		panell_camps.add( usuari );
		panell_camps.add( text_contrasenya );
		panell_camps.add( contrasenya );
		panell_camps.add( text_confirma_contrasenya );
		panell_camps.add( confirma_contrasenya );
		panell_camps.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		panell_camps.setOpaque( false );
		JPanel panell_botons = new JPanel();
		panell_botons.setLayout( new FlowLayout() );
		panell_botons.add( accepta );
		panell_botons.add( descarta );
		panell_botons.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		panell_botons.setOpaque( false );
		panell_dades.add( panell_camps );
		panell_dades.add( panell_botons );
	}

	@Override
	protected void inicialitzaPanellPeu()
	{
	}

	public void accioBotoAccepta( ActionEvent event )
	{
		try
		{
			String contrasenya_introduida = new String( contrasenya.getPassword() );
			if ( !contrasenya_introduida.equals( new String( confirma_contrasenya.getPassword() ) ) )
			{
				VistaDialeg dialeg = new VistaDialeg();
				String[] botons = { "Accepta" };
				String valor_seleccionat = dialeg.setDialeg( "Error", "Les dues contrasenyes no coincideixen.", botons,
						JOptionPane.WARNING_MESSAGE );
			}
			else
			{
				presentacio_ctrl.registraUsuari( usuari.getText(), contrasenya_introduida );
				presentacio_ctrl.setUsuariActual( usuari.getText(), contrasenya_introduida );
				presentacio_ctrl.vistaRegistraAMenuPrincipal();
			}
		}
		catch ( IllegalArgumentException excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat =
					dialeg.setDialeg( "Error", excepcio.getMessage(), botons, JOptionPane.WARNING_MESSAGE );
		}
		catch ( Exception excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat =
					dialeg.setDialeg( "Error", excepcio.getMessage(), botons, JOptionPane.ERROR_MESSAGE );
		}
	}

	public void accioBotoDescarta( ActionEvent event )
	{
		presentacio_ctrl.vistaRegistraAIniciaSessio();
	}

	@Override
	protected void assignaListeners()
	{
		super.assignaListeners();

		accepta.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoAccepta( event );
			}
		} );

		descarta.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoDescarta( event );
			}
		} );
	}
}
