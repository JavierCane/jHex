package prop.cluster.domini.models;

import prop.cluster.domini.models.estats.EstatCasella;

import java.util.Arrays;
import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;

public abstract class Tauler
{

	protected int mida;
	protected EstatCasella[][] caselles;
	protected int num_fitxes_a;
	protected int num_fitxes_b;

	/**
	 * Constructor del tauler. Crea un tauler de la mida desitjada amb totes les caselles buides (EstatCasella.BUIDA).
	 *
	 * @param mida Les dimensions que tindrà el tauler
	 */
	public Tauler( int mida )
	{
		this.mida = mida;
		num_fitxes_a = 0;
		num_fitxes_b = 0;

		caselles = new EstatCasella[mida][mida];
		for ( EstatCasella[] fila : caselles )
		{
			Arrays.fill( fila, EstatCasella.BUIDA );
		}
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
	public Tauler( int mida, EstatCasella[][] caselles, int num_fitxes_a, int num_fitxes_b )
	{
		this.mida = mida;
		this.num_fitxes_a = num_fitxes_a;
		this.num_fitxes_b = num_fitxes_b;
		this.caselles = caselles;
	}

	/**
	 * Constructor per còpia. Crea un nou tauler idèntic a original.
	 *
	 * @param original
	 */
	public Tauler( Tauler original )
	{
		this.mida = original.mida;
		this.num_fitxes_a = original.num_fitxes_a;
		this.num_fitxes_b = original.num_fitxes_b;
		this.caselles = new EstatCasella[this.mida][this.mida];

		for ( int i = 0; i < this.mida; ++i )
		{
			System.arraycopy( original.caselles[i], 0, this.caselles[i], 0, this.mida );
		}
	}

	/**
	 * Consulta la mida del tauler
	 *
	 * @return La mida del tauler
	 */
	public int getMida()
	{
		return mida;
	}

	/**
	 * Comprova si una casella és vàlida dins el tauler
	 *
	 * @param fila    Fila de la casella dins el tauler.
	 * @param columna Columna de la casella dins el tauler.
	 * @return Cert si la posició (fila, columna) és una casella vàlida. Fals altrament.
	 */
	public boolean esCasellaValida( int fila, int columna )
	{
		return ( fila >= 0 && fila < mida && columna >= 0 && columna < mida );
	}

	/**
	 * Consulta si el tauler és buit
	 *
	 * @return Cert si el tauler no té cap fitxa. Fals altrament.
	 */
	public boolean esBuit()
	{
		return ( num_fitxes_a + num_fitxes_b == 0 );
	}

	/**
	 * Consulta les fitxes del jugador A.
	 *
	 * @return La quantitat de fitxes del jugador A.
	 */
	public int getNumFitxesA()
	{
		return num_fitxes_a;
	}

	/**
	 * Consulta les fitxes del jugador B.
	 *
	 * @return La quantitat de fitxes del jugador A.
	 */
	public int getNumFitxesB()
	{
		return num_fitxes_b;
	}

	/**
	 * Consulta la quantitat de fitxes que hi ha al tauler.
	 *
	 * @return La quantitat total de fitxes que tenen els dos jugadors al tauler.
	 */
	public int getTotalFitxes()
	{
		return num_fitxes_a + num_fitxes_b;
	}

	/**
	 * Consulta l'estat d'una casella del tauler.
	 *
	 * @param fila    Fila de la casella del tauler que es vol consultar.
	 * @param columna Columna de la casella del tauler que es vol consultar.
	 * @return L'estat actual de la casella.
	 */
	public EstatCasella getEstatCasella( int fila, int columna ) throws IndexOutOfBoundsException
	{
		if ( !esCasellaValida( fila, columna ) )
		{
			throw new IndexOutOfBoundsException( "Casella fora del tauler" );
		}
		return caselles[fila][columna];
	}

	/**
	 * Canvia l'estat d'una casella i actualitza els comptadors.
	 *
	 * @param e       Estat nou de la casella
	 * @param fila    Fila de la casella del tauler que canvia d'estat
	 * @param columna Columna de la casella del tauler que canvia d'estat
	 * @return Cert si el canvi ha estat realitzat amb èxit. Fals altrament.
	 */
	protected boolean setEstatCasella( EstatCasella e, int fila, int columna ) throws IndexOutOfBoundsException
	{
		if ( !esCasellaValida( fila, columna ) )
		{
			throw new IndexOutOfBoundsException( "Casella fora del tauler" );
		}

		switch ( caselles[fila][columna] )
		{
			case JUGADOR_A:
				num_fitxes_a--;
				break;
			case JUGADOR_B:
				num_fitxes_b--;
				break;
			case BUIDA:
				break;
		}

		switch ( e )
		{
			case JUGADOR_A:
				num_fitxes_a++;
				break;
			case JUGADOR_B:
				num_fitxes_b++;
				break;
			case BUIDA:
				break;
		}

		caselles[fila][columna] = e;

		return true;
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
	public abstract boolean esMovimentValid( EstatCasella fitxa, int fila, int columna )
			throws IndexOutOfBoundsException, IllegalArgumentException;

	/**
	 * Mou la fitxa a la casella indicada i actualitza els comptadors.
	 *
	 * @param fitxa   Fitxa que es vol col·locar.
	 * @param fila    Fila de la casella dins el tauler.
	 * @param columna Columna de la casella dins el tauler.
	 * @return Cert si s’ha realitzat el moviment. Fals altrament.
	 * @throws IndexOutOfBoundsException si (fila, columna) no és una casella vàlida.
	 * @throws IllegalArgumentException  si fitxa no és de cap jugador (és EstatCasella.BUIDA)  o el moviment no és
	 *                                   vàlid.
	 */
	public boolean mouFitxa( EstatCasella fitxa, int fila, int columna )
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if ( !esMovimentValid( fitxa, fila, columna ) )
		{
			throw new IllegalArgumentException( "El moviment no segueix la normativa" );
		}
		else
		{
			setEstatCasella( fitxa, fila, columna );
			return true;
		}
	}

	/**
	 * Treu la fitxa de la casella indicada i actualitza els comptadors.
	 *
	 * @param fila    Fila de la casella dins el tauler.
	 * @param columna Columna de la casella dins el tauler.
	 * @return Cert si s’ha realitzat el moviment. Fals altrament.
	 * @throws IndexOutOfBoundsException si (fila, columna) no és una casella vàlida.
	 * @throws IllegalArgumentException  si la casella és buida (és EstatCasella.BUIDA).
	 */
	public boolean treuFitxa( int fila, int columna ) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if ( !esCasellaValida( fila, columna ) )
		{
			throw new IndexOutOfBoundsException( "Casella fora del tauler" );
		}
		else if ( caselles[fila][columna] == EstatCasella.BUIDA )
		{
			throw new IllegalArgumentException( "La casella ja és buida" );
		}
		else
		{
			setEstatCasella( EstatCasella.BUIDA, fila, columna );
			return true;
		}
	}

	/**
	 * Intercanvia la fitxa d'una casella amb la de l'altre jugador i actualitza els comptadors.
	 *
	 * @param fila    Fila de la casella dins el tauler.
	 * @param columna Columna de la casella dins el tauler.
	 * @return Cert si s'ha intercanviat la fitxa. Fals altrament.
	 * @throws IndexOutOfBoundsException si (fila, columna) no és una casella vàlida.
	 * @throws IllegalArgumentException  si la casella és buida (és EstatCasella.BUIDA).
	 */
	public boolean intercanviaFitxa( int fila, int columna ) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if ( !esCasellaValida( fila, columna ) )
		{
			throw new IndexOutOfBoundsException( "Casella fora del tauler" );
		}
		else if ( caselles[fila][columna] == EstatCasella.BUIDA )
		{
			throw new IllegalArgumentException( "Intercanvi de fitxes en casella buida" );
		}
		else if ( caselles[fila][columna] == EstatCasella.JUGADOR_A )
		{
			setEstatCasella( EstatCasella.JUGADOR_B, fila, columna );
		}
		else
		{
			setEstatCasella( EstatCasella.JUGADOR_B, fila, columna );
		}

		return true;
	}

	/**
	 * Crea una còpia de l’objecte. Per guardar-lo en un objecte Tauler cal fer un cast.
	 *
	 * @return Una còpia del paràmetre implícit. És equivalent a fer servir el constructor per còpia.
	 */
	public abstract Object clone();

	public void colocarFicha( Integer jugador, Integer fila, Integer columna )
	{
	}

	public int getTamano()
	{
		return 0;
	}

	public Object getNumJugadorCasilla( int i, int j )
	{
		return null;  //To change body of created methods use File | Settings | File Templates.
	}
}
