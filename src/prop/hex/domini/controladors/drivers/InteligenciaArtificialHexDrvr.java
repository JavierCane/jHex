package prop.hex.domini.controladors.drivers;
/*
import prop.cluster.domini.models.Partida;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;

import prop.hex.domini.controladors.InteligenciaArtificialHex;
import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixEnter;


/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 10/11/12
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
/*public class InteligenciaArtificialHexDrvr
{

	public static void main( String[] args )
	{
		TaulerHex tauler = new TaulerHex( 7 );
		PartidaHex partida = new PartidaHex( null, null, tauler, "Nompartida", null, null );

		InteligenciaArtificialHex IA = new InteligenciaArtificialHex();

		while(true) {
			int[] d = IA.minimax( partida, EstatCasella.JUGADOR_A, 4 );
			tauler.mouFitxa( EstatCasella.JUGADOR_A, d[0], d[1] );
			int[] c = IA.minimax( partida, EstatCasella.JUGADOR_A, 4 );
			tauler.mouFitxa( EstatCasella.JUGADOR_B, c[0], c[1] );
			System.out.println( tauler.toString() );
			System.out.println("Presiona per continuar: ");
			int f = llegeixEnter();
		}
	}

}*/

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.InteligenciaArtificialHex;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;
import prop.hex.domini.models.UsuariHex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class InteligenciaArtificialHexDrvr
{

	public static void HumaVsIA()
	{
		createAndShowGUI();
/*		SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				createAndShowGUI();
			}
		} );*/
	}

	private static void createAndShowGUI()
	{
		//System.out.println( "S'ha creat la gui en un EDT? " + SwingUtilities.isEventDispatchThread() );
		JFrame f = new JFrame( "Visor Hex" );
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		f.add( new Finestra() );
		f.pack();
		f.setVisible( true );
	}
}

class Finestra extends JPanel
{

	String nom_partida;
	TaulerHex tauler;
	UsuariHex jugador_a;
	UsuariHex jugador_b;
	PartidaHex partida;
	InteligenciaArtificialHex IA;

	Polygon hexagon;
	int dx, dy;
	double radi = 40.0;
	int iniciX = 60;
	int iniciY = 60;

	public Finestra()
	{
		nom_partida = "Nom_partida";
		tauler = new TaulerHex( 7 );
		jugador_a = new UsuariHex( "Nom_jugador_a", "Contrasenya_jugador_a" );
		jugador_b = new UsuariHex( "Nom_jugador_b", "Contrasenya_jugador_b" );
		partida = new PartidaHex( jugador_a, jugador_b, tauler, nom_partida );
		IA = new InteligenciaArtificialHex();

		System.out.println( "Iniciant partida Huma vs. IA" );

		//Make the hexagon.
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

		addMouseListener( new MouseAdapter()
		{

			@Override
			public void mouseClicked( MouseEvent mouseEvent )
			{
				comprovaClick( mouseEvent.getX(), mouseEvent.getY() );
			}
		} );
	}

	private void comprovaClick( int x, int y )
	{
		int rx = iniciX;
		int ry = iniciY;

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

	private void clickHexagon( int i, int j )
	{
		if ( partida.getTornsJugats() % 2 == 0 && !partida.estaFinalitzada() )
		{ //Es torn del primer jugador (A) i la partida no ha finalitzat.
			if ( tauler.esMovimentValid( EstatCasella.JUGADOR_A, i, j ) )
			{
				System.out.println( "Torn: " + partida.getTornsJugats() );

				tauler.mouFitxa( EstatCasella.JUGADOR_A, i, j );
				partida.incrementaTornsJugats( 1 );

				System.out.println( "Jugador A mou a " + i + "," + j );
				System.out.println( tauler.toString() );

				if ( partida.comprovaEstatPartida( i, j ) != EstatPartida.NO_FINALITZADA )
				{
					partida.setFinalitzada( true );
					System.out.println( "Partida Finalitzada!" );
					System.out.println( "Guanya el Jugador A" );
				}

				System.out.println( "Torn: " + partida.getTornsJugats() );

				int[] d = IA.minimax( partida, EstatCasella.JUGADOR_B, 2 );
				tauler.mouFitxa( EstatCasella.JUGADOR_B, d[0], d[1] );
				partida.incrementaTornsJugats( 1 );

				System.out.println( "Jugador B mou a " + d[0] + "," + j );
				System.out.println( tauler.toString() );

				if ( partida.comprovaEstatPartida( d[0], d[1] ) != EstatPartida.NO_FINALITZADA )
				{
					partida.setFinalitzada( true );

					System.out.println( "Partida Finalitzada!" );
					System.out.println( "Guanya B" );
				}

				repaint();
			}
		}
	}

	public Dimension getPreferredSize()
	{
		return new Dimension( 800, 600 );
	}

	protected void paintComponent( Graphics g )
	{
		super.paintComponent( g );

		// draw entire component white
		g.setColor( jugador_a.getCombinacionsColors().obteColorCasella( EstatCasella.BUIDA ) );
		g.fillRect( 0, 0, getWidth(), getHeight() );

		g.translate( iniciX, iniciY );
		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			g.translate( i * dx / 2, i * dy );
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				g.translate( j * dx, 0 );

				if ( tauler.getEstatCasella( i, j ) == EstatCasella.JUGADOR_A )
				{
					g.setColor( jugador_a.getCombinacionsColors().obteColorCasella( EstatCasella.JUGADOR_A ) );
					g.fillPolygon( hexagon );
				}
				else if ( tauler.getEstatCasella( i, j ) == EstatCasella.JUGADOR_B )
				{
					g.setColor( jugador_a.getCombinacionsColors().obteColorCasella( EstatCasella.JUGADOR_B ) );
					g.fillPolygon( hexagon );
				}

				g.setColor( Color.black );
				g.drawPolygon( hexagon );

				g.translate( -j * dx, 0 );
			}
			g.translate( -i * dx / 2, -i * dy );
		}
	}
}
