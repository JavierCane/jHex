package prop.hex.domini.models;

import prop.cluster.domini.models.Partida;
import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.Usuari;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.cluster.domini.models.estats.EstatCasella;

import java.io.Serializable;
import java.util.Date;
import java.util.Queue;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

public class PartidaHex extends Partida implements Serializable
{

	private String[] colors;
	private ModesInici mode_inici;


	public PartidaHex(Usuari jugador_a, Usuari jugador_b, Tauler tauler, int torns_jugats, Date data_creacio, String nom, boolean finalitzada, String[] colors, ModesInici mode_inici)
	{
		this.colors = colors;
		this.mode_inici = mode_inici;
	}

	/**
	 * Consulta el color demanat d'entre aquells que té assignats la partida.
	 *
	 * @param numero Índex del color que volem obtenir.
	 * @return Un String amb el nom del color demanat.
	 * @throws IndexOutOfBoundsException si el número de color és més gran que 1, ja que només hi ha dos colors.
	 */
	public String getColors( int numero ) throws IndexOutOfBoundsException
	{
		if ( numero > 1 )
		{
			throw new IndexOutOfBoundsException( "Índex de color massa gran" );
		}
		return colors[numero];
	}

	/**
	 * Modifica un cert color dels que té assignats la partida.
	 *
	 * @param numero Índex del color que volem obtenir.
	 * @param color  Un String amb el nom del color demanat.
	 * @return Cert si el color es modifica. Fals altrament.
	 * @throws IndexOutOfBoundsException si el número de color és més gran que 1, ja que només hi ha dos colors.
	 * @throws IllegalArgumentException  si el nom del color no és vàlid.
	 */
	public boolean setColors( int numero, String color ) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if ( numero > 1 )
		{
			throw new IndexOutOfBoundsException( "Índex de color massa alt" );
		}
		// Ejemplo
		else if ( color != "blau" || color != "vermell" )
		{
			throw new IllegalArgumentException( "Nom de color no vàlid" );
		} this.colors[numero] = color;
		return true;
	}

	/* Again, es necesario?
	public boolean setColors( String[] colors )
	{
		this.colors = colors;
	}
	*/

	/**
	 * Modifica el mode d'inici que té assignat l'usuari.
	 *
	 * @param mode ModeInici que es vol assignar a l'usuari.
	 */
	public void setModeInici( ModesInici mode )
	{
		this.mode_inici = mode;
	}

	/**
	 * Consulta el mode d'inici de la partida que té assignat l'usuari.
	 *
	 * @return Un ModeInici corresponent al mode d'inici preferit per l'usuari.
	 */
	public ModesInici getModeInici()
	{
		return mode_inici;
	}

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
				List<Casella> veins = ( TaulerHex ) tauler.getVeins( actual.getFila(), actual.getColumna() );
				for ( int i = 0; i < veins.size(); i++ )
				{
					Casella veina = veins.get( i );
					if ( !visitats[veina.getFila()][veina.getColumna()] &&
							tauler.getEstatCasella( veina.getFila(), veina.getColumna() ) == estat ))
					{
						cua_bfs.offer( veina );
						visitats[veina.getFila()][veina.getColumna()] = true;
					}
				}
			} boolean costat1 = false;
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
			}
			else
			{
				return EstatPartida.NO_FINALITZADA;
			}
		}
	}
}

