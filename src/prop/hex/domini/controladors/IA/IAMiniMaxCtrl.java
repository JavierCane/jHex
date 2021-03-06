package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IA.auxiliars.CamiMinim;
import prop.hex.domini.controladors.IA.auxiliars.ConnexionsVirtuals;
import prop.hex.domini.controladors.IA.auxiliars.ResistenciaTauler;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

/**
 * Herencia de InteligenciaArtificial que aplica el MiniMax. Implementa la funcioAvaluacio del minimax (de
 * InteligenciaArtificial i mouFitxa i setPartida per a funcionar amb MouFitxaIA
 * <p/>
 * La funció d'Avaluació d'aquesta intel·ligència funciona de la següent manera:
 * Si ens trobem en el primer torn (torn 0) busquem la casella més central possible (sense minimax ni res). Si ens
 * trobem en el segon torn (torn 1), comprovem la millor casella a la llista de contramoviments (moviments_obertura)
 * i, en el seu defecte, retornem la casella més central possible.
 * <p/>
 * A partir d'aquí s'utilitza un minimax que pot seguir dues estratègies (funcions d'avaluació) la passiva i
 * l'agresiva. Si l'enemic té un camí mínim amb cost inferior o igual a quatre, entenem que aviat conseguirà guanyar
 * i prioritzem perjudicar-lo per sobre de guanyar nosaltres, aquesta és l'estratègia agresiva. En cas contrari,
 * utilitzem l'estratègia passiva.
 * <p/>
 * Estratègia passiva: Tenim en compte 3 factors, la resistencia del tauler, el cost del camí mínim i el nombre de
 * connexions virtuals, tant nostres com de l'enemic. Es multiplica cada factor per un pes i aquest és el cost total.
 * <p/>
 * Estratègia agresiva: Té en compte els mateixos 3 factors, però només de l'enemic,
 * també es multiplica cada factor per un pes, similar al cas de la passiva però una mica modificats.
 *
 * @author Marc Junyent Martín (Grup 7.3, Hex)
 */
public final class IAMiniMaxCtrl extends InteligenciaArtificialHexCtrl
{

	/**
	 * Profunditat màxima per al minimax.
	 */
	private static int profunditat_maxima = 2;

	/**
	 * Si l'enemic esta a punt de guanyar, prioritzem perjudicar-lo en lloc de obtenir nosaltres millor posició.
	 */
	private boolean tactica_agresiva;

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

		if ( tactica_agresiva )
		{
			return agressiu( tauler, fitxa_jugador );
		}
		else
		{
			return passiu( tauler, fitxa_jugador );
		}
	}

	/**
	 * Per a cada jugador calculem les connexions virtuals, el camí de cost mínim i la resistencia del tauler.
	 * La funció d'evaluació és:
	 * Eval = ( - cost_camí_mínim_meu + cost_camí_mínim_enemic ) * 500
	 * + ( nombre_connexions_virtuals_meves - nombre_connexions_virtuals_enemic ) * (100 - 2 * torn_actual)
	 * + log(resistencia_enemic/resistencia_meva + 1) * 500;
	 * <p/>
	 * D'aquesta manera és te en compte reduir els nostres costos però també intentar reduir o no augmentar els de
	 * l'enemic, a més, les connexions virtuals perden importancia a mesura que avança la partida,
	 * ja que hem comprovat que a mesura que avança la partida i hi ha més caselles,
	 * el fet d'establir una connexió virtual nova dona cada cop menys ventatges sobre l'enemic,
	 * mentres que al principi si que és útil. És a dir, deixem de crear connexions per a connectar directament.
	 *
	 * @param tauler        Tauler sobre el que calculem l'evaluació.
	 * @param fitxa_jugador Jugador per al que calculem l'evaluació.
	 * @return La puntuació de l'evaluació
	 */
	private int passiu( Tauler tauler, EstatCasella fitxa_jugador )
	{
		int eval;

		ConnexionsVirtuals cv_A = new ConnexionsVirtuals( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );
		ConnexionsVirtuals cv_B = new ConnexionsVirtuals( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );
		CamiMinim cami_minim_A = new CamiMinim( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );
		CamiMinim cami_minim_B = new CamiMinim( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );
		ResistenciaTauler resistencia_A = new ResistenciaTauler( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );
		ResistenciaTauler resistencia_B = new ResistenciaTauler( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );

		int nombre_cv_A = cv_A.getConnexions_virtuals();
		int nombre_cv_B = cv_B.getConnexions_virtuals();
		int long_cami_A = cami_minim_A.evalua();
		int long_cami_B = cami_minim_B.evalua();
		double valor_resistencia_A = resistencia_A.evalua();
		double valor_resistencia_B = resistencia_B.evalua();

		int pes_long_cami = 500;
		int pes_nombre_cv = 100 - 2 * partida.getTornsJugats();
		int pes_resistencia = 500;

		if ( fitxa_jugador == EstatCasella.JUGADOR_A )
		{
			eval = ( -long_cami_A + long_cami_B ) * pes_long_cami;
			eval += ( nombre_cv_A - nombre_cv_B ) * pes_nombre_cv;
			eval += ( int ) ( Math.log( valor_resistencia_B / valor_resistencia_A + 1.0 ) * pes_resistencia );
		}
		else
		{
			eval = ( long_cami_A - long_cami_B ) * pes_long_cami;
			eval += ( -nombre_cv_A + nombre_cv_B ) * pes_nombre_cv;
			eval += ( int ) ( Math.log( valor_resistencia_A / valor_resistencia_B + 1.0 ) * pes_resistencia );
		}

		return eval;
	}

	/**
	 * Per al jugador enemic, calculem les connexions virtuals, el camí de cost mínim i la resistencia del tauler.
	 * <p/>
	 * La funció d'evaluació és:
	 * Eval = cost_camí_mínim_enemic * 100
	 * - nombre_connexions_virtuals_enemic * 30
	 * + resistencia_enemic * 400;
	 * <p/>
	 * L'únic objectiu que es persegueix és augmentar la resistencia del jugador enemic, és a dir,
	 * la dificultat que tè per connectar els dos extrems. Igualment hem comprovat que tenir en compte les connexions
	 * virtuals és útil per desfer empats de puntuació.
	 *
	 * @param tauler        Tauler sobre el que calculem l'evaluació.
	 * @param fitxa_jugador Jugador per al que calculem l'evaluació.
	 * @return La puntuació de l'evaluació.
	 */
	private int agressiu( Tauler tauler, EstatCasella fitxa_jugador )
	{
		if ( fitxa_jugador == EstatCasella.JUGADOR_A )
		{
			CamiMinim cami_minim_B = new CamiMinim( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );
			ConnexionsVirtuals cv_B = new ConnexionsVirtuals( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );
			ResistenciaTauler resistencia_B = new ResistenciaTauler( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );

			return cami_minim_B.evalua() * 100 - cv_B.getConnexions_virtuals() * 30 +
			       ( int ) ( 400.0 * resistencia_B.evalua() );
		}
		else
		{
			CamiMinim cami_minim_A = new CamiMinim( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );
			ConnexionsVirtuals cv_A = new ConnexionsVirtuals( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );
			ResistenciaTauler resistencia_A = new ResistenciaTauler( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );

			return cami_minim_A.evalua() * 100 - cv_A.getConnexions_virtuals() * 30 +
			       ( int ) ( 400.0 * resistencia_A.evalua() );
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
	public Casella obteMoviment( EstatCasella fitxa )
	{
		// En la primera jugada no es fa minimax, en lloc d'això, es mira en la llista de contramoviments o,
		// en el seu defecte, es retorna la casella més central possible.
		if ( partida.getTornsJugats() <= 1 )
		{
			Casella obertura = obertura();
			if ( obertura != null )
			{
				return obertura;
			}
			else
			{
				return posicioCentral();
			}
		}

		//Mirem la distància de l'enemic i disposem la tàctica adequada amb tactia_agresiva.
		int distancia_enemic;
		if ( fitxa == EstatCasella.JUGADOR_A )
		{
			CamiMinim cami_minim_B = new CamiMinim( partida.getTauler(), EstatCasella.JUGADOR_B );
			distancia_enemic = cami_minim_B.evalua();
		}
		else
		{
			CamiMinim cami_minim_A = new CamiMinim( partida.getTauler(), EstatCasella.JUGADOR_A );
			distancia_enemic = cami_minim_A.evalua();
		}
		if ( distancia_enemic <= 4 )
		{
			tactica_agresiva = true;
		}
		else
		{
			tactica_agresiva = false;
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
