package prop.hex.presentacio;

import java.awt.*;
import javax.swing.*;

public class JPanelImatge extends JPanel
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
