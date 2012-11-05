package prop.hex.domini.models;

import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;

import java.util.List;
import java.util.LinkedList;

public class TaulerHex extends Tauler
{

	public TaulerHex( int mida )
	{
		super( mida );
	}

	public TaulerHex( int mida, EstatCasella[][] caselles, int num_fitxes_a, int num_fitxes_b )
	{
		super( mida, caselles, num_fitxes_a, num_fitxes_b );
	}

	public TaulerHex( TaulerHex original )
	{
		super( original );
	}


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
	 * Consulta els veïns d'una casella.
	 *
	 * @param fila    Fila de la casella dins el tauler.
	 * @param columna Fila de la casella dins el tauler.
	 * @return Un array bidimensional amb les dues coordenades de les caselles veïnes.
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
				if ( casella.getFila() < 0 || casella.getFila() > getMida() || casella.getColumna() < 0 ||
						casella.getColumna() > getMida() )
				{
					veins.remove( i );
				}
			}
			return veins;
		}
	}

	public Object clone()
	{
		return new TaulerHex( this );
	}
}
