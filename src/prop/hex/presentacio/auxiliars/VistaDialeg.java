package prop.hex.presentacio.auxiliars;

import javax.swing.*;

/**
 * Vista de diàleg del joc Hex.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */
public final class VistaDialeg
{

	/**
	 * Donat el contingut desitjat per a un diàleg, obre el diàleg corresponent i retorna la opció seleccionada.
	 *
	 * @param titol             Títol del diàleg.
	 * @param text              Text del diàleg.
	 * @param botons            Botons que contindrà el diàleg.
	 * @param tipus_joptionpane Tipus de diàleg.
	 * @return El text del botó seleccionat.
	 */
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
