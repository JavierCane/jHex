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
	private static int profunditat_maxima = 2;

	public int funcioAvaluacio( Tauler tauler, EstatPartida estat_moviment, int profunditat, EstatCasella fitxa_jugador )
	{
/*
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
		else
		{
			ConnexionsVirtuals cv = new ConnexionsVirtuals( ( TaulerHex ) tauler, fitxa_jugador );
			CamiMinim cami_minim_A = new CamiMinim( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );
			CamiMinim cami_minim_B = new CamiMinim( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );

			if ( fitxa_jugador == EstatCasella.JUGADOR_A )
			{
				d1 = (int) (1000.0*30.0 * ( double ) cami_minim_B.evalua() / ( double ) cami_minim_A.evalua());
				d2 = 1000*cv.getConnexions_virtuals() * 2;
				d3 = ( int ) ( 1000.0 * ( 30.0 * ( double ) cami_minim_B.evalua() / ( double ) cami_minim_A.evalua() + cv.getConnexions_virtuals() * 2 ) );

				return ( int ) ( 1000.0 * ( 30.0 * ( double ) cami_minim_B.evalua() / ( double ) cami_minim_A.evalua() + cv.getConnexions_virtuals() * 2 ) );
			}
			else
			{
				d1 = (int) (1000.0 * 30.0 * ( double ) cami_minim_A.evalua() / ( double ) cami_minim_B.evalua());
				d2 = 1000*cv.getConnexions_virtuals() * 2;
				d3 = ( int ) ( 1000.0 * ( 30.0 * ( double ) cami_minim_A.evalua() / ( double ) cami_minim_B.evalua() + cv.getConnexions_virtuals() * 2 ) );
				return ( int ) ( 1000.0 * ( 30.0 * ( double ) cami_minim_A.evalua() / ( double ) cami_minim_B.evalua() + cv.getConnexions_virtuals() * 2 ) );
			}
		}
*/

//		ConnexionsVirtuals cv = new ConnexionsVirtuals( ( TaulerHex ) tauler, fitxa_jugador );

//		E = log(RB/RW).
/*		double eval;
		ResistenciaTauler resistencia_A = new ResistenciaTauler( ( TaulerHex ) tauler, EstatCasella.JUGADOR_A );
		ResistenciaTauler resistencia_B = new ResistenciaTauler( ( TaulerHex ) tauler, EstatCasella.JUGADOR_B );

		double valor_resistencia_A = resistencia_A.evalua();
		double valor_resistencia_B = resistencia_B.evalua();

		if ( fitxa_jugador == EstatCasella.JUGADOR_B )
		{
			if ( valor_resistencia_B == Double.MAX_VALUE )
			{
				return Integer.MIN_VALUE;
			}
			else if ( valor_resistencia_B == 0.0 )
			{
				return Integer.MAX_VALUE;
			}
			eval = Math.log( valor_resistencia_A / valor_resistencia_B );
		}
		else
		{
			if ( valor_resistencia_A == Double.MAX_VALUE )
			{
				return Integer.MIN_VALUE;
			}
			else if ( valor_resistencia_A == 0.0 )
			{
				return Integer.MAX_VALUE;
			}
			eval = Math.log( valor_resistencia_B / valor_resistencia_A );
		}

		return (int)(eval*10000.0) + cv.getConnexions_virtuals()*3000 + cv.getConnexions_semivirtuals()*300;
*/
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
		if ( partida.getTornsJugats() <= 1 )
		{
			return posicioCentral();
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
