package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.util.ArrayList;
import java.util.List;

/**
 * Per evaluar un tauler concret imaginem el tauler com un circuit elèctric. On cada casella té una resistencia,
 * Per a cada jugador resistencia de cada casella serà:
 * 1 si la casella està buida.
 * 0 si pertany al mateix jugador.
 * Inf. si pertany al jugador contrari.
 * Apliquem un voltatge a cada costat (A esquerra i dreta si és jugador A o a baix i a dalt si és jugador B),
 * i n'evaluem la resistencia total.
 * <p/>
 * La resistencia d'un tauler calculada d'aquesta manera ve a ser un indicador de la facilitat de creuar d'un punt a
 * un altre del tauler.
 * Si la resistencia d'un tauler és 0 vol dir que el jugador ha guanyat.
 * Si és Inf. vol dir que el jugador contrari ha guanyat.
 * <p/>
 * L'algoritme aplicat aqui per calcular la resistencia només fa una aproximació de la resistencia global on,
 * per cada casella mira la les caselles evaluades anteriorment i en treu la resistencia en paral·lel.
 * Calcular la resistencia globla bé és massa costos i aquesta aproximació funciona correctament.
 */
public class ResistenciaTauler
{

	/**
	 * Tauler sobre el que s'evalua la resistencia.
	 */
	private TaulerHex tauler;
	/**
	 * Jugador per al que s'evalua la resistencia.
	 */
	private EstatCasella jugador;
	/**
	 * Resistencia del tauler.
	 */
	private double resistencia;
	/**
	 * Array de la mida del tauler on es guarden les resistencies parcials de cada casella,
	 * necessari pel funcionament de l'algoritme.
	 */
	private double[][] resistencies_parcials;

	/**
	 * Instancia la classe amb el tauler i el jugador especificats.
	 *
	 * @param tauler  tauler a analitzar
	 * @param jugador jugador per al qual volem trobar la resistencia del tauler.
	 */
	public ResistenciaTauler( TaulerHex tauler, EstatCasella jugador )
	{
		this.tauler = tauler;
		this.jugador = jugador;
		this.resistencia = -1.0;
		this.resistencies_parcials = new double[tauler.getMida()][tauler.getMida()];
	}

	/**
	 * Evalua la resistencia del tauler.
	 *
	 * @return double amb la resistencia total.
	 */
	public double evalua()
	{
		if ( jugador == EstatCasella.JUGADOR_A )
		{
			evaluaA();
		}
		else if ( jugador == EstatCasella.JUGADOR_B )
		{
			evaluaB();
		}
		return resistencia;
	}

	/**
	 * Evalua la resistencia per al jugador A.
	 */
	private void evaluaA()
	{
		//Evaluem el tauler d'esquerra a dreta.

		// Omplim la primera columna del tauler amb les resistencies, (directe del seu estat).
		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			resistencies_parcials[i][0] = resistenciaCasella( new Casella( i, 0 ) );
		}

		// Calculem les resistencies parcials cap a baix a partir de la primera fila.
		for ( int columna = 1; columna < tauler.getMida(); columna++ )
		{
			for ( int fila = 0; fila < tauler.getMida(); fila++ )
			{
				double r_entrada = calculaResistenciaColumna( fila, columna );

				if ( r_entrada >= 10000.0 )
				{
					resistencies_parcials[fila][columna] = 10000.0;
				}
				else
				{
					if ( resistenciaCasella( new Casella( fila, columna ) ) >= 10000.0 )
					{
						resistencies_parcials[fila][columna] = 10000.0;
					}
					else
					{
						resistencies_parcials[fila][columna] =
								r_entrada + resistenciaCasella( new Casella( fila, columna ) );
					}
				}
			}
		}

		resistencia = calculaResistenciaColumna( 0, tauler.getMida() );
	}

	/**
	 * Evalua la resistencia per al jugador B.
	 */
	private void evaluaB()
	{
		//Evaluem el tauler d'adalt a baix.

		// Omplim la primera fila del tauler amb les resistencies, (directe del seu estat).
		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			resistencies_parcials[0][i] = resistenciaCasella( new Casella( 0, i ) );
		}

		// Calculem les resistencies parcials cap a baix a partir de la primera fila.
		for ( int fila = 1; fila < tauler.getMida(); fila++ )
		{
			for ( int columna = 0; columna < tauler.getMida(); columna++ )
			{
				double r_entrada = calculaResistenciaFila( fila, columna );

				if ( r_entrada >= 10000.0 )
				{
					resistencies_parcials[fila][columna] = 10000.0;
				}
				else
				{
					if ( resistenciaCasella( new Casella( fila, columna ) ) >= 10000.0 )
					{
						resistencies_parcials[fila][columna] = 10000.0;
					}
					else
					{
						resistencies_parcials[fila][columna] =
								r_entrada + resistenciaCasella( new Casella( fila, columna ) );
					}
				}
			}
		}

		resistencia = calculaResistenciaFila( tauler.getMida(), 0 );
	}

	/**
	 * Calcula la resistencia d'una casella en funció dels veins de la fila superior i de l'esquerra.
	 *
	 * @param fila    fila de la casella.
	 * @param columna columna de la casella.
	 * @return resistencia de la casella en qüestió.
	 */
	private double calculaResistenciaFila( int fila, int columna )
	{
		//Iterem per tots els veins superiors i de l'esquerra, si un es 0 el resultat serà 0, si tots són inf,
		// el resultat serà inf, en altres casos, serà la inversa de la suma inversa de tots els 1.

		/*Obtenim els veins, si la fila és la mida del tauler entenem que estem a la cantonada i totes les caselles
		 *de la última fila són les veines.
		 */
		List<Casella> veins;
		if ( fila == tauler.getMida() )
		{
			veins = new ArrayList<Casella>();
			for ( int i = 0; i < tauler.getMida(); i++ )
			{
				veins.add( new Casella( tauler.getMida() - 1, i ) );
			}
		}
		else
		{
			veins = tauler.getVeins( fila, columna );
		}

		boolean calcula_paralel = false;
		double inversa_rt = 0.0;
		for ( int i = 0; i < veins.size(); i++ )
		{
			//Per tots els veins de la fila superior o de l'esquerra i mateixa fila...
			if ( veins.get( i ).getFila() < fila ||
			     ( veins.get( i ).getFila() <= fila && veins.get( i ).getColumna() < columna ) )
			{
				//Si és 0, el resultat serà 0, no cal continuar.
				if ( resistencies_parcials[veins.get( i ).getFila()][veins.get( i ).getColumna()] == 0.0 )
				{
					return 0.0;
				}
				//Si no és Inf tampoc.
				else if ( resistencies_parcials[veins.get( i ).getFila()][veins.get( i ).getColumna()] < 10000 )
				{
					calcula_paralel = true;
					inversa_rt += 1.0 / resistencies_parcials[veins.get( i ).getFila()][veins.get( i ).getColumna()];
				}
			}
		}

		//Si calcula_paralel = false, ja ho tenim, sino li donem valor.
		if ( calcula_paralel )
		{
			return ( 1.0 / inversa_rt );
		}

		return 10000.0;
	}

	/**
	 * Calcula la resistencia d'una casella en funció dels veins de l'esquerra i de dalt.
	 *
	 * @param fila    fila de la casella.
	 * @param columna columna de la casella.
	 * @return resistencia de la casella en qüestió.
	 */
	private double calculaResistenciaColumna( int fila, int columna )
	{
		//Iterem per tots els veins de l'esquerra i els de dalt a l'esquerra, si un es 0 el resultat serà 0,
		// si tots són inf, el resultat serà inf, en altres casos, serà la inversa de la suma inversa de tots els 1.

		/*Obtenim els veins, si la columna és la mida del tauler entenem que estem a la cantonada i totes les caselles
		 *de la última columna són les veines.
		 */
		List<Casella> veins;
		if ( columna == tauler.getMida() )
		{
			veins = new ArrayList<Casella>();
			for ( int i = 0; i < tauler.getMida(); i++ )
			{
				veins.add( new Casella( i, tauler.getMida() - 1 ) );
			}
		}
		else
		{
			veins = tauler.getVeins( fila, columna );
		}

		boolean calcula_paralel = false;
		double inversa_rt = 0.0;
		for ( int i = 0; i < veins.size(); i++ )
		{
			//Per tots els veins de la columna esquerra o de la mateixa columna i superiors.
			if ( veins.get( i ).getColumna() < columna ||
			     ( veins.get( i ).getFila() < fila && veins.get( i ).getColumna() <= columna ) )
			{
				//Si és 0, el resultat serà 0, no cal continuar.
				if ( resistencies_parcials[veins.get( i ).getFila()][veins.get( i ).getColumna()] == 0.0 )
				{
					return 0.0;
				}
				//Si no és Inf tampoc.
				else if ( resistencies_parcials[veins.get( i ).getFila()][veins.get( i ).getColumna()] < 10000.0 )
				{
					calcula_paralel = true;
					inversa_rt += 1.0 / resistencies_parcials[veins.get( i ).getFila()][veins.get( i ).getColumna()];
				}
			}
		}

		//Si calcula_paralel = false, ja ho tenim, sino li donem valor.
		if ( calcula_paralel )
		{
			return ( 1.0 / inversa_rt );
		}

		return 10000.0;
	}

	/**
	 * retorna la resistencia d'una casella en funció del jugador.
	 *
	 * @param casella
	 * @return la resistencia de la casella.
	 */
	private double resistenciaCasella( Casella casella )
	{
		EstatCasella estat = tauler.getEstatCasella( casella );
		if ( estat == jugador )
		{
			return 0.0;
		}
		else if ( estat == EstatCasella.BUIDA )
		{
			return 1.0;
		}
		else
		{
			return 10000;
		}
	}
}
