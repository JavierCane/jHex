package prop.hex.domini.models;

/**
 * Representa una casella del tauler, guarda al fila i columna i implementa equals per a poder usar
 * algunes estructures de dades i fer cerques.
 */
public class Casella implements Comparable<Casella>
{

	/**
	 * Fila de la casella
	 */
	private int fila;
	/**
	 * Columna de la casella
	 */
	private int columna;

	/**
	 * Constructor de la casella
	 *
	 * @param fila    Fila de la casella
	 * @param columna Columna de la casella
	 */
	public Casella( int fila, int columna )
	{
		this.fila = fila;
		this.columna = columna;
	}

	/**
	 * Consulta la fila de la casella
	 *
	 * @return La fila de la casella
	 */
	public int getFila()
	{
		return fila;
	}

	/**
	 * Canvia la fila de la casella
	 *
	 * @param fila
	 * @return Cert si la fila s'ha canviat. Fals altrament.
	 */
	public boolean setFila( int fila )
	{
		this.fila = fila;
		return true;
	}

	/**
	 * Consulta la columna de la casella
	 *
	 * @return La columna de la casella
	 */
	public int getColumna()
	{
		return columna;
	}

	/**
	 * Canvia la columna de la casella
	 *
	 * @param columna
	 * @return Cert si la columna s'ha canviat. Fals altrament.
	 */
	public boolean setColumna( int columna )
	{
		this.columna = columna;
		return true;
	}

	/**
	 * Compara dues caselles
	 *
	 * @param o Object de tipus Casella a comparar
	 * @return Cert si la casella representa la mateixa posició. Fals altrament.
	 */
	public boolean equals( Object o )
	{
		// Si l'objecte es null retornem false directament.
		if ( o == null )
		{
			return false;
		}
		// Si és ell mateix retornem true (reciprocitat).
		if ( o == this )
		{
			return true;
		}
		// Si no és una casella, retornem false.
		if ( !( o instanceof Casella ) )
		{
			return false;
		}
		//Un cop descartats tots els casos anteriors, calculem el resultat d'equals.
		Casella c = ( Casella ) o;
		if ( c.getFila() == fila && c.getColumna() == columna )
		{
			return true;
		}
		return false;
	}

	public int compareTo( Casella casella )
	{
		if ( fila < casella.fila )
		{
			return -1;
		}
		else if ( fila > casella.fila )
		{
			return 1;
		}
		else if ( columna < casella.columna )
		{
			return -1;
		}
		else if ( columna > casella.columna )
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Crea un String amb tota la informació de la casella
	 *
	 * @return El String amb la informació completa de la casella.
	 */
	@Override
	public String toString()
	{
		return "[Fila: " + fila + ", columna: " + columna + " ]";
	}
}
