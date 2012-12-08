package prop.hex.presentacio;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.PartidaCtrl;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;
import prop.hex.domini.models.UsuariHex;
import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.TipusJugadors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class JPanelTauler extends JPanel
{

	/**
	 * Tauler d'Hex a dibuixar.
	 */
	private TaulerHex tauler;
	/**
	 * Jugador A
	 */
	private UsuariHex jugador_a;
	/**
	 * Jugador B
	 */
	private UsuariHex jugador_b;

	private Casella ultima_pista;

	private boolean pista_valida;
	/**
	 * Poligon que s'utilitza per dibuixar a pantalla.
	 */
	private Polygon hexagon;
	/**
	 * dx i dy són els increments horitzontals i verticals entre caselles.
	 */
	private int dx, dy;
	/**
	 * Radi de les caselles.
	 */
	private double radi = 40.0;
	/**
	 * Posició inicial del taulell a la pantalla.
	 */
	private int iniciX = 90;
	private int iniciY = 80;

	private boolean partida_en_curs;

	private boolean partida_ia;

	/**
	 * Constructora, obté el taulell i els jugadors, construeix un poligon hexagonal i
	 * afegeix el listener del ratoli pel cas del click.
	 */
	public JPanelTauler( boolean partida_en_curs )
	{
		tauler = PartidaCtrl.getInstancia().getPartidaActual().getTauler();
		jugador_a = PartidaCtrl.getInstancia().getPartidaActual().getJugadorA();
		jugador_b = PartidaCtrl.getInstancia().getPartidaActual().getJugadorB();
		ultima_pista = new Casella( 0, 0 );
		pista_valida = false;
		this.partida_en_curs = partida_en_curs;
		partida_ia = jugador_a.getTipusJugador() != TipusJugadors.JUGADOR &&
				jugador_a.getTipusJugador() != TipusJugadors.CONVIDAT &&
				jugador_b.getTipusJugador() != TipusJugadors.JUGADOR &&
				jugador_b.getTipusJugador() != TipusJugadors.CONVIDAT;

		//Creem l'hexagon que dibuixarem despres.
		int x[] = new int[6];
		int y[] = new int[6];
		for ( int i = 0; i < 6; i++ )
		{
			x[i] = ( int ) ( radi * Math.sin( ( double ) i * Math.PI / 3.0 ) );
			y[i] = ( int ) ( radi * Math.cos( ( double ) i * Math.PI / 3.0 ) );
		}
		hexagon = new Polygon( x, y, 6 );
		dx = ( int ) ( 2 * radi * Math.sin( Math.PI / 3.0 ) );
		dy = ( int ) ( 1.5 * radi );

		/**
		 * Afegim el listener del ratoli.
		 */
		addMouseListener( new MouseAdapter()
		{

			@Override
			public void mouseClicked( MouseEvent mouseEvent )
			{
				comprovaClick( mouseEvent.getX(), mouseEvent.getY() );
			}
		} );
	}

	/**
	 * Calcula sobre quina casella es corresponen les coordenades píxel i crida a clickHexagon amb aquesta
	 * informació. Listener de mouseClicked. Si s'ha fet click sobre una casella crida a clickHexagon,
	 * si s'ha fet click sobre el botó de moviment IA, crida a mouIA
	 *
	 * @param x Coordenades píxel horitxontals.
	 * @param y Coordenades píxel verticals.
	 */
	private void comprovaClick( int x, int y )
	{
		int rx = iniciX;
		int ry = iniciY;

		if ( partida_en_curs && ( ( PartidaCtrl.getInstancia().getPartidaActual().getTornsJugats() % 2 == 0 &&
				                ( x < 170 && x > -50 && y < 480 && y > 360 ) ) ||
								( PartidaCtrl.getInstancia().getPartidaActual().getTornsJugats() % 2 != 0 &&
								( x < 800 && x > 580 && y < 270 && y > 150 ) ) ) )
		{
			mouIAOMostraPista();
		}
		else
		{
			for ( int i = 0; i < tauler.getMida(); i++ )
			{
				rx += i * dx / 2;
				ry += i * dy;
				for ( int j = 0; j < tauler.getMida(); j++ )
				{
					rx += j * dx;
					if ( hexagon.contains( x - rx, y - ry ) )
					{
						clickHexagon( i, j );
					}
					rx -= j * dx;
				}
				rx -= i * dx / 2;
				ry -= i * dy;
			}
		}
	}

	/**
	 * Si la partida no està finalitzada i es torn de una IA, crida PartidaCtrl a executar moviment IA.
	 * Torna a pintar l'escena.
	 */
	private void mouIAOMostraPista()
	{
		if ( PartidaCtrl.getInstancia().consultaEstatPartida() == EstatPartida.NO_FINALITZADA && !PartidaCtrl
				.getInstancia().esTornHuma() )
		{
			PartidaCtrl.getInstancia().executaMovimentIA();
		}
		else if ( PartidaCtrl.getInstancia().consultaEstatPartida() == EstatPartida.NO_FINALITZADA && PartidaCtrl
				.getInstancia().esTornHuma() )
		{
			ultima_pista = PartidaCtrl.getInstancia().obtePista();
			pista_valida = true;
		}

		repaint();
	}

	/**
	 * Si la partida no està finalitzada i es torn d'un huma, crida a fer moviment a la casella i, j.
	 * Torna a pintar l'escena.
	 *
	 * @param i fila casella.
	 * @param j columna casella.
	 */
	private void clickHexagon( int i, int j )
	{
		if ( PartidaCtrl.getInstancia().consultaEstatPartida() == EstatPartida.NO_FINALITZADA && (PartidaCtrl
				.getInstancia().esTornHuma() || !partida_en_curs) )
		{
			try
			{
				//No ens cal comprovar si el moviment es fa o no (si retorna true o false).
				PartidaCtrl.getInstancia().mouFitxa( i, j );
				pista_valida = false;
				paintImmediately(0, 0, 800, 500);
				mouIAOMostraPista();
			}
			catch ( UnsupportedOperationException exepcio )
			{
				System.out.println( "Moviment no vàlid, partida finalitzada: " + exepcio.getMessage() );
			}
		}

		repaint();
	}

	/**
	 * Retorna la mida de la pantalla.
	 *
	 * @return
	 */
	public Dimension getPreferredSize()
	{
		return new Dimension( 800, 500 );
	}

	/**
	 * Pinta la pantalla.
	 *
	 * @param g paràmetre Graphics on es pinta.
	 */
	protected void paintComponent( Graphics g )
	{
		super.paintComponent( g );

		int max_num_pistes = PartidaHex.getMaxNumPistes();

		//Dibuixem el tauler, pintant cada hexagon del color que toca.
		g.translate( iniciX, iniciY );
		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			g.translate( i * dx / 2, i * dy );
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				g.translate( j * dx, 0 );

				if ( i == tauler.getMida() / 2 && j == tauler.getMida() / 2 &&
						PartidaCtrl.getInstancia().getPartidaActual().getTornsJugats() == 0 )
				{
					g.setColor( new Color( 0x333333 ) );
				}
				else
				{
					// Puc utilitzar-ho directament?
					if ( pista_valida && ( ultima_pista.getFila() == i && ultima_pista.getColumna() == j ) )
					{
						g.setColor( new Color( 0x333333 ) );
						ultima_pista.setColumna( 0 );
						ultima_pista.setFila( 0 );
					}
					else
					{
						g.setColor( jugador_a.getCombinacionsColors().getColorCasella( tauler.getEstatCasella( i,
								j ) ) );
					}
				}
				g.fillPolygon( hexagon );

				g.setColor( jugador_a.getCombinacionsColors().getColorVoraCaselles() );
				g.drawPolygon( hexagon );

				g.translate( -j * dx, 0 );
			}
			g.translate( -i * dx / 2, -i * dy );
		}

		//Si és torn de la IA mostrem el botó Mou IA, només si a la partida només juga la IA.
		if ( partida_en_curs && partida_ia && !PartidaCtrl.getInstancia().esTornHuma()
			 && PartidaCtrl.getInstancia().consultaEstatPartida() == EstatPartida.NO_FINALITZADA )
		{
			if ( PartidaCtrl.getInstancia().getPartidaActual().getTornsJugats() % 2 == 0 )
			{
				g.setColor( jugador_a.getCombinacionsColors().getColorCasella( EstatCasella.JUGADOR_A ) );
				g.fillRoundRect( -50, 360, 120, 40, 8, 8 );
				g.setColor( Color.black );
				g.drawRoundRect( -50, 360, 120, 40, 8, 8 );
				g.setColor( jugador_a.getCombinacionsColors().getColorTextMouFitxaIA( EstatCasella.JUGADOR_A ) );
				g.drawString( "Mou IA", -10, 385 );
			}
			else
			{
				g.setColor( jugador_a.getCombinacionsColors().getColorCasella( EstatCasella.JUGADOR_B ) );
				g.fillRoundRect( 580, 150, 120, 40, 8, 8 );
				g.setColor( Color.black );
				g.drawRoundRect( 580, 150, 120, 40, 8, 8 );
				g.setColor( jugador_a.getCombinacionsColors().getColorTextMouFitxaIA( EstatCasella.JUGADOR_B ) );
				g.drawString( "Mou IA", 620, 175 );
			}
		}

		//Si és torn d'un humà i té pistes per utilitzar mostrem el botó Demana Pista
		if ( partida_en_curs && !pista_valida && PartidaCtrl.getInstancia().esTornHuma() && PartidaCtrl.getInstancia()
				.consultaEstatPartida() == EstatPartida.NO_FINALITZADA )
		{
			if ( PartidaCtrl.getInstancia().getPartidaActual().getTornsJugats() % 2 == 0 )
			{
				// Com ho fem per carregar el valor màxim de pistes, que està a una classe del domini?
				if ( PartidaCtrl.getInstancia().getPartidaActual().getPistesUsades( 0 ) < max_num_pistes )
				{
					g.setColor( jugador_a.getCombinacionsColors().getColorCasella( EstatCasella.JUGADOR_A ) );
					g.fillRoundRect( -50, 360, 120, 40, 8, 8 );
					g.setColor( Color.black );
					g.drawRoundRect( -50, 360, 120, 40, 8, 8 );
					g.setColor( jugador_a.getCombinacionsColors().getColorTextMouFitxaIA( EstatCasella.JUGADOR_A ) );
					g.drawString( "Demana pista", -30, 385 );
				}
			}
			else
			{
				// Com ho fem per carregar el valor màxim de pistes, que està a una classe del domini?
				if ( PartidaCtrl.getInstancia().getPartidaActual().getPistesUsades( 1 ) < max_num_pistes )
				{
					g.setColor( jugador_a.getCombinacionsColors().getColorCasella( EstatCasella.JUGADOR_B ) );
					g.fillRoundRect( 580, 150, 120, 40, 8, 8 );
					g.setColor( Color.black );
					g.drawRoundRect( 580, 150, 120, 40, 8, 8 );
					g.setColor( jugador_a.getCombinacionsColors().getColorTextMouFitxaIA( EstatCasella.JUGADOR_B ) );
					g.drawString( "Demana pista", 600, 175 );
				}
			}
		}

		//Mostrem el torn actual.
		g.setColor( jugador_a.getCombinacionsColors().getColorTextInformacio( EstatCasella.BUIDA ) );
		g.drawString( "Torn: " + PartidaCtrl.getInstancia().getPartidaActual().getTornsJugats(), -50, 210 );
		g.drawString( "Torn: " + PartidaCtrl.getInstancia().getPartidaActual().getTornsJugats(), 580, 0 );

		//Si ha guanyat un jugador, mostrem el resultat.
		if ( PartidaCtrl.getInstancia().consultaEstatPartida() == EstatPartida.GUANYA_JUGADOR_A )
		{
			g.setColor( jugador_a.getCombinacionsColors().getColorTextInformacio( EstatCasella.JUGADOR_A ) );
			g.drawString( "Guanya " + jugador_a.getNom(), -50, 230 );
			g.drawString( "Guanya " + jugador_a.getNom(), 580, 20 );
		}
		else if ( PartidaCtrl.getInstancia().consultaEstatPartida() == EstatPartida.GUANYA_JUGADOR_B )
		{
			g.setColor( jugador_a.getCombinacionsColors().getColorTextInformacio( EstatCasella.JUGADOR_B ) );
			g.drawString( "Guanya " + jugador_b.getNom(), -50, 230 );
			g.drawString( "Guanya " + jugador_b.getNom(), 580, 20 );
		}

		//Mostrem algunes dades pel jugador A
		g.setColor( jugador_a.getCombinacionsColors().getColorTextInformacio( EstatCasella.JUGADOR_A ) );
		if ( PartidaCtrl.getInstancia().getPartidaActual().getTornsJugats() % 2 == 0 )
		{
			g.drawString( "Té el torn", -50, 270 );
		}
		g.drawString( jugador_a.getNom(), -50, 290 );
		g.drawString( "D'esquerra a dreta", -50, 310 );

		g.drawString( "Temps: " + PartidaCtrl.getInstancia().getPartidaActual().getTempsDeJoc( 0 ), -50, 330 );
		g.drawString( "Pistes disponibles: " + ( max_num_pistes - PartidaCtrl.getInstancia().getPartidaActual()
				.getPistesUsades( 0 ) ), -50, 350 );


		//I algunes dades pel jugador B
		g.setColor( jugador_a.getCombinacionsColors().getColorTextInformacio( EstatCasella.JUGADOR_B ) );
		if ( PartidaCtrl.getInstancia().getPartidaActual().getTornsJugats() % 2 == 1 )
		{
			g.drawString( "Té el torn", 580, 60 );
		}
		g.drawString( jugador_b.getNom(), 580, 80 );
		g.drawString( "De dalt a baix", 580, 100 );

		g.drawString( "Temps: " + PartidaCtrl.getInstancia().getPartidaActual().getTempsDeJoc( 1 ), 580, 120 );
		g.drawString( "Pistes disponibles: " + ( max_num_pistes - PartidaCtrl.getInstancia().getPartidaActual()
				.getPistesUsades( 1 ) ), 580, 140 );

		if ( jugador_a.getCombinacionsColors() == CombinacionsColors.VERMELL_BLAU )
		{
			g.drawImage( ( new ImageIcon( "img/tauler_vb.png" ) ).getImage(), -90, -80, getWidth(), getHeight(),
					null );
		}
		else
		{
			g.drawImage( ( new ImageIcon( "img/tauler_nb.png" ) ).getImage(), -90, -80, getWidth(), getHeight(), null );
		}
	}
}
