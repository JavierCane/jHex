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

	/**
	 * Tauler de la partida
	 */
	private TaulerHex tauler;

	/**
	 * Constructora alternativa per partides que no han estat jugades
	 *
	 * @param jugador_a Usuari que fa de jugador A
	 * @param jugador_b Usuari que fa de jugador B
	 * @param tauler    Tauler on es desenvolupa la partida
	 * @param nom       Nom de la partida
	 */
	public PartidaHex( Usuari jugador_a, Usuari jugador_b, TaulerHex tauler, String nom )
	{
		super( jugador_a, jugador_b, tauler, nom );
		this.tauler = tauler;
	}

	/**
	 * Mètode consultor de l’estat de la partida. Els paràmetres permeten aportar informació a sobre de l’últim moviment
	 * d’interès realitzat (normalment l’últim realitzat correctament).
	 *
	 * @param fila    Fila del moviment d'interès
	 * @param columna Columna del moviment d'interès
	 * @return L'estat de la partida
	 * @throws IndexOutOfBoundsException si (fila, columna) no és una coordenada dins dels límits del tauler on es
	 *                                   desenvolupa la partida
	 */
	public EstatPartida comprovaEstatPartida( int fila, int columna ) throws IndexOutOfBoundsException
	{
		if ( tauler.getEstatCasella( fila, columna ) == EstatCasella.BUIDA )
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
				List<Casella> veins = tauler.getVeins( actual.getFila(), actual.getColumna() );

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

			if ( estat == EstatCasella.JUGADOR_B )
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
			else if ( estat == EstatCasella.JUGADOR_A )
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

			if ( costat1 && costat2 )
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
}

