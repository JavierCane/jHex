package Dominio;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Usuario implements Serializable
{

	private String username;
	private String password;
	private ModosInicio modo_inicio;
	private String[] colores;
	private Float tiempo_minimo;
	private Integer fichas_minimas;
	private Integer partidas_jugadas;
	private Integer partidas_ganadas;

	private static final Set<String> not_allowed_usernames = Collections.unmodifiableSet( new HashSet<String>( Arrays.asList( new String[] { "Maquina_1", "Maquina_2", "Usuario_1", "Usuario_2" } ) ) );

	/**
	 * Constructor por defecto de Usuario
	 *
	 * @param username
	 * @param password
	 * @throws IllegalArgumentException
	 */
	public Usuario( String username, String password ) throws IllegalArgumentException
	{
		if ( not_allowed_usernames.contains( username ) )
		{
			throw new IllegalArgumentException( "No se pueden registrar los nombres de usuarios siguientes: " + not_allowed_usernames.toString() );
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
			this.fichas_minimas = new Integer( Integer.MAX_VALUE );
			this.partidas_ganadas = new Integer( 0 );
			this.partidas_jugadas = new Integer( 0 );
		}
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername( final String username )
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword( final String password )
	{
		this.password = password;
	}


	public ModosInicio getModoInicio()
	{
		return modo_inicio;
	}

	public void setModoInicio( ModosInicio m )
	{
		this.modo_inicio = m;
	}

	public String[] getColores()
	{
		return colores;
	}

	public String getColores( Integer i )
	{
		return colores[i];
	}

	public void setColores( Integer i, String c )
	{
		this.colores[i] = c;
	}

	public void setColores( String[] colores )
	{
		this.colores = colores;
	}

	public Float getTiempoMinimo()
	{
		return tiempo_minimo;
	}

	public void setTiempoMinimo( Float tiempo_minimo )
	{
		this.tiempo_minimo = tiempo_minimo;
	}

	public Integer getFichasMinimas()
	{
		return fichas_minimas;
	}

	public void setFichasMinimas( Integer fichas_minimas )
	{
		this.fichas_minimas = fichas_minimas;
	}

	public Integer getPartidasJugadas()
	{
		return partidas_jugadas;
	}

	public void setPartidasJugadas( Integer partidas_jugadas )
	{
		this.partidas_jugadas = partidas_jugadas;
	}

	public Integer getPartidasGanadas()
	{
		return partidas_ganadas;
	}

	public void setPartidasGanadas( Integer partidas_ganadas )
	{
		this.partidas_ganadas = partidas_ganadas;
	}

	public String toString()
	{
		return "[Username: " + username + ", password:" + password + ", modo de inicio:" + modo_inicio + ", color 1:" + colores[0] + ", color 2:" + colores[1] + ", tiempo minimo:" + tiempo_minimo + ", fichas minimas:" + fichas_minimas + ", partidas ganadas:" + partidas_ganadas + ", partidas jugadas:" + partidas_jugadas + "]";
	}
}