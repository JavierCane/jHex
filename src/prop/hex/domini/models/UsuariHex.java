package prop.hex.domini.models;

import prop.cluster.domini.models.Usuari;
import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.Dificultats;
import prop.hex.domini.models.enums.ModesInici;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe UsuariHex. S'esté d'Usuari i simplement conté alguns mètodes per el tractament de atributs especifics d'un
 * Usuari del joc Hex.
 */
public final class UsuariHex extends Usuari implements Serializable
{

	/**
	 * ID de serialització
	 */
	private static final long serialVersionUID = -2893672483693919721L;

	/**
	 * Mode d'inici de les partides seleccionat per l'usuari com a preferent
	 */
	private ModesInici mode_inici;

	/**
	 * Combinació de colors de les fitxes de les partides seleccionada per l'usuari com a preferent
	 */
	private CombinacionsColors combinacio_colors;

	/**
	 * Temps mínim en guanyar una partida
	 */
	private Long temps_minim;

	/**
	 * Fitxes mínimes utilitzades per guanyar una partida
	 */
	private Integer fitxes_minimes;

	/**
	 * Nombre de partides disputades
	 */
	private Integer partides_jugades;

	/**
	 * Nombre de partides guanyades
	 */
	private Integer partides_guanyades;

	/**
	 * Puntuació global de l'usuari
	 */
	private Integer puntuacio_global;

	/**
	 * Llista de noms d'usuari no permesos.
	 */
	private static final Set<String> noms_no_permesos =
			Collections.unmodifiableSet( new HashSet<String>( Arrays.asList( new String[] {
					"Maquina_1",
					"Maquina_2",
					"Usuari_1",
					"Usuari_2"
			} ) ) );

	/**
	 * Expressió regular amb els caràcters permesos.
	 */
	private static final String caracters_permesos = "^[A-Za-z0-9_ ]+$";

	/**
	 * Constructora per defecte, tot i que utilitzem la constructora de la classe pare,
	 * es sobreescriu amb la finalitat de poder comprovar que el nom d'usuari no es tracta d'un nom no permès.
	 *
	 * @param nom
	 * @param contrasenya
	 * @throws IllegalArgumentException
	 */
	public UsuariHex( String nom, String contrasenya ) throws IllegalArgumentException
	{
		super( nom, contrasenya, Dificultats.getNumDificultats() );

		mode_inici = ModesInici.ESTANDARD;
		combinacio_colors = CombinacionsColors.VERMELL_BLAU;
		temps_minim = Long.MAX_VALUE;
		fitxes_minimes = Integer.MAX_VALUE;
		partides_jugades = 0;
		partides_guanyades = 0;
		puntuacio_global = 0;
	}

	/**
	 * Consulta l'identificador unic de l'usuari. Utilitzada per guardar a disc el fitxer corresponent de l'usuari.
	 *
	 * @return L'identificador únic de l'usuari.
	 */
	public String getIdentificadorUnic()
	{
		return getNom().replace( ' ', '-' );
	}

	/**
	 * Mètode amb la finalitat de poder depurar el programa amb més facilitat. Simplement retorna tots els atributs
	 * de la classe y els seus corresponents valors.
	 *
	 * @return
	 */
	public String toString()
	{
		return super.toString() + " [Mode inici: " + mode_inici +
		       ", combinacio colors: " + combinacio_colors + ", temps minim: " + temps_minim +
		       ", fitxes minimes: " + fitxes_minimes + ", partides guanyades: " + partides_guanyades +
		       ", partides jugades: " + partides_jugades + ", puntuacio global: " + puntuacio_global + "]";
	}

	/**
	 * Consulta el llistat de noms d'usuari no permesos
	 *
	 * @return El llistat de noms d'usuari no permesos.
	 */
	public static Set<String> getNomsNoPermesos()
	{
		return noms_no_permesos;
	}

	/**
	 * Consulta l'expressió regular de noms permesos.
	 *
	 * @return L'expressió regular de noms permesos.
	 */
	public static String getCaractersPermesos()
	{
		return caracters_permesos;
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

	/**
	 * Consulta el temps mínim en guanyar una partida de l'usuari.
	 *
	 * @return El temps mínim en guanyar una partida de l'usuari.
	 */
	public Long getTempsMinim()
	{
		return temps_minim;
	}

	/**
	 * Modifica el temps mínim en guanyar una partida de l'usuari.
	 *
	 * @param temps_minim Valor de temps que es vol introduir.
	 * @return Cert, si es tracta d'un temps vàlid. Fals altrament.
	 */
	public boolean setTempsMinim( Long temps_minim )
	{
		if ( temps_minim <= 0 || fitxes_minimes > Float.POSITIVE_INFINITY )
		{
			return false;
		}
		else
		{
			this.temps_minim = temps_minim;
			return true;
		}
	}

	/**
	 * Consulta el nombre de fitxes mínim amb què l'usuari ha guanyat una partida.
	 *
	 * @return El nombre de fitxes mínim amb què l'usuari ha guanyat una partida
	 */
	public Integer getFitxesMinimes()
	{
		return fitxes_minimes;
	}

	/**
	 * Modifica el nombre de fitxes mínim amb què l'usuari ha guanyat una partida.
	 *
	 * @param fitxes_minimes Nombre de fitxes que es vol introduir.
	 * @return Cert, si es tracta d'un nombre de fitxes vàlid. Fals altrament.
	 */
	public boolean setFitxesMinimes( Integer fitxes_minimes )
	{
		if ( fitxes_minimes <= 0 || fitxes_minimes > Integer.MAX_VALUE )
		{
			return false;
		}
		else
		{
			this.fitxes_minimes = fitxes_minimes;
			return true;
		}
	}

	/**
	 * Consulta el nombre de partides jugades per l'usuari.
	 *
	 * @return El nombre de partides jugades per l'usuari.
	 */
	public Integer getPartidesJugades()
	{
		return partides_jugades;
	}

	/**
	 * Modifica el nombre de partides jugades per l'usuari.
	 *
	 * @param partides_jugades Nombre de partides jugades que es vol introduir.
	 * @return Cert, si es tracta d'un nombre de partides vàlid. Fals altrament.
	 */
	public boolean setPartidesJugades( Integer partides_jugades )
	{
		if ( partides_jugades < 0 || partides_jugades > Integer.MAX_VALUE )
		{
			return false;
		}
		else
		{
			this.partides_jugades = partides_jugades;
			return true;
		}
	}

	/**
	 * Consulta el nombre de partides guanyades per l'usuari.
	 *
	 * @return El nombre de partides guanyades per l'usuari.
	 */
	public Integer getPartidesGuanyades()
	{
		return partides_guanyades;
	}

	/**
	 * Modifica el nombre de partides guanyades per l'usuari.
	 *
	 * @param partides_guanyades Nombre de partides guanyades que es vol introduir.
	 * @return Cert, si es tracta d'un nombre de partides vàlid. Fals altrament.
	 */
	public boolean setPartidesGuanyades( Integer partides_guanyades )
	{
		if ( partides_jugades < 0 || partides_guanyades > Integer.MAX_VALUE )
		{
			return false;
		}
		else
		{
			this.partides_guanyades = partides_guanyades;
			return true;
		}
	}

	/**
	 * Consulta la puntuació global de l'usuari.
	 *
	 * @return
	 */
	public Integer getPuntuacioGlobal()
	{
		return puntuacio_global;
	}

	/**
	 * Recalcula la puntuació global d'un jugador tenint en compte el nombre de victories i derrotes en funció de la
	 * dificultat de l'usuari/IA contrari y els punts que orotga/resta aquest tipus de resultat.
	 */
	public void recalculaPuntuacioGlobal()
	{
		Integer sum_victories = 0;
		Integer sum_derrotes = 0;

		for ( Dificultats dificultat : Dificultats.values() )
		{
			sum_victories += num_victories[dificultat.getPosicioDificultat()] * dificultat.getPuntsPerGuanyar();
			sum_derrotes += num_derrotes[dificultat.getPosicioDificultat()] * dificultat.getPuntsPerPerdre();
		}

		if ( sum_victories - sum_derrotes < Integer.MIN_VALUE )
		{
			puntuacio_global = Integer.MIN_VALUE;
		}
		else if ( sum_victories - sum_derrotes > Integer.MAX_VALUE )
		{
			puntuacio_global = Integer.MAX_VALUE;
		}
		else
		{
			puntuacio_global = sum_victories - sum_derrotes;
		}
	}
}
