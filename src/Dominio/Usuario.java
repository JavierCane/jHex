package Dominio;

public class Usuario
{

	private String username;
	private String password;

	public Usuario()
	{
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
}