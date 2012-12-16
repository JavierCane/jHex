package prop.hex.presentacio.auxiliars;

/**
 * Model de taula no editable (hereta de ModelTaula) i ordenable amb tipus de columnes corresponents a:
 * String | String | String
 *
 * @author Javier Ferrer Gonzalez (Grup 7.3, Hex)
 */
public final class ModelTaulaPartides extends ModelTaula
{

	/**
	 * Constructor de la taula a partir de les seves dades.
	 *
	 * @param dades         Dades de la taula.
	 * @param noms_columnes Noms de les columnes de la taula.
	 */
	public ModelTaulaPartides( Object[][] dades, String[] noms_columnes )
	{
		super( dades, noms_columnes );
	}

	/**
	 * Especifica el tipus de dades que contè cada columna per poder-les ordenar
	 *
	 * @param columna nombre de columna
	 * @return el la classe del tipus de dades que correspon al número de columna donat
	 */
	@Override
	public Class<?> getColumnClass( int columna )
	{
		switch ( columna )
		{
			case 0: // Nom de la partida
				return String.class;
			case 1: // Nom de l'oponent
				return String.class;
			case 2: // Data de la partida (com està formatejada, no reconeix que es de tipus Date)
				return String.class;
			default:
				return String.class;
		}
	}
}
