package prop.hex.domini.models;

import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;

import java.util.List;
import java.util.LinkedList;

public class TaulerHex extends Tauler
{

	/**
	 * Constructor del tauler. Crea un tauler de la mida desitjada amb totes les caselles buides (EstatCasella.BUIDA).
	 *
	 * @param mida Les dimensions que tindrà el tauler
	 */
	public TaulerHex( int mida )
	{
		super( mida );
	}

	/**
	 * Constructor que inicialitza el tauler a un estat diferent del per defecte. No comprova que els paràmetres siguin
	 * correctes.
	 *
	 * @param mida         Les dimensions del tauler
	 * @param caselles     Un array bidimensional mida × mida amb l'estat inicial
	 * @param num_fitxes_a La quantitat de fitxes que té el jugador A al tauler
	 * @param num_fitxes_b La quantitat de fitxes que té el jugador B al tauler
	 */
	public TaulerHex( int mida, EstatCasella[][] caselles, int num_fitxes_a, int num_fitxes_b )
	{
		super( mida, caselles, num_fitxes_a, num_fitxes_b );
	}

	/**
	 * Constructor per còpia. Crea un nou tauler idèntic a original.
	 *
	 * @param original Tauler que es vol copiar
	 */
	public TaulerHex( TaulerHex original )
	{
		super( original );
	}

	/**
	 * Comprova si una casella és vàlida dins el tauler
	 *
	 * @param casella Casella del tauler
	 * @return Cert si és una casella vàlida. Fals altrament.
	 */
	public boolean esCasellaValida( Casella casella )
	{
		return esCasellaValida( casella.getFila(), casella.getColumna() );
	}

	/**
	 * Comprova si un moviment és vàlid.
	 *
	 * @param fitxa   Fitxa que es vol comprovar
	 * @param fila    Fila de la casella dins el tauler
	 * @param columna Columna de la casella dins el tauler.
	 * @return Cert si el moviment és vàlid. Fals altrament.
	 * @throws IndexOutOfBoundsException si (fila, columna) no és una casella vàlida.
	 * @throws IllegalArgumentException  si fitxa no és de cap jugador (és EstatCasella.BUIDA).
	 */
	public boolean esMovimentValid( EstatCasella fitxa, int fila, int columna )
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if ( !esCasellaValida( fila, columna ) )
		{
			throw new IndexOutOfBoundsException( "Casella fora del tauler" );
		}
		else if ( caselles[fila][columna] != EstatCasella.BUIDA )
		{
			throw new IllegalArgumentException( "El moviment no és vàlid" );
		}
		else if ( fitxa == EstatCasella.BUIDA )
		{
			throw new IllegalArgumentException( "La fitxa no és de cap jugador" );
		}
		else
		{
			return true;
		}
	}

	/**
	 * Comprova si un moviment és vàlid.
	 *
	 * @param fitxa   Fitxa que es vol comprovar
	 * @param casella Casella del tauler
	 * @return Cert si el moviment és vàlid. Fals altrament.
	 * @throws IndexOutOfBoundsException si casella no és una casella vàlida.
	 * @throws IllegalArgumentException  si fitxa no és de cap jugador (és EstatCasella.BUIDA).
	 */
	public boolean esMovimentValid( EstatCasella fitxa, Casella casella )
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		return esMovimentValid( fitxa, casella.getFila(), casella.getColumna() );
	}

	/**
	 * Consulta l'estat d'una casella del tauler.
	 *
	 * @param casella Casella del tauler.
	 * @return L'estat actual de la casella.
	 */
	public EstatCasella getEstatCasella( Casella casella )
	{
		return getEstatCasella( casella.getFila(), casella.getColumna() );
	}

	/**
	 * Mou la fitxa a la casella indicada i actualitza els comptadors.
	 *
	 * @param fitxa   Fitxa que es vol col·locar.
	 * @param casella Casella del tauler.
	 * @return Cert si s’ha realitzat el moviment. Fals altrament.
	 * @throws IndexOutOfBoundsException si casella no és una casella vàlida.
	 * @throws IllegalArgumentException  si fitxa no és de cap jugador (és EstatCasella.BUIDA)  o el moviment no és
	 *                                   vàlid.
	 */
	public boolean mouFitxa( EstatCasella fitxa, Casella casella )
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		return mouFitxa( fitxa, casella.getFila(), casella.getColumna() );
	}

	/**
	 * Treu la fitxa de la casella indicada i actualitza els comptadors.
	 *
	 * @param casella Casella del tauler.
	 * @return Cert si s’ha realitzat el moviment. Fals altrament.
	 * @throws IndexOutOfBoundsException si no és una casella vàlida.
	 * @throws IllegalArgumentException  si la casella és buida (és EstatCasella.BUIDA).
	 */
	public boolean treuFitxa( Casella casella ) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		return treuFitxa( casella.getFila(), casella.getColumna() );
	}

	/**
	 * Intercanvia la fitxa d'una casella amb la de l'altre jugador i actualitza els comptadors.
	 *
	 * @param casella Casella del tauler.
	 * @return Cert si s'ha intercanviat la fitxa. Fals altrament.
	 * @throws IndexOutOfBoundsException si no és una casella vàlida.
	 * @throws IllegalArgumentException  si la casella és buida (és EstatCasella.BUIDA).
	 */
	public boolean intercanviaFitxa( Casella casella ) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		return intercanviaFitxa( casella.getFila(), casella.getColumna() );
	}

	/**
	 * Consulta els veïns d'una casella.
	 *
	 * @param fila    Fila de la casella dins el tauler.
	 * @param columna Fila de la casella dins el tauler.
	 * @return Una llista amb les caselles veïnes de la casella (fila, columna).
	 */
	public List<Casella> getVeins( int fila, int columna ) throws IndexOutOfBoundsException
	{
		if ( !esCasellaValida( fila, columna ) )
		{
			throw new IndexOutOfBoundsException( "Casella fora del tauler" );
		}
		else
		{
			List<Casella> veins = new LinkedList<Casella>();
			veins.add( new Casella( fila - 1, columna ) );
			veins.add( new Casella( fila - 1, columna + 1 ) );
			veins.add( new Casella( fila, columna - 1 ) );
			veins.add( new Casella( fila, columna + 1 ) );
			veins.add( new Casella( fila + 1, columna ) );
			veins.add( new Casella( fila + 1, columna - 1 ) );
			for ( int i = veins.size() - 1; i >= 0; i-- )
			{
				Casella casella = veins.get( i );
				if ( casella.getFila() < 0 || casella.getFila() >= getMida() || casella.getColumna() < 0 ||
				     casella.getColumna() >= getMida() )
				{
					veins.remove( i );
				}
			}
			return veins;
		}
	}

	/**
	 * Consulta els veïns d'una casella.
	 *
	 * @param casella Casella del tauler.
	 * @return Una llista amb les caselles veïnes de la casella.
	 */
	public List<Casella> getVeins( Casella casella ) throws IndexOutOfBoundsException
	{
		return getVeins( casella.getFila(), casella.getColumna() );
	}
}
