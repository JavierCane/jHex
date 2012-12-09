package prop.hex.domini.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe encarregada de gestionar el rànquing d'usuaris.
 * Com es pot veure a través de la gestió de l'atribut instancia, s'ha implementant aplicant el patró Singleton per
 * asegurar-nos de que no es creen dues instàncies distintes d'aquesta classe.
 */
public final class Ranquing implements Serializable
{

	/**
	 * ID de serialització
	 */
	private static final long serialVersionUID = -4501688224648658253L;

	/**
	 * Atribut per mantenir l'instancia Singleton de la classe
	 */
	private static Ranquing instancia = null;

	/**
	 * Llista d'usuaris ordenada per les seves puntuacions globals
	 */
	private List<UsuariHex> classificacio;

	/**
	 * Identificador de l'usuari que hagi aconseguit guanyar una partida en el menor temps
	 */
	private String usuari_temps_minim;

	/**
	 * Rècord de temps minim
	 */
	private Long temps_minim;

	/**
	 * Identificador de l'usuari que hagi aconseguit guanyar una partida utilitzant menys fitxes
	 */
	private String usuari_fitxes_minimes;

	/**
	 * Rècord de fitxes minimes
	 */
	private Integer fitxes_minimes;

	/**
	 * Identificador de l'usuari que hagi guanyat més partides
	 */
	private String usuari_mes_partides_guanyades;

	/**
	 * Rècord de partides guanyades
	 */
	private Integer mes_partides_guanyades;

	/**
	 * Identificador de l'usuari que hagi jugat més partides
	 */
	private String usuari_mes_partides_jugades;

	/**
	 * Rècord de partides jugades
	 */
	private Integer mes_partides_jugades;

	/**
	 * Constructora de classe, inicialitza tots els atributs.
	 * És privada per evitar que es fagin noves instancies desde fora de la classe (patró Singleton).
	 */
	private Ranquing()
	{
		classificacio = new ArrayList<UsuariHex>();

		inicialitzaRecords();
	}

	/**
	 * Consultora de l'instancia actual de Ranquing.
	 * Si encara no s'ha inicialitzat l'objecte, crida a la constructora, si ja s'ha instanciat previament,
	 * simplement retorna l'instancia ja creada.
	 *
	 * @return L'objecte singleton amb el rànquing.
	 */
	public static synchronized Ranquing getInstancia()
	{
		if ( null == instancia )
		{
			instancia = new Ranquing();
		}

		return instancia;
	}

	/**
	 * Mètode amb la finalitat de poder depurar el programa amb més facilitat. Simplement retorna tots els atributs
	 * de la classe y els seus corresponents valors.
	 *
	 * @return Un String amb tots els atribus del rànquing.
	 */
	@Override
	public String toString()
	{
		return "[Classificació: " + classificacio.toString() + ", " +
				"usuari temps minim: " + usuari_temps_minim +
				", temps minim: " + temps_minim + ", usuari fitxes minimes: " + usuari_fitxes_minimes +
				", fitxes minimes: " + fitxes_minimes + ", usuari mes partides guanyades: " +
				usuari_mes_partides_guanyades + ", mes partides guanyades: " + mes_partides_guanyades +
				", usuari mes partides jugades: " + usuari_mes_partides_jugades + ", mes partides jugades: " +
				mes_partides_jugades + "]";
	}

	/**
	 * Consulta la llista d'usuaris amb puntuacions globals més altes
	 *
	 * @return La llista d'usuaris amb puntuacions globals més altes.
	 */
	public List<UsuariHex> getClassificacio()
	{
		return classificacio;
	}

	public String get_usuari_temps_minim()
	{
		return usuari_temps_minim;
	}

	public Long get_temps_minim()
	{
		return temps_minim;
	}

	public String get_usuari_fitxes_minimes()
	{
		return usuari_fitxes_minimes;
	}

	public Integer get_fitxes_minimes()
	{
		return fitxes_minimes;
	}

	public String get_usuari_mes_partides_guanyades()
	{
		return usuari_mes_partides_guanyades;
	}

	public Integer get_mes_partides_guanyades()
	{
		return mes_partides_guanyades;
	}

	public String get_usuari_mes_partides_jugades()
	{
		return usuari_mes_partides_jugades;
	}

	public Integer get_mes_partides_jugades()
	{
		return mes_partides_jugades;
	}

	/**
	 * Actualitza les dades del rànquing i dels millors rècords corresponents a un usuari. Si no hi està ja a la
	 * llista l'inserta. Després d'actualitzar/insertar l'usuari, actualitza l'ordre del rànquing.
	 *
	 * @param usuari
	 */
	public void actualitzaRanquingUsuari( UsuariHex usuari )
	{
		int posicio_usuari_ranquing = classificacio.indexOf( usuari );

		// Si l'usuari ja està insertat al rànquing actualitzo les seves dades
		if ( posicio_usuari_ranquing != -1 )
		{
			classificacio.set( posicio_usuari_ranquing, usuari );
		}
		else // Si l'usuari no està al rànquing el fico
		{
			classificacio.add( usuari );
		}

		Collections.sort( classificacio ); // Actualitzo l'ordre del rànquing
		comprovaRecords( usuari ); // Comprovo si els rècords son millors que els de l'usuari que estic actualitzant
	}

	/**
	 * Elimina les dades d'un usuari del rànquing. Automàticament actualitza el llistat.
	 *
	 * @param usuari
	 */
	public void eliminaRanquingUsuari( UsuariHex usuari )
	{
		String nom_usuari = usuari.getNom();

		// Elimino l'usuari de la classificació
		classificacio.remove( usuari );

		// Elimino l'usuari de les fites si és que va aconseguir alguna
		netejaFitesUsuari( nom_usuari );
	}

	public void netejaFitesUsuari( String nom_usuari )
	{
		if ( nom_usuari == usuari_temps_minim )
		{
			usuari_temps_minim = null;
			temps_minim = Long.MAX_VALUE;
		}

		if ( nom_usuari == usuari_fitxes_minimes )
		{
			usuari_fitxes_minimes = null;
			fitxes_minimes = Integer.MAX_VALUE;
		}

		if ( nom_usuari == usuari_mes_partides_guanyades )
		{
			usuari_mes_partides_guanyades = null;
			mes_partides_guanyades = 0;
		}

		if ( nom_usuari == usuari_mes_partides_jugades )
		{
			usuari_mes_partides_jugades = null;
			mes_partides_jugades = 0;
		}
	}
	/**
	 * Funció que neteja el rànquing actual (elimina els usuaris ja ordenats i reinicialitza els rècords)
	 */
	public void netejaRanquing()
	{
		classificacio.clear();
		inicialitzaRecords();
	}

	/**
	 * Sobreescritura del mètode readObject.
	 * Mètode utilitzat després de fer la deserialització de l'objecte llegit de disc mitjançant defaultReadObject()
	 * Necessari per poder establir els paràmetres de la classe de tipus Singleton (si no els deixava com els d'una
	 * nova instància).
	 *
	 * @param ois
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject( ObjectInputStream ois ) throws IOException, ClassNotFoundException
	{
		ois.defaultReadObject();

		// if ( instancia != null )
		//{
		//	synchronized ( instancia )
		//	{
				instancia = this;
		//	}
		//}
	}

	/**
	 * Inicialitza els rècords de temps, fitxes mínimes, més partides guanyades i més partides jugades.
	 */
	private void inicialitzaRecords()
	{
		usuari_temps_minim = null;
		temps_minim = Long.MAX_VALUE;

		usuari_fitxes_minimes = null;
		fitxes_minimes = Integer.MAX_VALUE;

		usuari_mes_partides_guanyades = null;
		mes_partides_guanyades = 0;

		usuari_mes_partides_jugades = null;
		mes_partides_jugades = 0;
	}

	/**
	 * Comprova si els rècords que té actualment la classe com millors rècords s'haurien d'actualitzar amb cualsevol
	 * dels de l'usuari a actualitzar, en cas de que així sigui, ho fa.
	 *
	 * @param usuari
	 */
	private void comprovaRecords( UsuariHex usuari )
	{
		String nom_usuari = usuari.getNom();

		if ( temps_minim > usuari.getTempsMinim() )
		{
			temps_minim = usuari.getTempsMinim();
			usuari_temps_minim = nom_usuari;
		}

		if ( fitxes_minimes > usuari.getFitxesMinimes() )
		{
			fitxes_minimes = usuari.getFitxesMinimes();
			usuari_fitxes_minimes = nom_usuari;
		}

		if ( mes_partides_guanyades < usuari.getPartidesGuanyades() )
		{
			mes_partides_guanyades = usuari.getPartidesGuanyades();
			usuari_mes_partides_guanyades = nom_usuari;
		}

		if ( mes_partides_jugades < usuari.getPartidesJugades() )
		{
			mes_partides_jugades = usuari.getPartidesJugades();
			usuari_mes_partides_jugades = nom_usuari;
		}
	}

}
