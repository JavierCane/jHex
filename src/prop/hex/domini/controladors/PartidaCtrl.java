package prop.hex.domini.controladors;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;
import prop.hex.domini.models.UsuariHex;
import prop.hex.domini.models.enums.ModesInici;
import prop.hex.domini.models.enums.TipusJugadors;
import prop.hex.gestors.PartidaHexGstr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

/**
 * Controlador de partida per al joc Hex.
 * Gestiona tot el flux d'execució d'una partida al joc de taula Hex a més de la càrrega i materialització de
 * partides en memòria secundària.
 * <p/>
 * Internament, els jugadors estan indexats per posició en un array; el jugador A es troba en la posició 0 i el B en
 * la posició 1.
 */
public class PartidaCtrl
{

	/**
	 * Instància amb la partida que s'està executant actualment.
	 */
	private static PartidaHex partida_actual = null;

	/**
	 * Instància del gestor de partides en disc.
	 */
	private static PartidaHexGstr gestor_partida = new PartidaHexGstr();

	/**
	 * Instàncies de les intel·ligències artificials per als jugadors no humans.
	 */
	private static MouFitxaIA[] jugadors_ia;

	/**
	 * Tipus de fitxa de cada jugador.
	 */
	private static EstatCasella[] fitxes_jugadors = {
			EstatCasella.JUGADOR_A,
			EstatCasella.JUGADOR_B
	};

	/**
	 * Conté si els jugadors de la partida actual són humans
	 */
	private static boolean[] jugadors_humans;

	/**
	 * Conté els identificadors únics dels usuaris de la partida actual.
	 */
	private static String[] id_usuaris;

	/**
	 * Instància d'intel·ligència artificial usada per donar les pistes.
	 */
	private static MouFitxaIA ia_pistes = new IAHexFacilCtrl();

	/**
	 * Instant de temps en què es va efectuar el darrer moviment.
	 */
	private static long instant_darrer_moviment;

	/**
	 * Instància de casella que conté la posició de la darrera fitxa.
	 */
	private static Casella darrera_fitxa;

	/**
	 * Inicialitza una partida nova.
	 *
	 * @param mida_tauler Mida del tauler de la partida.
	 * @param jugador_a   Usuari que fa de jugador A.
	 * @param jugador_b   Usuari que fa de jugador B.
	 * @param nom         Nom de la partida.
	 * @return Cert si s'ha inicialitzat la partida correctament. Fals altrament.
	 * @throws ClassNotFoundException Si no es pot carregar la classe de les intel·ligències artificials.
	 * @throws IllegalAccessError     Si s'intenta accedir a un lloc no permès quan es carreguen les intel·ligències
	 *                                artificials.
	 * @throws InstantiationError     Si hi ha problemes amb la instanciació de les intel·ligències artificials.
	 */
	public static boolean inicialitzaPartida( int mida_tauler, UsuariHex jugador_a, UsuariHex jugador_b, String nom )
			throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		TaulerHex tauler = new TaulerHex( mida_tauler );
		partida_actual = new PartidaHex( jugador_a, jugador_b, tauler, nom );

		inicialitzaEstructuresControl( jugador_a, jugador_b );

		return true;
	}

	/**
	 * Carrega de memòria secundària la partida identificada per identificador_partida i jugada pels usuaris
	 * jugador_a i jugador_b. A més, esborra del disc la versió antiga de la partida.
	 *
	 * @param identificador_partida Identificador únic de la partida que es vol carregar.
	 * @param jugador_a             Un dels usuaris que juga la partida.
	 * @param jugador_b             L'altre dels usuaris que juga la partida.
	 * @return Cert si s'ha carregat la partida. Fals altrament.
	 * @throws IOException              Si hi ha un error d'entrada/sortida al carregar la partida.
	 * @throws ClassNotFoundException   Si hi ha un problema de classes quan es carrega la partida o la
	 *                                  intel·ligència artificial.
	 * @throws FileNotFoundException    Si no es troba el fitxer de la partida que es vol carregar.
	 * @throws IllegalAccessException   Si s'intenta accedir a un lloc no permès quan es carrega la intel·ligència
	 *                                  artificial.
	 * @throws InstantiationException   Si hi ha problemes quan s'intenta instanciar la partida o la intel·ligència
	 *                                  artificial.
	 * @throws IllegalArgumentException Si algun dels usuaris no juga a la partida.
	 */
	public static boolean carregaPartida( String identificador_partida, UsuariHex jugador_a, UsuariHex jugador_b )
			throws IOException, ClassNotFoundException, FileNotFoundException, IllegalAccessException,
			       InstantiationException, IllegalArgumentException
	{
		partida_actual = gestor_partida.carregaElement( identificador_partida );
		gestor_partida.eliminaElement( identificador_partida );

		UsuariHex jugador_a_partida = ( UsuariHex ) partida_actual.getJugadorA();
		UsuariHex jugador_b_partida = ( UsuariHex ) partida_actual.getJugadorB();

		// Aquí es comprova que els jugadors passats com a paràmetre són realment els de la partida.
		if ( jugador_a_partida.equals( jugador_a ) && jugador_b_partida.equals( jugador_b ) )
		{
			jugador_a_partida = jugador_a;
			jugador_b_partida = jugador_b;
		}
		else if ( jugador_a_partida.equals( jugador_b ) && jugador_b_partida.equals( jugador_a ) )
		{
			jugador_a_partida = jugador_b;
			jugador_b_partida = jugador_a;
		}
		else
		{
			throw new IllegalAccessException( "Algun jugador no és d'aquesta partida" );
		}

		inicialitzaEstructuresControl( jugador_a_partida, jugador_b_partida );
		partida_actual.setJugadorA( jugador_a_partida );
		partida_actual.setJugadorB( jugador_b_partida );

		return ( partida_actual != null );
	}

	/**
	 * Inicialitza les estructures de control per a la partida actual.
	 *
	 * @param jugador_a Usuari que actua com a jugador A.
	 * @param jugador_b Usuari que actua com a jugador B.
	 * @throws ClassNotFoundException Si no es pot carregar la classe de les intel·ligències artificials.
	 * @throws IllegalAccessError     Si s'intenta accedir a un lloc no permès quan es carreguen les intel·ligències
	 *                                artificials.
	 * @throws InstantiationError     Si hi ha problemes amb la instanciació de les intel·ligències artificials.
	 */
	private static void inicialitzaEstructuresControl( UsuariHex jugador_a, UsuariHex jugador_b )
			throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		jugadors_humans = new boolean[2];
		jugadors_humans[0] = ( jugador_a.getTipusJugador() == TipusJugadors.JUGADOR );
		jugadors_humans[1] = ( jugador_b.getTipusJugador() == TipusJugadors.JUGADOR );

		id_usuaris = new String[2];
		id_usuaris[0] = jugador_a.getIdentificadorUnic();
		id_usuaris[1] = jugador_b.getIdentificadorUnic();

		jugadors_ia = new MouFitxaIA[2];
		if ( !jugadors_humans[0] )
		{
			jugadors_ia[0] = ( MouFitxaIA ) Class.forName( "prop.hex.domini.controladors." + jugador_a.getTipusJugador().getClasseCorresponent() )
							.newInstance();
			jugadors_ia[0].setPartida( partida_actual );
		}
		if ( !jugadors_humans[1] )
		{
			jugadors_ia[1] = ( MouFitxaIA ) Class.forName( "prop.hex.domini.controladors." + jugador_b.getTipusJugador().getClasseCorresponent() )
							.newInstance();
			jugadors_ia[1].setPartida( partida_actual );
		}

		ia_pistes.setPartida( partida_actual );

		darrera_fitxa = new Casella( 0, 0 );
		instant_darrer_moviment = new Date().getTime() / 1000L;
	}

	/**
	 * Guarda la partida actual.
	 *
	 * @return Cert si s'ha guardat correctament. Fals altrament.
	 * @throws FileNotFoundException         Si no existeix el fitxer que de la partida que es desa.
	 * @throws IOException                   Si hi ha un error d'entrada/sortida
	 * @throws UnsupportedOperationException Si es vol guardar una partida ja finalitzada.
	 */
	public static boolean guardaPartida() throws FileNotFoundException, IOException, UnsupportedOperationException
	{
		if ( partida_actual.estaFinalitzada() )
		{
			throw new UnsupportedOperationException( "La partida ja ha finalitzat" );
		}

		return gestor_partida.guardaElement( partida_actual, partida_actual.getIdentificadorUnic() );
	}

	/**
	 * Tanca la partida actual.
	 *
	 * @return Cert si s'ha tancat correctament. Fals altrament.
	 */
	public static boolean tancaPartida()
	{
		EstatPartida estat_actual = consultaEstatPartida();
		if ( estat_actual != EstatPartida.NO_FINALITZADA )
		{
			// Aquí se actualizan las estadísticas del Usuario mediante UsuariCtrl. La llamada a esa función debería
			// encargarse de actualizar el objeto de tipo Ranquing.
		}

		darrera_fitxa = null;
		partida_actual = null;
		jugadors_humans = null;
		id_usuaris = null;

		return true;
	}

	/**
	 * Calcula un moviment d'una intel·ligència artificial. Cal que sigui el seu torn.
	 *
	 * @return La casella resultat dels càlculs del moviment.
	 */
	private static Casella movimentIA()
	{
		return jugadors_ia[partida_actual.getTornsJugats() % 2]
				.mouFitxa( fitxes_jugadors[partida_actual.getTornsJugats() % 2] );
	}

	/**
	 * Consulta una pista per al jugador que la demana.
	 *
	 * @return La casella on mouria la intel·ligència artificial configurada per a les pistes.
	 */
	public static Casella obtePista()
	{
		return ia_pistes.mouFitxa( fitxes_jugadors[partida_actual.getTornsJugats() % 2] );
	}

	/**
	 * Executa un moviment d'una intel·ligència artificial (cal que sigui el seu torn)
	 *
	 * @return La casella on mou la intel·ligència artificial.
	 */
	public static Casella executaMovimentIA()
	{
		Casella resultat_moviment = movimentIA();
		mouFitxa( resultat_moviment.getFila(), resultat_moviment.getColumna() );

		return resultat_moviment;
	}

	/**
	 * Mou la fitxa del jugador que toca segons el torn a la casella indicada per (fila, columna).
	 *
	 * @param fila    Fila de la casella
	 * @param columna Columna de la casella
	 * @return Cert si s'ha mogut la fitxa. Fals altrament.
	 * @throws UnsupportedOperationException Si la partida ja ha finalitzat.
	 */
	public static boolean mouFitxa( int fila, int columna ) throws UnsupportedOperationException
	{
		long instant_actual = new Date().getTime() / 1000L;

		if ( partida_actual.estaFinalitzada() )
		{
			throw new UnsupportedOperationException( "La partida ja ha finalitzat" );
		}

		if ( !partida_actual.getTauler()
				.esMovimentValid( fitxes_jugadors[partida_actual.getTornsJugats() % 2], fila, columna ) )
		{
			throw new UnsupportedOperationException( "Moviment no vàlid" );
		}

		if ( partida_actual.getTauler()
				.mouFitxa( fitxes_jugadors[partida_actual.getTornsJugats() % 2], fila, columna ) )
		{
			darrera_fitxa = new Casella( fila, columna );
			partida_actual.incrementaTempsDeJoc( id_usuaris[partida_actual.getTornsJugats() % 2],
					instant_actual - instant_darrer_moviment );
			partida_actual.incrementaTornsJugats( 1 );

			// Per actualitzar l'estat de la partida en el controlador.
			consultaEstatPartida();

			instant_darrer_moviment = new Date().getTime() / 1000L;

			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Consulta si és un torn humà. Necessari perquè el controlador de presentació sàpiga si demanar un moviment
	 * d'intel·ligència artificial al controlador o esperar que l'usuari esculli una casella.
	 *
	 * @return Cert si el jugador és humà. Fals altrament.
	 */
	public static boolean esTornHuma()
	{
		return jugadors_humans[partida_actual.getTornsJugats() % 2];
	}

	/**
	 * Consulta l'estat de la partida.
	 *
	 * @return L'estat de la partida (si guanya algun jugador o encara no està finalitzada).
	 */
	public static EstatPartida consultaEstatPartida()
	{
		EstatPartida estat_partida =
				partida_actual.comprovaEstatPartida( darrera_fitxa.getFila(), darrera_fitxa.getColumna() );

		partida_actual.setFinalitzada( estat_partida != EstatPartida.NO_FINALITZADA );

		return estat_partida;
	}

	/**
	 * Intercanvia la darrera fitxa d'un jugador per l'altre. Útil si s'aplica la regla del pastís.
	 *
	 * @return Cert si s'ha canviat la fitxa. Fals altrament.
	 */
	public static boolean intercanviaDarreraFitxa()
	{
		if ( partida_actual.getTauler().intercanviaFitxa( darrera_fitxa.getFila(), darrera_fitxa.getColumna() ) )
		{
			return partida_actual.incrementaTornsJugats( 1 );
		}
		else
		{
			return false;
		}
	}

	/**
	 * Consulta si es pot intercanviar la darrera fitxa.
	 *
	 * @return Cert si es pot (està la regla del pastís i hi ha només una fitxa). Fals altrament.
	 */
	public static boolean esPotIntercanviarDarreraFitxa()
	{
		return ( partida_actual.getTornsJugats() == 1 && esReglaPastis() );
	}

	/**
	 * Consulta si el joc actual té la regla del pastís.
	 *
	 * @return Cert si comença amb la regla del pastís. Fals altrament.
	 */
	public static boolean esReglaPastis()
	{
		return partida_actual.getModeInici() == ModesInici.PASTIS;
	}

	public PartidaHex getPartidaActual()
	{
		return partida_actual;
	}

}
