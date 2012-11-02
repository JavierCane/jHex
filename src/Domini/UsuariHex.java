package Domini;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: javierferrer
 * Date: 29/10/12
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */
public class UsuariHex extends Usuari implements Serializable
{
	private Integer hex_param;

	/**
	 * Constructor por defecto de Usuari
	 *
	 * @param username
	 * @param password
	 * @throws IllegalArgumentException
	 */
	public UsuariHex( String username, String password ) throws IllegalArgumentException
	{
		super( username, password );
		this.hex_param = 2;
	}

	public String toString()
	{
		return "[Username: " + username + ", password:" + password + "]";
	}
}
