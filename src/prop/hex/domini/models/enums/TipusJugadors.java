package prop.hex.domini.models.enums;

import java.util.*;

/**
 * Enum amb les distintes dificultats del joc.
 * La gràcia d'aquest enum es que es dinàmic i els paràmetres de cada una de les dificultats només figuren aquí,
 * fent així que el fet de modificar aquestes (afegint o cambiant el nombre de dificultats,
 * o modificant la cuantitat de punts que otorgar/restar) numés impliqui editar aquest fitxer.
 * No es poden modificar les primeres dues posicions (Convidat i Jugador)
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
	CONVIDAT( 0, 0, 0, "IAHexSexSearch" ),
	JUGADOR( 1, 5, 4, "IAHexSexSearch" ),
	IA_FACIL( 2, 10, 3, "IAHexFacilCtrl", "Jose Antonio Camacho (MiniMax, nivell 0)" ),
	IA_MONTESCOUT( 3, 15, 2, "IAHexNegaMonteScout", "Bill Clinton (MonteScout, nivell 1)" ),
	IA_QUEENBEE( 4, 15, 2, "IAHexQueenBeeCtrl", "Hillary Clinton (QueenBee, nivell 1.5)" ),
	IA_SEXQUEENBEE( 5, 20, 1, "IAHexSexSearch", "Monica Lewinsky (SexQueenBee, nivell 2)" );

	/**
	 * Nombre total de dificultats del joc.
	 */
	private static final int num_dificultats = values().length;

	/**
	 * Llista no modificable dels possibles valors de l'enum
	 */
	private static final List<TipusJugadors> tipus_jugadors = Collections.unmodifiableList( Arrays.asList( values() ) );

	private static final Vector<TipusJugadors> jugadors_maquina = new Vector<TipusJugadors>();

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
	 * Mètode per obtenir la versió de text del valor de l'enum.
	 * Usada als JComboBox de les vistes per mostrar la traducció corresponent de cada valor de l'enum al seu nom
	 *
	 * @return String valor de la variable "nom_usuari" de l'enum (corresponent a la cinquena posició de la
	 *         configuració)
	 */
	@Override
	public String toString()
	{
		return nom_usuari;
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

	/**
	 * Mètode públic per poder establir el nom de l'usuari.
	 * Usat quan es creen usuaris de tipus convidat i jugador.
	 *
	 * @return true
	/**
	 * Obté un tipus de jugador de forma aleatòria garantitzant que serà de tipus màquina.
	 * Funció útil per jugar contra una màquina desconeguda.
	 *
	 * @return TipusJugadors Jugador de tipus màquina aleatori
	 */
	public static TipusJugadors getMaquinaAleatoria()
	{
		Random num_aleatori = new Random();

		return tipus_jugadors.get( num_aleatori.nextInt( num_dificultats - 2 ) + 2 );
	}

	/**
	 * Mètode que retorna un vector de tipus de jugadors de tipus màquines.
	 * Afegeix també una màquina aleatòria.
	 * Usada per elavorar els JComboBox de les vistes
	 *
	 * @return Vector<TipusJugadors> TipusJugadors de tipus màquina
	 */
	public static Vector<TipusJugadors> obteLlistatMaquines()
	{
		if ( jugadors_maquina.isEmpty() )
		{
			// Afegeixo tots els tipus de jugadors que siguin màquines (tingin nom de màquina)
			for ( TipusJugadors tipus_jugador : values() )
			{
				if ( tipus_jugador.posicio_dificultat != 0 && tipus_jugador.posicio_dificultat != 1 )
				{
					jugadors_maquina.add( tipus_jugador );
				}
			}
		}

		return jugadors_maquina;
	}
}
