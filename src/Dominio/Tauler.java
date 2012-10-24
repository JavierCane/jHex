package Dominio;

import com.sun.javaws.exceptions.InvalidArgumentException;

public class Tauler
{

	private EstatCasella[][] tauler;
	private Integer mida;
	private Integer fitxes_a;
	private Integer fitxes_b;

	/**
	 * Constructor del tauler
	 *
	 * @param mida Les dimensions que tindrà el tauler
	 */
	public Tauler( Integer mida )
	{
		this.mida = mida;
		tauler = new EstatCasella[mida][mida];
		fitxes_a = new Integer( 0 );
		fitxes_b = new Integer( 0 );
	}

	/**
	 * Consulta el tamaño del tauler
	 *
	 * @return El tamaño del tauler
	 */
	public Integer getMida()
	{
		return mida;
	}

	/**
	 * Coloca una ficha del jugador en la posición (fila, columna)
	 *
	 * @param e       Estat nou de la casella
	 * @param fila    Fila del tauler donde se coloca la ficha
	 * @param columna Columna del tauler donde se coloca la ficha
	 */
	public boolean canviaEstatCasella( EstatCasella e, Integer fila, Integer columna )
	{
		if ( fila < 0 || fila >= mida || columna < 0 || columna >= mida )
		{
			return false;
		}

		if ( )

		{
			tauler[fila][columna] = e;
		}

		return true;
	}

	public boolean intercanviaFitxa(Integer fila, Integer columna)
	{
		if ( fila < 0 || fila >= mida || columna < 0 || columna >= mida )
		{
			return false;
		}


	}

	/**
	 * Consulta la el número del jugador que hay en la casilla (fila, columna)
	 *
	 * @param fila    Fila donde está la casilla que se quiere consultar
	 * @param columna Columna donde está la casilla que se quiere consultar
	 * @return El número del jugador que hay en la casilla, null si no hay.
	 */
	public EstatCasella getNumJugadorCasilla( Integer fila, Integer columna )
	{
		return tauler[fila][columna];
	}
}
