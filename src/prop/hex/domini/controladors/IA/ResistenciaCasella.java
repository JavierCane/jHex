package prop.hex.domini.controladors.IA;

import prop.hex.domini.models.Casella;

/**
 * Classe que vincula una casella amb la seva resistencia i un comparador que ordena per resistencia.
 * Necessaria com a auxiliar de l'algoritme de Dijkstra a la classe CamiMinim.
 */
public final class ResistenciaCasella implements Comparable<ResistenciaCasella>
{

	/**
	 * Resistencia de la casella
	 */
	private int resistencia;
	/**
	 * Casella del tauler.
	 */
	private Casella casella;

	/**
	 * Crea una casella amb una resistencia asociada.
	 *
	 * @param casella
	 * @param resistencia resistencia asociada a la casella.
	 */
	public ResistenciaCasella( Casella casella, int resistencia )
	{
		this.casella = casella;
		this.resistencia = resistencia;
	}

	/**
	 * Obté la resistencia asociada a una casella.
	 *
	 * @return resistencia asociada a la casella.
	 */
	public int getResistencia()
	{
		return resistencia;
	}

	/**
	 * Obté la casella.
	 *
	 * @return Casella
	 */
	public Casella getCasella()
	{
		return casella;
	}

	/**
	 * Comparador, compara només les resistencies asociades.
	 *
	 * @param b ResistenciaCasella a comparar.
	 * @return -1 si és més petita que b, 1 si és més gran i 0 si són iguals.
	 */
	@Override
	public int compareTo( ResistenciaCasella b )
	{
		if ( resistencia < b.getResistencia() )
		{
			return -1;
		}
		if ( resistencia > b.getResistencia() )
		{
			return 1;
		}
		else
		{
			return casella.compareTo( b.getCasella() );
		}
	}
}
