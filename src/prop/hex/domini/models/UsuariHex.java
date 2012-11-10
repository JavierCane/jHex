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
	private float temps_minim;

	/**
	 * Fitxes mínimes utilitzades per guanyar una partida
	 */
	private int fitxes_minimes;

	/**
	 * Nombre de partides disputades
	 */
	private int partides_jugades;

	/**
	 * Nombre de partides guanyades
	 */
	private int partides_guanyades;

	/**
	 * Llista de noms d'usuari no permesos.
	 */
	private static final Set<String> noms_no_permesos =
			Collections.unmodifiableSet( new HashSet<String>( Arrays.asList( new String[] {
					"Maquina_1",
					"Maquina_2",
					"Usuario_1",
					"Usuario_2"
			} ) ) );

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
		super( nom, contrasenya );

		if ( noms_no_permesos.contains( nom ) )
		{
			throw new IllegalArgumentException(
					"No es poden enregistrar els següents noms d'usuari: " + noms_no_permesos.toString() );
		}
		else
		{
			num_derrotes = new int[Dificultats.num_dificultats];
			num_empats = new int[Dificultats.num_dificultats];
			num_victories = new int[Dificultats.num_dificultats];

			mode_inici = ModesInici.ESTANDARD;
			combinacio_colors = CombinacionsColors.VERMELL_BLAU;
			temps_minim = Float.POSITIVE_INFINITY;
			fitxes_minimes = Integer.MAX_VALUE;
			partides_guanyades = 0;
			partides_jugades = 0;
		}
	}

	/**
	 * Retorna un identificador unic de l'usuari. Utilitzada per guardar a disc el fitxer corresponent de l'usuari.
	 *
	 * @return
	 */
	public String getIdentificadorUnic()
	{
		return this.getNom();
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
		       ", partides jugades: " + partides_jugades + "]";
	}

	/**
	 * Retorna el llistat de noms d'usuari no permesos
	 *
	 * @return
	 */
	public static Set<String> getNomsNoPermesos()
	{
		return noms_no_permesos;
	}

	/**
	 * Estableix el nom d'usuari comprovant que no hi sigui un nom d'usuari no permès.
	 *
	 * @param nom
	 * @return
	 */
	public boolean setNom( String nom )
	{
		if ( noms_no_permesos.contains( nom ) )
		{
			throw new IllegalArgumentException(
					"No se pueden registrar los nombres de usuarios siguientes: " + noms_no_permesos.toString() );
		}
		else
		{
			this.nom = nom;
			return true;
		}
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
	public float getTempsMinim()
	{
		return temps_minim;
	}

	/**
	 * Modifica el temps mínim en guanyar una partida de l'usuari.
	 *
	 * @param temps_minim Valor de temps que es vol introduir.
	 * @return Cert, si es tracta d'un temps vàlid. Fals altrament.
	 */
	public boolean setTempsMinim( float temps_minim )
	{
		if ( temps_minim <= 0 || fitxes_minimes > Float.POSITIVE_INFINITY )
		{
			return false;
		}
		this.temps_minim = temps_minim;
		return true;
	}

	/**
	 * Consulta el nombre de fitxes mínim amb què l'usuari ha guanyat una partida.
	 *
	 * @return El nombre de fitxes mínim amb què l'usuari ha guanyat una partida
	 */
	public int getFitxesMinimes()
	{
		return fitxes_minimes;
	}

	/**
	 * Modifica el nombre de fitxes mínim amb què l'usuari ha guanyat una partida.
	 *
	 * @param fitxes_minimes Nombre de fitxes que es vol introduir.
	 * @return Cert, si es tracta d'un nombre de fitxes vàlid. Fals altrament.
	 */
	public boolean setFitxesMinimes( int fitxes_minimes )
	{
		if ( fitxes_minimes <= 0 || fitxes_minimes > Integer.MAX_VALUE )
		{
			return false;
		}
		this.fitxes_minimes = fitxes_minimes;
		return true;
	}

	/**
	 * Consulta el nombre de partides jugades per l'usuari.
	 *
	 * @return El nombre de partides jugades per l'usuari.
	 */
	public int getPartidesJugades()
	{
		return partides_jugades;
	}

	/**
	 * Modifica el nombre de partides jugades per l'usuari.
	 *
	 * @param partides_jugades Nombre de partides jugades que es vol introduir.
	 * @return Cert, si es tracta d'un nombre de partides vàlid. Fals altrament.
	 */
	public boolean setPartidesJugades( int partides_jugades )
	{
		if ( partides_jugades < 0 || partides_jugades > Integer.MAX_VALUE )
		{
			return false;
		}
		this.partides_jugades = partides_jugades;
		return true;
	}

	/**
	 * Consulta el nombre de partides guanyades per l'usuari.
	 *
	 * @return El nombre de partides guanyades per l'usuari.
	 */
	public int getPartidesGuanyades()
	{
		return partides_guanyades;
	}

	/**
	 * Modifica el nombre de partides guanyades per l'usuari.
	 *
	 * @param partides_guanyades Nombre de partides guanyades que es vol introduir.
	 * @return Cert, si es tracta d'un nombre de partides vàlid. Fals altrament.
	 */
	public boolean setPartidesGuanyades( int partides_guanyades )
	{
		if ( partides_jugades < 0 || partides_guanyades > Integer.MAX_VALUE )
		{
			return false;
		}
		this.partides_guanyades = partides_guanyades;
		return true;
	}

}
