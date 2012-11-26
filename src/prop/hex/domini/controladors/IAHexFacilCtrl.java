package prop.hex.domini.controladors;

import prop.cluster.domini.controladors.InteligenciaArtificial;
import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IA.CamiMinim;
import prop.hex.domini.controladors.IA.ConnexionsVirtuals;
import prop.hex.domini.controladors.IA.ResistenciaTauler;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;

/**
 * Herencia de InteligenciaArtificial que aplica el MiniMax. Implementa la funcioAvaluacio del minimax (de
 * InteligenciaArtificial i mouFitxa i setPartida per a funcionar amb MouFitxaIA
 * <p/>
 * La funció d'Avaluació d'aquesta inteligencia funciona de la següent manera:
 * Si ens trobem en el primer torn (torn 0 o torn 1), és a dir, no hem col·locat cap fitxa,
 * busquem la casella més centarl possible (sense fer minimax ni re).
 * <p/>
 * A partir d'aqui s'utilitza un minimax que pot seguir dues estrategies (funcions d'evaluació) la passiva i
 * l'agresiva. Si l'enemic té un camí mínim amb cost inferior o igual a quatre, entenem que aviat conseguirà guanyar
 * i prioritzem perjudicar-lo per sobre de guanyar nosaltres, aquesta és l'estratègia agresiva. En cas contrari,
 * utilitzem l'estratègia passiva.
 * <p/>
 * Estratègia passiva: Tenim en compte 3 factors, la resistencia del tauler, el cost del camí mínim i el nombre de
 * connexions virtuals, tant nostres com de l'enemic. Es multiplica cada factor per un pes i aquest és el cost total.
 * <p/>
 * Estratègia agresiva: Té en compte els mateixos 3 factors, però només de l'enemic,
 * també és multiplica cada factor per un pes, similar al cas de la passiva però una mica modificats.
 */
public class IAHexFacilCtrl extends InteligenciaArtificial implements MouFitxaIA
{

	/**
	 * Partida on juga la instància de la intel·ligència artificial.
	 */
	private PartidaHex partida;

	/**
	 * Profunditat màxima per al minimax.
	 */
	private static int profunditat_maxima = 2;

	/**
	 * Si l'enemic esta a punt de guanyar, prioritzem perjudicar-lo en lloc de obtenir nosaltres millor posició.
	 */
	private boolean tactica_agresiva;

	/**
	 * Array de "contramoviments" si és el 2n torn.
	 */
	private Casella[][] moviments_obertura = {
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
	 * Obté la casella més central on és possible moure.
	 *
	 * @return La casella més propera al centre on es pot moure.
	 */
	private Casella posicioCentral()
	{
		TaulerHex tauler = ( TaulerHex ) partida.getTauler();

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
		// En la primera jugada no es fa minimax, en lloc d'això, es mira en la llista de contramoviments o,
		// en el seu defecte, es retorna la casella més central possible.
		if ( partida.getTornsJugats() <= 1 )
		{
			if ( partida.getTornsJugats() == 1 )
			{
				Casella moviment = null;
				if ( partida.getTornsJugats() == 1 )
				{
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
				}
			}

			return posicioCentral();
		}

		//Mirem la distància de l'enemic i disposem la tàctica adequada amb tactia_agresiva.
		int distancia_enemic;
		if ( fitxa == EstatCasella.JUGADOR_A )
		{
			CamiMinim cami_minim_B = new CamiMinim( ( TaulerHex ) partida.getTauler(), EstatCasella.JUGADOR_B );
			distancia_enemic = cami_minim_B.evalua();
		}
		else
		{
			CamiMinim cami_minim_A = new CamiMinim( ( TaulerHex ) partida.getTauler(), EstatCasella.JUGADOR_A );
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
