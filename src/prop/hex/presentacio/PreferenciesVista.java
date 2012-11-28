package prop.hex.presentacio;

import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.ModesInici;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PreferenciesVista extends BaseVista
{

	private JPanel panel_central = new JPanel();
	private JPanel panel_botons = new JPanel();
	private JRadioButton colors_vermell_blau = new JRadioButton( "Vermell/Blau" );
	private JRadioButton colors_negre_blanc = new JRadioButton( "Negre/Blanc" );
	private JRadioButton mode_inici_estandard = new JRadioButton( "Estàndard" );
	private JRadioButton mode_inici_pastis = new JRadioButton( "Regla del pastís" );
	private ButtonGroup grup_colors = new ButtonGroup();
	private ButtonGroup grup_modes_inici = new ButtonGroup();
	private JLabel colors = new JLabel( "Combinació de colors:" );
	private JLabel modes_inici = new JLabel( "Mode d'inici de la partida:" );
	private JButton accepta = new JButton( "Accepta" );
	private JButton descarta = new JButton( "Descarta" );

	public PreferenciesVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );
		titol = new JLabel( "Preferències" );
		inicialitzaVista();
	}

	protected void inicialitzaVista()
	{
		inicialitzaPanelPrincipal();
		inicialitzaPanelTitol();
		inicialitzaPanelCentral();
		inicialitzaPanelBotons();
		inicialitzaPanelSortida();
		assignaListeners();
	}

	private void inicialitzaPanelCentral()
	{
		panel_central.setLayout( new GridLayout( 2, 1, 10, 10 ) );
		panel_central.setOpaque( false );
		JPanel panel_colors = new JPanelImatge( "img/caixa.png" );
		panel_colors.setBorder( BorderFactory.createRaisedBevelBorder() );
		panel_colors.setLayout( new GridLayout( 3, 1, 10, 10 ) );
		grup_colors.add( colors_vermell_blau );
		grup_colors.add( colors_negre_blanc );
		colors_vermell_blau.setOpaque( false );
		colors_negre_blanc.setOpaque( false );
		if ( presentacio_ctrl.obteCombinacioDeColorsJugadorPrincipal() == CombinacionsColors.VERMELL_BLAU )
		{
			colors_vermell_blau.setSelected( true );
		}
		else
		{
			colors_negre_blanc.setSelected( true );
		}
		panel_colors.add( colors );
		panel_colors.add( colors_vermell_blau );
		panel_colors.add( colors_negre_blanc );
		JPanel panel_modes_inici = new JPanelImatge( "img/caixa.png" );
		panel_modes_inici.setBorder( BorderFactory.createRaisedBevelBorder() );
		panel_modes_inici.setLayout( new GridLayout( 3, 1, 10, 10 ) );
		grup_modes_inici.add( mode_inici_estandard );
		grup_modes_inici.add( mode_inici_pastis );
		mode_inici_estandard.setOpaque( false );
		mode_inici_pastis.setOpaque( false );
		if ( presentacio_ctrl.obteModeIniciJugadorPrincipal() == ModesInici.ESTANDARD )
		{
			mode_inici_estandard.setSelected( true );
		}
		else
		{
			mode_inici_pastis.setSelected( true );
		}
		panel_modes_inici.add( modes_inici );
		panel_modes_inici.add( mode_inici_estandard );
		panel_modes_inici.add( mode_inici_pastis );
		panel_central.add( panel_colors );
		panel_central.add( panel_modes_inici );
	}

	private void inicialitzaPanelBotons()
	{
		panel_botons.setLayout( new FlowLayout() );
		panel_botons.add( accepta );
		panel_botons.add( descarta );
		panel_botons.setOpaque( false );
	}

	protected void inicialitzaPanelPrincipal()
	{
		panel_principal.setLayout( new GridBagLayout() );
		panel_principal.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		GridBagConstraints propietats_panel = new GridBagConstraints();
		propietats_panel.fill = GridBagConstraints.HORIZONTAL;
		propietats_panel.anchor = GridBagConstraints.CENTER;
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 0;
		propietats_panel.weightx = 0.5;
		propietats_panel.weighty = 0.2;
		panel_principal.add( panel_titol, propietats_panel );
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.6;
		panel_principal.add( panel_central, propietats_panel );
		propietats_panel.gridy = 2;
		propietats_panel.weighty = 0.2;
		panel_principal.add( panel_botons, propietats_panel );
		propietats_panel.fill = GridBagConstraints.NONE;
		propietats_panel.gridx = 2;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.25;
		propietats_panel.anchor = GridBagConstraints.SOUTHEAST;
		panel_principal.add( panel_sortida, propietats_panel );
		propietats_panel.gridx = 0;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.25;
		propietats_panel.anchor = GridBagConstraints.SOUTHWEST;
		panel_principal.add( titol_baix, propietats_panel );
	}

	public void accioBotoAccepta( ActionEvent event )
	{
		try
		{
			ModesInici mode_inici = ModesInici.ESTANDARD;
			CombinacionsColors combinacio_colors = CombinacionsColors.VERMELL_BLAU;

			if ( mode_inici_pastis.isSelected() )
			{
				mode_inici = ModesInici.PASTIS;
			}

			if ( colors_negre_blanc.isSelected() )
			{
				combinacio_colors = CombinacionsColors.NEGRE_BLANC;
			}

			presentacio_ctrl.modificaPreferenciesJugadorPrincipal( mode_inici, combinacio_colors );
			presentacio_ctrl.guardaJugadorPrincipal();
			presentacio_ctrl.vistaPreferenciesAMenuPrincipal();
		}
		catch ( Exception e )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Error al guardar el fitxer d'usuari.", botons, JOptionPane.ERROR_MESSAGE );
		}
	}

	public void accioBotoDescarta( ActionEvent event )
	{
		presentacio_ctrl.vistaPreferenciesAMenuPrincipal();
	}

	protected void assignaListeners()
	{
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
