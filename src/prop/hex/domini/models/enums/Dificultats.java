package prop.hex.domini.models.enums;

/**
 * Enum amb les distintes dificultats del joc.
 * La gràcia d'aquest enum es que es dinàmic i els paràmetres de cada una de les dificultats només figuren aquí,
 * fent així que el fet de modificar aquestes (afegint o cambiant el nombre de dificultats,
 * o modificant la cuantitat de punts que otorgar/restar) numés impliqui editar aquest fitxer.
 * <p/>
 * L'ordre dels valors de l'array de paràmetres de cada una de les dificultats (valors de l'enum) es important y ha de
 * ser el seguent:
 * Posició de la dificultat a l'array de nombres de victories/derrotes d'un Usuari,
 * Punts que otorga el guanyar una partida contra aquest tipus d'usuari/dificultat d'IA,
 * Punts que resta el perdre una partida contra aquest tipus d'usuari/dificultat d'IA
 */
public enum Dificultats
{
	JUGADOR( new int[] {
			0,
			5,
			3
	} ),
	IA_FACIL( new int[] {
			1,
			10,
			2
	} ),
	IA_DIFICIL( new int[] {
			2,
			15,
			1
	} );

	/**
	 * Nombre total de dificultats del joc
	 */
	private static final int num_dificultats = Dificultats.values().length;

	/**
	 * Posició de la dificultat a l'array de nombres de victories/derrotes d'un Usuari
	 */
	private int posicio_dificultat;

	/**
	 * Punts que otorga el guanyar una partida contra aquest tipus d'usuari/dificultat d'IA
	 */
	private int punts_per_guanyar;

	/**
	 * Punts que resta el perdre una partida contra aquest tipus d'usuari/dificultat d'IA
	 */
	private int punts_per_perdre;

	/**
	 * Constructora de l'enum, simplement estableix el valor de cada un dels atributs privats per després poder-los
	 * obtenir en base al valor de l'enum seleccionat
	 *
	 * @param parametres
	 */
	Dificultats( int[] parametres )
	{
		posicio_dificultat = parametres[0];
		punts_per_guanyar = parametres[1];
		punts_per_perdre = parametres[2];
	}

	/**
	 * Mètode públic per poder obtenir el nombre de dificultats del joc
	 *
	 * @return
	 */
	public static int getNumDificultats()
	{
		return num_dificultats;
	}

	/**
	 * Mètode públic per poder obtenir la posició de la dificultat a l'array de nombres de victories/derrotes d'un Usuari
	 *
	 * @return
	 */
	public int getPosicioDificultat()
	{
		return posicio_dificultat;
	}

	/**
	 * Mètode públic per poder obtenir el nombre de punts que otorga el guanyar una partida contra aquest tipus
	 * d'usuari/dificultat d'IA
	 *
	 * @return
	 */
	public int getPuntsPerGuanyar()
	{
		return punts_per_guanyar;
	}

	/**
	 * Mètode públic per poder obtenir el nombre de punts que resta el perdre una partida contra aquest tipus
	 * d'usuari/dificultat d'IA
	 *
	 * @return
	 */
	public int getPuntsPerPerdre()
	{
		return punts_per_perdre;
	}
}
