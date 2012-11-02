package prop.cluster.domini.models;

import java.io.Serializable;

public abstract class Usuari implements Serializable
{

	protected String nom;
	protected String contrasenya;

	public Usuari( String nom, String contrasenya ) throws IllegalArgumentException
	{
//		if ( not_allowed_usernames.contains( username ) )
//		{
//			throw new IllegalArgumentException(
//					"No se pueden registrar los nombres de usuarios siguientes: " + noms_no_permesos.toString() );
//		}
//		else
//		{
//			this.username = username;
//			this.password = password;
//			// Estos valores por defecto se pueden cambiar
//			this.modo_inicio = ModosInicio.ESTANDAR;
//			colores = new String[2];
//			this.colores[0] = "azul";
//			this.colores[1] = "rojo";
//			this.tiempo_minimo = new Float( Float.POSITIVE_INFINITY );
//			this.fitxes_minimas = new Integer( Integer.MAX_VALUE );
//			this.partidas_ganadas = new Integer( 0 );
//			this.partidas_jugadas = new Integer( 0 );
//		}
	}

	public String toString()
	{
		return "[Nom: " + nom + ", contrasenya:" + contrasenya + "]";
	}
}
