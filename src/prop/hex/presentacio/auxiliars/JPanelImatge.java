package prop.hex.presentacio.auxiliars;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Classe d'un panell amb imatge de fons.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */
public final class JPanelImatge extends JPanel
{

	/**
	 * Imatge de fons del panell.
	 */
	private Image fons;

	/**
	 * Constructor del panell passant com a paràmetre la ruta de la imatge de fons.
	 *
	 * @param img Ruta de la imatge de fons.
	 */
	public JPanelImatge( URL img )
	{
		fons = ( new ImageIcon( img ) ).getImage();
	}

	/**
	 * Pinta la imatge de fons al panell.
	 *
	 * @param g Paràmetre Graphics on es pinta.
	 */
	@Override
	public void paintComponent( Graphics g )
	{
		g.drawImage( fons, 0, 0, getWidth(), getHeight(), null );
	}
}
