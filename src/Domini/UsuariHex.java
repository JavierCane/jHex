package Domini;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UsuariHex extends Usuari implements Serializable
{

	private ModesInici mode_inici;
	private String[] colors;
	private float temps_minim;
	private int fitxes_minimes;
	private int partides_jugades;
	private int partides_guanyades;

	private static final Set<String> noms_no_permesos = Collections.unmodifiableSet( new HashSet<String>( Arrays.asList(
			new String[] {
					"Maquina_1",
					"Maquina_2",
					"Usuario_1",
					"Usuario_2"
			} ) ) );

	public UsuariHex( String nom, String contrasenya ) throws IllegalArgumentException
	{
		super( nom, contrasenya );
		this.fitxes_minimes = 2;
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
	 * @param mode ModeInici que es vol assignar a l'usuari.
	 */
	public void setModeInici( ModesInici mode )
	{
		this.mode_inici = mode;
	}

	/**
	 * Consulta els colors que té assignat l'usuari.
	 *
	 * @return Un array de dues posicions amb els dos colors de fitxes preferits per l'usuari.
	 */
	public String[] getColors()
	{
		return colors;
	}

	/**
	 * Consulta el color demanat d'entre aquells que té assignats l'usuari.
	 *
	 * @param numero Índex del color que volem obtenir.
	 * @return Un String amb el nom del color demanat.
	 * @throws IndexOutOfBoundsException si el número de color és més gran que 1, ja que només hi ha dos colors.
	 */
	public String getColors( int numero ) throws IndexOutOfBoundsException
	{
		if ( numero > 1 )
		{
			throw new IndexOutOfBoundsException( "Índex de color massa gran" );
		}
		return colors[numero];
	}

	/**
	 * Modifica un cert color dels que té assignats l'usuari.
	 *
	 * @param numero Índex del color que volem obtenir.
	 * @param color  Un String amb el nom del color demanat.
	 * @return Cert si el color es modifica. Fals altrament.
	 * @throws IndexOutOfBoundsException si el número de color és més gran que 1, ja que només hi ha dos colors.
	 * @throws IllegalArgumentException  si el nom del color no és vàlid.
	 */
	public boolean setColors( int numero, String color ) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if ( numero > 1 )
		{
			throw new IndexOutOfBoundsException( "Índex de color massa alt" );
		}
		// Ejemplo
		else if ( color != "blau" || color != "vermell" )
		{
			throw new IllegalArgumentException( "Nom de color no vàlid" );
		}
		this.colors[numero] = color;
		return true;
	}

	/* Es necesario?
	public boolean setColors( String[] colors )
	{
		this.colors = colors;
		return true;
	}*/

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
		if ( temps_minim <= 0 )
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
		if ( fitxes_minimes <= 0 )
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
		if ( partides_jugades < 0 )
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
		if ( partides_jugades < 0 )
		{
			return false;
		}
		this.partides_guanyades = partides_guanyades;
		return true;
	}

}
