package prop.hex.domini.controladors.IA.auxiliars;

/**
 * Tipus de fites per a les podes en un algorisme amb taules de transposicions.
 *
 * @author Isaac Sánchez Barrera
 */
public enum FitesDePoda
{
	/**
	 * La puntuació és un valor màxim
	 */
	FITA_SUPERIOR,

	/**
	 * La puntuació és un valor mínim
	 */
	FITA_INFERIOR,

	/**
	 * La puntuació s'ha calculat de manera exacta
	 */
	VALOR_EXACTE;
}
