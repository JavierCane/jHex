package prop.hex.domini.controladors;

import prop.cluster.domini.controladors.InteligenciaArtificial;
import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IA.ElementTaulaTransposicions;
import prop.hex.domini.controladors.IA.TwoDistance;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 28/11/12
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class IAHexQueenBeeCtrl extends InteligenciaArtificial implements MouFitxaIA
{

	/**
	 * Partida on juga la instància de la intel·ligència artificial.
	 */
	private PartidaHex partida;

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
/*
		if ( memoria.containsKey( ( ( TaulerHex ) tauler ).hashCode() ) )
		{
			retorn = memoria.get( ( ( TaulerHex ) tauler ).hashCode() ).getPuntuacio( fitxa_jugador );
		}
		else
		{*/
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

		if ( fitxa_jugador == EstatCasella.JUGADOR_A )
		{
			retorn = potencial_b - potencial_a;
		}
		else
		{
			retorn = potencial_a - potencial_b;
		}
		//		memoria.put( ( ( TaulerHex ) tauler ).hashCode(), new ElementTaulaTransposicions( retorn,
		//		fitxa_jugador ) );
		//	}

		return retorn;
	}

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
	 * Retorna un moviment adequat al tauler actual per al jugador indicat per fitxa.
	 * En la primera jugada de cada jugador es busca la posició més central, en les altres es crida a minimax,
	 * la funció d'evaluació pot variar. Si l'enemic es troba a un cami mínim amb cost inferior o igual a 4 es crida
	 * a l'estratègia agresiva, i si no, a la passiva.
	 *
	 * @param fitxa Fitxa que vol col·locar-se al tauler de la partida del paràmetre implícit.
	 * @return La casella on es mouria la fitxa.
	 */
	public Casella mouFitxa( EstatCasella fitxa )
	{
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
