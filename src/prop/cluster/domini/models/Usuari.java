package prop.cluster.domini.models;

import java.io.Serializable;

/**
 * Representa tota la informació d'un usuari.
 * Conté les dades principals del perfil i les seves
 * estadístiques en els diferents nivells de dificultat.
 * Cada usuari té un nom com a identificador.
 *
 * @author Àlex Moré Guardiola (Grup 7.1, Othello)
 */
public class Usuari implements Serializable
{

	/**
	 * ID de serialització
	 */
	protected static final long serialVersionUID = -6648226504410176256L;

	/**
	 * Nom (Identificador) de l'usuari.
	 */
	protected String nom;

	/**
	 * La contrasenya de l'usuari en el sistema.
	 */
	protected String contrasenya;

	/**
	 * Array on cada posició hi ha el número de victòries de l'usuari
	 * contra diferents tipus de contrincants.
	 */
	protected int[] num_victories;

	/**
	 * Array on cada posició hi ha el número de empats del usuari
	 * contra diferents tipus de contrincants.
	 */
	protected int[] num_empats;

	/**
	 * Array on cada posició hi ha el número de derrotes del usuari
	 * contra diferents tipus de contrincants.
	 */
	protected int[] num_derrotes;

	/**
	 * Constructora d'un usuari. Crea un usuari amb el nom i contrasenya desitjats.
	 * Els altres atributs s’inicialitzen a 0.
	 *
	 * @param nom         Nom de l'usuari.
	 * @param contrasenya Contrasenya de l'usuari.
	 * @param dificultat  Número total de dificultats en el joc (1 contra altres usuaris + N contra màquina).
	 */
	public Usuari( String nom, String contrasenya, int dificultat )
	{
		this.nom = nom;
		this.contrasenya = contrasenya;
		num_victories = new int[dificultat];
		num_empats = new int[dificultat];
		num_derrotes = new int[dificultat];
		int i = 0;
		while ( i < dificultat )
		{
			num_victories[i] = 0;
			num_empats[i] = 0;
			num_derrotes[i] = 0;
			i = i + 1;
		}
	}

	/**
	 * Mètode per definir el nom d'usuari.
	 *
	 * @param nom Nom de l'usuari que se li vol assignar.
	 * @return Retorna true sempre, ja que s'ha efectuat l'assignació del nom.
	 */
	public boolean setNom( String nom )
	{
		this.nom = nom;
		return true;
	}

	/**
	 * Mètode der definir una contrasenya.
	 *
	 * @param contrasenya Contrasenya de l'usuari que se li vol assignar.
	 * @return Retorna true sempre, ja que s'ha efectuat l'assignació de la contrasenya.
	 */
	public boolean setContrasenya( String contrasenya )
	{
		this.contrasenya = contrasenya;
		return true;
	}

	/**
	 * Mètode consultor del nom.
	 */
	public String getNom()
	{
		return nom;
	}

	/**
	 * Mètode consultor de la contrasenya de l'usuari.
	 */
	public String getContrasenya()
	{
		return contrasenya;
	}

	/**
	 * Mètode consultor de les victòres contra un contrincant concret.
	 *
	 * @param contrincant Contrincant contra qui ha guanyat l’usuari.
	 * @return El número de victòries contra un contrincant concret.
	 */
	public int getVictories( int contrincant )
	{
		return num_victories[contrincant];
	}

	/**
	 * Mètode consultor dels empats contra un contrincant concret.
	 *
	 * @param contrincant Contrincant contra qui ha empatat l’usuari.
	 * @return El número de empats contra un contrincant concret.
	 */
	public int getEmpats( int contrincant )
	{
		return num_empats[contrincant];
	}

	/**
	 * Mètode consultor de les derrotes contra un contrincant concret.
	 *
	 * @param contrincant Contrincant contra qui ha perdut l’usuari.
	 * @return El número de derrotes contra un contrincant concret.
	 */
	public int getDerrotes( int contrincant )
	{
		return num_derrotes[contrincant];
	}

	/**
	 * Mètode per incrementar les victòres d'un usuari
	 * contra un contrincant determinat.
	 *
	 * @param contrincant Contrincant contra qui ha guanyat l’usuari.
	 * @return Retorna true sempre.
	 */
	public boolean incrementaVictories( int contrincant )
	{
		num_victories[contrincant] = num_victories[contrincant] + 1;
		return true;
	}

	/**
	 * Mètode per incrementar els empats d'un usuari
	 * contra un contrincant determinat.
	 *
	 * @param contrincant Contrincant contra qui ha empatat l’usuari.
	 * @return Retorna true sempre.
	 */
	public boolean incrementaEmpats( int contrincant )
	{
		num_empats[contrincant] = num_empats[contrincant] + 1;
		return true;
	}

	/**
	 * Mètode per incrementar les derrotes d'un usuari
	 * contra un contrincant determinat.
	 *
	 * @param contrincant Contrincant contra qui ha perdut l’usuari.
	 * @return Retorna true sempre.
	 */
	public boolean incrementaDerrotes( int contrincant )
	{
		num_derrotes[contrincant] = num_derrotes[contrincant] + 1;
		return true;
	}

	/**
	 * Crea un String amb tota la informació de l'usuari.
	 *
	 * @return El String amb la informació completa de l'usuari.
	 */
	@Override
	public String toString()
	{
		return "[Nom: " + nom + ", contrasenya: " + contrasenya +
		       ", num victories: [" + obteStringDeVector( num_victories ) +
		       "], num empats: [" + obteStringDeVector( num_empats ) +
		       "], num derrotes: [" + obteStringDeVector( num_derrotes ) + "]]";
	}

	/**
	 * String amb les estadadístiques d'un usuari
	 * en victòries, empats o derrotes segons convingui.
	 *
	 * @param vector_origen Vector amb el número de victòries,
	 *                      empats o derrotes segons el paràmetre que es passi.
	 * @return Retorna les xifres de victòries,
	 *         empats o derrotes en forma d'enumeració. [x,y,z,...]
	 */
	private String obteStringDeVector( int[] vector_origen )
	{
		String string_resultant = new String();
		for ( int iterador : vector_origen )
		{
			string_resultant += iterador + ", ";
		}
		return string_resultant.substring( 0, string_resultant.length() - 2 );
	}

	/**
	 * Mètode que reinicia les estadístiques de victòries,
	 * empats i derrotes d'un usuari
	 */
	public void reiniciaEstadistiques()
	{
		int i;
		for ( i = 0; i < num_victories.length; i++ )
		{
			num_victories[i] = 0;
			num_empats[i] = 0;
			num_derrotes[i] = 0;
		}
	}
}
