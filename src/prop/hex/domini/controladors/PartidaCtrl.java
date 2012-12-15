package prop.hex.domini.controladors;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IA.InteligenciaArtificialHexCtrl;
import prop.hex.domini.models.*;
import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.ModesInici;
import prop.hex.domini.models.enums.TipusJugadors;
import prop.hex.gestors.PartidaHexGstr;
import prop.hex.gestors.RanquingGstr;

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
 *
 * @author Isaac Sánchez Barrera (Grup 7.3, Hex)
 */
public final class PartidaCtrl
{

	/**
	 * Instància del controlador. Feta així perquè segueix el patró <em>singleton</em>.
	 *
	 * @see #getInstancia()
	 */
	private static PartidaCtrl instancia;

	/**
	 * Instància amb la partida que s'està executant actualment.
	 */
	private PartidaHex partida_actual;

	/**
	 * Conté els usuaris pre inicialitzats de la partida actual, únicament son útils abans d'inicialitzar una partida.
	 *
	 * @see #preInicialitzaUsuariPartida(int, TipusJugadors, String, String)
	 */
	private UsuariHex[] usuaris_preinicialitzats_partida;

	/**
	 * Instàncies de les intel·ligències artificials per als jugadors no humans.
	 *
	 * @see #inicialitzaIAJugadors()
	 */
	private InteligenciaArtificialHexCtrl[] ia_jugadors;

	/**
	 * Constructor per defecte. Declarat privat perquè és una classe singleton.
	 *
	 * @see #getInstancia()
	 */
	private PartidaCtrl()
	{
		netejaParametresPartidaActual();
	}

	/**
	 * Neteja els paràmetres de la partida actual.
	 * <p/>
	 * S'encarrega d'esborrar tots els paràmetres de control sobre la partida que s'està jugant.
	 */
	public final void netejaParametresPartidaActual()
	{
		partida_actual = null;
		usuaris_preinicialitzats_partida = new UsuariHex[2];
		ia_jugadors = new InteligenciaArtificialHexCtrl[2];
	}

	/**
	 * Consultora de la instància actual del controlador de partida.
	 * Si encara no s'ha inicialitzat l'objecte, crida el constructor. Si ja s'ha instanciat prèviament,
	 * simplement retorna l'instancia ja creada.
	 *
	 * @return L'objecte singleton amb el el controlador de partida.
	 * @see #instancia
	 */
	public static synchronized PartidaCtrl getInstancia()
	{
		if ( instancia == null )
		{
			instancia = new PartidaCtrl();
		}

		return instancia;
	}

	/**
	 * Comprova la consistència dels fitxers necessaris per al programa. Entre ells,
	 * els fitxers amb les dades dels jugadors d'intel·ligència artificial i el del rànquing.
	 * <p/>
	 * L'estat real del joc pot seguir sent inconsistent, ja que si s'esborra un jugador manualment del directori de
	 * dades no desapareix de les estadístiques. De la mateixa manera, si s'esborra el fitxer amb el rànquing aquest
	 * no serà del tot correcte fins que tots els jugadors que havien jugat tornin a jugar.
	 *
	 * @throws IOException            Si hi ha algun problema d'entrada/sortida quan intentem llegir o crear el
	 *                                rànquing o les intel·ligències artificials.
	 * @throws ClassNotFoundException Si no es troben les classes UsuariHex o Ranquing.
	 * @see RanquingGstr
	 * @see UsuariCtrl#creaUsuari(String, String, TipusJugadors)
	 */
	public static void comprovaConsistenciaFitxersIDades() throws IOException, ClassNotFoundException
	{
		// Comprovo la consistència del rànquing (si no existeix a disc el creo, si ja existeix el carrego a memòria)
		// Si no existeix el fitxer del rànquing, el creo
		if ( RanquingGstr.getInstancia().existeixElement() )
		{
			RanquingGstr.getInstancia().carregaElement();
		}

		// Comprovo consistència de jugadors màquina (han de figurar tots els usuaris de tipus màquina a disc)
		for ( TipusJugadors tipus_jugador_maquina : TipusJugadors.obteLlistatMaquines() )
		{
			if ( !UsuariCtrl.getInstancia().existeixUsuari( tipus_jugador_maquina.getNomUsuari() ) )
			{
				UsuariCtrl.getInstancia().creaUsuari( tipus_jugador_maquina.getNomUsuari(), "", tipus_jugador_maquina );
			}
		}

		RanquingGstr.getInstancia().guardaElement();
	}

	/**
	 * Preinicialitza els usuaris que disputaran la partida.
	 * <p/>
	 * Carrega l'usuari corresponent al tipus de jugador seleccionat comprovant les credencials en cas que toqui,
	 * carregant de disc la versió corresponent de la IA o instanciant un usuari convidat temporal si és el cas.
	 * Cal cridar a aquesta funció abans d'inicialitzar la partida mitjançant inicialitzaPartida.
	 *
	 * @param num_jugador        Número de jugador que es vol preinicialitzar (0 si és el jugador A, 1 si és el B)
	 * @param tipus_jugador      Tipus de jugador que es vol preinicialitzar
	 * @param nom_usuari         Nom de l'usuari d'aquest jugador
	 * @param contrasenya_usuari Contrasenya de l'usuari associat a aquest jugador
	 * @throws IllegalArgumentException Si la contrasenya de l'usuari és incorrecta.
	 * @throws FileNotFoundException    Si no es troba el fitxer amb les dades de l'usuari.
	 * @throws IOException              Si hi ha algun problema d'entrada/sortida quan intentem carregar l'usuari de disc.
	 * @throws ClassNotFoundException   Si no es troba la classe UsuariHex quan s'intenta carregar l'usuari de disc.
	 * @throws NullPointerException     Si el fitxer de l'usuari és buit.
	 * @see UsuariHex#UsuariHex(String, String, TipusJugadors)
	 * @see UsuariCtrl#carregaUsuari(String, String, TipusJugadors)
	 */
	public void preInicialitzaUsuariPartida( int num_jugador, TipusJugadors tipus_jugador, String nom_usuari,
	                                         String contrasenya_usuari )
			throws IllegalArgumentException, FileNotFoundException, IOException, ClassNotFoundException,
			       NullPointerException
	{
		UsuariCtrl usuari_ctrl = UsuariCtrl.getInstancia();

		// Si l'usuari a establir és de tipus jugador, comprovem si es tracta de l'usuari principal del joc,
		// en aquest cas, simplement l'establim. Si és el jugador secundari, comprovem les seves credencials.
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
			// Si l'usuari a establir es de tipus convidat, creem un UsuariHex temporal amb el nom donat. Si no té
			// nom, en posem un de prefixat.
			if ( nom_usuari == null || nom_usuari.equals( "" ) )
			{
				nom_usuari = String.format( "Convidat %d", num_jugador + 1 );
			}
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
	 * @see #ia_jugadors
	 * @see prop.hex.domini.controladors.IA.InteligenciaArtificialHexCtrl#obteMoviment(EstatCasella)
	 * @see TipusJugadors
	 */
	private void inicialitzaIAJugadors() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		ia_jugadors[0] = ( InteligenciaArtificialHexCtrl ) Class.forName( "prop.hex.domini.controladors.IA." +
		                                                                  ( partida_actual.getJugadorA() )
				                                                                  .getTipusJugador()
				                                                                  .getClasseCorresponent() )
				.newInstance();
		ia_jugadors[0].setPartida( partida_actual );

		ia_jugadors[1] = ( InteligenciaArtificialHexCtrl ) Class.forName( "prop.hex.domini.controladors.IA." +
		                                                                  ( partida_actual.getJugadorB() )
				                                                                  .getTipusJugador()
				                                                                  .getClasseCorresponent() )
				.newInstance();
		ia_jugadors[1].setPartida( partida_actual );
	}

	/**
	 * Inicialitza una partida nova. Li assigna les preferències de l'usuari principal actual.
	 *
	 * @param mida_tauler      Mida del tauler de la partida.
	 * @param nom_partida      Nom de la partida.
	 * @param situacio_inicial Indica si la partida ve definida amb una situació inicial
	 * @throws NullPointerException     Si no s'han preinicialitzat els usuaris de la partida prèviament.
	 * @throws IllegalArgumentException Si no s'ha especificat un nom de partida o si ja existeix una partida amb
	 *                                  aquest identificador
	 * @throws ClassNotFoundException   Si no es pot carregar la classe de les intel·ligències artificials.
	 * @throws InstantiationException   Si hi ha problemes amb la instanciació de les intel·ligències artificials.
	 * @throws IllegalAccessException   Si s'intenta accedir a un lloc no permès quan es carreguen les
	 *                                  intel·ligències artificials.
	 * @see #usuaris_preinicialitzats_partida
	 * @see PartidaHex#PartidaHex(UsuariHex, UsuariHex, TaulerHex, String, ModesInici, CombinacionsColors, boolean)
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

			if ( PartidaHexGstr.getInstancia().existeixElement( partida_actual.getIdentificadorUnic() ) )
			{
				throw new IllegalArgumentException(
						"Ja existeix una partida amb aquest nom i per aquests usuaris a la mateixa data." );
			}

			inicialitzaIAJugadors();
		}
	}

	/**
	 * Carrega de memòria secundària la partida identificada per identificador_partida i la estableix com la partida
	 * en joc.
	 *
	 * @param id_partida              Identificador de la partida que es vol carregar
	 * @param contrasenya_contrincant Contrasenya de l'usuari contrincant
	 * @throws IOException            Si no es pot carregar la partida
	 * @throws ClassNotFoundException Si no existeix la classe PartidaHex o la de la intel·ligència artificial.
	 * @throws IllegalAccessError     Si hi ha un problema d'accés al fitxer amb la partida
	 * @throws InstantiationError     Si hi ha un problema de classes a la instanciació de la partida que es vol
	 *                                carregar
	 * @see #inicialitzaIAJugadors()
	 * @see PartidaHexGstr
	 * @see UsuariCtrl#getUsuariPrincipal()
	 */
	public void carregaPartida( String id_partida, String contrasenya_contrincant )
			throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException
	{
		partida_actual = PartidaHexGstr.getInstancia().carregaElement( id_partida );
		String nom_usuari_contrincant;
		TipusJugadors tipus_jugador_contrincant;
		if ( !partida_actual.getJugadorA().equals( UsuariCtrl.getInstancia().getUsuariPrincipal() ) )
		{
			nom_usuari_contrincant = partida_actual.getJugadorA().getNom();
			tipus_jugador_contrincant = partida_actual.getJugadorA().getTipusJugador();

			usuaris_preinicialitzats_partida[0] = UsuariCtrl.getInstancia()
					.carregaUsuari( nom_usuari_contrincant, contrasenya_contrincant, tipus_jugador_contrincant );
			usuaris_preinicialitzats_partida[1] = UsuariCtrl.getInstancia().getUsuariPrincipal();
		}
		else
		{
			nom_usuari_contrincant = partida_actual.getJugadorB().getNom();
			tipus_jugador_contrincant = partida_actual.getJugadorB().getTipusJugador();

			usuaris_preinicialitzats_partida[0] = UsuariCtrl.getInstancia().getUsuariPrincipal();
			usuaris_preinicialitzats_partida[1] = UsuariCtrl.getInstancia()
					.carregaUsuari( nom_usuari_contrincant, contrasenya_contrincant, tipus_jugador_contrincant );
		}

		partida_actual.setJugadorA( usuaris_preinicialitzats_partida[0] );
		partida_actual.setJugadorB( usuaris_preinicialitzats_partida[1] );

		inicialitzaIAJugadors();

		PartidaHexGstr.getInstancia().eliminaElement( id_partida );

		partida_actual.setInstantDarrerMoviment( new Date().getTime() );
	}

	/**
	 * Elimina de memòria secundària la partida identificada per id_partida.
	 *
	 * @param id_partida Identificador de la partida que es vol eliminar
	 */
	public void eliminaPartida( String id_partida )
	{
		PartidaHexGstr.getInstancia().eliminaElement( id_partida );
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
	 * @see PartidaHexGstr#carregaElement(String)
	 */
	public String usuariSenseAutenticarAPartida( String id_partida )
			throws IOException, ClassNotFoundException, IllegalAccessError, InstantiationError
	{
		String nom_usuari = null;

		PartidaHex partida_futura = PartidaHexGstr.getInstancia().carregaElement( id_partida );
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
	 *         <ol start="0">
	 *         <li>Identificador únic de la partida</li>
	 *         <li>Nom de la partida</li>
	 *         <li>Nom d'usuari de l'oponent (contra qui juga l'usuari principal de la sessió, no de la partida)</li>
	 *         <li>String formatat amb la data i hora d'inici de la partida</li>
	 *         </ol>
	 * @see PartidaHexGstr#llistaPartidesUsuari(String)
	 */
	public String[][] llistaPartidesUsuari()
	{
		String id_usuari = UsuariCtrl.getInstancia().getUsuariPrincipal().getIdentificadorUnic();
		Set<String> id_partides = PartidaHexGstr.getInstancia().llistaPartidesUsuari( id_usuari );
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
	 * @see PartidaHexGstr#eliminaElement(String)
	 */
	public boolean eliminaPartidesUsuari( String id_usuari )
	{
		Set<String> id_partides = PartidaHexGstr.getInstancia().llistaPartidesUsuari( id_usuari );
		boolean resultat = true;
		for ( String id_partida : id_partides )
		{
			resultat = resultat && PartidaHexGstr.getInstancia().eliminaElement( id_partida );
		}

		return resultat;
	}

	/**
	 * Guarda la partida actual.
	 * <p/>
	 * Cal tenir en compte que només es poden guardar les partides amb algun usuari registrat i que, a més,
	 * no hagin finalitzat.
	 *
	 * @return Cert si s'ha guardat correctament. Fals altrament.
	 * @throws FileNotFoundException         Si no existeix el fitxer que de la partida que es desa.
	 * @throws IOException                   Si hi ha un error d'entrada/sortida
	 * @throws UnsupportedOperationException Si es vol guardar una partida ja finalitzada o cap dels usuaris és
	 *                                       registrat.
	 * @see PartidaHexGstr#guardaElement(prop.hex.domini.models.PartidaHex)
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
			return PartidaHexGstr.getInstancia().guardaElement( partida_actual );
		}
	}

	/**
	 * Per una partida que té situació inicial, estableix que aquesta ja està acabada de definir.
	 */
	public void acabaDeDefinirSituacioInicial()
	{
		partida_actual.setSituacioInicialAcabadaDeDefinir();
	}

	/**
	 * Indica si la en la partida actual s'ha definit la situació inicial.
	 */
	public boolean esPartidaAmbSituacioInicial()
	{
		return partida_actual.teSituacioInicial();
	}

	/**
	 * Finalitza la partida actual.
	 * <p/>
	 * Actualitza les estadístiques dels usuaris si la partida ja ha finalitzat i no ha començat
	 * definint una situació inicial.
	 *
	 * @see #consultaEstatPartida()
	 * @see PartidaHex
	 * @see PartidaHexGstr#eliminaElement(String)
	 * @see Ranquing#actualitzaRanquingUsuari(prop.hex.domini.models.UsuariHex)
	 * @see RanquingGstr#guardaElement()
	 */
	public void finalitzaPartida() throws IOException
	{
		EstatPartida estat_actual = consultaEstatPartida();

		// Si la partida ha finalitzat i no ha començat definint una situació inicial,
		// actualitzem les estadístiques d'usuari
		if ( estat_actual != EstatPartida.NO_FINALITZADA )
		{
			// Si la partida està guardada, l'eliminem
			PartidaHexGstr.getInstancia().eliminaElement( partida_actual.getIdentificadorUnic() );

			// Si no ha començat definint situació inicial, actualitzem estadístiques d'usuari
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

					Ranquing.getInstancia().actualitzaRanquingUsuari( usuari_a );
				}

				if ( usuari_b.getTipusJugador() != TipusJugadors.CONVIDAT )
				{
					UsuariCtrl.getInstancia()
							.actualitzaEstadistiques( usuari_b, estat_actual == EstatPartida.GUANYA_JUGADOR_B,
									usuari_a.getTipusJugador(), partida_actual.getTempsDeJoc( 1 ),
									partida_actual.getTauler().getNumFitxesB() );

					Ranquing.getInstancia().actualitzaRanquingUsuari( usuari_b );
				}
			}
		}

		// Guardem el rànquing al disc
		try
		{
			RanquingGstr.getInstancia().guardaElement();
		}
		catch ( IOException excepcio )
		{
			throw new IOException( "No s'ha pogut desar el ranquing" );
		}
	}

	/**
	 * Tanca la partida actual i crea una de revenja si és el cas. Si es fa una revenja, intercanvia l'usuari que ha
	 * jugat al primer torn pel segon.
	 *
	 * @param revenja Indica si es vol crear una partida de revenja.
	 * @throws ClassNotFoundException Si no es pot carregar la classe de les intel·ligències artificials.
	 * @throws InstantiationException Si hi ha problemes amb la instanciació de les intel·ligències artificials.
	 * @throws IllegalAccessException Si s'intenta accedir a un lloc no permès quan es carreguen les intel·ligències
	 *                                artificials.
	 */
	public void tancaPartida( boolean revenja )
			throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		if ( revenja )
		{
			UsuariHex auxiliar = usuaris_preinicialitzats_partida[0];
			usuaris_preinicialitzats_partida[0] = usuaris_preinicialitzats_partida[1];
			usuaris_preinicialitzats_partida[1] = auxiliar;

			inicialitzaPartida( partida_actual.getTauler().getMida(), partida_actual.getNom(), false );
		}
		else
		{
			netejaParametresPartidaActual();
		}
	}

	/**
	 * Consulta una pista per al jugador que la demana.
	 *
	 * @return La casella on mouria la intel·ligència artificial configurada per a les pistes.
	 * @see #getMovimentIATornActual()
	 * @see PartidaHex#incrementaPistesUsades(int, int)
	 */
	public Casella obtePista()
	{
		partida_actual.incrementaPistesUsades( getNumJugadorTornActual(), 1 );
		return getMovimentIATornActual();
	}

	/**
	 * Executa un moviment d'una intel·ligència artificial (cal que sigui el seu torn)
	 *
	 * @return La casella on mou la intel·ligència artificial.
	 * @see #getMovimentIATornActual()
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
	 * <p/>
	 * Actualitza les estadístiques i propietats corresponents a la partida.
	 *
	 * @param fila    Fila de la casella
	 * @param columna Columna de la casella
	 * @return Cert si s'ha mogut la fitxa. Fals altrament.
	 * @throws UnsupportedOperationException Si la partida ja ha finalitzat.
	 * @see Casella
	 * @see TaulerHex#esMovimentValid(EstatCasella, Casella)
	 * @see TaulerHex#mouFitxa(EstatCasella, Casella)
	 * @see PartidaHex#setDarreraFitxa(Casella)
	 * @see PartidaHex#incrementaTempsDeJoc(int, long)
	 * @see PartidaHex#incrementaTornsJugats(int)
	 */
	public boolean mouFitxa( int fila, int columna ) throws UnsupportedOperationException
	{
		if ( partida_actual.estaFinalitzada() )
		{
			throw new UnsupportedOperationException( "La partida ja ha finalitzat" );
		}
		else
		{
			Casella destinacio = new Casella( fila, columna );
			if ( !partida_actual.getTauler().esMovimentValid( getTipusFitxesJugadorTornActual(), destinacio ) )
			{
				return false;
			}
			else
			{
				if ( partida_actual.getTauler().mouFitxa( getTipusFitxesJugadorTornActual(), destinacio ) )
				{
					partida_actual.setDarreraFitxa( destinacio );

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
	 * Consulta si una casella és central.
	 *
	 * @param fila    Fila de la casella dins el tauler.
	 * @param columna Columna de la casella dins el tauler.
	 * @return Cert si la casella (fila, columna) és central. Fals altrament.
	 * @throws IllegalArgumentException Si (fila, columna) no és una casella vàlida.
	 * @see TaulerHex#esCasellaCentral(int, int)
	 */
	public boolean esCasellaCentral( int fila, int columna ) throws IndexOutOfBoundsException
	{
		return partida_actual.getTauler().esCasellaCentral( fila, columna );
	}

	/**
	 * Consulta l'estat de la partida.
	 *
	 * @return L'estat de la partida (si guanya algun jugador o encara no està finalitzada).
	 * @see PartidaHex#comprovaEstatPartida(int, int)
	 * @see PartidaHex#setFinalitzada(boolean)
	 */
	public EstatPartida consultaEstatPartida()
	{
		return partida_actual.comprovaEstatPartida();
	}

	/**
	 * Intercanvia la darrera fitxa d'un jugador per l'altre. Útil si s'aplica la regla del pastís.
	 *
	 * @return Cert si s'ha canviat la fitxa. Fals altrament.
	 * @see TaulerHex#intercanviaFitxa(Casella)
	 * @see PartidaHex#incrementaTornsJugats(int)
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
	 * @see #esReglaPastis()
	 */
	public boolean esPotIntercanviarDarreraFitxa()
	{
		return ( partida_actual.getTornsJugats() == 1 && esReglaPastis() );
	}

	/**
	 * Consulta si el joc actual té la regla del pastís.
	 *
	 * @return Cert si comença amb la regla del pastís. Fals altrament.
	 * @see ModesInici#PASTIS
	 */
	public boolean esReglaPastis()
	{
		return partida_actual.getModeInici() == ModesInici.PASTIS;
	}

	/**
	 * Obté la partida actual en joc.
	 *
	 * @return PartidaHex amb la partida actual. Si no s'està jugant cap partida, o aquesta ha finalitzat,
	 *         retorna null.
	 */
	public PartidaHex getPartidaActual()
	{
		return partida_actual;
	}

	// Métodes auxiliars depenents del torn actual
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * Consulta el número del jugador del torn actual. A juga els torns parells (comencen per 0) i B els senars.
	 *
	 * @return Retorna el número del jugador del torn actual (0 si és A, 1 si és B).
	 * @see PartidaHex#getTornsJugats()
	 */
	private int getNumJugadorTornActual()
	{
		return partida_actual.getTornsJugats() % 2;
	}

	/**
	 * Retorna el jugador A o B depenent dels torns que s'hagin jugat
	 *
	 * @return jugador A o B depenent dels torns que s'hagin jugat
	 * @see #getNumJugadorTornActual()
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
	 * Consulta quin moviment faria la intel·ligència artificial del torn actual. Si és un torn humà,
	 * fa servir la intel·ligència artificial de les pistes.
	 *
	 * @return El moviment que faria la intel·ligència artificial corresponent.
	 * @see prop.hex.domini.controladors.IA.InteligenciaArtificialHexCtrl#obteMoviment(EstatCasella)
	 */
	private Casella getMovimentIATornActual()
	{
		return ia_jugadors[getNumJugadorTornActual()].obteMoviment( getTipusFitxesJugadorTornActual() );
	}

	/**
	 * Consulta si és un torn humà. Necessari perquè el controlador de presentació sàpiga si demanar un moviment
	 * d'intel·ligència artificial al controlador o esperar que l'usuari esculli una casella.
	 *
	 * @return Cert si el jugador és humà. Fals altrament.
	 * @see TipusJugadors
	 */
	public boolean esTornHuma()
	{
		TipusJugadors tipus_jugador_actual = obteJugadorTornActual().getTipusJugador();

		return ( TipusJugadors.JUGADOR == tipus_jugador_actual || TipusJugadors.CONVIDAT == tipus_jugador_actual );
	}

	/**
	 * Consulta amb quines fitxes juga el jugador del torn actual.
	 *
	 * @return El tipus de fitxa que fa servir el jugador del torn actual.
	 * @see #getNumJugadorTornActual()
	 * @see PartidaHex#getTipusFitxesJugador(int)
	 */
	private EstatCasella getTipusFitxesJugadorTornActual()
	{
		return partida_actual.getTipusFitxesJugador( getNumJugadorTornActual() );
	}

	// Mètodes auxiliars per les vistes
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * Consulta quin és l'estat de la casella en el moment actual.
	 *
	 * @param fila    Fila de la casella dins el tauler
	 * @param columna Columna de la casella dins el tauler
	 * @return L'estat de la casella (fila, columna).
	 * @throws IndexOutOfBoundsException Si (fila, columna) no és una casella vàlida.
	 * @see TaulerHex#getEstatCasella(int, int)
	 */
	public EstatCasella getEstatCasella( int fila, int columna ) throws IndexOutOfBoundsException
	{
		return partida_actual.getTauler().getEstatCasella( fila, columna );
	}

	/**
	 * Consulta els elements de control de la partida per a la vista.
	 *
	 * @return Els elements de control de la partida necessaris per a la capa de presentació. Aquests són
	 *         <ol start="0">
	 *         <li>Nombre màxim de pistes</li>
	 *         <li>Mida del tauler</li>
	 *         <li>Total de torns jugats</li>
	 *         <li>Combinació de colors</li>
	 *         <li>Mode d'inici</li>
	 *         </ol>
	 * @see CombinacionsColors
	 * @see ModesInici
	 */
	public Object[] getElementsDeControlPartida()
	{
		Object[] elements_de_control = new Object[5];

		elements_de_control[0] = PartidaHex.getMaxNumPistes();
		elements_de_control[1] = partida_actual.getTauler().getMida();
		elements_de_control[2] = partida_actual.getTornsJugats();
		elements_de_control[3] = partida_actual.getCombinacioColors();
		elements_de_control[4] = partida_actual.getModeInici();

		return elements_de_control;
	}

	/**
	 * Consulta els element de control dels jugadors de la partida per a la vista.
	 *
	 * @return Els elements de control dels jugadors de la partida necessaris per a la capa de presentació. Aquests són
	 *         <ol start="0">
	 *         <li>Tipus de jugador</li>
	 *         <li>Pistes usades pel jugador</li>
	 *         <li>Temps de joc (en segons) del jugador</li>
	 *         <li>Nom del jugador</li>
	 *         <li>Número de fitxes del jugador</li>
	 *         </ol>
	 *         <p/>
	 *         I, dins de cada element, els índexs 0 i 1 contenen dades del jugadors A i B respectivament.
	 * @see TipusJugadors
	 */
	public Object[][] getElementsDeControlJugadors()
	{
		Object[][] elements_de_control = new Object[5][2];

		elements_de_control[0][0] = partida_actual.getJugadorA().getTipusJugador();
		elements_de_control[1][0] = partida_actual.getPistesUsades( 0 );
		elements_de_control[2][0] = String.valueOf( partida_actual.getTempsDeJoc( 0 ) / 1000L ) + "." +
		                            ( partida_actual.getTempsDeJoc( 0 ) % 1000L ) / 100 + " s";
		elements_de_control[3][0] = partida_actual.getJugadorA().getNom();
		elements_de_control[4][0] = partida_actual.getTauler().getNumFitxesA();
		elements_de_control[0][1] = partida_actual.getJugadorB().getTipusJugador();
		elements_de_control[1][1] = partida_actual.getPistesUsades( 1 );
		elements_de_control[2][1] = String.valueOf( partida_actual.getTempsDeJoc( 1 ) / 1000L ) + "." +
		                            ( partida_actual.getTempsDeJoc( 1 ) % 1000L ) / 100 + " s";
		elements_de_control[3][1] = partida_actual.getJugadorB().getNom();
		elements_de_control[4][1] = partida_actual.getTauler().getNumFitxesB();

		return elements_de_control;
	}
}
