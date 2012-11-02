import Dominio.Tablero;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class DrawTablero extends JComponent
{

	private static ImageIcon logo = new ImageIcon( "res/img/logo.png" );
	private static Tablero taula;

	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );

		// draw entire component white
		g.setColor( Color.white );
		g.fillRect( 0, 0, getWidth(), getHeight() );

		// yellow circle
		//g.setColor( Color.yellow );
		//g.fillOval( 0, 0, 240, 240 );

		int x[] = new int[6];
		int y[] = new int[6];

		for ( int i = 0; i < 6; i++ )
		{
			x[i] = ( int ) ( 40.0 * Math.sin( ( double ) i * Math.PI / 3.0 ) );
			y[i] = ( int ) ( 40.0 * Math.cos( ( double ) i * Math.PI / 3.0 ) );
		}

		//logo.paintIcon(this, g, 50, 50);

		g.translate( 60, 60 );

		int dx = 2 * x[1];
		int dy = 60;

		for ( int i = 0; i < 7; i++ )
		{
			g.translate( i * dx / 2, i * dy );
			for ( int j = 0; j < 7; j++ )
			{
				g.translate( j * dx, 0 );

				if ( MainMarc.tabla.getNumJugadorCasilla( i, j ) != null )
				{
					if ( MainMarc.tabla.getNumJugadorCasilla( i, j ) == 0 )
					{
						g.setColor( Color.blue );
						g.fillPolygon( x, y, 6 );
					}
					else if ( MainMarc.tabla.getNumJugadorCasilla( i, j ) == 1 )
					{
						g.setColor( Color.red );
						g.fillPolygon( x, y, 6 );
					}
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
