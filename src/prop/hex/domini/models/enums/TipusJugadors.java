package prop.hex.domini.models.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Enum amb les distintes dificultats i tipus de jugadors del joc.
 * La gràcia d'aquest enum es que aconsegueix abstraure i encapsular en un únic lloc tot el referent a les diferents
 * dificultats del joc, fent així que el fet de modificar aquestes (afegint o cambiant el nombre de dificultats,
 * o modificant la cuantitat de punts que otorgar/restar quan es guanya/perd) només impliqui editar aquest fitxer.
 * No es poden modificar les primeres dues posicions (Convidat i Jugador)
 * <p/>
 * L'ordre dels valors de l'array de paràmetres de cada una de les dificultats (valors de l'enum) es important y ha de
 * ser el seguent:
 * Posició de la dificultat a l'array de nombres de victories/derrotes d'un Usuari, ha de ser incremental,
 * Punts que otorga el guanyar una partida contra aquest tipus d'usuari/dificultat d'IA,
 * Punts que resta el perdre una partida contra aquest tipus d'usuari/dificultat d'IA,
 * Nom de la classe controladora corresponent a la dificultat, utilitzada per saber quina funció de moviment ,
 * Nom de l'usuari a crear amb aquesta dificultat.
 *
 * @author Javier Ferrer Gonzalez (Grup 7.3, Hex)
 */
public enum TipusJugadors
{
	CONVIDAT( 0, 0, 0, "IAHexQueenBeeCtrl" ),
	JUGADOR( 1, 5, 4, "IAHexQueenBeeCtrl" ),
	IA_ALEATORIA( 2, "Mr. X (Algorisme aleatori, nivell X)" ),
	IA_FACIL( 3, 10, 3, "IAMiniMaxCtrl", "Jose Antonio Camacho (MiniMax, nivell 0)" ),
	IA_POTENCIAL( 4, 10, 3, "IAHexPotencialsCtrl", "Louis van Gaal (Potencials, nivell 0.5)" ),
	IA_MONTESCOUT( 5, 15, 2, "IAHexNegaMonteScoutCtrl", "Bill Clinton (MonteScout, nivell 1)" ),
	IA_QUEENBEE( 6, 15, 2, "IAHexQueenBeeCtrl", "Hillary Clinton (QueenBee, nivell 1.5)" ),
	IA_SEXQUEENBEE( 7, 20, 1, "IAHexSexSearchCtrl", "Monica Lewinsky (SexQueenBee, nivell 2)" );

	/**
	 * Nombre total de dificultats del joc.
	 */
	private static final int num_dificultats = values().length;

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
	 * obtenir en base al valor de l'enum seleccionat. Utilitzada per inicialitzar els tipus de jugadors usuaris no IA.
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
	 * obtenir en base al valor de l'enum seleccionat. Utilitzada per inicialitzar les distintes IAs conegudes.
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
	 * Constructora de l'enum, simplement estableix el valor de cada un dels atributs privats per després poder-los
	 * obtenir en base al valor de l'enum seleccionat. Utilitzada per inicialitzar la IA aleatòria.
	 *
	 * @param posicio_dificultat
	 * @param nom_usuari
	 */
	TipusJugadors( int posicio_dificultat, String nom_usuari )
	{
		this.posicio_dificultat = posicio_dificultat;
		this.nom_usuari = nom_usuari;
		// Els altres paràmetres s'inicialitzaràn mitjançant la funció inicialitzaMaquinaAleatoria (explicació allà).
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
	 * Obté un tipus de jugador de forma aleatòria garantitzant que serà de tipus màquina.
	 * Funció útil per jugar contra una màquina desconeguda.
	 *
	 * @return TipusJugadors Jugador de tipus màquina aleatori
	 */
	private static TipusJugadors inicialitzaMaquinaAleatoria()
	{
		// Primer obtinc un tipus d'usuari de la llista de forma aleatòria sense tenir en compte els 3 primers
		List<TipusJugadors> llista_tipus_jugadors = Arrays.asList( values() );
		Random num_aleatori = new Random();
		TipusJugadors jugador_base = llista_tipus_jugadors.get( num_aleatori.nextInt( num_dificultats - 3 ) + 3 );

		// Un cop tinc la IA que faré servir debase, defineixo els mateixos paràmetres per la IA aleatòria.
		// Podría retornar directament la IA obtinguda de forma aleatòria sense crear una nova posició de l'enum,
		// però llavors estaria utilitzant el mateix objecte, i això implica que si modifiqués el nom pero
		// l'aleatori, també el modificaria per la IA que faci de base.

		IA_ALEATORIA.punts_per_guanyar = jugador_base.punts_per_guanyar;
		IA_ALEATORIA.punts_per_perdre = jugador_base.punts_per_perdre;
		IA_ALEATORIA.classe_corresponent = jugador_base.classe_corresponent;

		return IA_ALEATORIA;
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
		final Vector<TipusJugadors> jugadors_maquina = new Vector<TipusJugadors>();

		// Afegeixo tots els tipus de jugadors que siguin màquines no aleatories
		for ( TipusJugadors tipus_jugador : values() )
		{
			if ( tipus_jugador.posicio_dificultat > 2 )
			{
				jugadors_maquina.add( tipus_jugador );
			}
		}

		jugadors_maquina.add( inicialitzaMaquinaAleatoria() );

		return jugadors_maquina;
	}
}
