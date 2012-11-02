package Domini;

public class Jugador
{

	private Integer fichas_puestas;
	private Integer pistas_usadas;
	private Float tiempo;

	public Jugador()
	{
		fichas_puestas = new Integer( 0 );
		pistas_usadas = new Integer( 0 );
		tiempo = new Float( 0.0 );
	}

	public Integer getFichasPuestas()
	{
		return fichas_puestas;
	}

	public Integer getPistasUsadas()
	{
		return pistas_usadas;
	}

	public Float getTiempo()
	{
		return tiempo;
	}

	public void setFichasPuestas( Integer f )
	{
		this.fichas_puestas = f;
	}

	public void setPistasUsadas( Integer p )
	{
		this.pistas_usadas = p;
	}

	public void setTiempo( Float t )
	{
		this.tiempo = t;
	}

	public void incrementarPistas()
	{
		pistas_usadas++;
	}

	public void incrementarFichas()
	{
		fichas_puestas++;
	}
}
