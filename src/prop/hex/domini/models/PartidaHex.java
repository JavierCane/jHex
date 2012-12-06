package prop.hex.domini.models;

import prop.cluster.domini.models.Partida;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.MouFitxaIA;
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
	 * Diccionari que relaciona els identificadors del usuaris amb la quantitat de pistes usades.
	 */
	private HashMap<String, Integer> pistes_usades;

	/**
	 * Diccionari que relaciona els identificadors dels usuaris amb el seu temps jugat.
	 */
	private HashMap<String, Long> temps_de_joc;

	/**
	 * Expressió regular amb els caràcters permesos.
	 */
	private static final String caracters_permesos = "^[A-Za-z0-9_ ]+$";

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
	 * Instàncies de les intel·ligències artificials per als jugadors no humans.
	 */
	private MouFitxaIA[] ia_jugadors = new MouFitxaIA[2];

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

		pistes_usades = new HashMap<String, Integer>();
		pistes_usades.put( jugador_a.getIdentificadorUnic(), 0 );
		pistes_usades.put( jugador_b.getIdentificadorUnic(), 0 );

		temps_de_joc = new HashMap<String, Long>();
		temps_de_joc.put( jugador_a.getIdentificadorUnic(), 0L );
		temps_de_joc.put( jugador_b.getIdentificadorUnic(), 0L );

		mode_inici = jugador_a.getModeInici();
		tauler.setModeIniciPartida( mode_inici );
		combinacio_colors = jugador_a.getCombinacionsColors();

		instant_darrer_moviment = new Date().getTime() / 1000L;
		darrera_fitxa = new Casella( 0, 0 );

		this.situacio_inicial = situacio_inicial;

		inicialitzaIAJugadors();
	}

	/**
	 * Comprova si una partida tenia una situació inicial
	 *
	 * @return Cert si tenia situació inicial. Fals altrament.
	 */
	public boolean esPartidaAmbSituacioInicial()
	{
		return situacio_inicial;
	}

	/**
	 * Inicialitza les estructures de control per a la partida actual.
	 *
	 * @throws ClassNotFoundException Si no es pot carregar la classe de les intel·ligències artificials.
	 * @throws IllegalAccessError     Si s'intenta accedir a un lloc no permès quan es carreguen les intel·ligències
	 *                                artificials.
	 * @throws InstantiationError     Si hi ha problemes amb la instanciació de les intel·ligències artificials.
	 */
	private void inicialitzaIAJugadors() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		ia_jugadors[0] = ( MouFitxaIA ) Class.forName( "prop.hex.domini.controladors." +
		                                               ( ( UsuariHex ) jugador_a ).getTipusJugador()
				                                               .getClasseCorresponent() ).newInstance();
		ia_jugadors[0].setPartida( this );

		ia_jugadors[1] = ( MouFitxaIA ) Class.forName( "prop.hex.domini.controladors." +
		                                               ( ( UsuariHex ) jugador_b ).getTipusJugador()
				                                               .getClasseCorresponent() ).newInstance();
		ia_jugadors[1].setPartida( this );
	}

	/**
	 * Consulta l'identificador unic de la partida. Utilitzada per guardar a disc el fitxer corresponent.
	 *
	 * @return L'identificador únic de la partida.
	 */
	public String getIdentificadorUnic()
	{
		return Long.toString( data_creacio.getTime() / 1000L ) + "@" + nom.replace( " ", "-" ) + "@" +
		       ( ( UsuariHex ) jugador_a ).getIdentificadorUnic() + "@" +
		       ( ( UsuariHex ) jugador_b ).getIdentificadorUnic();
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

				for ( int i = 0; i < veins.size(); i++ )
				{
					Casella veina = veins.get( i );
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

	/**
	 * Consulta el nombre màxim de pistes que es poden utilitzar per jugador i partida.
	 *
	 * @return El nombre màxim de pistes a utilitzar per jugador i partida.
	 */
	public static int getMaxNumPistes()
	{
		return max_num_pistes;
	}

	/**
	 * Consulta el nombre de pistes usades de l'usuari amb id_jugador.
	 *
	 * @param id_jugador Identificador únic del jugador.
	 * @return El nombre de pistes utilitzades pel jugador.
	 * @throws IllegalArgumentException Si el jugador amb l'identificador únic passat com a paràmetre no juga la
	 *                                  partida.
	 */
	public int getPistesUsades( String id_jugador ) throws IllegalArgumentException
	{
		if ( !pistes_usades.containsKey( id_jugador ) )
		{
			throw new IllegalArgumentException( "El jugador no juga la partida." );
		}
		else
		{
			return pistes_usades.get( id_jugador );
		}
	}

	/**
	 * Consulta el temps de joc de l'usuari amb id_jugador.
	 *
	 * @param id_jugador Identificador únic del jugador.
	 * @return El temps de joc que el jugador ha utilitzat a la partida.
	 * @throws IllegalArgumentException Si el jugador amb l'identificador únic passat com a paràmetre no juga la
	 *                                  partida.
	 */
	public long getTempsDeJoc( String id_jugador ) throws IllegalArgumentException
	{
		if ( !temps_de_joc.containsKey( id_jugador ) )
		{
			throw new IllegalArgumentException( "El jugador no juga la partida." );
		}
		else
		{
			return temps_de_joc.get( id_jugador );
		}
	}

	/**
	 * Modifica les pistes usades per l'usuari amb id_jugador.
	 *
	 * @param id_jugador Identificador únic del jugador.
	 * @param num_pistes Número de pistes usades pel jugador.
	 * @return Cert si es modifiquen les pistes. Fals altrament.
	 * @throws IllegalArgumentException Si el jugador amb l'identificador únic passat com a paràmetre no juga la
	 *                                  partida, o si el nombre de pistes és negatiu.
	 */
	public boolean setPistesUsades( String id_jugador, int num_pistes ) throws IllegalArgumentException
	{
		if ( !pistes_usades.containsKey( id_jugador ) )
		{
			throw new IllegalArgumentException( "El jugador no juga la partida." );
		}
		if ( num_pistes < 0 )
		{
			throw new IllegalArgumentException( "El nombre de pistes no pot ser negatiu." );
		}
		pistes_usades.put( id_jugador, num_pistes );
		return true;
	}

	/**
	 * Incrementa les pistes usades per l'usuari amb id_jugador.
	 *
	 * @param id_jugador Identificador únic del jugador.
	 * @param quantitat  Quantitat en la que incrementar el nombre actual de pistes usades.
	 * @return Cert si es modifiquen les pistes. Fals altrament.
	 * @throws IllegalArgumentException Si el jugador amb l'identificador únic passat com a paràmetre no juga la
	 *                                  partida, o si el nombre de pistes resultant és negatiu.
	 */
	public boolean incrementaPistesUsades( String id_jugador, int quantitat ) throws IllegalArgumentException
	{
		return setPistesUsades( id_jugador, getPistesUsades( id_jugador ) + quantitat );
	}

	/**
	 * Modifica el temps de joc de l'usuari amb id_jugador.
	 *
	 * @param id_jugador Identificador únic del jugador.
	 * @param temps      Temps de joc del jugador.
	 * @return Cert si es modifica el temps de joc. Fals altrament.
	 * @throws IllegalArgumentException Si el jugador amb l'identificador únic passat com a paràmetre no juga la
	 *                                  partida, o si el temps de joc és negatiu.
	 */
	public boolean setTempsDeJoc( String id_jugador, long temps ) throws IllegalArgumentException
	{
		if ( !temps_de_joc.containsKey( id_jugador ) )
		{
			throw new IllegalArgumentException( "El jugador no juga la partida." );
		}
		if ( temps < 0 )
		{
			throw new IllegalArgumentException( "El temps de joc no pot ser negatiu." );
		}
		temps_de_joc.put( id_jugador, temps );
		return true;
	}

	/**
	 * Incrementa el temps de joc de l'usuari amb id_jugador
	 *
	 * @param id_jugador Identificador únic del jugador.
	 * @param temps      Temps de joc del jugador.
	 * @return Cert si es modifica el temps de joc. Fals altrament.
	 * @throws IllegalArgumentException Si el jugador amb l'identificador únic passat com a paràmetre no juga la
	 *                                  partida, o si el temps de joc resultant és negatiu.
	 */
	public boolean incrementaTempsDeJoc( String id_jugador, long temps ) throws IllegalArgumentException
	{
		return setTempsDeJoc( id_jugador, getTempsDeJoc( id_jugador ) + temps );
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

	/**
	 * Mètode per obtenir la casella a la que mouria la IA del jugador del torn actual
	 *
	 * @return la casella a la que mouria la IA del jugador del torn actual
	 */
	public Casella getMovimentIATornActual()
	{
		return ia_jugadors[torns_jugats % 2].mouFitxa( fitxes_jugadors[torns_jugats % 2] );
	}

	public EstatCasella[] getFitxesJugadors()
	{
		return fitxes_jugadors;
	}

	public EstatCasella getFitxaJugadorTornActual()
	{
		return fitxes_jugadors[torns_jugats % 2];
	}

	public UsuariHex getUsuariTornActual()
	{
		if ( 0 == torns_jugats % 2 )
		{
			return ( UsuariHex ) getJugadorA();
		}
		else
		{
			return ( UsuariHex ) getJugadorB();
		}
	}
}
