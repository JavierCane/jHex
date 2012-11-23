package prop.hex.presentacio;


import javax.swing.*;
import java.awt.*;

public class IniciaSessioVista
{

	private JFrame frame_vista = new JFrame( "jHex - Inicia sessió" );
	private JPanelImatge panel_principal = new JPanelImatge( "img/fons.png" );
	private JPanel panel_titol = new JPanel();
	private JPanel panel_dades = new JPanelImatge("img/caixa.png");
	private JPanel panel_botons = new JPanel();
	private JPanel panel_sortida = new JPanel();
	private JButton accepta = new JButton( "Accepta" );
	private JButton registra = new JButton( "Registra't" );
	private JButton convidat = new JButton( "Entra com a convidat" );
	private JButton surt = new JButton( "", new ImageIcon( "img/surt.png" ) );
	private JTextField usuari = new JTextField();
	private JPasswordField contrasenya = new JPasswordField();
	private JLabel titol = new JLabel( "Inicia sessió" );
	private JLabel text_usuari = new JLabel( "Nom d'usuari:" );
	private JLabel text_contrasenya = new JLabel( "Contrasenya:" );
	private JLabel titol_baix = new JLabel( "jHex v1.0" );

	public IniciaSessioVista()
	{
		inicialitzaVista();
	}

	public void fesVisible()
	{
		frame_vista.pack();
		frame_vista.setVisible( true );
	}

	public void fesInvisible()
	{
		frame_vista.setVisible( false );
	}

	private void inicialitzaVista()
	{
		inicialitzaPanelPrincipal();
		inicialitzaPanelTitol();
		inicialitzaPanelDades();
		inicialitzaPanelBotons();
		inicialitzaPanelSortida();
		inicialitzaFrame();
	}

	private void inicialitzaFrame()
	{
		frame_vista.setMinimumSize( new Dimension( 800, 600 ) );
		frame_vista.setPreferredSize( frame_vista.getMinimumSize() );
		frame_vista.setResizable( false );
		frame_vista.setLocationRelativeTo( null );
		frame_vista.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame_vista.getContentPane().add( panel_principal );
	}

	private void inicialitzaPanelTitol()
	{
		panel_titol.add( titol );
		panel_titol.setOpaque( false );
	}

	private void inicialitzaPanelDades()
	{
		panel_dades.setBorder( BorderFactory.createRaisedBevelBorder() );
		panel_dades.setLayout( new BoxLayout(panel_dades, BoxLayout.PAGE_AXIS) );
		JPanel panel_camps = new JPanel();
		panel_camps.setLayout(new GridLayout(2,2,10,10));
		panel_camps.add( text_usuari );
		panel_camps.add(usuari);
		panel_camps.add( text_contrasenya );
		panel_camps.add(contrasenya);
		panel_camps.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		panel_camps.setOpaque( false );
		panel_dades.add( panel_camps );
		accepta.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_dades.add( accepta );
		panel_dades.add(Box.createVerticalStrut(10));


	}

	private void inicialitzaPanelBotons()
	{
		panel_botons.setLayout( new BoxLayout(panel_botons, BoxLayout.PAGE_AXIS) );
		registra.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_botons.add( registra );
		panel_botons.add(Box.createVerticalStrut(10));
		convidat.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_botons.add( convidat );
		panel_botons.setOpaque( false );
	}

	private void inicialitzaPanelSortida()
	{
		panel_sortida.add( surt );
		panel_sortida.setOpaque( false );
	}

	private void inicialitzaPanelPrincipal()
	{
		panel_principal.setLayout( new GridBagLayout() );
		panel_principal.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
		GridBagConstraints propietats_panel = new GridBagConstraints();
		propietats_panel.fill = GridBagConstraints.HORIZONTAL;
		propietats_panel.anchor = GridBagConstraints.CENTER;
		propietats_panel.gridx = 0;
		propietats_panel.gridy = 0;
		propietats_panel.weightx = 0.5;
		propietats_panel.weighty = 0.1;
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 0;
		propietats_panel.weightx = 0.5;
		propietats_panel.weighty = 0.1;
		panel_principal.add( panel_titol, propietats_panel );
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 1;
		propietats_panel.weighty = 0.5;
		panel_principal.add( panel_dades, propietats_panel );
		propietats_panel.gridx = 1;
		propietats_panel.gridy = 2;
		propietats_panel.weighty = 0.4;
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
}
