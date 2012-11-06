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

public class UsuariHex extends Usuari implements Serializable
{

	private ModesInici mode_inici;
	private CombinacionsColors combinacio_colors;
	private float temps_minim;
	private int fitxes_minimes;
	private int partides_jugades;
	private int partides_guanyades;

	private static final Set<String> noms_no_permesos =
			Collections.unmodifiableSet( new HashSet<String>( Arrays.asList( new String[] {
					"Maquina_1",
					"Maquina_2",
					"Usuario_1",
					"Usuario_2"
			} ) ) );

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
	 * Retorna un identificador unic de l'usuari
	 *
	 * @return
	 */
	public String getIdentificadorUnic()
	{
		return this.getNom();
	}

	public String toString()
	{
		return super.toString() + " [Mode inici: " + mode_inici +
		       ", combinacio colors: " + combinacio_colors + ", temps minim: " + temps_minim +
		       ", fitxes minimes: " + fitxes_minimes + ", partides guanyades: " + partides_guanyades +
		       ", partides jugades: " + partides_jugades + "]";
	}

	public static Set<String> getNomsNoPermesos()
	{
		return noms_no_permesos;
	}

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
