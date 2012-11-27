package prop.hex.domini.controladors.drivers;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.controladors.IA.TwoDistance;
import prop.hex.domini.models.TaulerHex;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 23/11/12
 * Time: 08:28
 * To change this template use File | Settings | File Templates.
 */
public class IAQueenBeeDrvr
{
	public static void TwoDistanceTest()
	{
		TaulerHex tauler = new TaulerHex(5);

		tauler.mouFitxa( EstatCasella.JUGADOR_A, 1, 2);

		System.out.println("AAAAAAAA");
		TwoDistance distancia_A = new TwoDistance(tauler, EstatCasella.JUGADOR_A);
		System.out.println();
		distancia_A.shout();

		System.out.println("BBBBBBBB");
		TwoDistance distancia_B = new TwoDistance(tauler, EstatCasella.JUGADOR_B);
		System.out.println();
		distancia_B.shout();

	}


}
