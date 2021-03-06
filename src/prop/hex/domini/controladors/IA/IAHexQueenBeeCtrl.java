package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IA.auxiliars.ElementTaulaTransposicions;
import prop.hex.domini.controladors.IA.auxiliars.TwoDistance;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.util.HashMap;

/**
 * Intel·ligència artificial per al joc Hex que utilitza un mètode MiniMax amb una funció d'evaluació basada en
 * Two-Distance i similar a la utilitzada en la IA QueenBee <i>(Jack van Rijswijck, Computer Hex: Are Bees Better Than
 * Fruitflies?, 2000)</i>.
 * <p/>
 * En el primer torn s'utilitza una taula d'obertures, a partir d'aquí s'utilitza el miniMax amb una profunditat de 2.
 *
 * @author Marc Junyent Martín (Grup 7.3, Hex)
 */
public final class IAHexQueenBeeCtrl extends InteligenciaArtificialHexCtrl
{

	private HashMap<Integer, ElementTaulaTransposicions> memoria;

	/**
	 * Profunditat màxima per al minimax.
	 */
	private static int profunditat_maxima = 2;

	public IAHexQueenBeeCtrl()
	{
		memoria = new HashMap<Integer, ElementTaulaTransposicions>();
	}

	/**
	 * Funció d'avaluació del MiniMax, si estem en un estat termianl on ja ha guanyat un jugador retornem 1000000 o
	 * -1000000, que son valors prou grans, sinó, apliquem l'estratègia agresiva o la passiva en funció del valor de
	 * la variable tactica_agresiva.
	 *
	 * @param tauler         Objecte de la classe <code>Tauler</code> sobre el qual es disputa una partida.
	 * @param estat_moviment Descriu en quin estat ha quedat <em>tauler</em> en funció de l'últim moviment efectuat
	 *                       sobre aquest.
	 * @param profunditat    És la profunditat a la que s'ha arribat durant l'exploració de les diferents possibilitats de
	 *                       moviment. Cada unitat de <em>profunditat</em> representa un torn jugat de la partida.
	 * @param fitxa_jugador  Indica el jugador de la partida a partir del qual avaluar <em>tauler</em>.
	 * @return La puntuació de l'evaluació
	 */
	public int funcioAvaluacio( Tauler tauler, EstatPartida estat_moviment, int profunditat,
	                            EstatCasella fitxa_jugador )
	{

		int retorn;

		if ( estat_moviment == EstatPartida.GUANYA_JUGADOR_A )
		{
			if ( fitxa_jugador == EstatCasella.JUGADOR_A )
			{
				return 1000000;
			}
			else
			{
				return -1000000;
			}
		}
		else if ( estat_moviment == EstatPartida.GUANYA_JUGADOR_B )
		{
			if ( fitxa_jugador == EstatCasella.JUGADOR_B )
			{
				return 1000000;
			}
			else
			{
				return -1000000;
			}
		}

		TwoDistance distancia_a = new TwoDistance( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );
		TwoDistance distancia_b = new TwoDistance( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );

		int potencial_a = distancia_a.getPotencial();
		int potencial_b = distancia_b.getPotencial();
		int desempat_a = distancia_a.getNombrePotencialsMinims();
		int desempat_b = distancia_b.getNombrePotencialsMinims();

		if ( fitxa_jugador == EstatCasella.JUGADOR_A )
		{
			retorn = 100 * ( potencial_b - potencial_a ) + desempat_b - desempat_a;
		}
		else
		{
			retorn = 100 * ( potencial_a - potencial_b ) + desempat_a - desempat_b;
		}

		return retorn;
	}

	/**
	 * Retorna un moviment adequat al tauler actual per al jugador indicat per fitxa.
	 * En la primera jugada de cada jugador es busca la posició més central, en les altres es crida a minimax,
	 * la funció d'evaluació pot variar. Si l'enemic es troba a un cami mínim amb cost inferior o igual a 4 es crida
	 * a l'estratègia agresiva, i si no, a la passiva.
	 *
	 * @param fitxa Fitxa que vol col·locar-se al tauler de la partida del paràmetre implícit.
	 * @return La casella on es mouria la fitxa.
	 */
	public Casella obteMoviment( EstatCasella fitxa )
	{
		if ( partida.getTornsJugats() <= 1 )
		{
			Casella obertura = obertura();
			if ( obertura != null )
			{
				return obertura;
			}
		}

		//Cridem al minimax.
		int[] casella = super.minimax( partida, fitxa, profunditat_maxima );

		//Retornem la casella.
		if ( casella != null )
		{
			return new Casella( casella[0], casella[1] );
		}
		else
		{
			return null;
		}
	}
}
