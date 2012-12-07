package prop.hex.presentacio;

import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.ModesInici;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class PreferenciesVista extends BaseVista
{

	private JPanel panell_central;
	private JPanel panell_botons;
	private JRadioButton colors_vermell_blau;
	private JRadioButton colors_negre_blanc;
	private JRadioButton mode_inici_estandard;
	private JRadioButton mode_inici_pastis;
	private ButtonGroup grup_colors;
	private ButtonGroup grup_modes_inici;
	private JLabel colors;
	private JLabel modes_inici;
	private JButton reinicia_estadistiques;
	private JButton canvia_contrasenya;
	private JButton accepta;
	private JButton descarta;
	private JDialog dialeg_canvi_contrasenya;

	public PreferenciesVista( PresentacioCtrl presentacio_ctrl, JFrame frame_principal )
	{
		super( presentacio_ctrl, frame_principal );

		titol = new JLabel( "Preferències" );
		panell_central = new JPanel();
		panell_botons = new JPanel();
		colors_vermell_blau = new JRadioButton( "Vermell/Blau" );
		colors_negre_blanc = new JRadioButton( "Negre/Blanc" );
		mode_inici_estandard = new JRadioButton( "Estàndard" );
		mode_inici_pastis = new JRadioButton( "Regla del pastís" );
		grup_colors = new ButtonGroup();
		grup_modes_inici = new ButtonGroup();
		colors = new JLabel( "Combinació de colors:" );
		modes_inici = new JLabel( "Mode d'inici de la partida:" );
		reinicia_estadistiques = new JButton( "Reinicia les estadístiques" );
		canvia_contrasenya = new JButton( "Canvia la contrasenya" );
		accepta = new JButton( "Accepta" );
		descarta = new JButton( "Descarta" );
		dialeg_canvi_contrasenya = new JDialog( new JFrame(), "Canvia la contrasenya" );

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
		panell_principal.add( panell_central, propietats_panel );
		propietats_panel.gridy = 2;
		propietats_panel.weighty = 0.2;
		panell_principal.add( panell_botons, propietats_panel );
		propietats_panel.fill = GridBagConstraints.NONE;
		propietats_panel.gridx = 2;
		propietats_panel.gridy = 2;
		propietats_panel.weightx = 0.25;
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
		panell_central.setLayout( new GridLayout( 3, 1, 10, 10 ) );
		panell_central.setOpaque( false );
		JPanel panell_colors = new JPanelImatge( "img/caixa.png" );
		panell_colors.setBorder( BorderFactory.createRaisedBevelBorder() );
		panell_colors.setLayout( new GridLayout( 3, 1, 10, 10 ) );
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

		panell_colors.add( colors );
		panell_colors.add( colors_vermell_blau );
		panell_colors.add( colors_negre_blanc );
		JPanel panell_modes_inici = new JPanelImatge( "img/caixa.png" );
		panell_modes_inici.setBorder( BorderFactory.createRaisedBevelBorder() );
		panell_modes_inici.setLayout( new GridLayout( 3, 1, 10, 10 ) );
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

		panell_modes_inici.add( modes_inici );
		panell_modes_inici.add( mode_inici_estandard );
		panell_modes_inici.add( mode_inici_pastis );
		panell_central.add( panell_colors );
		panell_central.add( panell_modes_inici );
		JPanel panell_botons_opcions = new JPanel();
		if ( presentacio_ctrl.getEsConvidat() )
		{
			reinicia_estadistiques.setEnabled( false );
			canvia_contrasenya.setEnabled( false );
		}
		panell_botons_opcions.add( reinicia_estadistiques );
		panell_botons_opcions.add( canvia_contrasenya );
		panell_botons_opcions.setOpaque( false );
		panell_central.add( panell_botons_opcions );
	}

	@Override
	protected void inicialitzaPanellPeu()
	{
		panell_botons.setLayout( new FlowLayout() );

		panell_botons.add( accepta );
		panell_botons.add( descarta );
		panell_botons.setOpaque( false );
	}

	public void accioBotoReiniciaEstadistiques( ActionEvent event )
	{
		VistaDialeg dialeg = new VistaDialeg();
		String[] botons = {
				"Sí", "No"
		};
		String valor_seleccionat = dialeg.setDialeg( "Reinicia les estadístiques", "Estàs segur que vols reiniciar " +
				"" + "les teves estadístiques? Aquesta acció no es podrà desfer.", botons,
				JOptionPane.WARNING_MESSAGE );
		if ( valor_seleccionat == "Sí" )
		{
			presentacio_ctrl.reiniciaEstadistiquesJugadorPrincipal();
		}
	}

	public void accioBotoCanviaContrasenya( ActionEvent event )
	{
		if ( presentacio_ctrl.getEsConvidat() )
		{
			VistaDialeg dialeg = new VistaDialeg();
			String[] botons = { "Accepta" };
			String valor_seleccionat = dialeg.setDialeg( "Error", "Els usuaris convidats no tenen contrasenya.",
					botons, JOptionPane.WARNING_MESSAGE );
		}
		else
		{
			presentacio_ctrl.vistaPreferenciesACanviaContrasenya();
		}
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
			String valor_seleccionat = dialeg.setDialeg( "Error", "Error al guardar el fitxer d'usuari.", botons,
					JOptionPane.ERROR_MESSAGE );
		}
	}

	public void accioBotoDescarta( ActionEvent event )
	{
		presentacio_ctrl.vistaPreferenciesAMenuPrincipal();
	}

	@Override
	protected void assignaListeners()
	{
		super.assignaListeners();

		reinicia_estadistiques.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoReiniciaEstadistiques( event );
			}
		} );

		canvia_contrasenya.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent event )
			{
				accioBotoCanviaContrasenya( event );
			}
		} );

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
