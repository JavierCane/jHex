package prop.hex.presentacio.auxiliars;

import javax.swing.table.DefaultTableModel;

/**
 * Model bàsic d'una taula no editable.
 *
 * @author Isaac Sánchez Barrera (Grup 7.3, Hex)
 */
public final class ModelTaula extends DefaultTableModel
{

	/**
	 * Constructor de la taula a partir de les seves dades.
	 *
	 * @param dades         Dades de la taula.
	 * @param noms_columnes Noms de les columnes de la taula.
	 */
	public ModelTaula( Object[][] dades, String[] noms_columnes )
	{
		super( dades, noms_columnes );
	}

	/**
	 * Indica si una certa cel·la d'una taula és editable.
	 *
	 * @param fila    Fila de la cel·la que es vol consultar.
	 * @param columna Columna de la cel·la que es vol consultar.
	 * @return Fals, independentment de la cel·la, ja que volem una taula no editable.
	 */
	public boolean isCellEditable( int fila, int columna )
	{
		return false;
	}
}
