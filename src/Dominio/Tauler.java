package Dominio;

import java.util.Arrays;

public class Tauler
{

	protected int mida;
	protected EstatCasella[][] tauler;
	protected int fitxes_a;
	protected int fitxes_b;

	/**
	 * Constructor del tauler. Crea un tauler amb la mida desitjada amb totes les caselles buides (EstatCasella.BUIDA).
	 *
	 * @param mida Les dimensions que tindrà el tauler
	 */
	public Tauler( int mida )
	{
		this.mida = mida;
		fitxes_a = 0;
		fitxes_b = 0;

		tauler = new EstatCasella[mida][mida];
		for ( EstatCasella[] fila : tauler )
		{
			Arrays.fill( fila, EstatCasella.BUIDA );
		}
	}

	/**
	 * Constructor que inicialitza el tauler a un estat diferent de l'original. No comprova que els paràmetres siguin
	 * correctes en relació amb el tauler.
	 *
	 * @param mida     Les dimensions del tauler
	 * @param tauler   Un array bidimensional amb l'estat inicial
	 * @param fitxes_a La quantitat de fitxes que té el jugador A al tauler
	 * @param fitxes_b La quantitat de fitxes que té el jugador B al tauler
	 */
	public Tauler( int mida, EstatCasella[][] tauler, int fitxes_a, int fitxes_b )
	{
		this.mida = mida;
		this.fitxes_a = fitxes_a;
		this.fitxes_b = fitxes_b;
		this.tauler = tauler;
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
		return ( fitxes_a + fitxes_b == 0 );
	}

	/**
	 * Consulta les fitxes del jugador A.
	 *
	 * @return La quantitat de fitxes del jugador A.
	 */
	public int getFitxesA()
	{
		return fitxes_a;
	}

	/**
	 * Consulta les fitxes del jugador B.
	 *
	 * @return La quantitat de fitxes del jugador A.
	 */
	public int getFitxesB()
	{
		return fitxes_b;
	}

	/**
	 * Consulta la quantitat de fitxes que hi ha al tauler.
	 *
	 * @return La quantitat total de fitxes que tenen els dos jugadors al tauler.
	 */
	public int getTotalFitxes()
	{
		return fitxes_a + fitxes_b;
	}

	/**
	 * Consulta l'estat d'una casella del tauler.
	 *
	 * @param fila    Fila de la casella del tauler que es vol consultar.
	 * @param columna Columna de la casella del tauler que es vol consultar.
	 * @return L'estat actual de la casella.
	 */
	public EstatCasella getEstatCasella( int fila, int columna )
	{
		return tauler[fila][columna];
	}

	/**
	 * Canvia l'estat de la casella
	 *
	 * @param e       Estat nou de la casella
	 * @param fila    Fila de la casella del tauler que canvia d'estat
	 * @param columna Columna de la casella del tauler que canvia d'estat
	 * @return Cert si el canvi ha estat realitzat amb èxit. Fals altrament.
	 */
	public boolean setEstatCasella( EstatCasella e, int fila, int columna )
	{
		if ( !esCasellaValida( fila, columna ) )
		{
			return false;
		}

		switch ( tauler[fila][columna] )
		{
			case JUGADOR_A:
				fitxes_a--;
				break;
			case JUGADOR_B:
				fitxes_b--;
				break;
			case BUIDA:
				break;
		}

		switch ( e )
		{
			case JUGADOR_A:
				fitxes_a++;
				break;
			case JUGADOR_B:
				fitxes_b++;
				break;
			case BUIDA:
				break;
		}

		tauler[fila][columna] = e;

		return true;
	}

	/**
	 * Intercanvia la fitxa d'una casella amb la de l'altre jugador.
	 *
	 * @param fila    Fila
	 * @param columna
	 * @return Cert si s'ha intercanviat la fitxa. Fals altrament.
	 */
	public boolean intercanviaFitxa( int fila, int columna )
	{
		if ( !esCasellaValida( fila, columna ) || tauler[fila][columna] == EstatCasella.BUIDA )
		{
			return false;
		}
		else if ( tauler[fila][columna] == EstatCasella.JUGADOR_A )
		{
			tauler[fila][columna] = EstatCasella.JUGADOR_B;
			fitxes_a--;
			fitxes_b++;
		}
		else
		{
			tauler[fila][columna] = EstatCasella.JUGADOR_A;
			fitxes_b--;
			fitxes_a++;
		}

		return true;
	}
}
