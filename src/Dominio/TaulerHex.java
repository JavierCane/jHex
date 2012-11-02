package Dominio;

public class TaulerHex extends Tauler
{

	public TaulerHex()
	{

	}

	public boolean esMovimentValid( EstatCasella fitxa, int fila, int columna ) throws IndexOutOfBoundsException, IllegalArgumentException
	{

	}


	/**
	 * Consulta els veïns d'una casella.
	 *
	 * @param fila    Fila de la casella dins el tauler.
	 * @param columna Fila de la casella dins el tauler.
	 * @return Un array bidimensional amb les dues coordenades de les caselles veïnes.
	 */
	public int[][] getVeins( int fila, int columna ) throws IndexOutOfBoundsException
	{
		if ( !esCasellaValida( fila, columna ) )
		{
			throw new IndexOutOfBoundsException( "Casella fora del tauler" );
		}
	}

	public Object clone()
	}
	}
}
