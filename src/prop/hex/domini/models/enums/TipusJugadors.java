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
 * Punts que resta el perdre una partida contra aquest tipus d'usuari/dificultat d'IA,
 * Nom de la classe controladora corresponent a la dificultat, utilitzada per saber quina funció de moviment ,
 * Nom de l'usuari a crear amb aquesta dificultat.
 */
public enum TipusJugadors
{
	JUGADOR( 0, 5, 3, "IAHexFacilCtrl" ),
	IA_FACIL( 1, 10, 2, "IAHexFacilCtrl", "Maquina facil" ),
	IA_DIFICIL( 2, 15, 1, "IAHexDificilCtrl", "Maquina dificil" );

	/**
	 * Nombre total de dificultats del joc.
	 */
	private static final int num_dificultats = TipusJugadors.values().length;

	/**
	 * Posició de la dificultat a l'array de nombres de victories/derrotes d'un Usuari.
	 */
	private int posicio_dificultat;

	/**
	 * Punts que otorga el guanyar una partida contra aquest tipus d'usuari/dificultat d'IA.
	 */
	private int punts_per_guanyar;

	/**
	 * Punts que resta el perdre una partida contra aquest tipus d'usuari/dificultat d'IA.
	 */
	private int punts_per_perdre;

	/**
	 * Nom de la classe corresponent a la dificultat, utilitzada per carregar una funció de moviment o una altra. Si
	 * és un usuari humà, conté el nom de la classe d'intel·ligència artificial usada per donar les pistes.
	 */
	private String classe_corresponent;

	/**
	 * Nom de l'usuari a crear amb aquesta dificultat. És un String buit si és un usuari humà (JUGADOR).
	 */
	private String nom_usuari;

	/**
	 * Constructora de l'enum, simplement estableix el valor de cada un dels atributs privats per després poder-los
	 * obtenir en base al valor de l'enum seleccionat
	 *
	 * @param posicio_dificultat
	 * @param punts_per_guanyar
	 * @param punts_per_perdre
	 */
	TipusJugadors( int posicio_dificultat, int punts_per_guanyar, int punts_per_perdre, String classe_corresponent )
	{
		this.posicio_dificultat = posicio_dificultat;
		this.punts_per_guanyar = punts_per_guanyar;
		this.punts_per_perdre = punts_per_perdre;
		this.classe_corresponent = classe_corresponent;
		nom_usuari = null;
	}

	/**
	 * Constructora de l'enum, simplement estableix el valor de cada un dels atributs privats per després poder-los
	 * obtenir en base al valor de l'enum seleccionat
	 *
	 * @param posicio_dificultat
	 * @param punts_per_guanyar
	 * @param punts_per_perdre
	 * @param classe_corresponent
	 * @param nom_usuari
	 */
	TipusJugadors( int posicio_dificultat, int punts_per_guanyar, int punts_per_perdre, String classe_corresponent,
	               String nom_usuari )
	{
		this.posicio_dificultat = posicio_dificultat;
		this.punts_per_guanyar = punts_per_guanyar;
		this.punts_per_perdre = punts_per_perdre;
		this.classe_corresponent = classe_corresponent;
		this.nom_usuari = nom_usuari;
	}

	/**
	 * Mètode públic per poder obtenir el nombre de dificultats del joc
	 *
	 * @return El total nombre de dificultats que té el joc.
	 */
	public static int getNumDificultats()
	{
		return num_dificultats;
	}

	/**
	 * Mètode públic per poder obtenir la posició de la dificultat a l'array de nombres de victories/derrotes d'un
	 * Usuari
	 *
	 * @return La posició en l'array de victories/derrotes de la dificultat.
	 */
	public int getPosicioDificultat()
	{
		return posicio_dificultat;
	}

	/**
	 * Mètode públic per poder obtenir el nombre de punts que s'atorga quan es una partida contra aquest tipus
	 * d'usuari/dificultat d'IA
	 *
	 * @return El nombre de punts que s'atorga quan es guanya contra aquest tipus d'usuari.
	 */
	public int getPuntsPerGuanyar()
	{
		return punts_per_guanyar;
	}

	/**
	 * Mètode públic per poder obtenir el nombre de punts que es penalitzen quan es perd una partida contra aquest
	 * tipus d'usuari/dificultat d'IA
	 *
	 * @return El nombre de punts que es penalitzen quan es perd contra aquest tipus d'usuari.
	 */
	public int getPuntsPerPerdre()
	{
		return punts_per_perdre;
	}

	/**
	 * Mètode públic per poder obtenir el nom de la classe d'inteligencia artificial corresponent al nivell de
	 * dificultat del valor de l'enum
	 *
	 * @return El nom de la classe
	 */
	public String getClasseCorresponent()
	{
		return classe_corresponent;
	}

	/**
	 * Mètode públic per poder obtenir el nom de l'usuari a crear amb aquesta dificultat.
	 *
	 * @return El nom per defecte de l'usuari
	 */
	public String getNomUsuari()
	{
		return nom_usuari;
	}
}
