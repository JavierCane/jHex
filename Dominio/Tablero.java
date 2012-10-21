package Dominio;

public class Tablero
{

	private Integer[][] tablero;
	private Integer tamano;

	/**
	 * Constructor del tablero
	 *
	 * @param tamano Las dimensiones que tendrá el tablero
	 */
	public Tablero( Integer tamano )
	{
		this.tamano = tamano;
		tablero = new Integer[tamano][tamano];
	}

	/**
	 * Consulta el tamaño del tablero
	 *
	 * @return El tamaño del tablero
	 */
	public Integer getTamano()
	{
		return tamano;
	}

	/**
	 * Coloca una ficha del jugador en la posición (fila, columna)
	 *
	 * @param jugador Número del jugador que coloca la ficha
	 * @param fila    Fila del tablero donde se coloca la ficha
	 * @param columna Columna del tablero donde se coloca la ficha
	 */
	public void colocarFicha( Integer jugador, Integer fila, Integer columna )
	{
		tablero[fila][columna] = jugador;
	}

	/**
	 * Consulta la el número del jugador que hay en la casilla (fila, columna)
	 *
	 * @param fila    Fila donde está la casilla que se quiere consultar
	 * @param columna Columna donde está la casilla que se quiere consultar
	 * @return El número del jugador que hay en la casilla, null si no hay.
	 */
	public Integer getNumJugadorCasilla( Integer fila, Integer columna )
	{
		return tablero[fila][columna];
	}
}
