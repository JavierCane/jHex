package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CanviaContrasenyaVista extends BaseVista
{

	private JPanel panell_dades;
	private JButton accepta;
	private JButton descarta;
	private JPasswordField contrasenya_actual;
	private JPasswordField contrasenya_nova;
	private JPasswordField confirma_contrasenya_nova;
	private JLabel text_contrasenya_actual;
	private JLabel text_contrasenya_nova;
	private JLabel text_confirma_contrasenya_nova;

	public CanviaContrasenyaVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "Canvia la contrasenya" );
		panell_dades = new JPanelImatge( "img/caixa.png" );
		accepta = new JButton( "Accepta" );
		descarta = new JButton( "Descarta" );
		contrasenya_actual = new JPasswordField();
		contrasenya_nova = new JPasswordField();
		confirma_contrasenya_nova = new JPasswordField();
		text_contrasenya_actual = new JLabel( "Contrasenya actual:" );
		text_contrasenya_nova = new JLabel( "Contrasenya nova:" );
		text_confirma_contrasenya_nova = new JLabel( "Confirma la contrasenya nova:" );

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
		panell_camps.add( text_contrasenya_actual );
		panell_camps.add( contrasenya_actual );
		panell_camps.add( text_contrasenya_nova );
		panell_camps.add( contrasenya_nova );
		panell_camps.add( text_confirma_contrasenya_nova );
		panell_camps.add( confirma_contrasenya_nova );
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
			String contrasenya_nova_introduida = new String( contrasenya_nova.getPassword() );
			if ( !contrasenya_nova_introduida.equals( new String( confirma_contrasenya_nova.getPassword() ) ) )
			{
				VistaDialeg dialeg = new VistaDialeg();
				String[] botons = { "Accepta" };
				String valor_seleccionat = dialeg.setDialeg( "Error", "Les dues contrasenyes no coincideixen.",
						botons, JOptionPane.WARNING_MESSAGE );
			}
			else
			{
				presentacio_ctrl.canviaContrasenyaJugadorPrincipal( new String( contrasenya_actual.getPassword() ),
						contrasenya_nova_introduida );
				presentacio_ctrl.guardaJugadorPrincipal();
				presentacio_ctrl.vistaCanviaContrasenyaAMenuPrincipal();

			}
		}
		catch ( IllegalArgumentException excepcio )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "La contrasenya actual de l'usuari no coincideix " +
					"" + "amb la introduïda.", botons, JOptionPane.WARNING_MESSAGE );
		}
		catch ( Exception e )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Error al guardar el fitxer d'usuari.", botons,
					JOptionPane.ERROR_MESSAGE );
		}
	}

	public void accioBotoDescarta( ActionEvent event )
	{
		presentacio_ctrl.vistaCanviaContrasenyaAPreferencies();
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
