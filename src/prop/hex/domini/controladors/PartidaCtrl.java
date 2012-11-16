package prop.hex.domini.controladors;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.gestors.PartidaHexGstr;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: isaacsanchezbarrera
 * Date: 13/11/2012
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */
public class PartidaCtrl
{

	private static PartidaHex partida_actual = null;
	private static PartidaHexGstr gestor_partida = new PartidaHexGstr();
	private static InteligenciaArtificialHex inteligencia_artificial = new InteligenciaArtificialHex();
	private static EstatCasella[] fitxes_jugadors = {
			EstatCasella.JUGADOR_A,
			EstatCasella.JUGADOR_B
	};

	public static boolean carregaPartida( String identificador_partida )
			throws IOException, ClassNotFoundException, FileNotFoundException
	{
		partida_actual = gestor_partida.carregaElement( identificador_partida );

		return ( partida_actual != null );
	}

	public static boolean guardaPartida() throws FileNotFoundException, IOException
	{
		return gestor_partida.guardaElement( partida_actual, partida_actual.getIdentificadorUnic() );
	}

	public static boolean mouFitxa( int fila, int columna )
	{
		partida_actual.incrementaTornsJugats( 1 );
	}

	public static int[] donaPista( int jugador )
	{
		return inteligencia_artificial.minimax( partida_actual, fitxes_jugadors[jugador], 15 );
	}
}
