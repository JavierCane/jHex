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
		return "[Nom: " + nom + ", contrasenya: " + contrasenya +
		       ", num victories: " + obteStringDeVector( num_victories ) +
		       ", num empats: " + obteStringDeVector( num_empats ) +
		       ", num derrotes: " + obteStringDeVector( num_derrotes ) + "]";
	}

	private String obteStringDeVector( int[] vector_origen )
	{
		String string_resultant = new String();

		for ( int iterador : vector_origen )
		{
			string_resultant += vector_origen[iterador] + ", ";
		}

		return string_resultant.substring( 0, string_resultant.length() - 2 );
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
