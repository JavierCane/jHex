package prop.hex.domini.models;

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
	private List<UsuariHex> ranquing;

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
		ranquing = new ArrayList<UsuariHex>();

		usuari_temps_minim = null;
		temps_minim = Long.MAX_VALUE;

		usuari_fitxes_minimes = null;
		fitxes_minimes = Integer.MAX_VALUE;

		usuari_mes_partides_guanyades = null;
		mes_partides_guanyades = Integer.MIN_VALUE;

		usuari_mes_partides_jugades = null;
		mes_partides_jugades = Integer.MIN_VALUE;
	}

	/**
	 * Consultora de l'instancia actual de Ranquing.
	 * Si encara no s'ha inicialitzat l'objecte, crida a la constructora, si ja s'ha instanciat previament,
	 * simplement retorna l'instancia ja creada.
	 *
	 * @return L'objecte singleton amb el rànquing.
	 */
	public static Ranquing getInstancia()
	{
		if ( null == instancia )
		{
			instancia = new Ranquing();
		}

		return instancia;
	}

	/**
	 * Consulta la llista d'usuaris amb puntuacions globals més altes
	 *
	 * @return La llista d'usuaris amb puntuacions globals més altes.
	 */
	public List<UsuariHex> getRanquing()
	{
		return ranquing;
	}

	/**
	 * Actualitza les dades del rànquing i dels millors rècords corresponents a un usuari. Si no hi està ja a la
	 * llista l'inserta. Després d'actualitzar/insertar l'usuari, actualitza l'ordre del rànquing.
	 *
	 * @param usuari
	 */
	public void actualitzaUsuari( UsuariHex usuari )
	{
		int posicio_usuari_ranquing = ranquing.indexOf( usuari );

		// Si l'usuari ja està insertat al rànquing actualitzo les seves dades
		if ( posicio_usuari_ranquing != -1 )
		{
			ranquing.set( posicio_usuari_ranquing, usuari );
		}
		else // Si l'usuari no està al rànquing el fico
		{
			ranquing.add( usuari );
		}

		Collections.sort( ranquing ); // Actualitzo l'ordre del rànquing
		comprovaRecords( usuari ); // Comprovo si els rècords son millors que els de l'usuari que estic actualitzant
	}

	/**
	 * Elimina les dades d'un usuari del rànquing. Automàticament actualitza el llistat.
	 *
	 * @param usuari
	 */
	public void eliminaUsuari( UsuariHex usuari )
	{
		ranquing.remove( usuari );
	}

	/**
	 * Comprova si els rècords que té actualment la classe com millors rècords s'haurien d'actualitzar amb cualsevol
	 * dels de l'usuari a actualitzar, en cas de que així sigui, ho fa.
	 *
	 * @param usuari
	 */
	private void comprovaRecords( UsuariHex usuari )
	{
		String identificador_usuari = usuari.getIdentificadorUnic();

		if ( temps_minim >= usuari.getTempsMinim() )
		{
			temps_minim = usuari.getTempsMinim();
			usuari_temps_minim = identificador_usuari;
		}

		if ( fitxes_minimes >= usuari.getFitxesMinimes() )
		{
			fitxes_minimes = usuari.getFitxesMinimes();
			usuari_fitxes_minimes = identificador_usuari;
		}

		if ( mes_partides_guanyades <= usuari.getPartidesGuanyades() )
		{
			mes_partides_guanyades = usuari.getPartidesGuanyades();
			usuari_mes_partides_guanyades = identificador_usuari;
		}

		if ( mes_partides_jugades <= usuari.getPartidesJugades() )
		{
			mes_partides_jugades = usuari.getPartidesJugades();
			usuari_mes_partides_jugades = identificador_usuari;
		}
	}
}
