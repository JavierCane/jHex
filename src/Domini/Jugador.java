package Domini;

public class Jugador
{

	private int pistes_usades;
	private float temps;

	/**
	 * Constructor del jugador. Crea un jugador inicialitzant les pistes utilitzades i el temps gastat a 0.
	 */
	public Jugador()
	{
		pistes_usades = 0;
		temps = 0;
	}

	/**
	 * Consulta el nombre de pistes usades pel jugador.
	 *
	 * @return El nombre de pistes usades pel jugador.
	 */
	public int getPistesUsades()
	{
		return pistes_usades;
	}

	/**
	 * Consulta el temps que ha estat jugant el jugador.
	 *
	 * @return El temps que ha estat jugant el jugador.
	 */
	public float getTemps()
	{
		return temps;
	}

	/**
	 * Modifica el nombre de pistes usades pel jugador.
	 *
	 * @param num_pistes Nombre de pistes que es vol introduir.
	 * @return Cert, si és un nombre de pistes vàlid. Fals altrament.
	 */
	public boolean setPistesUsades( int num_pistes )
	{
		if ( num_pistes < 0 )
		{
			return false;
		}
		this.pistes_usades = num_pistes;
		return true;
	}

	/**
	 * Modifica el temps que ha estat jugant el jugador.
	 *
	 * @param temps Temps de joc que es vol introduir.
	 * @return Cert, si és un temps vàlid. Fals altrament.
	 */
	public boolean setTemps( float temps )
	{
		if (temps < 0) return false;
		this.temps = temps;
		return true;
	}

	/**
	 * Incrementa en 1 el nombre de pistes utilitzades pel jugador.
	 */
	public void incrementaPistes()
	{
		pistes_usades++;
	}

}
