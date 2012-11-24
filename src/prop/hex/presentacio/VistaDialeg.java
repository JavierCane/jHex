package prop.hex.presentacio;

import javax.swing.*;

public class VistaDialeg
{

	public String setDialeg( String titol, String text, String[] botons, int tipus )
	{
		int tipus_opcio = 1;
		switch ( tipus )
		{
			case 0:
				tipus_opcio = JOptionPane.ERROR_MESSAGE;
				break;
			case 1:
				tipus_opcio = JOptionPane.INFORMATION_MESSAGE;
				break;
			case 2:
				tipus_opcio = JOptionPane.WARNING_MESSAGE;
				break;
			case 3:
				tipus_opcio = JOptionPane.QUESTION_MESSAGE;
				break;
			case 4:
				tipus_opcio = JOptionPane.PLAIN_MESSAGE;
				break;
		}

		JOptionPane opcions = new JOptionPane( text, tipus_opcio );
		opcions.setOptions( botons );
		JDialog dialeg = opcions.createDialog( new JFrame(), titol );
		dialeg.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		dialeg.pack();
		dialeg.setVisible( true );

		return ( String ) opcions.getValue();
	}
}
