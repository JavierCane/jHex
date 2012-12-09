package prop.hex.presentacio;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.PartidaCtrl;
import prop.hex.domini.models.TaulerHex;
import prop.hex.domini.models.UsuariHex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Classe temporal per poder veure i jugar una partida mitjançant PartidaCtrl. Obre una finestra on dibuixa el tauler
 * i captura els clicks del ratoli fent moure a qui toqui en cada situació.
 * Aquesta classe s'utilitza des del driver de Jugar que comprova que funcioni PartidaCtrl així com IAHexFacilCtrl i
 * totes les classes que aquest utilitza.
 *
 * @author Marc Junyent Martín (Grup 7.3, Hex)
 */
public final class VisualitzadorPartida extends JPanel
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
	private int iniciX = 60;
	private int iniciY = 60;

	/**
	 * Constructora, obté el taulell i els jugadors, construeix un poligon hexagonal i
	 * afegeix el listener del ratoli pel cas del click.
	 */
	public VisualitzadorPartida()
	{
		tauler = PartidaCtrl.getInstancia().getPartidaActual().getTauler();
		jugador_a = PartidaCtrl.getInstancia().getPartidaActual().getJugadorA();
		jugador_b = PartidaCtrl.getInstancia().getPartidaActual().getJugadorB();

		//Creem l'exagon que dibuixarem despres.
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
		 * Fegim el listener del ratoli.
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

		if ( x < 720 && x > 500 && y < 570 && y > 450 )
		{
			mouIA();
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
	private void mouIA()
	{
		if ( PartidaCtrl.getInstancia().consultaEstatPartida() == EstatPartida.NO_FINALITZADA &&
		     !PartidaCtrl.getInstancia().esTornHuma() )
		{
			PartidaCtrl.getInstancia().executaMovimentIA();
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
		if ( PartidaCtrl.getInstancia().consultaEstatPartida() == EstatPartida.NO_FINALITZADA &&
		     PartidaCtrl.getInstancia().esTornHuma() )
		{
			try
			{
				//No ens cal comprovar si el moviment es fa o no (si retorna true o false).
				PartidaCtrl.getInstancia().mouFitxa( i, j );
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
		return new Dimension( 800, 600 );
	}

	/**
	 * Pinta la pantalla.
	 *
	 * @param g paràmetre Graphics on es pinta.
	 */
	protected void paintComponent( Graphics g )
	{
		super.paintComponent( g );

		//Pintem tot de blanc.
		g.setColor( jugador_a.getCombinacionsColors().getColorFonsFinestra() );
		g.fillRect( 0, 0, getWidth(), getHeight() );

		//Dibuixem les linies horitzontals i verticals que indiquen cap a on juga cada jugador.
		g.setColor( jugador_a.getCombinacionsColors().getColorCasella( EstatCasella.JUGADOR_A ) );
		g.drawLine( 5, 50, 240, 460 );
		g.drawLine( 505, 25, 740, 440 );
		g.setColor( jugador_a.getCombinacionsColors().getColorCasella( EstatCasella.JUGADOR_B ) );
		g.drawLine( 15, 15, 500, 15 );
		g.drawLine( 245, 465, 720, 465 );

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
					g.setColor( jugador_a.getCombinacionsColors().getColorCasella( tauler.getEstatCasella( i, j ) ) );
				}
				g.fillPolygon( hexagon );

				g.setColor( jugador_a.getCombinacionsColors().getColorVoraCaselles() );
				g.drawPolygon( hexagon );

				g.translate( -j * dx, 0 );
			}
			g.translate( -i * dx / 2, -i * dy );
		}

		//Si és torn de la IA mostrem el botó Mou IA.
		if ( !PartidaCtrl.getInstancia().esTornHuma() &&
		     PartidaCtrl.getInstancia().consultaEstatPartida() == EstatPartida.NO_FINALITZADA )
		{
			g.setColor( new Color( 0xAA0000 ) );
			g.fillRoundRect( 500, 450, 120, 40, 8, 8 );
			g.setColor( Color.black );
			g.drawRoundRect( 500, 450, 120, 40, 8, 8 );
			g.setColor( Color.white );
			g.drawString( "Mou IA", 540, 475 );
		}

		//Mostrem el torn actual.
		g.setColor( Color.black );
		g.drawString( "Torn: " + PartidaCtrl.getInstancia().getPartidaActual().getTornsJugats(), 0, 300 );

		//Si ha guanyat un jugador, mostrem el resultat.
		if ( PartidaCtrl.getInstancia().consultaEstatPartida() == EstatPartida.GUANYA_JUGADOR_A )
		{
			g.setColor( jugador_a.getCombinacionsColors().getColorCasella( EstatCasella.JUGADOR_A ) );
			g.drawString( "Guanya " + jugador_a.getNom(), 0, 330 );
		}
		else if ( PartidaCtrl.getInstancia().consultaEstatPartida() == EstatPartida.GUANYA_JUGADOR_B )
		{
			g.setColor( jugador_a.getCombinacionsColors().getColorCasella( EstatCasella.JUGADOR_B ) );
			g.drawString( "Guanya " + jugador_b.getNom(), 0, 330 );
		}

		//Mostrem algunes dades pel jugador A
		g.setColor( jugador_a.getCombinacionsColors().getColorCasella( EstatCasella.JUGADOR_A ) );
		g.drawString( jugador_a.getNom(), 10, 460 );
		g.drawString( "D'esquerra a dreta", 10, 480 );

		g.drawString( "Temps: " + PartidaCtrl.getInstancia().getPartidaActual().getTempsDeJoc( 0 ), 10, 500 );

		//I algunes dades pel jugador B
		g.setColor( jugador_a.getCombinacionsColors().getColorCasella( EstatCasella.JUGADOR_B ) );
		g.drawString( jugador_b.getNom(), 300, 460 );
		g.drawString( "De dalt a baix", 300, 480 );

		g.drawString( "Temps: " + PartidaCtrl.getInstancia().getPartidaActual().getTempsDeJoc( 1 ), 300, 500 );
	}
}
