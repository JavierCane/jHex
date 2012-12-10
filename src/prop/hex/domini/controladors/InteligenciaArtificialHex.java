package prop.hex.domini.controladors;

import prop.cluster.domini.controladors.InteligenciaArtificial;
import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 10/12/12
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class InteligenciaArtificialHex extends InteligenciaArtificial
{
	/**
	 * Partida on juga la instància de la intel·ligència artificial.
	 */
	protected PartidaHex partida;

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

	public abstract int funcioAvaluacio( Tauler tauler, EstatPartida estat_moviment, int profunditat,
	                            EstatCasella fitxa_jugador );

	public abstract Casella obteMoviment( EstatCasella fitxa );

}
