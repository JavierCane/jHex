package prop.hex.domini.controladors;

import prop.cluster.domini.controladors.InteligenciaArtificial;
import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
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
	private static int profunditat_maxima = 3;

	public int funcioAvaluacio( Tauler tauler, EstatPartida estat_moviment, int profunditat,
	                            EstatCasella fitxa_jugador )
	{
		ConnexionsVirtuals cv = new ConnexionsVirtuals( ( TaulerHex ) tauler, fitxa_jugador );

//		E = log(RB/RW).
		double eval;
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

		return ( int ) ( eval * 10000.0 ) + cv.getConnexions_virtuals() * 3000 + cv.getConnexions_semivirtuals() * 300;

//		return cv.getConnexions_virtuals() * 4 + cv.getConnexions_semivirtuals();
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
