package prop.hex.presentacio;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.TipusJugadors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class JPanelTauler extends JPanel
{

	private static PresentacioCtrl presentacio_ctrl;

	private Object[] elements_de_control_partida;

	private Object[][] elements_de_control_jugadors;

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
	public JPanelTauler( boolean partida_en_curs, PresentacioCtrl presentacio_ctrl )
	{
		this.presentacio_ctrl = presentacio_ctrl;
		elements_de_control_partida = presentacio_ctrl.getElementsDeControlPartida();
		elements_de_control_jugadors = presentacio_ctrl.getElementsDeControlJugadors();
		ultima_pista = new Casella( 0, 0 );
		pista_valida = false;
		this.partida_en_curs = partida_en_curs;
		partida_ia = ( ( TipusJugadors ) elements_de_control_jugadors[0][0] ) != TipusJugadors.JUGADOR &&
				( ( TipusJugadors ) elements_de_control_jugadors[0][0] ) != TipusJugadors.CONVIDAT &&
				( ( TipusJugadors ) elements_de_control_jugadors[0][1] ) != TipusJugadors.JUGADOR &&
				( ( TipusJugadors ) elements_de_control_jugadors[0][1] ) != TipusJugadors.CONVIDAT;

		//Creem l'hexàgon que dibuixarem després.
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

		// Afegim el listener del ratoli.
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

		elements_de_control_partida = presentacio_ctrl.getElementsDeControlPartida();
		elements_de_control_jugadors = presentacio_ctrl.getElementsDeControlJugadors();

		if ( partida_en_curs && ( ( (Integer) elements_de_control_partida[2] % 2 == 0 &&
	                            ( x < 170 && x > -50 && y < 480 && y > 360 ) ) ||
	                          ( (Integer) elements_de_control_partida[2] % 2 != 0 &&
	                            ( x < 800 && x > 580 && y < 270 && y > 150 ) ) ) )
		{
			mouIAOMostraPista();
		}
		else
		{
			for ( int i = 0; i < (Integer) elements_de_control_partida[1]; i++ )
			{
				rx += i * dx / 2;
				ry += i * dy;
				for ( int j = 0; j < (Integer) elements_de_control_partida[1]; j++ )
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
		if ( presentacio_ctrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA && !presentacio_ctrl.esTornHuma
				() )
		{
			presentacio_ctrl.executaMovimentIA();
		}
		else if ( presentacio_ctrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA && presentacio_ctrl
				.esTornHuma() )
		{
			ultima_pista = presentacio_ctrl.obtePista();
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
		if ( presentacio_ctrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA &&
			( presentacio_ctrl.esTornHuma() || !partida_en_curs ) )
		{
			try
			{
				//No ens cal comprovar si el moviment es fa o no (si retorna true o false).
				presentacio_ctrl.mouFitxa( i, j );
				pista_valida = false;
				paintImmediately( 0, 0, 800, 500 );
				// De moment sembla que falla, ja ho mirarem.
				/*if ( presentacio_ctrl.esPartidaAmbSituacioInicial() &&
				     presentacio_ctrl.esPartidaAmbSituacioInicialAcabadaDeDefinir() )
				{
					mouIAOMostraPista();
				}*/
				if ( partida_en_curs )
				{
					mouIAOMostraPista();
				}
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
	 * @param g Paràmetre Graphics on es pinta.
	 */
	protected void paintComponent( Graphics g )
	{
		super.paintComponent( g );

		elements_de_control_partida = presentacio_ctrl.getElementsDeControlPartida();
		elements_de_control_jugadors = presentacio_ctrl.getElementsDeControlJugadors();

		//Dibuixem el tauler, pintant cada hexagon del color que toca.
		g.translate( iniciX, iniciY );
		for ( int i = 0; i < (Integer) elements_de_control_partida[1]; i++ )
		{
			g.translate( i * dx / 2, i * dy );
			for ( int j = 0; j < (Integer) elements_de_control_partida[1]; j++ )
			{
				g.translate( j * dx, 0 );

				if ( i == (Integer) elements_de_control_partida[1] / 2 && j == (Integer) elements_de_control_partida[1] / 2 &&
						(Integer) elements_de_control_partida[2] == 0 )
				{
					g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] )
							.getColorCasellesInhabilitades() );
				}
				else
				{
					if ( pista_valida && ( ultima_pista.getFila() == i && ultima_pista.getColumna() == j ) )
					{
						g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] )
								.getColorCasellesPista() );
						ultima_pista.setColumna( 0 );
						ultima_pista.setFila( 0 );
					}
					else
					{
						g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorCasella(
								presentacio_ctrl.getEstatCasella( i, j ) ) );
					}
				}
				g.fillPolygon( hexagon );

				g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorVoraCaselles() );
				g.drawPolygon( hexagon );

				g.translate( -j * dx, 0 );
			}
			g.translate( -i * dx / 2, -i * dy );
		}

		// Si és torn de la IA mostrem el botó Mou IA, només si a la partida només juga la IA.
		if ( partida_en_curs && ( partida_ia || (Integer) elements_de_control_partida[2] == 0 ||
            presentacio_ctrl.esPartidaAmbSituacioInicialAcabadaDeDefinir()) && !presentacio_ctrl.esTornHuma()
			&& presentacio_ctrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA )
		{
			if ( (Integer) elements_de_control_partida[2] % 2 == 0 )
			{
				g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorCasella(
						EstatCasella.JUGADOR_A ) );
				g.fillRoundRect( -50, 360, 120, 40, 8, 8 );
				g.setColor( Color.black );
				g.drawRoundRect( -50, 360, 120, 40, 8, 8 );
				g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorTextMouFitxaIA(
						EstatCasella.JUGADOR_A ) );
				g.drawString( "Mou IA", -10, 385 );
			}
			else
			{
				g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorCasella(
						EstatCasella.JUGADOR_B ) );
				g.fillRoundRect( 580, 150, 120, 40, 8, 8 );
				g.setColor( Color.black );
				g.drawRoundRect( 580, 150, 120, 40, 8, 8 );
				g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorTextMouFitxaIA(
						EstatCasella.JUGADOR_B ) );
				g.drawString( "Mou IA", 620, 175 );
			}
		}

		// Si és torn de la IA i a la partida juga algun humà,
		if ( partida_en_curs && !partida_ia && !presentacio_ctrl.esTornHuma() &&
				presentacio_ctrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA )
		{
			if ( (Integer) elements_de_control_partida[2] % 2 == 0 )
			{
				g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorCasella(
						EstatCasella.JUGADOR_A ) );
				g.drawString( "Pensant moviment...", -50, 370 );
			}
			else
			{
				g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorCasella(
						EstatCasella.JUGADOR_B ) );
				g.drawString( "Pensant moviment...", 580, 160 );
			}
		}

		// Si és torn d'un humà i té pistes per utilitzar mostrem el botó Demana Pista
		if ( partida_en_curs && !pista_valida && presentacio_ctrl.esTornHuma() &&
				presentacio_ctrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA )
		{
			if ( (Integer) elements_de_control_partida[2] % 2 == 0 )
			{
				if ( ( ( Integer ) elements_de_control_jugadors[1][0] ) < (Integer) elements_de_control_partida[0] )
				{
					g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorCasella(
							EstatCasella.JUGADOR_A ) );
					g.fillRoundRect( -50, 360, 120, 40, 8, 8 );
					g.setColor( Color.black );
					g.drawRoundRect( -50, 360, 120, 40, 8, 8 );
					g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] )
							.getColorTextMouFitxaIA( EstatCasella.JUGADOR_A ) );
					g.drawString( "Demana pista", -30, 385 );
				}
			}
			else
			{
				// Com ho fem per carregar el valor màxim de pistes, que està a una classe del domini?
				if ( ( ( Integer ) elements_de_control_jugadors[1][1] ) < (Integer) elements_de_control_partida[0] )
				{
					g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorCasella(
							EstatCasella.JUGADOR_B ) );
					g.fillRoundRect( 580, 150, 120, 40, 8, 8 );
					g.setColor( Color.black );
					g.drawRoundRect( 580, 150, 120, 40, 8, 8 );
					g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] )
							.getColorTextMouFitxaIA( EstatCasella.JUGADOR_B ) );
					g.drawString( "Demana pista", 600, 175 );
				}
			}
		}

		//Mostrem el torn actual.
		g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorTextInformacio(
				EstatCasella.BUIDA ) );
		g.drawString( "Torn: " + elements_de_control_partida[2], -50, 210 );
		g.drawString( "Torn: " + elements_de_control_partida[2], 580, 0 );


		//Mostrem algunes dades pel jugador A
		g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorTextInformacio(
				EstatCasella.JUGADOR_A ) );
		if ( (Integer) elements_de_control_partida[2] % 2 == 0 )
		{
			g.drawString( "Té el torn", -50, 270 );
		}
		g.drawString( ( ( String ) elements_de_control_jugadors[3][0] ), -50, 290 );
		g.drawString( "D'esquerra a dreta", -50, 310 );

		g.drawString( "Temps: " + ( ( Long ) elements_de_control_jugadors[2][0] ) / 1000, -50, 330 );
		g.drawString( "Pistes disponibles: " + ( (Integer) elements_de_control_partida[0] -
                     ( ( Integer )  elements_de_control_jugadors[1][0] ) ), -50, 350 );

		//I algunes dades pel jugador B
		g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorTextInformacio( EstatCasella.JUGADOR_B ) );
		if ( (Integer) elements_de_control_partida[2] % 2 == 1 )
		{
			g.drawString( "Té el torn", 580, 60 );
		}
		g.drawString( ( ( String ) elements_de_control_jugadors[3][1] ), 580, 80 );
		g.drawString( "De dalt a baix", 580, 100 );

		g.drawString( "Temps: " + ( ( Long ) elements_de_control_jugadors[2][1] ) / 1000, 580, 120 );
		g.drawString( "Pistes disponibles: " + ( (Integer) elements_de_control_partida[0] - ( ( Integer ) elements_de_control_jugadors[1][1] ) ), 580, 140 );

		if ( ( ( CombinacionsColors ) elements_de_control_partida[3] ) == CombinacionsColors.VERMELL_BLAU )
		{
			g.drawImage( ( new ImageIcon( "img/tauler_vb.png" ) ).getImage(), -iniciX, -iniciY, getWidth(), getHeight(), null );
		}
		else
		{
			g.drawImage( ( new ImageIcon( "img/tauler_nb.png" ) ).getImage(), -iniciX, -iniciY, getWidth(), getHeight(), null );
		}

		//Si ha guanyat un jugador, mostrem el resultat.
		if ( presentacio_ctrl.consultaEstatPartida() == EstatPartida.GUANYA_JUGADOR_A )
		{
			g.setColor( new Color( 0x66CCFF ) );
			g.fillRoundRect( 150, 0, 300, 200, 16, 16 );
			g.setColor( Color.black );
			g.drawRoundRect( 150, 0, 300, 200, 16, 16 );
			g.drawString( "Partida finalitzada.", 160, 30 );
			g.drawString( "Guanya " + ( ( String ) elements_de_control_jugadors[3][0] ), 160, 50 );
			g.drawString( "amb un temps de " + ( ( Long ) elements_de_control_jugadors[2][0] )/1000 + " s,", 160, 70 );
			g.drawString( "col·locant un total de " + ( ( Integer )  elements_de_control_jugadors[4][0] ) + " fitxes,", 160, 90 );
			g.drawString( "i utilitzant " + ( ( Integer )  elements_de_control_jugadors[1][0] )  + " pistes.", 160, 110 );
			g.drawString( "Per a continuar, pitja el botó Abandona partida.", 160, 150);
		}
		else if ( presentacio_ctrl.consultaEstatPartida() == EstatPartida.GUANYA_JUGADOR_B )
		{
			g.setColor( new Color( 0x66CCFF ) );
			g.fillRoundRect( 150, 0, 300, 200, 16, 16 );
			g.setColor( Color.black );
			g.drawRoundRect( 150, 0, 300, 200, 16, 16 );
			g.drawString( "Partida finalitzada.", 160, 30 );
			g.drawString( "Guanya " + ( ( String ) elements_de_control_jugadors[3][1] ), 160, 50 );
			g.drawString( "amb un temps de " + ( ( Long ) elements_de_control_jugadors[2][1] )/1000 + " s,", 160, 70 );
			g.drawString( "col·locant un total de " + ( ( Integer )  elements_de_control_jugadors[4][1] ) + " fitxes,", 160, 90 );
			g.drawString( "i utilitzant " + ( ( Integer )  elements_de_control_jugadors[1][1] )  + " pistes.", 160, 110 );
			g.drawString( "Per a continuar, pitja el botó Abandona partida.", 160, 150);
		}
	}
}
