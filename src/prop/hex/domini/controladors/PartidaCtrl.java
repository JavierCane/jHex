package prop.hex.domini.controladors;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.*;
import prop.hex.domini.models.enums.ModesInici;
import prop.hex.domini.models.enums.TipusJugadors;
import prop.hex.gestors.PartidaHexGstr;
import prop.hex.gestors.RanquingGstr;
import prop.hex.gestors.UsuariHexGstr;

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
public final class PartidaCtrl
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
	 * Conté els usuaris pre inicialitzats de la partida actual, únicament son útils abans d'inicialitzar una partida
	 * o carregar-la de disc.
	 */
	private UsuariHex[] usuaris_preinicialitzats_partida;

	/**
	 * Instàncies de les intel·ligències artificials per als jugadors no humans.
	 */
	private MouFitxaIA[] ia_jugadors;

	/**
	 * Constructor per defecte. Declarat privat perquè és una classe singleton
	 */
	private PartidaCtrl()
	{
		netejaParametresPartidaActual();
	}

	public final void netejaParametresPartidaActual()
	{
		partida_actual = null;
		gestor_partida = new PartidaHexGstr();
		usuaris_preinicialitzats_partida = new UsuariHex[2];
		ia_jugadors = new MouFitxaIA[2];
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
		else // Si el fitxer de rànquing ja existeix, el carrego a memòria
		{
			ranquing_gestor.carregaElement();
		}

		// Comprovo consistència de jugadors màquina (han de figurar tots els usuaris de tipus màquina a disc)
		UsuariHexGstr usuari_gestor = new UsuariHexGstr();
		for ( TipusJugadors tipus_jugador_maquina : TipusJugadors.obteLlistatMaquines() )
		{
			if ( !usuari_gestor.existeixElement( tipus_jugador_maquina.getNomUsuari() ) )
			{
				UsuariCtrl.getInstancia().creaUsuari( tipus_jugador_maquina.getNomUsuari(), "", tipus_jugador_maquina );
			}
		}
	}

	/**
	 * Preinicialitza els usuaris que disputaran la partida. Aixo vol dir que carrega l'usuari corresponent a el
	 * tipus de jugador seleccionat comprovant les credencials en cas que toqui, carregant de disc la versió
	 * corresponent de la IA o instanciant un usuari convidat temporal si és el cas.
	 * Cal cridar a aquesta funció abans d'inicialitzar la partida mitjançant inicialitzaPartida.
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
		                                               ( partida_actual.getJugadorA() ).getTipusJugador()
				                                               .getClasseCorresponent() ).newInstance();
		ia_jugadors[0].setPartida( partida_actual );

		ia_jugadors[1] = ( MouFitxaIA ) Class.forName( "prop.hex.domini.controladors." +
		                                               ( partida_actual.getJugadorB() ).getTipusJugador()
				                                               .getClasseCorresponent() ).newInstance();
		ia_jugadors[1].setPartida( partida_actual );
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
					new TaulerHex( mida_tauler ), nom_partida,
					UsuariCtrl.getInstancia().getUsuariPrincipal().getModeInici(),
					UsuariCtrl.getInstancia().obteCombinacioColors(), situacio_inicial );

			if ( gestor_partida.existeixElement( partida_actual.getIdentificadorUnic() ) )
			{
				throw new IllegalArgumentException(
						"Ja existeix una partida amb aquest nom i per aquests usuaris a la mateixa data." );
			}

			inicialitzaIAJugadors();
		}
	}

	/**
	 * Carrega de memòria secundària la partida identificada per identificador_partida i la estableix com la partida
	 * en joc
	 *
	 * @param id_partida              Identificador de la partida que es vol carregar
	 * @param contrasenya_contrincant Contrasenya de l'usuari contrincant
	 * @throws IOException            Si no es pot carregar la partida
	 * @throws ClassNotFoundException Si no existeix la classe PartidaHex o la de la intel·ligència artificial.
	 * @throws IllegalAccessError     Si hi ha un problema d'accés al fitxer amb la partida
	 * @throws InstantiationError     Si hi ha un problema de classes a la instanciació de la partida que es vol
	 *                                carregar
	 */
	public void carregaPartida( String id_partida, String contrasenya_contrincant )
			throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException
	{
		partida_actual = gestor_partida.carregaElement( id_partida );
		String nom_usuari_contrincant;
		TipusJugadors tipus_jugador_contrincant;
		if ( !partida_actual.getJugadorA().equals( UsuariCtrl.getInstancia().getUsuariPrincipal() ) )
		{
			nom_usuari_contrincant = partida_actual.getJugadorA().getNom();
			tipus_jugador_contrincant = partida_actual.getJugadorA().getTipusJugador();

			partida_actual.setJugadorB( UsuariCtrl.getInstancia().getUsuariPrincipal() );
			partida_actual.setJugadorA( UsuariCtrl.getInstancia()
					.carregaUsuari( nom_usuari_contrincant, contrasenya_contrincant, tipus_jugador_contrincant ) );
		}
		else
		{
			nom_usuari_contrincant = partida_actual.getJugadorB().getNom();
			tipus_jugador_contrincant = partida_actual.getJugadorB().getTipusJugador();

			partida_actual.setJugadorA( UsuariCtrl.getInstancia().getUsuariPrincipal() );
			partida_actual.setJugadorB( UsuariCtrl.getInstancia()
					.carregaUsuari( nom_usuari_contrincant, contrasenya_contrincant, tipus_jugador_contrincant ) );
		}

		inicialitzaIAJugadors();

		gestor_partida.eliminaElement( id_partida );
	}

	/**
	 * Consulta quin usuari no ha iniciat sessió (si és el cas) per poder jugar a la partida actual.
	 *
	 * @param id_partida Identificador únic de la partida
	 * @return El nom de l'usuari que no ha iniciat sessió (si és el cas). Si no n'hi ha, retorna null.
	 * @throws IOException            Si no es pot carregar la partida
	 * @throws ClassNotFoundException Si no existeix la classe PartidaHex.
	 * @throws IllegalAccessError     Si hi ha un problema d'accés al fitxer amb la partida
	 * @throws InstantiationError     Si hi ha un problema de classes a la instanciació de la partida que es vol
	 *                                consultar
	 */
	public String usuariSenseAutenticarAPartida( String id_partida )
			throws IOException, ClassNotFoundException, IllegalAccessError, InstantiationError
	{
		String nom_usuari = null;

		PartidaHex partida_futura = gestor_partida.carregaElement( id_partida );
		if ( !partida_futura.getJugadorA().equals( UsuariCtrl.getInstancia().getUsuariPrincipal() ) &&
		     partida_futura.getJugadorA().getTipusJugador() == TipusJugadors.JUGADOR )
		{
			nom_usuari = partida_futura.getJugadorA().getNom();
		}
		else if ( !partida_futura.getJugadorB().equals( UsuariCtrl.getInstancia().getUsuariPrincipal() ) &&
		          partida_futura.getJugadorB().getTipusJugador() == TipusJugadors.JUGADOR )
		{
			nom_usuari = partida_futura.getJugadorB().getNom();
		}

		return nom_usuari;
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
	public boolean guardaPartida() throws IOException, UnsupportedOperationException
	{
		if ( partida_actual.estaFinalitzada() )
		{
			throw new UnsupportedOperationException( "La partida ja ha finalitzat" );
		}
		else if ( partida_actual.getJugadorA().getTipusJugador() != TipusJugadors.JUGADOR &&
		          partida_actual.getJugadorB().getTipusJugador() != TipusJugadors.JUGADOR )
		{
			throw new UnsupportedOperationException(
					"Per guardar la partida cal que un dels jugadors estigui registrat" );
		}
		else
		{
			return gestor_partida.guardaElement( partida_actual );
		}
	}

	/**
	 * Comprova si la partida actual té situació inicial
	 *
	 * @return Cert si la partida actual té situació inicial. Fals altrament.
	 */
	public boolean esPartidaAmbSituacioInicial()
	{
		return partida_actual.teSituacioInicial();
	}

	/**
	 * Comprova si la partida actual té situació inicial i està acabada de definir
	 *
	 * @return Cert si la partida actual té situació inicial i està acabada de definr. Fals altrament.
	 */
	public boolean esPartidaAmbSituacioInicialAcabadaDeDefinir()
	{
		return partida_actual.teSituacioInicialAcabadaDeDefinir();
	}

	/**
	 * Per una partida que té situació inicial, estableix que aquesta ja està acabada de definir.
	 */
	public void acabaDeDefinirSituacioInicial()
	{
		partida_actual.setSituacioInicialAcabadaDeDefinir();
	}

	/**
	 * Finalitza la partida actual.
	 * <p/>
	 * Actualitza les estadístiques dels usuaris si la partida ja ha finalitzat i no ha començat
	 * definint una situació inicial.
	 */
	public void finalitzaPartida() throws IOException
	{
		EstatPartida estat_actual = consultaEstatPartida();

		// Si la partida ha finalitzat i no ha començat definint una situació inicial,
		// actualitzo estadístiques d'usuari
		if ( estat_actual != EstatPartida.NO_FINALITZADA )
		{
			// En cas de que la partida estigués guardada, l'elimino
			gestor_partida.eliminaElement( partida_actual.getIdentificadorUnic() );

			// Si no ha començat definint situació inicial, actualitzo estadístiques d'usuari
			if ( !partida_actual.teSituacioInicial() )
			{
				UsuariHex usuari_a = partida_actual.getJugadorA();
				UsuariHex usuari_b = partida_actual.getJugadorB();

				if ( usuari_a.getTipusJugador() != TipusJugadors.CONVIDAT )
				{
					UsuariCtrl.getInstancia()
							.actualitzaEstadistiques( usuari_a, estat_actual == EstatPartida.GUANYA_JUGADOR_A,
									usuari_b.getTipusJugador(), partida_actual.getTempsDeJoc( 0 ),
									partida_actual.getTauler().getNumFitxesA() );

					Ranquing.getInstancia().actualitzaUsuari( usuari_a );
				}

				if ( usuari_b.getTipusJugador() != TipusJugadors.CONVIDAT )
				{
					UsuariCtrl.getInstancia()
							.actualitzaEstadistiques( usuari_b, estat_actual == EstatPartida.GUANYA_JUGADOR_B,
									usuari_a.getTipusJugador(), partida_actual.getTempsDeJoc( 1 ),
									partida_actual.getTauler().getNumFitxesA() );

					Ranquing.getInstancia().actualitzaUsuari( usuari_b );
				}
			}
		}

		// Guardo el rànquing a disc.
		try
		{
			new RanquingGstr().guardaElement();
		}
		catch ( IOException excepcio )
		{
			throw new IOException( "No s'ha pogut desar el ranquing" );
		}

		// Reinicialitzo els paràmetres del controlador de partides pero netejar valors anteriors.
		netejaParametresPartidaActual();
	}

	private Casella getMovimentIATornActual()
	{
		return ia_jugadors[getNumJugadorTornActual()].mouFitxa( getTipusFitxesJugadorTornActual() );
	}

	/**
	 * Consulta una pista per al jugador que la demana.
	 *
	 * @return La casella on mouria la intel·ligència artificial configurada per a les pistes.
	 */
	public Casella obtePista()
	{
		return getMovimentIATornActual();
	}

	/**
	 * Executa un moviment d'una intel·ligència artificial (cal que sigui el seu torn)
	 *
	 * @return La casella on mou la intel·ligència artificial.
	 */
	public Casella executaMovimentIA()
	{
		partida_actual.setInstantDarrerMoviment( new Date().getTime() );
		Casella resultat_moviment = getMovimentIATornActual();
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
			if ( !partida_actual.getTauler().esMovimentValid( getTipusFitxesJugadorTornActual(), fila, columna ) )
			{
				return false;
			}
			else
			{
				if ( partida_actual.getTauler().mouFitxa( getTipusFitxesJugadorTornActual(), fila, columna ) )
				{
					partida_actual.setDarreraFitxa( new Casella( fila, columna ) );

					long instant_actual = new Date().getTime();

					partida_actual.incrementaTempsDeJoc( getNumJugadorTornActual(),
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
	 * Consulta l'estat de la partida.
	 *
	 * @return L'estat de la partida (si guanya algun jugador o encara no està finalitzada).
	 */
	public EstatPartida consultaEstatPartida()
	{
		EstatPartida estat_partida = partida_actual.comprovaEstatPartida();
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
		       ( partida_actual.getTauler() ).intercanviaFitxa( partida_actual.getDarreraFitxa() ) &&
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

	// Métodes auxiliars depenents del torn actual
	// ----------------------------------------------------------------------------------------------------------------

	private int getNumJugadorTornActual()
	{
		return partida_actual.getTornsJugats() % 2;
	}

	/**
	 * Retorna el jugador A o B depenent del torn que s'hagin jugat
	 *
	 * @return jugador A o B depenent del torn que s'hagin jugat
	 */
	private UsuariHex obteJugadorTornActual()
	{
		if ( 0 == getNumJugadorTornActual() )
		{
			return partida_actual.getJugadorA();
		}
		else
		{
			return partida_actual.getJugadorB();
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
		TipusJugadors tipus_jugador_actual = obteJugadorTornActual().getTipusJugador();

		return ( TipusJugadors.JUGADOR == tipus_jugador_actual || TipusJugadors.CONVIDAT == tipus_jugador_actual );
	}

	private EstatCasella getTipusFitxesJugadorTornActual()
	{
		return partida_actual.getTipusFitxesJugador( getNumJugadorTornActual() );
	}

	// Métodes auxiliars per les vistes
	// ----------------------------------------------------------------------------------------------------------------

	public EstatCasella getEstatCasella( int fila, int columna )
	{
		return partida_actual.getTauler().getEstatCasella( fila, columna );
	}

	public int[] getElementsDeControlPartida()
	{
		int[] elements_de_control = new int[3];

		elements_de_control[0] = PartidaHex.getMaxNumPistes();
		elements_de_control[1] = partida_actual.getTauler().getMida();
		elements_de_control[2] = partida_actual.getTornsJugats();

		return elements_de_control;
	}

	public Object[][] getElementsDeControlJugadors()
	{
		Object[][] elements_de_control = new Object[5][2];

		elements_de_control[0][0] = partida_actual.getJugadorA().getTipusJugador();
		elements_de_control[1][0] = partida_actual.getJugadorA().getCombinacionsColors();
		elements_de_control[2][0] = partida_actual.getPistesUsades( 0 );
		elements_de_control[3][0] = partida_actual.getTempsDeJoc( 0 );
		elements_de_control[4][0] = partida_actual.getJugadorA().getNom();
		elements_de_control[0][1] = partida_actual.getJugadorB().getTipusJugador();
		elements_de_control[1][1] = partida_actual.getJugadorB().getCombinacionsColors();
		elements_de_control[2][1] = partida_actual.getPistesUsades( 1 );
		elements_de_control[3][1] = partida_actual.getTempsDeJoc( 1 );
		elements_de_control[4][1] = partida_actual.getJugadorB().getNom();

		return elements_de_control;
	}
}
