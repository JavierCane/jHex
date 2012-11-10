package prop.hex.domini.models;

public class Casella
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
		if ( o == null )
		{
			return false;
		}

		if ( o == this )
		{
			return true;
		}

		if ( !( o instanceof Casella ) )
		{
			return false;
		}

		Casella c = ( Casella ) o;
		if ( c.getFila() == fila && c.getColumna() == columna )
		{
			return true;
		}
		return false;
	}

	/**
	 * Crea un String amb tota la informació de la casella
	 *
	 * @return El String amb la informació completa de la casella.
	 */
	public String toString()
	{
		return "[Fila: " + fila + ", columna: " + columna + " ]";
	}
}
