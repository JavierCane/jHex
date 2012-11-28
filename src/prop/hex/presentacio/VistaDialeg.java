package prop.hex.presentacio;

import javax.swing.*;

public class VistaDialeg
{

	public String setDialeg( String titol, String text, String[] botons, int tipus_joptionpane )
	{
		JOptionPane opcions = new JOptionPane( text, tipus_joptionpane );
		opcions.setOptions( botons );
		
		JDialog dialeg = opcions.createDialog( new JFrame(), titol );
		dialeg.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		dialeg.pack();
		dialeg.setVisible( true );

		return ( String ) opcions.getValue();
	}
}
