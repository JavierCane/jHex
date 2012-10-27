package Dominio;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//Añadiremos extends Usuari implements Serializable
public class Usuari
{

	private ModesInici mode_inici;
	private String[] colors;
	private Float temps_minim;
	private Integer fitxes_minimes;
	private Integer partides_jugades;
	private Integer partides_guanyades;

	private static final Set<String> noms_no_permesos = Collections.unmodifiableSet( new HashSet<String>( Arrays.asList( new String[] { "Maquina_1", "Maquina_2", "Usuario_1", "Usuario_2" } ) ) );

	/**
	 * Constructor por defecto de Usuario
	 *
	 * @param username
	 * @param password
	 * @throws IllegalArgumentException
	 */
	public Usuari( String username, String password ) throws IllegalArgumentException
	{
		/* A cambiar una vez sepamos la herencia
		if ( not_allowed_usernames.contains( username ) )
		{
			throw new IllegalArgumentException( "No se pueden registrar los nombres de usuarios siguientes: " + noms_no_permesos.toString() );
		}
		else
		{
			this.username = username;
			this.password = password;
			// Estos valores por defecto se pueden cambiar
			this.modo_inicio = ModosInicio.ESTANDAR;
			colores = new String[2];
			this.colores[0] = "azul";
			this.colores[1] = "rojo";
			this.tiempo_minimo = new Float( Float.POSITIVE_INFINITY );
			this.fitxes_minimas = new Integer( Integer.MAX_VALUE );
			this.partidas_ganadas = new Integer( 0 );
			this.partidas_jugadas = new Integer( 0 );
		}
		*/
	}

	public ModesInici getModeInici()
	{
		return mode_inici;
	}

	public void setModeInici( ModesInici m )
	{
		this.mode_inici = m;
	}

	public String[] getColors()
	{
		return colors;
	}

	public String getColors( Integer i )
	{
		return colors[i];
	}

	public void setColors( Integer i, String c )
	{
		this.colors[i] = c;
	}

	public void setColors( String[] colors )
	{
		this.colors = colors;
	}

	public Float getTempsMinim()
	{
		return temps_minim;
	}

	public void setTempsMinim( Float temps_minim )
	{
		this.temps_minim = temps_minim;
	}

	public Integer getFitxesMinimes()
	{
		return fitxes_minimes;
	}

	public void setFitxesMinimes( Integer fitxes_minimes )
	{
		this.fitxes_minimes = fitxes_minimes;
	}

	public Integer getPartidesJugades()
	{
		return partides_jugades;
	}

	public void setPartidesJugades( Integer partides_jugades )
	{
		this.partides_jugades = partides_jugades;
	}

	public Integer getPartidesGuanyades()
	{
		return partides_guanyades;
	}

	public void setPartidesGuanyades( Integer partides_guanyades )
	{
		this.partides_guanyades = partides_guanyades;
	}

	/*public String toString()
	{
		// A redefinir
	}*/
}