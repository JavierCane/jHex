package prop.hex.domini.models;

import prop.hex.domini.models.enums.TipusJugadors;

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
 *
 * @author Javier Ferrer Gonzalez (Grup 7.3, Hex)
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
	 * Consulta la llista d'usuaris amb puntuacions globals més altes.
	 *
	 * @return la llista d'usuaris amb puntuacions globals més altes.
	 */
	public List<UsuariHex> getClassificacio()
	{
		return classificacio;
	}

	/**
	 * Consulta el nom de l'usuari que ha guanyat una partida en menys temps.
	 *
	 * @return el nom de l'usuari que ha guanyat una partida en menys temps.
	 */
	public String getUsuariTempsMinim()
	{
		return usuari_temps_minim;
	}

	/**
	 * Consulta el rècord de temps en guanyar una partida.
	 *
	 * @return el rècord de temps en guanyar una partida.
	 */
	public Long getTempsMinim()
	{
		return temps_minim;
	}

	/**
	 * Consulta el nom de l'usuari que ha guanyat una partida amb menys fitxes.
	 *
	 * @return el nom de l'usuari que ha guanyat una partida amb menys fitxes.
	 */
	public String getUsuariFitxesMinimes()
	{
		return usuari_fitxes_minimes;
	}

	/**
	 * Consulta el rècord de fitxes minimes per guanyar una partida.
	 *
	 * @return el rècord de fitxes minimes per guanyar una partida.
	 */
	public Integer getFitxesMinimes()
	{
		return fitxes_minimes;
	}

	/**
	 * Consulta el nom de l'usuari que ha guanyat més partides.
	 *
	 * @return el nom de l'usuari que ha guanyat més partides.
	 */
	public String getUsuariMesPartidesGuanyades()
	{
		return usuari_mes_partides_guanyades;
	}

	/**
	 * Consulta el rècord de partides guanyades.
	 *
	 * @return el rècord de partides guanyades.
	 */
	public Integer getMesPartidesGuanyades()
	{
		return mes_partides_guanyades;
	}

	/**
	 * Consulta el nom de l'usuari que ha jugat més partides.
	 *
	 * @return el nom de l'usuari que ha jugat més partides.
	 */
	public String getUsuariMesPartidesJugades()
	{
		return usuari_mes_partides_jugades;
	}

	/**
	 * Consulta el rècord de partides jugades.
	 *
	 * @return el rècord de partides jugades.
	 */
	public Integer getMesPartidesJugades()
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

		if ( usuari.getTipusJugador() == TipusJugadors.JUGADOR )
		{
			comprovaRecords( usuari ); // Comprovo si els rècords son millors que els de l'usuari que estic actualitzant
		}
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
		netejaRecordsUsuari( nom_usuari );
	}

	/**
	 * Comprova que l'usuari no tingui registrat algún rècord del Hall of Fame. En cas de que així sigui,
	 * l'esborra i recalcula aquest rècord en base als usuaris de la classificació.
	 * Úitl quan s'elimina o esreinicien les estadístiques d'un usuari.
	 *
	 * @param nom_usuari Nom de l'usuari a revisar estadístiques.
	 */
	public void netejaRecordsUsuari( String nom_usuari )
	{
		if ( nom_usuari.equals( usuari_temps_minim ) )
		{
			usuari_temps_minim = null;
			temps_minim = Long.MAX_VALUE;
			recalculaTempsMinim();
		}

		if ( nom_usuari.equals( usuari_fitxes_minimes ) )
		{
			usuari_fitxes_minimes = null;
			fitxes_minimes = Integer.MAX_VALUE;
			recalculaFitxesMinimes();
		}

		if ( nom_usuari.equals( usuari_mes_partides_guanyades ) )
		{
			usuari_mes_partides_guanyades = null;
			mes_partides_guanyades = 0;
			recalculaPartidesGuanyades();
		}

		if ( nom_usuari.equals( usuari_mes_partides_jugades ) )
		{
			usuari_mes_partides_jugades = null;
			mes_partides_jugades = 0;
			recalculaPartidesJugades();
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

		comprovaTempsMinim( usuari.getTempsMinim(), nom_usuari );
		comprovaFitxesMinimes( usuari.getFitxesMinimes(), nom_usuari );
		comprovaPartidesGuanyades( usuari.getPartidesGuanyades(), nom_usuari );
		comprovaPartidesJugades( usuari.getPartidesJugades(), nom_usuari );
	}

	/**
	 * Comprova si el possible rècord de temps mínim es millor que l'actual
	 *
	 * @param possible_record Possible rècord
	 * @param nom_usuari Nom de l'usuari del possible rècord
	 */
	private void comprovaTempsMinim( Long possible_record, String nom_usuari )
	{
		if ( temps_minim > possible_record )
		{
			temps_minim = possible_record;
			usuari_temps_minim = nom_usuari;
		}
	}

	/**
	 * Comprova si el possible rècord de temps mínim es millor que l'actual i en cas de ser així, ho estableix
	 *
	 * @param possible_record Possible rècord
	 * @param nom_usuari Nom de l'usuari del possible rècord
	 */
	private void comprovaFitxesMinimes( Integer possible_record, String nom_usuari )
	{
		if ( fitxes_minimes > possible_record )
		{
			fitxes_minimes = possible_record;
			usuari_fitxes_minimes = nom_usuari;
		}
	}

	/**
	 * Comprova si el possible rècord de partides guanyades es millor que l'actual i en cas de ser així, ho estableix
	 *
	 * @param possible_record Possible rècord
	 * @param nom_usuari Nom de l'usuari del possible rècord
	 */
	private void comprovaPartidesGuanyades( Integer possible_record, String nom_usuari )
	{
		if ( mes_partides_guanyades < possible_record )
		{
			mes_partides_guanyades = possible_record;
			usuari_mes_partides_guanyades = nom_usuari;
		}
	}

	/**
	 * Comprova si el possible rècord de partides jugades es millor que l'actual i en cas de ser així, ho estableix
	 *
	 * @param possible_record Possible rècord
	 * @param nom_usuari Nom de l'usuari del possible rècord
	 */
	private void comprovaPartidesJugades( Integer possible_record, String nom_usuari )
	{
		if ( mes_partides_jugades < possible_record )
		{
			mes_partides_jugades = possible_record;
			usuari_mes_partides_jugades = nom_usuari;
		}
	}

	/**
	 * Recalcula l'usuari que té que tindre el rècord de temps mínim.
	 * Útil quan es borra
	 */
	private void recalculaTempsMinim()
	{
		for ( UsuariHex usuari_classificat : classificacio )
		{
			if ( usuari_classificat.getTipusJugador() == TipusJugadors.JUGADOR )
			{
				comprovaTempsMinim( usuari_classificat.getTempsMinim(), usuari_classificat.getNom() );
			}
		}
	}

	private void recalculaFitxesMinimes()
	{
		for ( UsuariHex usuari_classificat : classificacio )
		{
			if ( usuari_classificat.getTipusJugador() == TipusJugadors.JUGADOR )
			{
				comprovaFitxesMinimes( usuari_classificat.getFitxesMinimes(), usuari_classificat.getNom() );
			}
		}
	}

	private void recalculaPartidesGuanyades()
	{
		for ( UsuariHex usuari_classificat : classificacio )
		{
			if ( usuari_classificat.getTipusJugador() == TipusJugadors.JUGADOR )
			{
				comprovaPartidesGuanyades( usuari_classificat.getPartidesGuanyades(), usuari_classificat.getNom() );
			}
		}
	}

	private void recalculaPartidesJugades()
	{
		for ( UsuariHex usuari_classificat : classificacio )
		{
			if ( usuari_classificat.getTipusJugador() == TipusJugadors.JUGADOR )
			{
				comprovaPartidesJugades( usuari_classificat.getPartidesJugades(), usuari_classificat.getNom() );
			}
		}
	}
}
