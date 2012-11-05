package prop.cluster.domini.models;

import java.io.Serializable;

public abstract class Usuari implements Serializable
{
	protected String nom;
	protected String contrasenya;

	public Usuari( String nom, String contrasenya ) throws IllegalArgumentException
	{
		this.nom = nom;
		this.contrasenya = contrasenya;
	}

	public String toString()
	{
		return "[Nom: " + nom + ", contrasenya: " + contrasenya + "]";
	}

	public String getContrasenya()
	{
		return contrasenya;
	}

	public String getNom()
	{
		return nom;
	}

	public boolean setNom( String nom )
	{
		this.nom = nom;
		return true;
	}

	public boolean setContrasenya( String contrasenya )
	{
		this.contrasenya = contrasenya;
		return true;
	}
}
