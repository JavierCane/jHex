package prop.hex.domini.controladors.drivers;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.controladors.IA.GrupCaselles;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixEnter;
import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixParaula;


/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 07/11/12
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
public class IAGrupCasellesDrvr
{

	static TaulerHex tauler;

	public static void main( String[] args )
	{
		int opcio = 0;
		tauler = new TaulerHex( 7 );
		tauler.mouFitxa(EstatCasella.JUGADOR_A, 4, 4);
		DibuixaTauler( tauler );

		while ( opcio != 9 )
		{
			System.out.println();
			System.out.println( "------------------------------------------------------" );
			System.out.println( "Proves de la clase GrupCaselles" );
			System.out.println( "Escull una opció:" );
			System.out.println( "1 - Posar fitxa" );
			System.out.println( "2 - Treure fitxa" );
			System.out.println( "3 - Fer un grup de caselles" );
			System.out.println( "9 - Surt del programa" );

			try
			{
				opcio = llegeixEnter();
			}
			catch ( NumberFormatException excepcio )
			{
				System.err.println( "No s'ha introduït cap nombre" );
			}

			switch ( opcio )
			{
				case 1:
					posaFitxa();
					break;
				case 2:
		//			treuFitxa();
					break;
				case 3:
		//			ferGrup();
					break;
				case 9:
					break;
				default:
					System.out.println( "No és una opció vàlida." );
					System.out.println();
			}
		}


/*		tauler.mouFitxa( EstatCasella.JUGADOR_A, 4, 3 );
		tauler.mouFitxa( EstatCasella.JUGADOR_A, 3, 3 );
		tauler.mouFitxa( EstatCasella.JUGADOR_A, 3, 2 );
		tauler.mouFitxa( EstatCasella.JUGADOR_A, 2, 2 );
		tauler.mouFitxa( EstatCasella.JUGADOR_A, 2, 1 );
		tauler.mouFitxa( EstatCasella.JUGADOR_A, 1, 1 );
		tauler.mouFitxa( EstatCasella.JUGADOR_A, 1, 0 );


		tauler.mouFitxa( EstatCasella.JUGADOR_A, 6, 0 );
		//tauler.mouFitxa( EstatCasella.JUGADOR_A, 3, 4 );

		GrupCaselles jugadorA = new GrupCaselles( tauler );
		jugadorA.estendre( new Casella( 4, 3 ) );
		ArrayList<Casella> grup = jugadorA.getGrup();
		System.out.println( "Grup A creat, hi ha " + grup.size() + " caselles: " );

		for ( int i = 0; i < grup.size(); i++ )
		{
			System.out.println( "Casella: " + grup.get( i ).getFila() + "," + grup.get( i ).getColumna() );
		}

		GrupCaselles veines = jugadorA.getVeins();
		ArrayList<Casella> grup_veines = veines.getGrup();

		System.out.println( "Veins creats, hi ha " + grup_veines.size() + " caselles: " );
		for ( int i = 0; i < grup_veines.size(); i++ )
		{
			System.out.println( "Casella: " + grup_veines.get( i ).getFila() + "," + grup_veines.get( i ).getColumna() );
			tauler.mouFitxa( EstatCasella.JUGADOR_B, grup_veines.get( i ).getFila(), grup_veines.get( i ).getColumna() );
		}
*/
	}

	private static void posaFitxa()
	{
		System.out.println( "Escull un jugador (A, B, cap):" );
		String jugador = llegeixParaula();
		EstatCasella fitxa;
		if ( jugador.equalsIgnoreCase( "A" ) )
		{
			fitxa = EstatCasella.JUGADOR_A;
		}
		else if ( jugador.equalsIgnoreCase( "B" ) )
		{
			fitxa = EstatCasella.JUGADOR_B;
		}
		else
		{
			fitxa = EstatCasella.BUIDA;
		}

		System.out.println( "Escriu la fila de la casella:" );
		int fila = llegeixEnter();

		System.out.println( "Escriu la columna de la casella:" );
		int columna = llegeixEnter();

		try
		{
			tauler.mouFitxa( fitxa, fila, columna );
		}
		catch ( IndexOutOfBoundsException excepcio )
		{
			System.err.println( "Error:" );
			System.err.println( excepcio.getMessage() );
		}
		catch ( IllegalArgumentException excepcio )
		{
			System.err.println( "Error:" );
			System.err.println( excepcio.getMessage() );
		}
	}

	public static void DibuixaTauler( TaulerHex tauler )
	{
		JFrame mainFrame = new JFrame( "Visor Tauler" );

		DrawTablero imatge = new DrawTablero();
		imatge.setTauler( tauler );

		mainFrame.getContentPane().add( imatge );
		mainFrame.pack();

		mainFrame.setVisible( true );
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	public static class DrawTablero extends JComponent
	{

		private TaulerHex tauler;

		public void setTauler( TaulerHex tauler )
		{
			this.tauler = tauler;
		}

		public void paintComponent( Graphics g )
		{
			super.paintComponent( g );

			// draw entire component white
			g.setColor( Color.white );
			g.fillRect( 0, 0, getWidth(), getHeight() );

			//Do the hexagon vertex coords.
			int x[] = new int[6];
			int y[] = new int[6];
			for ( int i = 0; i < 6; i++ )
			{
				x[i] = ( int ) ( 40.0 * Math.sin( ( double ) i * Math.PI / 3.0 ) );
				y[i] = ( int ) ( 40.0 * Math.cos( ( double ) i * Math.PI / 3.0 ) );
			}

			g.translate( 60, 60 );
			int dx = 2 * x[1];
			int dy = 60;
			for ( int i = 0; i < tauler.getMida(); i++ )
			{
				g.translate( i * dx / 2, i * dy );
				for ( int j = 0; j < tauler.getMida(); j++ )
				{
					g.translate( j * dx, 0 );

					if ( tauler.getEstatCasella( i, j ) == EstatCasella.JUGADOR_A )
					{
						g.setColor( Color.blue );
						g.fillPolygon( x, y, 6 );
					}
					else if ( tauler.getEstatCasella( i, j ) == EstatCasella.JUGADOR_B )
					{
						g.setColor( Color.red );
						g.fillPolygon( x, y, 6 );
					}
					g.setColor( Color.black );
					g.drawPolygon( x, y, 6 );

					g.translate( -j * dx, 0 );
				}
				g.translate( -i * dx / 2, -i * dy );
			}
		}

		public Dimension getPreferredSize()
		{
			return new Dimension( 800, 600 );
		}

		public Dimension getMinimumSize()
		{
			return getPreferredSize();
		}
	}


}
