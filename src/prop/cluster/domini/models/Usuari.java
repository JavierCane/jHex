package prop.cluster.domini.models;

import java.io.Serializable;

public abstract class Usuari implements Serializable
{

	protected String nom;
	protected String contrasenya;
	protected int[] num_victories;
	protected int[] num_empats;
	protected int[] num_derrotes;

	public String toString()
	{
		return "[Nom: " + nom + ", contrasenya: " + contrasenya + ", num victories: " + num_victories.toString() +
		       ", num empats: " + num_empats.toString() + ", num derrotes: " + num_derrotes.toString() + "]";
	}

	public Usuari( String nom, String contrasenya ) throws IllegalArgumentException
	{
		this.nom = nom;
		this.contrasenya = contrasenya;
	}

	public String getNom()
	{
		return nom;
	}

	public String getContrasenya()
	{
		return contrasenya;
	}

	public int getVictories( int dificultat )
	{
		return num_victories[dificultat];
	}

	public int[] getNum_empats()
	{
		return num_empats;
	}

	public int[] getNum_derrotes()
	{
		return num_derrotes;
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
