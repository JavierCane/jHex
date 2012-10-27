package Dominio;

import java.util.Date;

// Hay que poner extends Partida y renombrar a PartidaHex
public class Partida
{


	private String[] colors;
	private ModesInici mode_inici;


	public Partida()
	{
		// A hacer
	}

	public String getColors( Integer c )
	{
		return colors[c];
	}

	public void setColors( Integer c, String color )
	{
		this.colors[c] = color;
	}

	public void setColors( String[] colors )
	{
		this.colors = colors;
	}

	/* Cuando tengamos la clase ya lo haremos bien
	public void incrementaTornsTotals()
	{
		torns_totals++;
	}

	public Jugador getJugadorCasilla( Integer fila, Integer columna )
	{
		Integer res = tablero.getNumJugadorCasilla( fila, columna );
		return ( res != null ? jugadores[res] : null );
	}*/

	public void setModeInici( ModesInici mode_inici )
	{
		this.mode_inici = mode_inici;
	}

	public ModesInici getModeInici()
	{
		return mode_inici;
	}

	public void intercambiaFitxes()
	{
		//A hacer
	}
}
