package prop.cluster.domini.models.estats;

/**
 * Representa l'estat d'una partida
 *
 * @author Mauricio Ignacio Contreras Pinilla
 */
public enum EstatPartida
{
	/**
	 * Representa l'estat on guanya el jugador A
	 */
	GUANYA_JUGADOR_A,
	/**
	 * Representa l'estat on guanaya el jugador B
	 */
	GUANYA_JUGADOR_B,
	/**
	 * Representa l'estat on la partida finalitza en empat
	 */
	EMPAT,
	/**
	 * Representa l'estat on una partida no ha finalitzat
	 */
	NO_FINALITZADA
};
