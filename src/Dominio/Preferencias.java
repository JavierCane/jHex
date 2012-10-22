package Dominio;

public class Preferencias
{

	// Aquí hago la suposición de que se carga el nombre del usuario actual y su contraseña por si hay que cambiarla.
	// Además pongo los colores como un String de 2, si hay que hacerlo como enumeration se cambia sin problemas.
	private String username;
	private String password;
	private ModosInicio modo_inicio;
	private String[] colores;

	public Preferencias()
	{
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public ModosInicio getModoInicio()
	{
		return modo_inicio;
	}

	public String[] getColores()
	{
		return colores;
	}

	public String getColores( Integer i )
	{
		return colores[i];
	}

	public void setUsername( String u )
	{
		this.username = u;
	}

	public void setPassword( String p )
	{
		this.password = p;
	}

	public void setModoInicio( ModosInicio m )
	{
		this.modo_inicio = m;
	}

	public void setColores( Integer i, String c )
	{
		this.colores[i] = c;
	}

	public void setColores( String[] colores )
	{
		this.colores = colores;
	}
}
