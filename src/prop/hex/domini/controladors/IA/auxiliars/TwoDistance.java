package prop.hex.domini.controladors.IA.auxiliars;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * La classe calcula la Two-Distance dos cops per a cada casella, un per cada cantonada del jugador indicat
 * (horitzontals o verticals). Després les suma per trobar el potencial de cada casella. I finalment busca quin és
 * el potencial mínim d'entre totes les caselles i quants potencials hi ha amb el mateix valor.
 * <p/>
 * La Two-Distance, tal com defineix <i>Jack van Rijswijck, Computer Hex: Are Bees Better Than
 * Fruitflies?, 2000</i>, en lloc d'agafar el camí mínim que arriba a cada casella per calcular-ne el cost per
 * arribar-hi (com en un Dijkstra), n'agafem el segòn més petit. Els grups de caselles d'un mateix jugador es
 * consideren una sola casella, i el cost de pasar per una casella amb una fitxa del jugador contrari és infinit.
 * La idea darrere del Two-Distance es pensar que l'oponent ens bloquejarà la millor jugada sempre. Així s'eviten
 * molts problemes que causa utilitzar el Camí Mínim.
 *
 * @author Marc Junyent Martín (Grup 7.3, Hex)
 */
public final class TwoDistance
{

	/**
	 * Tauler sobre el que es busquen les distàncies.
	 */
	private TaulerHex tauler;
	/**
	 * Jugador per al que busquem la distància.
	 */
	private EstatCasella jugador;
	/**
	 * Arrays per guardar els valors temporals de les distàncies i els potencials.
	 */
	private int[][] distancies_a, distancies_b, potencials;
	/**
	 * Valor més gran que qualsevol distància que poguem calcular però prou petit per no causar overflow al sumar o
	 * multiplicar.
	 */
	private int infinit = 1000000;
	/**
	 * Valor del potencial mínim del tauler per al jugador especificat.
	 */
	private int potencial;

	/**
	 * Calcula la Two-Distance de cada casella a les dues bores del taulell pel jugador especificat i en calcula els
	 * potencials i el potencial mínim.
	 *
	 * @param tauler  Tauler a analitzar.
	 * @param jugador Jugador per al qual calcular el potencial.
	 */
	public TwoDistance( TaulerHex tauler, EstatCasella jugador )
	{
		this.tauler = tauler;
		this.jugador = jugador;

		distancies_a = new int[tauler.getMida()][tauler.getMida()];
		distancies_b = new int[tauler.getMida()][tauler.getMida()];
		potencials = new int[tauler.getMida()][tauler.getMida()];

		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				distancies_a[i][j] = infinit;
				distancies_b[i][j] = infinit;
			}
		}

		if ( jugador == EstatCasella.JUGADOR_A )
		{
			ompleCantonadesJugadorA();
		}
		else
		{
			ompleCantonadesJugadorB();
		}

		buscaTwoDistance( distancies_a );
		buscaTwoDistance( distancies_b );

		potencial = infinit;

		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				potencials[i][j] = distancies_a[i][j] + distancies_b[i][j];
				//I assginem a potencial el nombre més petit de potencials.
				if ( potencials[i][j] < potencial )
				{
					potencial = potencials[i][j];
				}
			}
		}
	}

	/**
	 * Calcula la Two-Distance del taulell per a totes les caselles i guarda el valor en distancia. Les caselles
	 * marcades amb un 1 a l'array distancia són les que agafarà com inicials i busca les distàncies cap a aquestes
	 * caselles.
	 *
	 * @param distancia Array on guardar els valors de les distàncies.
	 */
	private void buscaTwoDistance( int[][] distancia )
	{
		ArrayList<Casella> pendents = new ArrayList<Casella>();
		int contador = 0;
		int maxim = tauler.getMida();

		//Afegim a pendents totes les caselles buides del tauler no checkejades (no estan a la cantonada).
		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				Casella actual = new Casella( i, j );
				//Si la distancia es infinit (casella no chequejada) i la casella esta buida, l'afegim a pendents.
				if ( distancia[i][j] >= infinit && tauler.esMovimentValid( jugador, actual ) )
				{
					pendents.add( actual );
				}
			}
		}

		HashMap<Casella, ArrayList<Casella>> meta_veins = new HashMap<Casella, ArrayList<Casella>>();
		for ( Casella actual : pendents )
		{
			ArrayList<Casella> veins = getVeins( actual );
			meta_veins.put( actual, veins );
		}

		//Iterem pendents si el contador es menor a un maxim establert.
		while ( contador < maxim )
		{
			boolean modificat = false;
			contador++;

			for ( Casella actual : pendents )
			{

				int min_u = infinit;
				int min_dos = infinit;
				ArrayList<Casella> veins = meta_veins.get( actual );
				for ( Casella vei : veins )
				{
					if ( distancia[vei.getFila()][vei.getColumna()] <= min_u )
					{
						min_dos = min_u;
						min_u = distancia[vei.getFila()][vei.getColumna()];
					}
					else if ( distancia[vei.getFila()][vei.getColumna()] < min_dos )
					{
						min_dos = distancia[vei.getFila()][vei.getColumna()];
					}
				}

				if ( min_dos < infinit )
				{
					distancia[actual.getFila()][actual.getColumna()] = min_dos + 1;
					modificat = true;
				}
			}

			//si en una iteració no es modifica cap valor, ja hem acabat.
			if ( !modificat )
			{
				break;
			}
		}
	}

	/**
	 * Omple els arrays de distàncies amb les posicions inicials pel jugador A (d'est a oest) per a que
	 * buscaTwoDistance calculi la Two-Distance.
	 */
	private void ompleCantonadesJugadorA()
	{
		for ( int fila = 0; fila < tauler.getMida(); fila++ )
		{
			if ( tauler.getEstatCasella( fila, 0 ) == EstatCasella.BUIDA )
			{
				distancies_a[fila][0] = 1;
			}
			else if ( tauler.getEstatCasella( fila, 0 ) == jugador )
			{
				GrupCaselles grup = new GrupCaselles( tauler );
				grup.estendre( new Casella( fila, 0 ) );
				ArrayList<Casella> grup_veins = grup.getVeins().getGrup();

				for ( Casella vei : grup_veins )
				{
					distancies_a[vei.getFila()][vei.getColumna()] = 1;
				}
			}

			if ( tauler.getEstatCasella( fila, tauler.getMida() - 1 ) == EstatCasella.BUIDA )
			{
				distancies_b[fila][tauler.getMida() - 1] = 1;
			}
			else if ( tauler.getEstatCasella( fila, tauler.getMida() - 1 ) == jugador )
			{
				GrupCaselles grup = new GrupCaselles( tauler );
				grup.estendre( new Casella( fila, tauler.getMida() - 1 ) );
				ArrayList<Casella> grup_veins = grup.getVeins().getGrup();

				for ( Casella vei : grup_veins )
				{
					distancies_b[vei.getFila()][vei.getColumna()] = 1;
				}
			}
		}
	}

	/**
	 * Omple els arrays de distàncies amb les posicions inicials pel jugador B (nord a sud) per a que
	 * buscaTwoDistance calculi la Two-Distance.
	 */
	private void ompleCantonadesJugadorB()
	{
		for ( int columna = 0; columna < tauler.getMida(); columna++ )
		{
			if ( tauler.getEstatCasella( 0, columna ) == EstatCasella.BUIDA )
			{
				distancies_a[0][columna] = 1;
			}
			else if ( tauler.getEstatCasella( 0, columna ) == jugador )
			{
				GrupCaselles grup = new GrupCaselles( tauler );
				grup.estendre( new Casella( 0, columna ) );
				ArrayList<Casella> grup_veins = grup.getVeins().getGrup();

				for ( Casella vei : grup_veins )
				{
					distancies_a[vei.getFila()][vei.getColumna()] = 1;
				}
			}
			if ( tauler.getEstatCasella( tauler.getMida() - 1, columna ) == EstatCasella.BUIDA )
			{
				distancies_b[tauler.getMida() - 1][columna] = 1;
			}
			else if ( tauler.getEstatCasella( tauler.getMida() - 1, columna ) == jugador )
			{
				GrupCaselles grup = new GrupCaselles( tauler );
				grup.estendre( new Casella( tauler.getMida() - 1, columna ) );
				ArrayList<Casella> grup_veins = grup.getVeins().getGrup();

				for ( Casella vei : grup_veins )
				{
					distancies_b[vei.getFila()][vei.getColumna()] = 1;
				}
			}
		}
	}

	/**
	 * Obté tots els veins d'una casella, en aquest cas per a veins no ens referim a les caselles adjacents al
	 * taulell sinò a les caselles connectades virtualment (connexions virtuals de nivell 0).
	 *
	 * @param casella casella per a buscar-ne els veins.
	 * @return ArrayList amb els veins.
	 */
	private ArrayList<Casella> getVeins( Casella casella )
	{
		tauler.mouFitxa( jugador, casella );
		GrupCaselles grup = new GrupCaselles( tauler );
		grup.estendre( casella );
		ArrayList<Casella> grup_veins = grup.getVeins().getGrup();
		tauler.treuFitxa( casella );
		return grup_veins;
	}

	/**
	 * Obté el potencial
	 *
	 * @return
	 */
	public int[][] getPotencials()
	{
		return potencials;
	}

	/**
	 * Obté el potencial mínim entre les caselles que no siguin la indicada.
	 *
	 * @param casella Casella a no tenir en compte.
	 * @return
	 */
	public int getPotencialMinim( Casella casella )
	{
		int minim = Integer.MAX_VALUE;

		for ( int fila = 0; fila < tauler.getMida(); fila++ )
		{
			for ( int columna = 0; columna < tauler.getMida(); columna++ )
			{
				if ( !casella.equals( new Casella( fila, columna ) ) && potencials[fila][columna] < minim )
				{
					minim = potencials[fila][columna];
				}
			}
		}

		return minim;
	}

	/**
	 * Obté el potencial mínim del tauler.
	 *
	 * @return
	 */
	public int getPotencial()
	{
		return potencial;
	}

	/**
	 * Obté el nombre caselles amb un potencial igual al mínim.
	 *
	 * @return
	 */
	public int getNombrePotencialsMinims()
	{
		int contador = 0;
		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				if ( potencials[i][j] == potencial )
				{
					contador++;
				}
			}
		}
		return contador;
	}
}
