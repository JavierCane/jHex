package prop.hex.domini.controladors.IA;

import prop.cluster.domini.controladors.InteligenciaArtificial;
import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;

/**
 * Classe abstracta de la que hereden tots els jugadors de IA. Serveix com a pont per a que les clases de jugadors de
 * IA siguin transparents per al controlador de partida.
 *
 * @author Marc Junyent Martín (Grup 7.3, Hex)
 */
public abstract class InteligenciaArtificialHexCtrl extends InteligenciaArtificial
{

	/**
	 * Partida on juga la instància de la intel·ligència artificial.
	 */
	protected PartidaHex partida;

	/**
	 * Array de "contramoviments" si és el 2n torn.
	 */
	protected Casella[][] moviments_obertura = {
			{
					new Casella( 2, 3 ),
					new Casella( 4, 2 ),
					new Casella( 3, 3 ),
					null,
					new Casella( 1, 5 ),
					null,
					null
			},
			{
					new Casella( 2, 3 ),
					new Casella( 4, 2 ),
					null,
					null,
					null,
					null,
					new Casella( 1, 5 )
			},
			{
					new Casella( 2, 3 ),
					null,
					null,
					null,
					null,
					null,
					new Casella( 1, 5 )
			},
			{
					new Casella( 1, 5 ),
					new Casella( 2, 4 ),
					null,
					null,
					null,
					new Casella( 4, 2 ),
					new Casella( 1, 5 ),
			},
			{
					new Casella( 1, 5 ),
					null,
					null,
					null,
					null,
					null,
					new Casella( 3, 2 )
			},
			{
					new Casella( 1, 5 ),
					null,
					null,
					null,
					null,
					new Casella( 2, 4 ),
					new Casella( 3, 2 ),
			},
			{
					null,
					null,
					new Casella( 5, 1 ),
					null,
					new Casella( 2, 3 ),
					new Casella( 2, 4 ),
					new Casella( 3, 2 )
			}
	};

	/**
	 * Configura la instància de partida per a la intel·ligència artificial.
	 *
	 * @param partida Partida on es vol jugar amb la intel·ligència artificial.
	 * @return Cert si s'ha canviat de partida. Fals altrament.
	 */
	public boolean setPartida( PartidaHex partida )
	{
		if ( partida != null )
		{
			this.partida = partida;
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Funció abstracta d'avaluació que crida el miniMax.
	 *
	 * @param tauler         Objecte de la classe <code>Tauler</code> sobre el qual es disputa una partida.
	 * @param estat_moviment Descriu en quin estat ha quedat <em>tauler</em> en funció de l'últim moviment efectuat
	 *                       sobre aquest.
	 * @param profunditat    És la profunditat a la que s'ha arribat durant l'exploració de les diferents possibilitats de
	 *                       moviment. Cada unitat de <em>profunditat</em> representa un torn jugat de la partida.
	 * @param fitxa_jugador  Indica el jugador de la partida a partir del qual avaluar <em>tauler</em>.
	 * @return Un enter indicant l'avaulació de <em>tauler</em>.
	 */
	public abstract int funcioAvaluacio( Tauler tauler, EstatPartida estat_moviment, int profunditat,
	                                     EstatCasella fitxa_jugador );

	/**
	 * Funció abstracta que calcula el millor moviment en una partida.
	 *
	 * @param fitxa
	 * @return La casella on es mouria la fitxa.
	 */
	public abstract Casella obteMoviment( EstatCasella fitxa );

	/**
	 * Obté la casella més central on és possible moure.
	 *
	 * @return La casella més propera al centre on es pot moure.
	 */
	protected Casella posicioCentral()
	{
		TaulerHex tauler = partida.getTauler();

		for ( int fila = 0; fila < tauler.getMida() / 2; fila++ )
		{
			for ( int columna = 0; columna < tauler.getMida() / 2; columna++ )
			{
				if ( tauler.esMovimentValid( EstatCasella.JUGADOR_A, tauler.getMida() / 2 + fila,
						tauler.getMida() / 2 + columna ) )
				{
					return new Casella( tauler.getMida() / 2 + fila, tauler.getMida() / 2 + columna );
				}
				if ( tauler.esMovimentValid( EstatCasella.JUGADOR_A, tauler.getMida() / 2 - fila,
						tauler.getMida() / 2 - columna ) )
				{
					return new Casella( tauler.getMida() / 2 - fila, tauler.getMida() / 2 - fila );
				}
			}
		}

		//si el tauler no esta ple no hauriem d'arribar mai aqui.
		return new Casella( 0, 0 );
	}

	/**
	 * Calcula una jugada basant-se en el llibre d'obertures.
	 *
	 * @return La casella de resposta, si n'hi ha, o null.
	 */
	protected Casella obertura()
	{
		if ( partida.getTornsJugats() == 0 )
		{
			return new Casella( 4, 2 );
		}

		Casella fitxa_posada = null;

		// Busquem quina és la fitxa que ha posat l'altre jugador
		int fila = 0;
		while ( fitxa_posada == null && fila < partida.getTauler().getMida() )
		{
			int columna = 0;
			while ( fitxa_posada == null && columna < partida.getTauler().getMida() )
			{
				if ( partida.getTauler().getEstatCasella( fila, columna ) != EstatCasella.BUIDA )
				{
					fitxa_posada = new Casella( fila, columna );
				}

				columna++;
			}

			fila++;
		}

		if ( fitxa_posada != null )
		{
			if ( moviments_obertura[fitxa_posada.getFila()][fitxa_posada.getColumna()] != null )
			{
				return moviments_obertura[fitxa_posada.getFila()][fitxa_posada.getColumna()];
			}
		}

		return null;
	}
}
