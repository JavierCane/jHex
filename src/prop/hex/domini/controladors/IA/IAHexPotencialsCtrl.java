package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IA.auxiliars.ResistenciaCasella;
import prop.hex.domini.controladors.IA.auxiliars.TwoDistance;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

/**
 * Intel·ligència artificial per a Hex que utilitza un càlcul de potencials mínims segons l'algorisme de QueenBee.
 * <p/>
 * L'algorisme que utilitza es basa en el càlcul de les caselles amb potencial mínim utilitzat per a l'ordenació de
 * moviments a visitar en un algorisme de cerca basat en minimax. Com que a un nivell principiant,
 * aquest càlcul és prou bo, es pot utilitzar directament com a funció d'avaluació negada d'un moviment (perquè volem
 * minimitzar-ne el cost).
 * <p/>
 * La funció que ordena els moviments prové de
 * <a href="http://javhar.net/AreBeesBetterThanFruitfliesThesis.pdf">QueenBee</a>,
 * de <a href="http://javhar.net/">Jack van Rijswijck</a>.
 *
 * @author Isaac Sánchez Barrera (Grup 7.3, Hex)
 */
public final class IAHexPotencialsCtrl extends InteligenciaArtificialHexCtrl
{

	/**
	 * Tauler on es desenvolupa la partida
	 */
	private TaulerHex tauler;

	/**
	 * Retorna una casella on hi ha un moviment factible. Un moviment factible és una casella amb cost mínim segons
	 * QueenBee. Si \(p\) és una casella,
	 * el seu cost o resistència és
	 * \[
	 * c(p) = \left\{
	 * \begin{array}{ll}
	 * -V^{*} & \text{si } V(p) = 0 \\
	 * V(p)  & \text{altrament}
	 * \end{array}\right.
	 * \]
	 * on \(V^{*}\) és el potencial del segon millor moviment. Si hi ha més d'un moviment amb potencial nul,
	 * aleshores \(V^{*} = 0\).
	 * <p/>
	 * De tots els possibles moviments amb cost mínim, aquest en retorna un qualsevol.
	 *
	 * @param fitxa_jugador Fitxa del jugador per qui es vol calcular el moviment
	 * @return Una casella amb potencial mínim
	 */
	private Casella movimentFactible( EstatCasella fitxa_jugador )
	{
		TreeSet<ResistenciaCasella> moviments_ordenats = new TreeSet<ResistenciaCasella>();

		TwoDistance two_distance = new TwoDistance( tauler, fitxa_jugador );

		int[][] potencials = two_distance.getPotencials();

		for ( int fila = 0; fila < tauler.getMida(); fila++ )
		{
			for ( int columna = 0; columna < tauler.getMida(); columna++ )
			{
				Casella casella = new Casella( fila, columna );

				if ( tauler.esMovimentValid( fitxa_jugador, casella ) )
				{
					int potencial_moviment = potencials[fila][columna];
					if ( potencial_moviment == 0 )
					{
						potencial_moviment -= two_distance.getPotencialMinim( casella );
					}
					moviments_ordenats.add( new ResistenciaCasella( casella, potencial_moviment ) );
				}
			}
		}

		Iterator<ResistenciaCasella> resistencies = moviments_ordenats.iterator();
		ResistenciaCasella actual = resistencies.next();
		int resistencia_actual = actual.getResistencia();
		int resistencia_minima = resistencia_actual;
		ArrayList<Casella> caselles = new ArrayList<Casella>( moviments_ordenats.size() );
		caselles.add( actual.getCasella() );

		while ( resistencies.hasNext() && resistencia_actual == resistencia_minima )
		{
			actual = resistencies.next();
			resistencia_actual = actual.getResistencia();
			if ( resistencia_actual == resistencia_minima )
			{
				caselles.add( actual.getCasella() );
			}
		}

		return caselles.get( new Random().nextInt( caselles.size() ) );
	}

	/**
	 * Definida perquè cal per herència, no es fa servir.
	 *
	 * @param tauler         Tauler on es juga la partida
	 * @param estat_moviment En quin estat ha quedat la partida al darrer moviment
	 * @param profunditat    Profunditat on s'avalua
	 * @param fitxa_jugador  Fitxa del jugador per qui s'avalua
	 * @return Un zero (0).
	 */
	@Override
	public int funcioAvaluacio( Tauler tauler, EstatPartida estat_moviment, int profunditat,
	                            EstatCasella fitxa_jugador )
	{
		return 0;
	}

	/**
	 * Retorna un moviment adequat al tauler actual per al jugador indicat per fitxa.
	 * <p/>
	 * Simplement utilitza el càlcul la casella de cost mínim.
	 *
	 * @param fitxa Fitxa que vol col·locar-se al tauler de la partida del paràmetre implícit.
	 * @return La casella on es mouria la fitxa.
	 * @see #movimentFactible(EstatCasella)
	 * @see InteligenciaArtificialHexCtrl
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

		tauler = partida.getTauler();

		return movimentFactible( fitxa );
	}
}
