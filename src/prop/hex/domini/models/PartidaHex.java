package prop.hex.domini.models;

import prop.cluster.domini.models.Partida;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.ModesInici;

import java.io.Serializable;
import java.util.*;

/**
 * Representa una partida del joc Hex.
 */
public class PartidaHex extends Partida implements Serializable
{

	/**
	 * ID de serialització
	 */
	private static final long serialVersionUID = 2627134177791902081L;

	/**
	 * Màxim de pistes permeses per jugador.
	 */
	private final static int max_num_pistes = 3;

	/**
	 * Diccionari que relaciona els usuaris amb la quantitat de pistes usades.
	 */
	private int[] pistes_usades;

	/**
	 * Diccionari que relaciona els usuaris amb el seu temps jugat en mil·lèssimes de segon.
	 */
	private long[] temps_de_joc;

	/**
	 * Expressió regular amb els caràcters permesos.
	 */
	private static final String caracters_permesos = "^[A-Za-z0-9,_ ]+$";

	/**
	 * Mode d'inici de la partida
	 */
	private ModesInici mode_inici;

	/**
	 * Combinació de colors de la partida.
	 */
	private CombinacionsColors combinacio_colors;

	/**
	 * Instant de temps en què es va efectuar el darrer moviment.
	 */
	private long instant_darrer_moviment;

	/**
	 * Instància de casella que conté la posició de la darrera fitxa.
	 */
	private Casella darrera_fitxa;

	/**
	 * Tipus de fitxa de cada jugador.
	 */
	private static EstatCasella[] fitxes_jugadors = {
			EstatCasella.JUGADOR_A,
			EstatCasella.JUGADOR_B
	};

	/**
	 * Indica si la partida tenia una situació inicial predefinida.
	 */
	private boolean situacio_inicial;

	/**
	 * Indica si la situació inicial està acabada de definir
	 */
	private boolean situacio_inicial_acabada;

	/**
	 * Constructora alternativa per partides que no han estat jugades
	 *
	 * @param jugador_a        Usuari que farà de jugador A
	 * @param jugador_b        Usuari que farà de jugador B
	 * @param tauler           Tauler on es desenvoluparà la partida
	 * @param nom              Nom de la partida
	 * @param situacio_inicial Indica si la partida està definida amb una situació inicial
	 * @throws ClassNotFoundException Si no es pot carregar la classe de les intel·ligències artificials.
	 * @throws InstantiationError     Si hi ha problemes amb la instanciació de les intel·ligències artificials.
	 * @throws IllegalAccessError     Si s'intenta accedir a un lloc no permès quan es carreguen les intel·ligències
	 *                                artificials.
	 */
	public PartidaHex( UsuariHex jugador_a, UsuariHex jugador_b, TaulerHex tauler, String nom,
	                   boolean situacio_inicial )
			throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		super( jugador_a, jugador_b, tauler, nom );

		pistes_usades = new int[] {
				0,
				0
		};

		temps_de_joc = new long[] {
				0L,
				0L
		};

		mode_inici = jugador_a.getModeInici();
		tauler.setModeIniciPartida( mode_inici );
		combinacio_colors = jugador_a.getCombinacionsColors();

		instant_darrer_moviment = new Date().getTime();
		darrera_fitxa = new Casella( 0, 0 );

		this.situacio_inicial = situacio_inicial;
		situacio_inicial_acabada = false;
	}

	/**
	 * Comprova si una partida tenia una situació inicial
	 *
	 * @return Cert si tenia situació inicial. Fals altrament.
	 */
	public boolean teSituacioInicial()
	{
		return situacio_inicial;
	}

	public boolean teSituacioInicialAcabadaDeDefinir()
	{
		return situacio_inicial_acabada;
	}

	public boolean setSituacioInicialAcabadaDeDefinir()
	{
		situacio_inicial_acabada = true;

		return true;
	}

	/**
	 * Consulta l'identificador unic de la partida. Utilitzada per guardar a disc el fitxer corresponent.
	 *
	 * @return L'identificador únic de la partida.
	 */
	public String getIdentificadorUnic()
	{
		return Long.toString( data_creacio.getTime() / 1000L ) + "@" + nom.replace( " ", "-" ) + "@" +
		       getJugadorA().getIdentificadorUnic() + "@" + getJugadorB().getIdentificadorUnic();
	}

	/**
	 * Consulta l'expressió regular de noms permesos.
	 *
	 * @return L'expressió regular de noms permesos.
	 */
	public static String getCaractersPermesos()
	{
		return caracters_permesos;
	}

	/**
	 * Mètode consultor de l’estat de la partida. Els paràmetres permeten aportar informació a sobre de l’últim moviment
	 * d’interès realitzat (normalment l’últim realitzat correctament).
	 *
	 * @param fila    Fila del moviment d'interès
	 * @param columna Columna del moviment d'interès
	 * @return L'estat de la partida
	 * @throws IndexOutOfBoundsException si (fila, columna) no és una coordenada dins dels límits del tauler on es
	 *                                   desenvolupa la partida
	 */
	public EstatPartida comprovaEstatPartida( int fila, int columna ) throws IndexOutOfBoundsException
	{
		if ( !tauler.esCasellaValida( fila, columna ) )
		{
			throw new IndexOutOfBoundsException( "Casella fora del tauler" );
		}
		else if ( tauler.getEstatCasella( fila, columna ) == EstatCasella.BUIDA )
		{
			return EstatPartida.NO_FINALITZADA;
		}
		else
		{
			EstatCasella estat = tauler.getEstatCasella( fila, columna );

			boolean[][] visitats = new boolean[tauler.getMida()][tauler.getMida()];

			for ( boolean[] fila_visitats : visitats )
			{
				Arrays.fill( fila_visitats, false );
			}

			Queue<Casella> cua_bfs = new LinkedList<Casella>();

			cua_bfs.offer( new Casella( fila, columna ) );

			visitats[fila][columna] = true;

			while ( cua_bfs.peek() != null )
			{
				Casella actual = cua_bfs.remove();
				List<Casella> veins = ( ( TaulerHex ) tauler ).getVeins( actual.getFila(), actual.getColumna() );

				for ( Casella veina : veins )
				{
					if ( !visitats[veina.getFila()][veina.getColumna()] &&
					     tauler.getEstatCasella( veina.getFila(), veina.getColumna() ) == estat )
					{
						cua_bfs.offer( veina );
						visitats[veina.getFila()][veina.getColumna()] = true;
					}
				}
			}

			boolean costat1 = false;
			boolean costat2 = false;

			if ( estat == EstatCasella.JUGADOR_B )
			{
				for ( int i = 0; i < tauler.getMida(); i++ )
				{
					if ( visitats[0][i] )
					{
						costat1 = true;
					}

					if ( visitats[tauler.getMida() - 1][i] )
					{
						costat2 = true;
					}
				}
			}
			else if ( estat == EstatCasella.JUGADOR_A )
			{
				for ( int i = 0; i < tauler.getMida(); i++ )
				{
					if ( visitats[i][0] )
					{
						costat1 = true;
					}

					if ( visitats[i][tauler.getMida() - 1] )
					{
						costat2 = true;
					}
				}
			}

			if ( costat1 && costat2 )
			{
				if ( estat == EstatCasella.JUGADOR_A )
				{
					return EstatPartida.GUANYA_JUGADOR_A;
				}
				else if ( estat == EstatCasella.JUGADOR_B )
				{
					return EstatPartida.GUANYA_JUGADOR_B;
				}
				else
				{
					return EstatPartida.NO_FINALITZADA;
				}
			}
			else
			{
				return EstatPartida.NO_FINALITZADA;
			}
		}
	}

	public EstatPartida comprovaEstatPartida()
	{
		return comprovaEstatPartida( darrera_fitxa.getFila(), darrera_fitxa.getColumna() );
	}

	/**
	 * Consulta el nombre màxim de pistes que es poden utilitzar per jugador i partida.
	 *
	 * @return El nombre màxim de pistes a utilitzar per jugador i partida.
	 */
	public static int getMaxNumPistes()
	{
		return max_num_pistes;
	}

	public TaulerHex getTauler()
	{
		return ( TaulerHex ) super.getTauler();
	}

	/**
	 * Consulta el nombre de pistes usades d'un jugador
	 *
	 * @param jugador Número del jugador (0 és A, 1 és B)
	 * @return El nombre de pistes utilitzades pel jugador.
	 * @throws IndexOutOfBoundsException Si jugador < 0 o bé jugador > 1.
	 */
	public int getPistesUsades( int jugador ) throws IndexOutOfBoundsException
	{
		if ( jugador < 0 || jugador > 1 )
		{
			throw new IndexOutOfBoundsException( "No hi ha cap jugador amb aquest número!" );
		}

		return pistes_usades[jugador];
	}

	/**
	 * Consulta el temps de joc d'un jugador
	 *
	 * @param jugador Número del jugador (0 és A, 1 és B)
	 * @return El temps de joc que el jugador ha utilitzat a la partida.
	 * @throws IndexOutOfBoundsException Si jugador < 0 o bé jugador > 1.
	 */
	public long getTempsDeJoc( int jugador ) throws IndexOutOfBoundsException
	{
		if ( jugador < 0 || jugador > 1 )
		{
			throw new IndexOutOfBoundsException( "No hi ha cap jugador amb aquest número!" );
		}

		return temps_de_joc[jugador];
	}

	/**
	 * Modifica les pistes usades d'un jugador
	 *
	 * @param jugador    Número del jugador (0 és A, 1 és B)
	 * @param num_pistes Número de pistes usades pel jugador.
	 * @return Cert si es modifiquen les pistes. Fals altrament.
	 * @throws IndexOutOfBoundsException Si jugador < 0 o bé jugador > 1.
	 * @throws IllegalArgumentException  Si el nombre de pistes és negatiu.
	 */
	public boolean setPistesUsades( int jugador, int num_pistes )
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if ( jugador < 0 || jugador > 1 )
		{
			throw new IndexOutOfBoundsException( "No hi ha cap jugador amb aquest número!" );
		}
		if ( num_pistes < 0 )
		{
			throw new IllegalArgumentException( "El nombre de pistes no pot ser negatiu." );
		}
		pistes_usades[jugador] = num_pistes;
		return true;
	}

	/**
	 * Incrementa les pistes usades per un jugador.
	 *
	 * @param jugador   Número del jugador (0 és A, 1 és B)
	 * @param quantitat Quantitat en la que incrementar el nombre actual de pistes usades.
	 * @return Cert si es modifiquen les pistes. Fals altrament.
	 * @throws IndexOutOfBoundsException Si jugador < 0 o bé jugador > 1.
	 * @throws IllegalArgumentException  Si el nombre de pistes resultant és negatiu.
	 */
	public boolean incrementaPistesUsades( int jugador, int quantitat )
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		return setPistesUsades( jugador, getPistesUsades( jugador ) + quantitat );
	}

	/**
	 * Modifica el temps de joc d'un jugador
	 *
	 * @param jugador Número del jugador (0 és A, 1 és B)
	 * @param temps   Temps de joc del jugador.
	 * @return Cert si es modifica el temps de joc. Fals altrament.
	 * @throws IndexOutOfBoundsException Si jugador < 0 o bé jugador > 1.
	 * @throws IllegalArgumentException  Si el temps de joc és negatiu.
	 */
	public boolean setTempsDeJoc( int jugador, long temps ) throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if ( jugador < 0 || jugador > 1 )
		{
			throw new IndexOutOfBoundsException( "No hi ha cap jugador amb aquest número!" );
		}
		else if ( temps < 0 )
		{
			throw new IllegalArgumentException( "El temps de joc no pot ser negatiu." );
		}

		temps_de_joc[jugador] = temps;

		return true;
	}

	/**
	 * Incrementa el temps de joc d'un jugador
	 *
	 * @param jugador Número del jugador (0 és A, 1 és B)
	 * @param temps   Temps de joc del jugador.
	 * @return Cert si es modifica el temps de joc. Fals altrament.
	 * @throws IndexOutOfBoundsException Si jugador < 0 o bé jugador > 1.
	 * @throws IllegalArgumentException  Si el temps de joc resultant és negatiu.
	 */
	public boolean incrementaTempsDeJoc( int jugador, long temps )
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		return setTempsDeJoc( jugador, getTempsDeJoc( jugador ) + temps );
	}

	/**
	 * Consulta la combinació de colors de la partida.
	 *
	 * @return La combinació de colors de la partida.
	 */
	public CombinacionsColors getCombinacioColors()
	{
		return combinacio_colors;
	}

	/**
	 * Consulta el mode d'inici de la partida.
	 *
	 * @return El mode d'inici de la partida.
	 */
	public ModesInici getModeInici()
	{
		return mode_inici;
	}

	public long getInstantDarrerMoviment()
	{
		return instant_darrer_moviment;
	}

	public boolean setInstantDarrerMoviment( Long instant_darrer_moviment )
	{
		this.instant_darrer_moviment = instant_darrer_moviment;
		return true;
	}

	public Casella getDarreraFitxa()
	{
		return darrera_fitxa;
	}

	public boolean setDarreraFitxa( Casella darrera_fitxa )
	{
		this.darrera_fitxa = darrera_fitxa;
		return true;
	}

	public EstatCasella getTipusFitxesJugador( int jugador )
	{
		return fitxes_jugadors[jugador];
	}

	public UsuariHex getJugadorA()
	{
		return ( UsuariHex ) super.getJugadorA();
	}

	public UsuariHex getJugadorB()
	{
		return ( UsuariHex ) super.getJugadorB();
	}
}
