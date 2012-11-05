package prop.hex.domini.models;

import prop.cluster.domini.models.Partida;
import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.Usuari;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.enums.*;

import java.io.Serializable;
import java.util.Date;

public class PartidaHex extends Partida implements Serializable
{

	private ModesInici mode_inici;
	private CombinacionsColors combinacio_colors;

	public PartidaHex( Usuari jugador_a, Usuari jugador_b, Tauler tauler, Date data_creacio,
	                   String nom, boolean finalitzada, String[] colors, ModesInici mode_inici )
	{
		super( jugador_a, jugador_b, tauler, 0 );

		this.combinacio_colors = combinacio_colors;
		this.mode_inici = mode_inici;
;
	}

	public String toString()
	{
		return super.toString() + " [Mode inici: " + mode_inici +
		       ", combinacio colors:" + combinacio_colors + "]";
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

	/**
	 * Modifica el mode d'inici que té assignat l'usuari.
	 *
	 * @param mode_inici ModeInici que es vol assignar a l'usuari.
	 */
	public boolean setModeInici( ModesInici mode_inici )
	{
		this.mode_inici = mode_inici;
		return true;
	}

	/**
	 * Consulta els colors que té assignat l'usuari.
	 *
	 * @return Un array de dues posicions amb els dos colors de fitxes preferits per l'usuari.
	 */
	public CombinacionsColors getCombinacionsColors()
	{
		return combinacio_colors;
	}

	/**
	 * Modifica un cert color dels que té assignats l'usuari.
	 *
	 * @return Cert si el color es modifica. Fals altrament.
	 */
	public boolean setCombinacionsColors( CombinacionsColors combinacio_colors )
	{
		this.combinacio_colors = combinacio_colors;
		return true;
	}

	public EstatPartida comprovaEstatPartida( int fila, int columna ) throws IndexOutOfBoundsException
	{

		return null;
	}
}

