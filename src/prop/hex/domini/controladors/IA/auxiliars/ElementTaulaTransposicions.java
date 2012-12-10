package prop.hex.domini.controladors.IA.auxiliars;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.controladors.IA.auxiliars.enums.FitesDePoda;

/**
 * Conté les dades necessàries per a un element d'una taula de transposicions.
 *
 * @author Isaac Sánchez Barrera
 */
public final class ElementTaulaTransposicions
{

	private int profunditat;
	private FitesDePoda fita;
	private int puntuacio;
	private EstatCasella jugador_avaluacio;

	/**
	 * Constructor amb tots els paràmetres.
	 * <p/>
	 * Crea una instància d'un element de la taula útil per a emmagatzemar càlculs d'un algorisme de cerca en un
	 * arbre de jugades.
	 *
	 * @param profunditat       Profunditat a la qual s'ha calculat la puntuació.
	 * @param fita              Tipus de fita de la puntuació
	 * @param puntuacio         Puntuació avaluada
	 * @param jugador_avaluacio Jugador per qui s'ha calculat la puntuació
	 */
	public ElementTaulaTransposicions( int profunditat, FitesDePoda fita, int puntuacio,
	                                   EstatCasella jugador_avaluacio )
	{
		this.profunditat = profunditat;
		this.fita = fita;
		this.puntuacio = puntuacio;
		this.jugador_avaluacio = jugador_avaluacio;
	}

	/**
	 * Constructor sense profunditats ni fites.
	 * <p/>
	 * Crea una instància d'un element de la taula útil per a emmagatzemar càlculs estàtics de puntuacions de taulers.
	 *
	 * @param puntuacio         Puntuació avaluada
	 * @param jugador_avaluacio Jugador per qui s'ha calculat la puntuació
	 */
	public ElementTaulaTransposicions( int puntuacio, EstatCasella jugador_avaluacio )
	{
		this.profunditat = 0;
		this.fita = FitesDePoda.VALOR_EXACTE;
		this.puntuacio = puntuacio;
		this.jugador_avaluacio = jugador_avaluacio;
	}

	/**
	 * Consulta la profunditat per a l'avaluació.
	 *
	 * @return La profunditat de l'avaluació.
	 */
	public int getProfunditat()
	{
		return profunditat;
	}

	/**
	 * Consulta el tipus de fita de la puntuació per a un jugador
	 *
	 * @param jugador Fitxa del jugador per qui es vol consultar el tipus de fita.
	 * @return El tipus de fita de la puntuació per al jugador indicat.
	 */
	public FitesDePoda getFita( EstatCasella jugador )
	{
		if ( fita == FitesDePoda.VALOR_EXACTE || jugador == jugador_avaluacio )
		{
			return fita;
		}
		else if ( fita == FitesDePoda.FITA_SUPERIOR )
		{
			return FitesDePoda.FITA_INFERIOR;
		}
		else
		{
			return FitesDePoda.FITA_SUPERIOR;
		}
	}

	/**
	 * Consulta la puntuació de l'avaluació per a un jugador.
	 *
	 * @param jugador Fitxa del jugador per qui es vol consultar la puntuació de l'avaluació.
	 * @return La puntuació de l'avaluació per al jugador indicat.
	 */
	public int getPuntuacio( EstatCasella jugador )
	{
		if ( jugador == jugador_avaluacio )
		{
			return puntuacio;
		}
		else
		{
			return -puntuacio;
		}
	}
}
