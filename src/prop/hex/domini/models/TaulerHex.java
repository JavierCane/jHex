package prop.hex.domini.models;

import prop.cluster.domini.models.Tauler;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.enums.ModesInici;

import java.io.Serializable;
import java.util.*;

/**
 * Representa un tauler del joc Hex. El jugador A ha de connectar els costats esquerre i dret (columnes 0 i mida - 1),
 * el jugador B ha de connectar els costats superior i inferior (files 0 i mida - 1).
 */
public class TaulerHex extends Tauler implements Serializable
{

	/**
	 * ID de serialització
	 */
	private static final long serialVersionUID = -9106084993427149334L;

	/**
	 * Codis hash de cada moviment en concret. Estan indexats considerant el tauler com un array unidimensional.
	 */
	private ArrayList<HashMap<EstatCasella, Integer>> codis_hash_moviments;

	/**
	 * Codi de hash del tauler
	 */
	private int codi_hash;

	/**
	 * Mode d'inici de la partida que juga amb el tauler.
	 */
	private ModesInici mode_inici_partida;

	/**
	 * Constructor del tauler. Crea un tauler de la mida desitjada amb totes les caselles buides (EstatCasella.BUIDA).
	 *
	 * @param mida Les dimensions que tindrà el tauler
	 */
	public TaulerHex( int mida )
	{
		super( mida );
		generaCodiHashTaulerBuit();
		mode_inici_partida = ModesInici.ESTANDARD;
	}

	/**
	 * Constructor que inicialitza el tauler a un estat diferent del per defecte. No comprova que els paràmetres siguin
	 * correctes.
	 *
	 * @param mida         Les dimensions del tauler
	 * @param caselles     Un array bidimensional mida × mida amb l'estat inicial
	 * @param num_fitxes_a La quantitat de fitxes que té el jugador A al tauler
	 * @param num_fitxes_b La quantitat de fitxes que té el jugador B al tauler
	 */
	public TaulerHex( int mida, EstatCasella[][] caselles, int num_fitxes_a, int num_fitxes_b )
	{
		super( mida, caselles, num_fitxes_a, num_fitxes_b );
		generaCodiHashTaulerBuit();

		for ( int fila = 0; fila < mida; fila++ )
		{
			for ( int columna = 0; columna < mida; columna++ )
			{
				codi_hash ^= getCodiHashMoviment( EstatCasella.BUIDA, fila, columna );
				codi_hash ^= getCodiHashMoviment( caselles[fila][columna], fila, columna );
			}
		}

		mode_inici_partida = ModesInici.ESTANDARD;
	}

	/**
	 * Constructor per còpia. Crea un nou tauler idèntic a original.
	 *
	 * @param original Tauler que es vol copiar
	 */
	public TaulerHex( TaulerHex original )
	{
		super( original );
		codi_hash = original.codi_hash;
		codis_hash_moviments = original.codis_hash_moviments;
		mode_inici_partida = original.getModeIniciPartida();
	}

	/**
	 * Comprova el mode d'inici de la partida del tauler
	 *
	 * @return El mode d'inici de la partida que juga amb el tauler
	 */
	public ModesInici getModeIniciPartida()
	{
		return mode_inici_partida;
	}

	/**
	 * Canvia el mode d'inici de la partida del tauler.
	 *
	 * @param mode_inici El mode d'inici de la partida.
	 * @return Cert si s'ha canviat.
	 */
	public boolean setModeIniciPartida( ModesInici mode_inici )
	{
		mode_inici_partida = mode_inici;

		return true;
	}

	/**
	 * Genera el codi hash per a un tauler sense fitxes i calcula els codis hash de cada moviment.
	 */
	public void generaCodiHashTaulerBuit()
	{
		Random generador = new Random( serialVersionUID );
		codis_hash_moviments = new ArrayList<HashMap<EstatCasella, Integer>>( mida * mida );
		for ( int i = 0; i < mida * mida; i++ )
		{
			codis_hash_moviments.add( new HashMap<EstatCasella, Integer>( 3 ) );

			int codi_hash_inicial = generador.nextInt();
			codi_hash ^= codi_hash_inicial;

			codis_hash_moviments.get( i ).put( EstatCasella.BUIDA, codi_hash_inicial );
			codis_hash_moviments.get( i ).put( EstatCasella.JUGADOR_A, generador.nextInt() );
			codis_hash_moviments.get( i ).put( EstatCasella.JUGADOR_B, generador.nextInt() );
		}
	}

	/**
	 * Comprova si una casella és vàlida dins el tauler
	 *
	 * @param casella Casella del tauler
	 * @return Cert si és una casella vàlida. Fals altrament.
	 */
	public boolean esCasellaValida( Casella casella )
	{
		return esCasellaValida( casella.getFila(), casella.getColumna() );
	}

	/**
	 * Comprova si un moviment és vàlid.
	 *
	 * @param fitxa   Fitxa que es vol comprovar
	 * @param fila    Fila de la casella dins el tauler
	 * @param columna Columna de la casella dins el tauler.
	 * @return Cert si el moviment és vàlid. Fals altrament.
	 * @throws IndexOutOfBoundsException si (fila, columna) no és una casella vàlida.
	 * @throws IllegalArgumentException  si fitxa no és de cap jugador (és EstatCasella.BUIDA).
	 */
	@Override
	public boolean esMovimentValid( EstatCasella fitxa, int fila, int columna )
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if ( !esCasellaValida( fila, columna ) )
		{
			throw new IndexOutOfBoundsException( "Casella fora del tauler" );
		}
		else if ( caselles[fila][columna] != EstatCasella.BUIDA )
		{
			return false;
		}
		else if ( fitxa == EstatCasella.BUIDA )
		{
			throw new IllegalArgumentException( "La fitxa no és de cap jugador" );
		}
		else if ( mode_inici_partida == ModesInici.ESTANDARD && num_fitxes_a + num_fitxes_b == 0 && fila == mida / 2 &&
		          columna == mida / 2 )
		{
			return false;
		}

		return true;
	}

	/**
	 * Comprova si un moviment és vàlid.
	 *
	 * @param fitxa   Fitxa que es vol comprovar
	 * @param casella Casella del tauler
	 * @return Cert si el moviment és vàlid. Fals altrament.
	 * @throws IndexOutOfBoundsException si casella no és una casella vàlida.
	 * @throws IllegalArgumentException  si fitxa no és de cap jugador (és EstatCasella.BUIDA).
	 */
	public boolean esMovimentValid( EstatCasella fitxa, Casella casella )
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		return esMovimentValid( fitxa, casella.getFila(), casella.getColumna() );
	}

	/**
	 * Consulta l'estat d'una casella del tauler.
	 *
	 * @param casella Casella del tauler.
	 * @return L'estat actual de la casella.
	 */
	public EstatCasella getEstatCasella( Casella casella )
	{
		return getEstatCasella( casella.getFila(), casella.getColumna() );
	}

	/**
	 * Mou la fitxa a la casella indicada i actualitza els comptadors.
	 *
	 * @param fitxa   Fitxa que es vol col·locar.
	 * @param fila    Fila de la casella del tauler.
	 * @param columna Columna de la casella del tauler.
	 * @return Cert si s’ha realitzat el moviment. Fals altrament.
	 * @throws IndexOutOfBoundsException si casella no és una casella vàlida.
	 * @throws IllegalArgumentException  si fitxa no és de cap jugador (és EstatCasella.BUIDA)  o el moviment no és
	 *                                   vàlid.
	 */
	@Override
	public boolean mouFitxa( EstatCasella fitxa, int fila, int columna )
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		boolean resultat = super.mouFitxa( fitxa, fila, columna );
		if ( resultat )
		{
			codi_hash ^= getCodiHashMoviment( fitxa, fila, columna );
		}

		return resultat;
	}

	/**
	 * Mou la fitxa a la casella indicada i actualitza els comptadors.
	 *
	 * @param fitxa   Fitxa que es vol col·locar.
	 * @param casella Casella del tauler.
	 * @return Cert si s’ha realitzat el moviment. Fals altrament.
	 * @throws IndexOutOfBoundsException si casella no és una casella vàlida.
	 * @throws IllegalArgumentException  si fitxa no és de cap jugador (és EstatCasella.BUIDA)  o el moviment no és
	 *                                   vàlid.
	 */
	public boolean mouFitxa( EstatCasella fitxa, Casella casella )
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		return mouFitxa( fitxa, casella.getFila(), casella.getColumna() );
	}

	/**
	 * Treu la fitxa de la casella indicada i actualitza els comptadors.
	 *
	 * @param fila    Fila de la casella del tauler.
	 * @param columna Columna de la casella del tauler.
	 * @return Cert si s’ha realitzat el moviment. Fals altrament.
	 * @throws IndexOutOfBoundsException si no és una casella vàlida.
	 * @throws IllegalArgumentException  si la casella és buida (és EstatCasella.BUIDA).
	 */
	@Override
	public boolean treuFitxa( int fila, int columna ) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		int codi_antic = getCodiHashMoviment( caselles[fila][columna], fila, columna );
		boolean resultat = super.treuFitxa( fila, columna );
		if ( resultat )
		{
			codi_hash ^= codi_antic ^ getCodiHashMoviment( EstatCasella.BUIDA, fila, columna );
		}

		return resultat;
	}

	/**
	 * Treu la fitxa de la casella indicada i actualitza els comptadors.
	 *
	 * @param casella Casella del tauler.
	 * @return Cert si s’ha realitzat el moviment. Fals altrament.
	 * @throws IndexOutOfBoundsException si no és una casella vàlida.
	 * @throws IllegalArgumentException  si la casella és buida (és EstatCasella.BUIDA).
	 */
	public boolean treuFitxa( Casella casella ) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		return treuFitxa( casella.getFila(), casella.getColumna() );
	}

	/**
	 * Calcula el codi de hash associat a un moviment.
	 *
	 * @param fitxa   Fitxa que es mou
	 * @param fila    Fila on es mou la fitxa
	 * @param columna Columna on es mou la fitxa
	 * @return El codi de hash d'un moviment.
	 */
	public int getCodiHashMoviment( EstatCasella fitxa, int fila, int columna )
	{
		return codis_hash_moviments.get( fila * mida + columna ).get( fitxa );
	}

	/**
	 * Intercanvia la fitxa d'una casella amb la de l'altre jugador i actualitza els comptadors.
	 *
	 * @param fila    Fila de la casella del tauler.
	 * @param columna Columna de la casella del tauler.
	 * @return Cert si s'ha intercanviat la fitxa. Fals altrament.
	 * @throws IndexOutOfBoundsException si no és una casella vàlida.
	 * @throws IllegalArgumentException  si la casella és buida (és EstatCasella.BUIDA).
	 */
	@Override
	public boolean intercanviaFitxa( int fila, int columna ) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		int codi_antic = getCodiHashMoviment( caselles[fila][columna], fila, columna );
		boolean resultat = super.intercanviaFitxa( fila, columna );

		if ( resultat )
		{
			codi_hash ^= codi_antic ^ getCodiHashMoviment( caselles[fila][columna], fila, columna );
		}

		return resultat;
	}

	/**
	 * Intercanvia la fitxa d'una casella amb la de l'altre jugador i actualitza els comptadors.
	 *
	 * @param casella Casella del tauler.
	 * @return Cert si s'ha intercanviat la fitxa. Fals altrament.
	 * @throws IndexOutOfBoundsException si no és una casella vàlida.
	 * @throws IllegalArgumentException  si la casella és buida (és EstatCasella.BUIDA).
	 */
	public boolean intercanviaFitxa( Casella casella ) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		return intercanviaFitxa( casella.getFila(), casella.getColumna() );
	}

	/**
	 * Consulta els veïns d'una casella.
	 *
	 * @param fila    Fila de la casella dins el tauler.
	 * @param columna Fila de la casella dins el tauler.
	 * @return Una llista amb les caselles veïnes de la casella (fila, columna).
	 */
	public List<Casella> getVeins( int fila, int columna ) throws IndexOutOfBoundsException
	{
		if ( !esCasellaValida( fila, columna ) )
		{
			throw new IndexOutOfBoundsException( "Casella fora del tauler" );
		}
		else
		{
			List<Casella> veins = new LinkedList<Casella>();
			veins.add( new Casella( fila - 1, columna ) );
			veins.add( new Casella( fila - 1, columna + 1 ) );
			veins.add( new Casella( fila, columna - 1 ) );
			veins.add( new Casella( fila, columna + 1 ) );
			veins.add( new Casella( fila + 1, columna ) );
			veins.add( new Casella( fila + 1, columna - 1 ) );
			for ( int i = veins.size() - 1; i >= 0; i-- )
			{
				Casella casella = veins.get( i );
				if ( casella.getFila() < 0 || casella.getFila() >= getMida() || casella.getColumna() < 0 ||
				     casella.getColumna() >= getMida() )
				{
					veins.remove( i );
				}
			}
			return veins;
		}
	}

	/**
	 * Consulta els veïns d'una casella.
	 *
	 * @param casella Casella del tauler.
	 * @return Una llista amb les caselles veïnes de la casella.
	 */
	public List<Casella> getVeins( Casella casella ) throws IndexOutOfBoundsException
	{
		return getVeins( casella.getFila(), casella.getColumna() );
	}

	/**
	 * Crea un String amb tota la informació del tauler.
	 *
	 * @return El String amb la informació completa del tauler.
	 */
	@Override
	public String toString()
	{
		String informacio = "[Mida: " + mida + ", num. fitxes jugador A: " + num_fitxes_a + ", " +
		                    "num. fitxes jugador B: " + num_fitxes_b + ", estat de les caselles:\n";

		int espais = 0;
		for ( EstatCasella[] fila : caselles )
		{
			informacio = informacio + "\t";
			for ( int i = 0; i < espais; i++ )
			{
				informacio += " ";
			}
			for ( EstatCasella actual : fila )
			{
				switch ( actual )
				{
					case JUGADOR_A:
						informacio = informacio + "A ";
						break;
					case JUGADOR_B:
						informacio = informacio + "B ";
						break;
					case BUIDA:
						informacio = informacio + "· ";
						break;
				}
			}
			informacio = informacio + "\n";
			espais++;
		}

		informacio = informacio + "]";

		return informacio;
	}

	/**
	 * Consulta el codi de hash del tauler.
	 *
	 * @return El codi de hash del tauler.
	 */
	@Override
	public int hashCode()
	{
		return codi_hash;
	}
}
