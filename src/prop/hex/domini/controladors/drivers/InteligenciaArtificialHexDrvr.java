package prop.hex.domini.controladors.drivers;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IA.CamiMinim;
import prop.hex.domini.controladors.IA.ConnexionsVirtuals;
import prop.hex.domini.controladors.IA.ResistenciaTauler;
import prop.hex.domini.controladors.IAHexFacilCtrl;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;
import prop.hex.domini.models.UsuariHex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//http://stackoverflow.com/questions/258099/how-to-close-a-java-swing-application-from-the-code
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
	IAHexFacilCtrl IA;

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
		IA = new IAHexFacilCtrl();
		IA.setPartida(partida);

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
		// Es torn del primer jugador (A) i la partida no ha finalitzat.
		if ( partida.getTornsJugats() % 2 == 0 && !partida.estaFinalitzada() )
		{
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

				Casella nou_moviment = IA.mouFitxa(EstatCasella.JUGADOR_B);
				tauler.mouFitxa( EstatCasella.JUGADOR_B, nou_moviment );
				partida.incrementaTornsJugats( 1 );

//				System.out.println( "Jugador B mou a " + d[0] + "," + j );
				System.out.println( tauler.toString() );

				if ( partida.comprovaEstatPartida( nou_moviment.getFila(), nou_moviment.getColumna() ) != EstatPartida.NO_FINALITZADA )
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
		g.setColor( jugador_a.getCombinacionsColors().getColorFonsFinestra() );
		g.fillRect( 0, 0, getWidth(), getHeight() );

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

//		g.setColor( Color.black );
//		g.drawString( "Camí mínim B: " + cami_minim_B, 10, 500 );
	}
}
