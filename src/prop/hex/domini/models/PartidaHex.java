package prop.hex.domini.models;

import prop.cluster.domini.models.Partida;
import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.ModesInici;

import java.io.Serializable;
import java.util.*;

public class PartidaHex extends Partida implements Serializable
{

	/**
	 * ID de serialització
	 */
	private static final long serialVersionUID = -3193881780232604048L;

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
	 * Constructora alternativa per partides que no han estat jugades
	 *
	 * @param jugador_a Usuari que farà de jugador A
	 * @param jugador_b Usuari que farà de jugador B
	 * @param tauler    Tauler on es desenvoluparà la partida
	 * @param nom       Nom de la partida
	 */
	public PartidaHex( UsuariHex jugador_a, UsuariHex jugador_b, TaulerHex tauler, String nom )
			throws IllegalArgumentException
	{
		super( jugador_a, jugador_b, tauler, nom );

		pistes_usades = new HashMap<String, Integer>();
		pistes_usades.put( jugador_a.getIdentificadorUnic(), 0 );
		pistes_usades.put( jugador_b.getIdentificadorUnic(), 0 );

		temps_de_joc = new HashMap<String, Long>();
		temps_de_joc.put( jugador_a.getIdentificadorUnic(), 0L );
		temps_de_joc.put( jugador_b.getIdentificadorUnic(), 0L );

		mode_inici = jugador_a.getModeInici();
		combinacio_colors = jugador_a.getCombinacionsColors();
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
	 *                                  partida.
	 */
	public boolean incrementaPistesUsades( String id_jugador, int quantitat ) throws IllegalArgumentException
	{
		if ( !pistes_usades.containsKey( id_jugador ) )
		{
			throw new IllegalArgumentException( "El jugador no juga la partida." );
		}
		else
		{
			int nou_num_pistes = pistes_usades.get( id_jugador ) + quantitat;
			pistes_usades.put( id_jugador, nou_num_pistes );
			return true;
		}
	}

	/**
	 * Modifica les pistes usades per l'usuari amb id_jugador.
	 *
	 * @param id_jugador Identificador únic del jugador.
	 * @param temps      Número de pistes usades pel jugador.
	 * @return Cert si es modifiquen les pistes. Fals altrament.
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
}
