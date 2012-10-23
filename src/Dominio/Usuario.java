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

	public String toString()
	{
		return "[Username: " + username + ", password:" + password + "]";
	}
}