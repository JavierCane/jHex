package prop.hex.domini.controladors.drivers;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IAHexFacilCtrl;
import prop.hex.domini.controladors.PartidaCtrl;
import prop.hex.domini.controladors.UsuariCtrl;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;
import prop.hex.domini.models.UsuariHex;
import prop.hex.domini.models.enums.TipusJugadors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 18/11/12
 * Time: 20:13
 * To change this template use File | Settings | File Templates.
 */
public class JugarDrvr
{
	public static void IAVsHuma()
	{
		UsuariHex usuari_A = UsuariCtrl.creaUsuari( "Maquina facil", "contrasenya", TipusJugadors.IA_FACIL, false );
		UsuariHex usuari_B = UsuariCtrl.creaUsuari( "Huma", "contrasenya", TipusJugadors.JUGADOR, false );

		try
		{
			PartidaCtrl.inicialitzaPartida( 7, usuari_A, usuari_B, "Partida de Prova" );
		}
		catch ( Exception e )
		{
			System.out.println( "Excepció al inicialitzar la partida: " + e.getMessage() );
		}

		instanciaFinestra();
	}
	public static void HumaVsIA()
	{
		UsuariHex usuari_A = UsuariCtrl.creaUsuari( "Huma", "contrasenya", TipusJugadors.JUGADOR, false );
		UsuariHex usuari_B = UsuariCtrl.creaUsuari( "Maquina facil", "contrasenya", TipusJugadors.IA_FACIL, false );

		try
		{
			PartidaCtrl.inicialitzaPartida( 7, usuari_A, usuari_B, "Partida de Prova" );
		}
		catch ( Exception e )
		{
			System.out.println( "Excepció al inicialitzar la partida: " + e.getMessage() );
		}

		instanciaFinestra( );
	}

	public static void HumaVsHuma()
	{

		UsuariHex usuari_A = UsuariCtrl.creaUsuari( "Huma 1", "contrasenya", TipusJugadors.JUGADOR, false );
		UsuariHex usuari_B = UsuariCtrl.creaUsuari( "Huma 2", "contrasenya", TipusJugadors.JUGADOR, false );

		try
		{
			PartidaCtrl.inicialitzaPartida( 7, usuari_A, usuari_B, "Partida de Prova" );
		}
		catch ( Exception e )
		{
			System.out.println( "Error al inicialitzar la partida." );
		}

		instanciaFinestra();
	}

	public static void IAVsIA()
	{

		UsuariHex usuari_A = UsuariCtrl.creaUsuari( "Maquina facil 2", "contrasenya", TipusJugadors.IA_FACIL, false );
		UsuariHex usuari_B = UsuariCtrl.creaUsuari( "Maquina facil 1", "contrasenya", TipusJugadors.IA_FACIL, false );

		try
		{
			PartidaCtrl.inicialitzaPartida( 7, usuari_A, usuari_B, "Partida de Prova" );
		}
		catch ( Exception e )
		{
			System.out.println( "Excepció al inicialitzar la partida: " + e.getMessage() );
		}

		instanciaFinestra();
	}


	private static void instanciaFinestra( )
	{
		JFrame f = new JFrame( "Visor partida" );
		//f.setDefaultCloseOperation();
		f.add( new Finestra( ) );
		f.pack();
		f.setVisible( true );
	}

	static class Finestra extends JPanel
	{

		private TaulerHex tauler;
		private UsuariHex jugador_a;
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
		 * Radi de les caselles i posició d'inici del taulell a la pantalla.
		 */
		private double radi = 40.0;
		private int iniciX = 60;
		private int iniciY = 60;

		public Finestra( )
		{
			tauler = ( TaulerHex ) PartidaCtrl.getPartidaActual().getTauler();
			jugador_a = ( UsuariHex ) PartidaCtrl.getPartidaActual().getJugadorA();
			jugador_b = ( UsuariHex ) PartidaCtrl.getPartidaActual().getJugadorB();

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
		 * informació.
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

		private void mouIA()
		{
			if ( PartidaCtrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA && !PartidaCtrl.esTornHuma() )
			{
				PartidaCtrl.executaMovimentIA();
			}

			repaint();
		}

		private void clickHexagon( int i, int j )
		{
			if ( PartidaCtrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA && PartidaCtrl.esTornHuma() )
			{
				try
				{
					PartidaCtrl.mouFitxa( i, j );
				}
				catch ( UnsupportedOperationException exepcio )
				{
					System.out.println( "Moviment no vàlid: " + exepcio.getMessage() );
				}
			}

			repaint();
		}

		public Dimension getPreferredSize()
		{
			return new Dimension( 800, 600 );
		}

		protected void paintComponent( Graphics g )
		{
			super.paintComponent( g );

			g.setColor( jugador_a.getCombinacionsColors().getColorFonsFinestra() );
			g.fillRect( 0, 0, getWidth(), getHeight() );

			g.setColor(jugador_a.getCombinacionsColors().getColorCasella(EstatCasella.JUGADOR_A));
			g.drawLine(5, 50, 240, 460);
			g.setColor(jugador_a.getCombinacionsColors().getColorCasella(EstatCasella.JUGADOR_B));
			g.drawLine(245, 465, 720, 465);



			g.translate( iniciX, iniciY );

			for ( int i = 0; i < tauler.getMida(); i++ )
			{
				g.translate( i * dx / 2, i * dy );
				for ( int j = 0; j < tauler.getMida(); j++ )
				{
					g.translate( j * dx, 0 );

					g.setColor( jugador_a.getCombinacionsColors().getColorCasella( tauler.getEstatCasella( i, j ) ) );
					g.fillPolygon( hexagon );

					g.setColor( jugador_a.getCombinacionsColors().getColorVoraCaselles() );
					g.drawPolygon( hexagon );

					g.translate( -j * dx, 0 );
				}
				g.translate( -i * dx / 2, -i * dy );
			}

			if ( !PartidaCtrl.esTornHuma() )
			{
				g.setColor( new Color( 0xAA0000 ) );
				g.fillRoundRect( 500, 450, 120, 40, 8, 8 );
				g.setColor( Color.black );
				g.drawRoundRect( 500, 450, 120, 40, 8, 8 );
				g.setColor( Color.white );
				g.drawString( "Mou IA", 540, 475);
			}

//		g.setColor( Color.black );
//		g.drawString( "Camí mínim B: " + cami_minim_B, 10, 500 );
		}
	}

}
