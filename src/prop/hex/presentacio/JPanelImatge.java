package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;

public final class JPanelImatge extends JPanel
{

	private Image fons;

	public JPanelImatge( String img )
	{
		fons = ( new ImageIcon( img ) ).getImage();
	}

	public void paintComponent( Graphics g )
	{
		g.drawImage( fons, 0, 0, getWidth(), getHeight(), null );
	}
}
