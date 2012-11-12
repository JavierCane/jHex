package prop.hex.domini.controladors;

import prop.cluster.domini.controladors.InteligenciaArtificial;
import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IA.GrupCaselles;
import prop.hex.domini.controladors.IA.ConnexionsVirtuals;
import prop.hex.domini.models.TaulerHex;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 10/11/12
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
public class InteligenciaArtificialHex extends InteligenciaArtificial
{
	public int funcioAvaluacio( Tauler tauler, EstatPartida estat_moviment, int profunditat,
	                            EstatCasella fitxa_jugador ) {
		ConnexionsVirtuals cv = new ConnexionsVirtuals((TaulerHex) tauler, fitxa_jugador);

		return cv.getConnexions_virtuals()*4+cv.getConnexions_semivirtuals();
	}

}
