package prop.cluster.domini.controladors;

import prop.cluster.domini.models.Tauler;
import prop.hex.domini.models.Casella;

import java.util.Vector;

public class InteligenciaArtificial
{

	private Casella mejor_jugada;
	private Integer max_profundidad;
	private Integer MaxPlayer;

	public InteligenciaArtificial( int level )
	{

	}


	public Casella minimax( Tauler p, Integer jugador, Integer profundidad )
	{

		return mejor_jugada;
	}

	private Double recursion( Tauler p, Integer jugador, Integer profundidad, Double alpha, Double beta )
	{
		Vector<Casella> moves_list = moves_available( p );

		if ( profundidad == max_profundidad || moves_list.size() == 0 )
		{    //end recursion if we reach the maximum depth or if we can't keep playing.
			return heuristic_value( p, jugador );
		}
		else
		{
			if ( jugador == MaxPlayer )
			{       //if player is max do max    (cambia maxplayer por me)
				for ( int i = 0; i < moves_list.size(); i++ )
				{
					p.colocarFicha( jugador, moves_list.get( i ).getFila(), moves_list.get( i ).getColumna() );  //put the piece
					alpha = Math.max( alpha, recursion( p, 1 - jugador, profundidad + 1, alpha, beta ) );         //do recursion
					p.colocarFicha( null, moves_list.get( i ).getFila(), moves_list.get( i ).getColumna() );    //remove the piece
					if ( alpha >= beta )
					{
						break;
					}
				}
				return alpha;
			}
			else
			{
				for ( int i = 0; i < moves_list.size(); i++ )
				{
					p.colocarFicha( jugador, moves_list.get( i ).getFila(), moves_list.get( i ).getColumna() );  //put the piece
					beta = Math.min( beta, recursion( p, 1 - jugador, profundidad + 1, alpha, beta ) );         //do recursion
					p.colocarFicha( null, moves_list.get( i ).getFila(), moves_list.get( i ).getColumna() );    //remove the piece
					if ( beta <= alpha )
					{
						break;
					}
				}
				return beta;
			}
		}


	}

	private Double heuristic_value( Tauler p, Integer jugador )
	{
		return 0.0;
	}

	private Vector<Casella> moves_available( Tauler p )
	{
		Vector<Casella> moves_list = new Vector<Casella>();

		for ( int i = 0; i < p.getTamano(); i++ )
		{
			for ( int j = 0; j < p.getTamano(); j++ )
			{
				if ( p.getNumJugadorCasilla( i, j ) == null )
				{
					moves_list.add( new Casella( i, j ) );
				}
			}
		}

		return moves_list;
	}
}
