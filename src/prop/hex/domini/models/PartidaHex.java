package prop.hex.domini.models;

import prop.cluster.domini.models.Partida;
import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.Usuari;
import prop.cluster.domini.models.estats.EstatPartida;

import java.io.Serializable;
import java.util.Date;

public class PartidaHex extends Partida implements Serializable
{

	private String[] colors;
	private ModesInici mode_inici;


	public PartidaHex(Usuari jugador_a, Usuari jugador_b, Tauler tauler, int torns_jugats, Date data_creacio, String nom, boolean finalitzada, String[] colors, ModesInici mode_inici)
	{
		this.colors = colors;
		this.mode_inici = mode_inici;
	}

	/**
	 * Consulta el color demanat d'entre aquells que té assignats la partida.
	 *
	 * @param numero Índex del color que volem obtenir.
	 * @return Un String amb el nom del color demanat.
	 * @throws IndexOutOfBoundsException si el número de color és més gran que 1, ja que només hi ha dos colors.
	 */
	public String getColors( int numero ) throws IndexOutOfBoundsException
	{
		if ( numero > 1 )
		{
			throw new IndexOutOfBoundsException( "Índex de color massa gran" );
		}
		return colors[numero];
	}

	/**
	 * Modifica un cert color dels que té assignats la partida.
	 *
	 * @param numero Índex del color que volem obtenir.
	 * @param color  Un String amb el nom del color demanat.
	 * @return Cert si el color es modifica. Fals altrament.
	 * @throws IndexOutOfBoundsException si el número de color és més gran que 1, ja que només hi ha dos colors.
	 * @throws IllegalArgumentException  si el nom del color no és vàlid.
	 */
	public boolean setColors( int numero, String color ) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if ( numero > 1 )
		{
			throw new IndexOutOfBoundsException( "Índex de color massa alt" );
		}
		// Ejemplo
		else if ( color != "blau" || color != "vermell" )
		{
			throw new IllegalArgumentException( "Nom de color no vàlid" );
		} this.colors[numero] = color;
		return true;
	}

	/* Again, es necesario?
	public boolean setColors( String[] colors )
	{
		this.colors = colors;
	}
	*/

	/**
	 * Modifica el mode d'inici que té assignat l'usuari.
	 *
	 * @param mode ModeInici que es vol assignar a l'usuari.
	 */
	public void setModeInici( ModesInici mode )
	{
		this.mode_inici = mode;
	}

	/**
	 * Consulta el mode d'inici de la partida que té assignat l'usuari.
	 *
	 * @return Un ModeInici corresponent al mode d'inici preferit per l'usuari.
	 */
	public ModesInici getModeInici()
	{
		return mode_inici;
	}

	public EstatPartida comprovaEstatPartida( int fila, int columna ) throws IndexOutOfBoundsException
	{

		return null;
	}
}

