package prop.hex.domini.models;

public class Casella
{

	private int fila;
	private int columna;

	public Casella( int fila, int columna )
	{
		this.fila = fila;
		this.columna = columna;
	}

	public int getFila()
	{
		return fila;
	}

	public void setFila( int fila )
	{
		this.fila = fila;
	}

	public int getColumna()
	{
		return columna;
	}

	public void setColumna( int columna )
	{
		this.columna = columna;
	}

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
}
