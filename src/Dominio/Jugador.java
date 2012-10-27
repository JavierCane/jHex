package Dominio;

public class Jugador
{

	private Integer pistes_usades;
	private Float temps;

	public Jugador()
	{
		pistes_usades = new Integer( 0 );
		temps = new Float( 0.0 );
	}

	public Integer getPistesUsades()
	{
		return pistes_usades;
	}

	public Float getTemps()
	{
		return temps;
	}

	public void setPistesUsades( Integer p )
	{
		this.pistes_usades = p;
	}

	public void setTemps( Float t )
	{
		this.temps = t;
	}

	public void incrementaPistes()
	{
		pistes_usades++;
	}

}
