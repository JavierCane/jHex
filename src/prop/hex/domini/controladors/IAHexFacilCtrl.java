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
	private int profunditat_maxima = 2;

	/**
	 * Si l'enemic esta a punt de guanyar, prioritzem perjudicar-lo en lloc de obtenir nosaltres millor posició.
	 */
	private boolean tactica_agresiva;

	/**
	 * @param tauler         Objecte de la classe <code>Tauler</code> sobre el qual es disputa una partida.
	 * @param estat_moviment Descriu en quin estat ha quedat <em>tauler</em> en funció de l'últim moviment efectuat
	 *                       sobre aquest.
	 * @param profunditat    És la profunditat a la que s'ha arribat durant l'exploració de les diferents possibilitats de
	 *                       moviment. Cada unitat de <em>profunditat</em> representa un torn jugat de la partida.
	 * @param fitxa_jugador  Indica el jugador de la partida a partir del qual avaluar <em>tauler</em>.
	 * @return
	 */

	public int funcioAvaluacio( Tauler tauler, EstatPartida estat_moviment, int profunditat, EstatCasella fitxa_jugador )
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

		if(tactica_agresiva)
		{
			return agressiu(tauler, fitxa_jugador);
		}
		else
		{
			return passiu(tauler, fitxa_jugador);
		}

	}

	private int passiu( Tauler tauler, EstatCasella fitxa_jugador)
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
			//	eval += ( nombre_cv_A ) * pes_nombre_cv;
			//		eval += ( cv_A.getConnexions_semivirtuals() - cv_B.getConnexions_semivirtuals() ) * ( 10 - partida.getTornsJugats() / 4 );
			eval += ( int ) ( Math.log( valor_resistencia_B / valor_resistencia_A + 1.0 ) * pes_resistencia );

			//	eval = (30*long_cami_B)/long_cami_A + nombre_cv_A;
		}
		else
		{
			eval = ( long_cami_A - long_cami_B ) * pes_long_cami;
			eval += ( -nombre_cv_A + nombre_cv_B ) * pes_nombre_cv;
			//	eval += ( nombre_cv_B ) * pes_nombre_cv;
			//		eval += ( -cv_A.getConnexions_semivirtuals() + cv_B.getConnexions_semivirtuals() ) * ( 10 - partida.getTornsJugats() / 4 );
			eval += ( int ) ( Math.log( valor_resistencia_A / valor_resistencia_B + 1.0 ) * pes_resistencia );

			//	eval = (30*long_cami_A)/long_cami_B + nombre_cv_B;
		}

//		eval = 30*long_cami_B +

		return eval + 2000;

//		return -cami_minim.evalua()*16 + cv.getConnexions_virtuals()*6 + cv.getConnexions_semivirtuals();
//		return cv.getConnexions_virtuals() * 4 + cv.getConnexions_semivirtuals();
	}


	private int agressiu( Tauler tauler, EstatCasella fitxa_jugador)
	{
		if ( fitxa_jugador == EstatCasella.JUGADOR_A )
		{
			CamiMinim cami_minim_B = new CamiMinim( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );
			ConnexionsVirtuals cv_B = new ConnexionsVirtuals( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );
			ResistenciaTauler resistencia_B = new ResistenciaTauler( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );

			return cami_minim_B.evalua()*100 - cv_B.getConnexions_virtuals()*30 + (int)(1000.0*resistencia_B.evalua());
		}
		else
		{
			CamiMinim cami_minim_A = new CamiMinim( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );
			ConnexionsVirtuals cv_A = new ConnexionsVirtuals( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );
			ResistenciaTauler resistencia_A = new ResistenciaTauler( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );

			return cami_minim_A.evalua()*100 - cv_A.getConnexions_virtuals()*30 + (int)(1000.0*resistencia_A.evalua());
		}
	}


	/**
	 * Obté la casella més central possible.
	 *
	 * @return
	 */
	private Casella posicioCentral()
	{
		TaulerHex tauler = ( TaulerHex ) partida.getTauler();

		for ( int fila = 0; fila < tauler.getMida() / 2; fila++ )
		{
			for ( int columna = 0; columna < tauler.getMida() / 2; columna++ )
			{
				if ( tauler.getEstatCasella( tauler.getMida() / 2 + fila, tauler.getMida() / 2 + columna ) == EstatCasella.BUIDA )
				{
					return new Casella( tauler.getMida() / 2 + fila, tauler.getMida() / 2 + columna );
				}
				if ( tauler.getEstatCasella( tauler.getMida() / 2 - fila, tauler.getMida() / 2 - columna ) == EstatCasella.BUIDA )
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
	 * @param fitxa Fitxa que vol col·locar-se al tauler de la partida del paràmetre implícit.
	 * @return La casella on es mouria la fitxa.
	 */
	public Casella mouFitxa( EstatCasella fitxa )
	{
		// En la primera jugada no es fa minimax, en lloc d'això, és mira la casella més central possible.
		if ( partida.getTornsJugats() <= 1 )
		{
			return posicioCentral();
		}

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
		if ( distancia_enemic <= 3 )
		{
			tactica_agresiva = true;
		}
		else
		{
			tactica_agresiva = false;
		}

		int[] casella = super.minimax( partida, fitxa, profunditat_maxima );

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
