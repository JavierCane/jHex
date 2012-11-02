package prop.hex.domini.models;

public class Casella
{

	private Integer fila;
	private Integer columna;

	public Casella( Integer fila, Integer columna )
	{
		this.fila = fila;
		this.columna = columna;
	}

	public Integer getFila()
	{
		return fila;
	}

	public void setFila( Integer fila )
	{
		this.fila = fila;
	}

	public Integer getColumna()
	{
		return columna;
	}

	public void setColumna( Integer columna )
	{
		this.columna = columna;
	}
}
