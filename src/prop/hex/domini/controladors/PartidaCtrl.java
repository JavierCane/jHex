package prop.hex.domini.controladors;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;
import prop.hex.domini.models.UsuariHex;
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
	private static Casella darrera_fitxa = null;
	private static String[] id_jugadors;
	private static boolean[] jugadors_humans = null;

	public static boolean carregaPartida( String identificador_partida )
			throws IOException, ClassNotFoundException, FileNotFoundException
	{
		partida_actual = gestor_partida.carregaElement( identificador_partida );
		gestor_partida.eliminaElement( identificador_partida );
		inicialitzaEstructuresControl( ( UsuariHex ) partida_actual.getJugadorA(),
				( UsuariHex ) partida_actual.getJugadorB() );
		darrera_fitxa = null;

		return ( partida_actual != null );
	}

	private static void inicialitzaEstructuresControl( UsuariHex jugador_a, UsuariHex jugador_b )
	{
		id_jugadors = new String[2];
		id_jugadors[0] = jugador_a.getIdentificadorUnic();
		id_jugadors[1] = jugador_b.getIdentificadorUnic();

		jugadors_humans = new boolean[2];
		jugadors_humans[0] = jugador_a.esUsuariHuma();
		jugadors_humans[1] = jugador_b.esUsuariHuma();
	}

	public static boolean guardaPartida() throws FileNotFoundException, IOException, UnsupportedOperationException
	{
		if ( partida_actual.estaFinalitzada() )
		{
			throw new UnsupportedOperationException( "La partida ja ha finalitzat" );
		}

		return gestor_partida.guardaElement( partida_actual, partida_actual.getIdentificadorUnic() );
	}

	public static boolean tancaPartida()
	{
		darrera_fitxa = null;
		partida_actual = null;
		id_jugadors = null;
		jugadors_humans = null;
		return ( partida_actual == null && darrera_fitxa == null && id_jugadors == null && jugadors_humans == null );
	}

	/**
	 * @param mida_tauler
	 * @param jugador_a
	 * @param jugador_b
	 * @param nom
	 * @return
	 */
	public static boolean inicialitzaPartida( int mida_tauler, UsuariHex jugador_a, UsuariHex jugador_b, String nom )
	{
		TaulerHex tauler = new TaulerHex( mida_tauler );
		partida_actual = new PartidaHex( jugador_a, jugador_b, tauler, nom );

		inicialitzaEstructuresControl( jugador_a, jugador_b );

		return true;
	}

	private static int[] movimentIA()
	{
		return inteligencia_artificial
				.minimax( partida_actual, fitxes_jugadors[partida_actual.getTornsJugats() % 2], 15 );
	}

	public static int[] obtePista()
	{

	}

	public static int[] executaMovimentIA()
	{
		int[] resultat_moviment = movimentIA();
		mouFitxa( resultat_moviment[0], resultat_moviment[1] );

		return resultat_moviment;
	}

	/**
	 * Mou la fitxa del jugador que toca segons el torn a la casella indicada per (fila, columna). A més,
	 * si l'altre jugador és una intel·ligència artificial executa automàticament el seu moviment si s'escau.
	 *
	 * @param fila    Fila de la casella
	 * @param columna Columna de la casella
	 * @return
	 */
	public static boolean mouFitxa( int fila, int columna )
	{
		if ( partida_actual.estaFinalitzada() )
		{
			throw new UnsupportedOperationException( "La partida ja ha finalitzat" );
		}

		if ( partida_actual.getTauler()
				.mouFitxa( fitxes_jugadors[partida_actual.getTornsJugats() % 2], fila, columna ) )
		{
			darrera_fitxa = new Casella( fila, columna );
			partida_actual.incrementaTornsJugats( 1 );

			if ( consultaEstatPartida() == EstatPartida.NO_FINALITZADA && )
			{

			}
		}
	}

	public static boolean esTornHuma()
	{

	}

	public static EstatPartida consultaEstatPartida()
	{
		EstatPartida estat_partida =
				partida_actual.comprovaEstatPartida( darrera_fitxa.getFila(), darrera_fitxa.getColumna() );

		partida_actual.setFinalitzada( estat_partida != EstatPartida.NO_FINALITZADA );

		return estat_partida;
	}

	public static boolean intercanviaDarreraFitxa()
	{
		if ( partida_actual.getTauler().intercanviaFitxa( darrera_fitxa.getFila(), darrera_fitxa.getColumna() ) )
		{
			return partida_actual.incrementaTornsJugats( 1 );
		}
	}
}
