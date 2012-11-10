package prop.hex.domini.models;

import prop.cluster.domini.models.Partida;
import prop.cluster.domini.models.Usuari;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.ModesInici;

import java.io.Serializable;
import java.util.*;

public class PartidaHex extends Partida implements Serializable
{

	private static final long serialVersionUID = 2232229330252834467L;
	private TaulerHex tauler;
	private ModesInici mode_inici;
	private CombinacionsColors combinacio_colors;

	public PartidaHex( Usuari jugador_a, Usuari jugador_b, TaulerHex tauler, String nom,
	                   CombinacionsColors combinacio_colors, ModesInici mode_inici )
	{
		super( jugador_a, jugador_b, tauler, nom );
		this.tauler = tauler;
		this.combinacio_colors = combinacio_colors;
		this.mode_inici = mode_inici;
	}

	private final static int max_num_pistes = 3;

	private HashMap<String, Integer> pistes_usades;

	private HashMap<String, Integer> temps_de_joc;


	public EstatPartida comprovaEstatPartida( int fila, int columna ) throws IndexOutOfBoundsException
	{
		if ( !tauler.esCasellaValida( fila, columna ) )
		{
			throw new IndexOutOfBoundsException( "Casella fora del tauler" );
		}
		else if ( tauler.getEstatCasella( fila, columna ) == EstatCasella.BUIDA )
		{
			return EstatPartida.NO_FINALITZADA;
		}
		else
		{
			EstatCasella estat = tauler.getEstatCasella( fila, columna );

			boolean[][] visitats = new boolean[tauler.getMida()][tauler.getMida()];

			for ( boolean[] fila_visitats : visitats )
			{
				Arrays.fill( fila_visitats, false );
			}

			Queue<Casella> cua_bfs = new LinkedList<Casella>();

			cua_bfs.offer( new Casella( fila, columna ) );

			visitats[fila][columna] = true;

			while ( cua_bfs.peek() != null )
			{
				Casella actual = cua_bfs.remove();
				List<Casella> veins = ( ( TaulerHex ) tauler ).getVeins( actual.getFila(), actual.getColumna() );

				for ( int i = 0; i < veins.size(); i++ )
				{
					Casella veina = veins.get( i );
					if ( !visitats[veina.getFila()][veina.getColumna()] &&
					     tauler.getEstatCasella( veina.getFila(), veina.getColumna() ) == estat )
					{
						cua_bfs.offer( veina );
						visitats[veina.getFila()][veina.getColumna()] = true;
					}
				}
			}

			boolean costat1 = false;
			boolean costat2 = false;

			if ( estat == EstatCasella.JUGADOR_A )
			{
				for ( int i = 0; i < tauler.getMida(); i++ )
				{
					if ( visitats[0][i] )
					{
						costat1 = true;
					}

					if ( visitats[tauler.getMida() - 1][i] )
					{
						costat2 = true;
					}
				}
			}
			else if ( estat == EstatCasella.JUGADOR_B )
			{
				for ( int i = 0; i < tauler.getMida(); i++ )
				{
					if ( visitats[i][0] )
					{
						costat1 = true;
					}

					if ( visitats[i][tauler.getMida() - 1] )
					{
						costat2 = true;
					}
				}
			}

			if ( !costat1 && !costat2 )
			{
				if ( estat == EstatCasella.JUGADOR_A )
				{
					return EstatPartida.GUANYA_JUGADOR_A;
				}
				else if ( estat == EstatCasella.JUGADOR_B )
				{
					return EstatPartida.GUANYA_JUGADOR_B;
				}
				else
				{
					return EstatPartida.NO_FINALITZADA;
				}
			}
			else
			{
				return EstatPartida.NO_FINALITZADA;
			}
		}
	}

	public static int getMaxNumPistes()
	{
		return max_num_pistes;
	}

	/**
	 * Consulta el nombre de pistes usades d'un jugador
	 *
	 * @param id_jugador Identificador únic del jugador.
	 * @return El nombre de pistes utilitzades pel jugador.
	 * @throws IllegalArgumentException Si el jugador amb l'identificador únic passat com a paràmetre no juga la
	 * partida.
	 */
	public int getPistesUsades( String id_jugador ) throws IllegalArgumentException
	{
		if ( !pistes_usades.containsKey( id_jugador ) )
		{
			throw new IllegalArgumentException( "El jugador no juga la partida." );
		}
		else
		{
			return pistes_usades.get( id_jugador );
		}
	}

	/**
	 * Consulta el temps de joc d'un jugador.
	 *
	 * @param id_jugador Identificador únic del jugador.
	 * @return El temps de joc que el jugador ha utilitzat a la partida.
	 * @throws IllegalArgumentException Si el jugador amb l'identificador únic passat com a paràmetre no juga la
	 * partida.
	 */
	public int getTempsDeJoc( String id_jugador ) throws IllegalArgumentException
	{
		if ( !temps_de_joc.containsKey( id_jugador ) )
		{
			throw new IllegalArgumentException( "El jugador no juga la partida." );
		}
		else
		{
			return temps_de_joc.get( id_jugador );
		}
	}

	/**
	 * Modifica les pistes usades per l'usuari amb id_jugador
	 *
	 * @param id_jugador Identificador únic del jugador
	 * @param num_pistes Número de pistes usades pel jugador
	 * @return Cert si es modifiquen les pistes. Fals altrament.
	 * @throws IllegalArgumentException
	 */
	public boolean setPistesUsades( String id_jugador, int num_pistes ) throws IllegalArgumentException
	{
		if ( !pistes_usades.containsKey( id_jugador ) )
		{
			throw new IllegalArgumentException( "El jugador no juga la partida." );
		}
		else
		{
			pistes_usades.put( id_jugador, num_pistes );
			return true;
		}
	}
}
