package prop.hex.domini.controladors;

import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.*;
import prop.hex.domini.models.enums.ModesInici;
import prop.hex.domini.models.enums.TipusJugadors;
import prop.hex.gestors.PartidaHexGstr;
import prop.hex.gestors.RanquingGstr;
import prop.hex.gestors.UsuariGstr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

/**
 * Controlador de partida per al joc Hex. Programat seguint el patró singleton.
 * Gestiona tot el flux d'execució d'una partida al joc de taula Hex a més de la càrrega i materialització de
 * partides en memòria secundària.
 * <p/>
 * Internament, els jugadors estan indexats per posició en un array; el jugador A es troba en la posició 0 i el B en
 * la posició 1.
 */
public class PartidaCtrl
{

	/**
	 * Instància del controlador.
	 */
	private static PartidaCtrl instancia;

	/**
	 * Instància amb la partida que s'està executant actualment.
	 */
	private PartidaHex partida_actual;

	/**
	 * Instància del gestor de partides en disc.
	 */
	private PartidaHexGstr gestor_partida;

	/**
	 * Conté els usuaris pre inicialitzats de la partida actual, únicament son útils avans de cridat a
	 * inicialitzaPartida.
	 */
	private UsuariHex[] usuaris_preinicialitzats_partida = new UsuariHex[2];

	/**
	 * Constructor per defecte. Declarat privat perquè és una classe singleton
	 */
	private PartidaCtrl()
	{
		gestor_partida = new PartidaHexGstr();
	}

	/**
	 * Consultora de l'instancia actual del controlador de partida.
	 * Si encara no s'ha inicialitzat l'objecte, crida el constructor. Si ja s'ha instanciat prèviament,
	 * simplement retorna l'instancia ja creada.
	 *
	 * @return L'objecte singleton amb el el controlador de partida.
	 */
	public static synchronized PartidaCtrl getInstancia()
	{
		if ( instancia == null )
		{
			instancia = new PartidaCtrl();
		}

		return instancia;
	}

	public static void comprovaConsistenciaFitxersIDades() throws IOException, ClassNotFoundException
	{
		// Comprovo la consistència del rànquing (si no existeix a disc el creo, si ja existeix el carrego a memòria)
		RanquingGstr ranquing_gestor = new RanquingGstr();

		// Si no existeix el fitxer del rànquing, el creo
		if ( !ranquing_gestor.existeixElement() )
		{
			ranquing_gestor.guardaElement();
		}
		else // Si el fitxer de rànquign ya existeix, el carrego a memòria
		{
			ranquing_gestor.carregaElement();
		}

		// Comprovo consistència de jugadors màquina (han de figurar tots els usuaris de tipus màquina a disc)
		UsuariGstr usuari_gestor = new UsuariGstr();
		for ( TipusJugadors tipus_jugador_maquina : TipusJugadors.obteLlistatMaquines() )
		{
			if ( !usuari_gestor.existeixElement( tipus_jugador_maquina.getNomUsuari() ) )
			{
				UsuariHex usuari_maquina =
						new UsuariHex( tipus_jugador_maquina.getNomUsuari(), "", tipus_jugador_maquina );

				usuari_gestor.guardaElement( usuari_maquina, usuari_maquina.getIdentificadorUnic() );
			}
		}
	}

	/**
	 * Preinicialitza els usuaris que disputarán la partida. Aixó vol dir que carrega l'usuari corresponent a el
	 * tipus de jugador seleccionat comprovant les credencials en cas que toqui, carregant de disc la versió
	 * corresponent de la IA o instanciant un usuari convidat temporal si es el cas.
	 * Cal cridar a aquesta funció avans d'inicialitzar la partida mitjançant inicialitzaPartida.
	 *
	 * @param num_jugador
	 * @param tipus_jugador
	 * @param nom_usuari
	 * @param contrasenya_usuari
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NullPointerException
	 */
	public void preInicialitzaUsuariPartida( int num_jugador, TipusJugadors tipus_jugador, String nom_usuari,
	                                         String contrasenya_usuari )
			throws IllegalArgumentException, FileNotFoundException, IOException, ClassNotFoundException,
			       NullPointerException
	{
		UsuariCtrl usuari_ctrl = UsuariCtrl.getInstancia();

		// Si l'usuari a establir es de tipus jugador, comprovaré si es tracta de l'usuari principal del joc,
		// en aquest cas, simplement l'establiré, en cas que sigui el jugador secundari, comrpovaré les credencials
		if ( TipusJugadors.JUGADOR == tipus_jugador )
		{
			if ( 0 == num_jugador )
			{
				usuaris_preinicialitzats_partida[num_jugador] = usuari_ctrl.getUsuariPrincipal();
			}
			else
			{
				usuaris_preinicialitzats_partida[num_jugador] =
						usuari_ctrl.carregaUsuari( nom_usuari, contrasenya_usuari, tipus_jugador );
			}
		}
		else if ( TipusJugadors.CONVIDAT == tipus_jugador )
		{
			// Si l'usuari a establir es de tipus convidat, crearé un UsuariHex temporal amb el nom donat
			usuaris_preinicialitzats_partida[num_jugador] = new UsuariHex( nom_usuari, "", tipus_jugador );
		}
		else
		{
			// Si l'usuari a establir es de tipus màquina, simplement el carregaré de disc
			usuaris_preinicialitzats_partida[num_jugador] =
					usuari_ctrl.carregaUsuari( tipus_jugador.getNomUsuari(), "", tipus_jugador );
		}
	}

	/**
	 * Inicialitza una partida nova.
	 *
	 * @param mida_tauler      Mida del tauler de la partida.
	 * @param nom_partida      Nom de la partida.
	 * @param situacio_inicial Indica si la partida ve definida amb una situació inicial
	 * @throws NullPointerException     Si no s'han preinicialitzat els usuaris de la partida previament.
	 * @throws IllegalArgumentException Si no s'ha especificat un nom de partida o si ja existeix una partida amb
	 *                                  aquest identificador
	 * @throws ClassNotFoundException   Si no es pot carregar la classe de les intel·ligències artificials.
	 * @throws InstantiationException   Si hi ha problemes amb la instanciació de les intel·ligències artificials.
	 * @throws IllegalAccessException   Si s'intenta accedir a un lloc no permès quan es carreguen les
	 *                                  intel·ligències artificials.
	 */
	public void inicialitzaPartida( int mida_tauler, String nom_partida, boolean situacio_inicial )
			throws NullPointerException, IllegalArgumentException, ClassNotFoundException, InstantiationException,
			       IllegalAccessException
	{
		if ( null == usuaris_preinicialitzats_partida[0] || null == usuaris_preinicialitzats_partida[1] )
		{
			throw new NullPointerException(
					"No s'han preinicialitzat els usuaris de la partida abans d'intentar-la crear." );
		}
		else if ( nom_partida.isEmpty() )
		{
			throw new IllegalArgumentException( "S'ha de definir un nom per poder començar la partida." );
		}
		else
		{
			partida_actual = new PartidaHex( usuaris_preinicialitzats_partida[0], usuaris_preinicialitzats_partida[1],
					new TaulerHex( mida_tauler ), nom_partida, situacio_inicial );

			if ( gestor_partida.existeixElement( partida_actual.getIdentificadorUnic() ) )
			{
				throw new IllegalArgumentException(
						"Ja existeix una partida amb aquest nom i per aquests usuaris a la mateixa data." );
			}
		}
	}

	/**
	 * Carrega de memòria secundària la partida identificada per identificador_partida i la estableix com la partida
	 * en joc
	 *
	 * @param identificador_partida
	 * @throws ClassNotFoundException Si hi ha un problema de classes quan es carrega la partida o la
	 *                                intel·ligència artificial.
	 * @throws IOException            Si hi ha un error d'entrada/sortida al carregar la partida.
	 */
	public void carregaPartida( String identificador_partida ) throws ClassNotFoundException, IOException
	{
		partida_actual = gestor_partida.carregaElement( identificador_partida );
		gestor_partida.eliminaElement( identificador_partida );
	}

	/**
	 * Consulta les partides de l'usuari principal
	 *
	 * @return Una llista amb les dades de les partides. Dins de cada element de la llista, els elements són
	 *         - 0: Identificador únic de la partida
	 *         - 1: Nom de la partida
	 *         - 2: Nom d'usuari de l'oponent (contra qui juga l'usuari principal de la sessió, no de la partida)
	 *         - 3: String formatat amb la data i hora d'inici de la partida
	 */
	public String[][] llistaPartidesUsuari()
	{
		String id_usuari = UsuariCtrl.getInstancia().getUsuariPrincipal().getIdentificadorUnic();
		Set<String> id_partides = gestor_partida.llistaPartidesUsuari( id_usuari );
		String[][] llista_partides = new String[id_partides.size()][4];
		int i = 0;
		for ( String id_partida : id_partides )
		{
			String[] info_partida = new String[4];
			String[] camps = id_partida.split( "@" );

			// L'identificador s'inclou perquè el controlador de presentació pugui demanar la partida concreta.
			info_partida[0] = id_partida;

			// Es formaten la data i l'hora perquè no ho hagi de fer la vista.
			info_partida[3] = String.format( "%1$td/%1$tm/%1$tY %1$tR", Long.valueOf( camps[0] ) * 1000L );

			info_partida[1] = camps[1].replace( '-', ' ' );
			if ( id_usuari.equals( camps[2] ) )
			{
				info_partida[2] = camps[3].replace( '-', ' ' );
			}
			else
			{
				info_partida[2] = camps[2].replace( '-', ' ' );
			}

			llista_partides[i] = info_partida;
			i++;
		}

		return llista_partides;
	}

	/**
	 * Esborra les partides d'un usuari
	 *
	 * @param id_usuari Identificador únic de l'usuari de qui es volen esborrar les partides.
	 * @return Cert si s'han esborrat. Fals altrament.
	 */
	public boolean esborraPartidesUsuari( String id_usuari )
	{
		Set<String> id_partides = gestor_partida.llistaPartidesUsuari( id_usuari );
		for ( String id_partida : id_partides )
		{
			gestor_partida.eliminaElement( id_partida );
		}

		return true;
	}

	/**
	 * Guarda la partida actual.
	 *
	 * @return Cert si s'ha guardat correctament. Fals altrament.
	 * @throws FileNotFoundException         Si no existeix el fitxer que de la partida que es desa.
	 * @throws IOException                   Si hi ha un error d'entrada/sortida
	 * @throws UnsupportedOperationException Si es vol guardar una partida ja finalitzada.
	 */
	public boolean guardaPartida() throws FileNotFoundException, IOException, UnsupportedOperationException
	{
		if ( partida_actual.estaFinalitzada() )
		{
			throw new UnsupportedOperationException( "La partida ja ha finalitzat" );
		}
		else if ( usuaris_preinicialitzats_partida[0].getTipusJugador() != TipusJugadors.JUGADOR &&
		          usuaris_preinicialitzats_partida[1].getTipusJugador() != TipusJugadors.JUGADOR )
		{
			throw new UnsupportedOperationException(
					"Per guardar la partida cal que algun dels jugadors estigui registrat!" );
		}

		return gestor_partida.guardaElement( partida_actual, partida_actual.getIdentificadorUnic() );
	}

	/**
	 * Tanca la partida actual. Actualitza les estadístiques i esborra el fitxer de partida si la partida ja ha
	 * finalitzat.
	 *
	 * @return Cert si s'ha tancat correctament. Fals altrament.
	 */
	public void tancaPartida()
	{
		EstatPartida estat_actual = consultaEstatPartida();
		if ( estat_actual != EstatPartida.NO_FINALITZADA )
		{
			if ( !partida_actual.esPartidaAmbSituacioInicial() )
			{
				UsuariHex usuari_a = ( UsuariHex ) partida_actual.getJugadorA();
				UsuariHex usuari_b = ( UsuariHex ) partida_actual.getJugadorA();

				UsuariCtrl.getInstancia()
						.actualitzaEstadistiques( usuari_a, estat_actual == EstatPartida.GUANYA_JUGADOR_A,
								usuari_b.getTipusJugador(),
								partida_actual.getTempsDeJoc( usuari_a.getIdentificadorUnic() ),
								partida_actual.getTauler().getNumFitxesA() );

				UsuariCtrl.getInstancia()
						.actualitzaEstadistiques( usuari_b, estat_actual == EstatPartida.GUANYA_JUGADOR_B,
								usuari_a.getTipusJugador(),
								partida_actual.getTempsDeJoc( usuari_b.getIdentificadorUnic() ),
								partida_actual.getTauler().getNumFitxesA() );

				Ranquing.getInstancia().actualitzaUsuari( usuari_a );
				Ranquing.getInstancia().actualitzaUsuari( usuari_b );

				gestor_partida.eliminaElement( partida_actual.getIdentificadorUnic() );
			}
		}

		partida_actual = null;
		usuaris_preinicialitzats_partida = null;
	}

	/**
	 * Tanca la partida actual i l'esborra del disc. Actualitza les estadístiques si la partida ja ha finalitzat.
	 *
	 * @return Cert si s'ha tancat correctament. Fals altrament.
	 */
	public void tancaIEliminaPartida() throws IOException
	{
		tancaPartida();
		gestor_partida.eliminaElement( partida_actual.getIdentificadorUnic() );
	}

	/**
	 * Consulta una pista per al jugador que la demana.
	 *
	 * @return La casella on mouria la intel·ligència artificial configurada per a les pistes.
	 */
	public Casella obtePista()
	{
		return partida_actual.getMovimentIATornActual();
	}

	/**
	 * Executa un moviment d'una intel·ligència artificial (cal que sigui el seu torn)
	 *
	 * @return La casella on mou la intel·ligència artificial.
	 */
	public Casella executaMovimentIA()
	{
		Casella resultat_moviment = partida_actual.getMovimentIATornActual();
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
	public boolean mouFitxa( int fila, int columna ) throws UnsupportedOperationException
	{
		if ( partida_actual.estaFinalitzada() )
		{
			throw new UnsupportedOperationException( "La partida ja ha finalitzat" );
		}
		else
		{
			if ( !partida_actual.getTauler()
					.esMovimentValid( partida_actual.getFitxaJugadorTornActual(), fila, columna ) )
			{
				return false;
			}
			else
			{
				if ( partida_actual.getTauler().mouFitxa( partida_actual.getFitxaJugadorTornActual(), fila, columna ) )
				{
					partida_actual.setDarreraFitxa( new Casella( fila, columna ) );

					long instant_actual = new Date().getTime() / 1000L;

					partida_actual.incrementaTempsDeJoc( partida_actual.getUsuariTornActual().getIdentificadorUnic(),
							instant_actual - partida_actual.getInstantDarrerMoviment() );

					partida_actual.incrementaTornsJugats( 1 );

					// Per actualitzar l'estat de la partida en el controlador.
					consultaEstatPartida();

					partida_actual.setInstantDarrerMoviment( instant_actual );

					return true;
				}
				else
				{
					return false;
				}
			}
		}
	}

	/**
	 * Consulta si és un torn humà. Necessari perquè el controlador de presentació sàpiga si demanar un moviment
	 * d'intel·ligència artificial al controlador o esperar que l'usuari esculli una casella.
	 *
	 * @return Cert si el jugador és humà. Fals altrament.
	 */
	public boolean esTornHuma()
	{
		TipusJugadors tipus_jugador_actual =
				usuaris_preinicialitzats_partida[partida_actual.getTornsJugats() % 2].getTipusJugador();

		return ( TipusJugadors.JUGADOR == tipus_jugador_actual || TipusJugadors.CONVIDAT == tipus_jugador_actual );
	}

	/**
	 * Consulta l'estat de la partida.
	 *
	 * @return L'estat de la partida (si guanya algun jugador o encara no està finalitzada).
	 */
	public EstatPartida consultaEstatPartida()
	{
		EstatPartida estat_partida = partida_actual.comprovaEstatPartida( partida_actual.getDarreraFitxa().getFila(),
				partida_actual.getDarreraFitxa().getColumna() );

		partida_actual.setFinalitzada( estat_partida != EstatPartida.NO_FINALITZADA );

		return estat_partida;
	}

	/**
	 * Intercanvia la darrera fitxa d'un jugador per l'altre. Útil si s'aplica la regla del pastís.
	 *
	 * @return Cert si s'ha canviat la fitxa. Fals altrament.
	 */
	public boolean intercanviaDarreraFitxa()
	{
		return esPotIntercanviarDarreraFitxa() &&
		       ( ( TaulerHex ) partida_actual.getTauler() ).intercanviaFitxa( partida_actual.getDarreraFitxa() ) &&
		       partida_actual.incrementaTornsJugats( 1 );
	}

	/**
	 * Consulta si es pot intercanviar la darrera fitxa.
	 *
	 * @return Cert si es pot (està la regla del pastís i hi ha només una fitxa). Fals altrament.
	 */
	public boolean esPotIntercanviarDarreraFitxa()
	{
		return ( partida_actual.getTornsJugats() == 1 && esReglaPastis() );
	}

	/**
	 * Consulta si el joc actual té la regla del pastís.
	 *
	 * @return Cert si comença amb la regla del pastís. Fals altrament.
	 */
	public boolean esReglaPastis()
	{
		return partida_actual.getModeInici() == ModesInici.PASTIS;
	}

	/**
	 * Obté la partida actual en joc.
	 *
	 * @return PartidaHex amb la partida actual.
	 */
	public PartidaHex getPartidaActual()
	{
		return partida_actual;
	}
}
