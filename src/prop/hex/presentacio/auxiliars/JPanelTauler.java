package prop.hex.presentacio.auxiliars;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.ModesInici;
import prop.hex.domini.models.enums.TipusJugadors;
import prop.hex.presentacio.PresentacioCtrl;
import prop.hex.presentacio.auxiliars.VistaDialeg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Classe d'un panell amb el tauler i totes les dades i accions relacionades.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */
public final class JPanelTauler extends JPanel
{

	/**
	 * Controlador de presentació.
	 */
	private static PresentacioCtrl presentacio_ctrl;

	/**
	 * Array que guarda diversos elements de control de la partida.
	 */
	private Object[] elements_de_control_partida;

	/**
	 * Array que guarda en cada fila elements de control de cada jugador.
	 */
	private Object[][] elements_de_control_jugadors;

	/**
	 * Úlima pista utilitzada.
	 */
	private Casella ultima_pista;

	/**
	 * Indica si la casella que tenim emmagatzemada a la variable ultima_pista ha de ser o no dibuixada al tauler.
	 */
	private boolean pista_valida;

	/**
	 * Indica si la partida està en curs o si estem definint una situació inicial.
	 */
	private boolean partida_en_curs;

	/**
	 * Indica si a la partida només participen jugadors de la IA.
	 */
	private boolean partida_ia;

	/**
	 * Indica si la partida està en un estat en el qual es processa un moviment de la IA.
	 */
	private boolean processant_moviment;

	/**
	 * Poligon que s'utilitza per dibuixar a pantalla cada una de les caselles.
	 */
	private Polygon hexagon;

	/**
	 * dx i dy són els increments horitzontals i verticals entre caselles.
	 */
	private int dx, dy;

	/**
	 * Radi de les caselles.
	 */
	private double radi;

	/**
	 * Posició inicial del tauler a la pantalla.
	 */
	private int iniciX, iniciY;

	/**
	 * Constructora, obté el tauler i els jugadors, construeix un poligon hexagonal i
	 * afegeix el listener del ratoli pel cas del click.
	 *
	 * @param partida_en_curs  Indica si la partida representada al panell està o no en curs.
	 * @param presentacio_ctrl Controlador de presentació.
	 */
	public JPanelTauler( boolean partida_en_curs, PresentacioCtrl presentacio_ctrl )
	{
		// Inicialitzem els atributs.
		this.presentacio_ctrl = presentacio_ctrl;
		elements_de_control_partida = presentacio_ctrl.getElementsDeControlPartida();
		elements_de_control_jugadors = presentacio_ctrl.getElementsDeControlJugadors();
		ultima_pista = new Casella( 0, 0 );
		pista_valida = false;
		this.partida_en_curs = partida_en_curs;
		partida_ia = ( ( TipusJugadors ) elements_de_control_jugadors[0][0] ) != TipusJugadors.JUGADOR &&
				( ( TipusJugadors ) elements_de_control_jugadors[0][0] ) != TipusJugadors.CONVIDAT &&
				( ( TipusJugadors ) elements_de_control_jugadors[0][1] ) != TipusJugadors.JUGADOR &&
				( ( TipusJugadors ) elements_de_control_jugadors[0][1] ) != TipusJugadors.CONVIDAT;
		processant_moviment = false;
		radi = 40.0;
		iniciX = 90;
		iniciY = 80;

		//Creem l'hexàgon que dibuixarem després.
		int x[] = new int[6];
		int y[] = new int[6];
		for ( int i = 0; i < 6; i++ )
		{
			x[i] = ( int ) ( radi * Math.sin( ( double ) i * Math.PI / 3.0 ) );
			y[i] = ( int ) ( radi * Math.cos( ( double ) i * Math.PI / 3.0 ) );
		}
		hexagon = new Polygon( x, y, 6 );
		dx = ( int ) ( 2 * radi * Math.sin( Math.PI / 3.0 ) );
		dy = ( int ) ( 1.5 * radi );

		// Afegim el listener del ratoli.
		addMouseListener( new MouseAdapter()
		{

			@Override
			public void mouseClicked( MouseEvent mouseEvent )
			{
				comprovaClick( mouseEvent.getX(), mouseEvent.getY() );
			}
		} );
	}

	/**
	 * Mostra un diàleg al visualitzador de partides quan la partida ha finalitzat.
	 */
	private void mostraDialegVictoria()
	{
		elements_de_control_partida = presentacio_ctrl.getElementsDeControlPartida();
		elements_de_control_jugadors = presentacio_ctrl.getElementsDeControlJugadors();

		int num_jugador;
		if ( presentacio_ctrl.consultaEstatPartida() == EstatPartida.GUANYA_JUGADOR_A )
		{
			num_jugador = 0;
		}
		else
		{
			num_jugador = 1;
		}
		// Inicialitzem el missatge a mostrar.
		String missatge = new String( "Guanya " + ( ( String ) elements_de_control_jugadors[3][num_jugador] ) +
				", amb un temps de " + ( String ) elements_de_control_jugadors[2][num_jugador] + ", " +
				"col·locant un total de " +
				( ( Integer ) elements_de_control_jugadors[4][num_jugador] ) + " fitxes," + " i utilitzant " +
				( ( Integer ) elements_de_control_jugadors[1][num_jugador] ) + " pistes." );
		if ( partida_en_curs )
		{
			presentacio_ctrl.mostraDialegVictoriaPartida( missatge );
		}
		else
		{
			presentacio_ctrl.mostraDialegVictoriaDefineixSituacio( missatge );
		}
	}

	/**
	 * Calcula sobre quina casella es corresponen les coordenades píxel i crida a clickHexagon amb aquesta
	 * informació. Listener de mouseClicked. Si s'ha fet click sobre una casella crida a clickHexagon.
	 *
	 * @param x Coordenades píxel horitzontals.
	 * @param y Coordenades píxel verticals.
	 */
	private void comprovaClick( int x, int y )
	{
		int rx = iniciX;
		int ry = iniciY;

		elements_de_control_partida = presentacio_ctrl.getElementsDeControlPartida();
		elements_de_control_jugadors = presentacio_ctrl.getElementsDeControlJugadors();

		for ( int i = 0; i < ( Integer ) elements_de_control_partida[1]; i++ )
		{
			rx += i * dx / 2;
			ry += i * dy;
			for ( int j = 0; j < ( Integer ) elements_de_control_partida[1]; j++ )
			{
				rx += j * dx;
				if ( hexagon.contains( x - rx, y - ry ) )
				{
					clickHexagon( i, j );
				}
				rx -= j * dx;
			}
			rx -= i * dx / 2;
			ry -= i * dy;
		}
	}

	/**
	 * Calcula si en un determinat moment de la partida es pot demanar una pista.
	 *
	 * @return Cert, si es pot demanar una pista. Fals, altrament.
	 */
	private boolean potDemanarPista()
	{
		elements_de_control_partida = presentacio_ctrl.getElementsDeControlPartida();
		elements_de_control_jugadors = presentacio_ctrl.getElementsDeControlJugadors();

		int i = ( Integer ) elements_de_control_partida[2] % 2;
		return ( !processant_moviment && partida_en_curs && !pista_valida && presentacio_ctrl.esTornHuma() &&
				         presentacio_ctrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA &&
				         ( ( Integer ) elements_de_control_partida[0] > ( Integer )
						         elements_de_control_jugadors[1][i] )
		);
	}

	/**
	 * Si la partida no està finalitzada i és torn de una IA, crida el controlador de presentació a executar moviment
	 * IA. Si és torn d'un humà, es calcula la casella resultat de demanar una pista.
	 */
	public void mouIAOMostraPista()
	{
		if ( presentacio_ctrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA && !presentacio_ctrl.esTornHuma
				() )
		{
			processant_moviment = true;
			paintImmediately( 0, 0, 800, 500 );
			presentacio_ctrl.executaMovimentIA();
			processant_moviment = false;
		}
		else if ( presentacio_ctrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA && presentacio_ctrl
				.esTornHuma() )
		{
			ultima_pista = presentacio_ctrl.obtePista();
			pista_valida = true;
		}

		paintImmediately( 0, 0, 800, 500 );

		if ( presentacio_ctrl.consultaEstatPartida() == EstatPartida.GUANYA_JUGADOR_A || presentacio_ctrl
				.consultaEstatPartida() == EstatPartida.GUANYA_JUGADOR_B )
		{
			mostraDialegVictoria();
		}

	}

	/**
	 * Canvia de jugador l'última fitxa col·locada al tauler.
	 */
	public void intercanviaFitxa()
	{
		presentacio_ctrl.intercanviaFitxa();
		pista_valida = false;
		paintImmediately( 0, 0, 800, 500 );
		if ( partida_en_curs && !presentacio_ctrl.esTornHuma() )
		{
			mouIAOMostraPista();
		}
	}

	/**
	 * Si la partida no està finalitzada i és torn d'un humà, crida a fer moviment a la casella i, j, i si el rival és
	 * una IA, inicia el moviment d'una fitxa per part de la IA.
	 *
	 * @param i fila de la casella.
	 * @param j columna de la casella.
	 */
	private void clickHexagon( int i, int j )
	{
		if ( presentacio_ctrl.consultaEstatPartida() == EstatPartida.NO_FINALITZADA && ( presentacio_ctrl.esTornHuma()
				                                                                         || !partida_en_curs ) )
		{
			try
			{
				//No ens cal comprovar si el moviment es fa o no (si retorna true o false).
				presentacio_ctrl.mouFitxa( i, j );
				pista_valida = false;
				paintImmediately( 0, 0, 800, 500 );
				if ( presentacio_ctrl.consultaEstatPartida() == EstatPartida.GUANYA_JUGADOR_A || presentacio_ctrl
						.consultaEstatPartida() == EstatPartida.GUANYA_JUGADOR_B )
				{
					mostraDialegVictoria();
				}
				else if ( partida_en_curs && !presentacio_ctrl.esTornHuma() )
				{
					mouIAOMostraPista();
				}
			}
			catch ( UnsupportedOperationException exepcio )
			{
				System.out.println( "Moviment no vàlid, partida finalitzada: " + exepcio.getMessage() );
			}
		}
	}

	/**
	 * Retorna la mida predeterminada del panell.
	 *
	 * @return Un objecte de tipus Dimension amb La mida predeterminada del panell.
	 */
	public Dimension getPreferredSize()
	{
		return new Dimension( 800, 500 );
	}

	/**
	 * Pinta el tauler al panell.
	 *
	 * @param g Paràmetre Graphics on es pinta.
	 */
	protected void paintComponent( Graphics g )
	{
		super.paintComponent( g );

		elements_de_control_partida = presentacio_ctrl.getElementsDeControlPartida();
		elements_de_control_jugadors = presentacio_ctrl.getElementsDeControlJugadors();

		//Dibuixem el tauler, pintant cada hexàgon del color que toca.
		g.translate( iniciX, iniciY );
		for ( int i = 0; i < ( Integer ) elements_de_control_partida[1]; i++ )
		{
			g.translate( i * dx / 2, i * dy );
			for ( int j = 0; j < ( Integer ) elements_de_control_partida[1]; j++ )
			{
				g.translate( j * dx, 0 );

				// Si el mode d'inici és estàndard i estem al primer torn, marquem la casella central com no vàlida.
				if ( ( Integer ) elements_de_control_partida[2] == 0 &&
						elements_de_control_partida[4] == ModesInici.ESTANDARD &&
						presentacio_ctrl.esCasellaCentral( i, j ) )
				{
					g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] )
							.getColorCasellesInhabilitades() );
				}
				else
				{
					// Pintem la casella de la pista, si s'ha demanat alguna.
					if ( pista_valida && ( ultima_pista.getFila() == i && ultima_pista.getColumna() == j ) )
					{
						g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorCasellesPista
								() );
						ultima_pista.setColumna( 0 );
						ultima_pista.setFila( 0 );
					}
					// Pintem la resta de caselles.
					else
					{
						g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorCasella(
																		presentacio_ctrl.getEstatCasella( i, j ) ) );
					}
				}
				g.fillPolygon( hexagon );

				g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorVoraCaselles() );
				g.drawPolygon( hexagon );

				g.translate( -j * dx, 0 );
			}
			g.translate( -i * dx / 2, -i * dy );
		}

		// Activem o desactivem els botons depenent de si es pot demanar una pista i/o es pot demanar un moviment
		// de la
		// IA.
		if ( partida_en_curs )
		{

			if ( potDemanarPista() )
			{
				presentacio_ctrl.estatBotoDemanaPista( true );
			}
			else
			{
				presentacio_ctrl.estatBotoDemanaPista( false );
			}

			if ( ( Integer ) elements_de_control_partida[2] == 1 && presentacio_ctrl.esTornHuma() )
			{
				presentacio_ctrl.estatBotoIntercanviaFitxa( true );
			}
			else
			{
				presentacio_ctrl.estatBotoIntercanviaFitxa( false );
			}
		}

		// Si és torn de la IA i s'està processant un moviment, mostrem per pantalla un missatge indicant-ho.
		if ( processant_moviment )
		{
			if ( ( Integer ) elements_de_control_partida[2] % 2 == 0 )
			{
				g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorCasella( EstatCasella
						.JUGADOR_A ) );
				g.drawString( "Processant moviment...", -50, 370 );
			}
			else
			{
				g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorCasella( EstatCasella
						.JUGADOR_B ) );
				g.drawString( "Processant moviment...", 580, 160 );
			}
		}

		// Mostrem el torn actual.
		g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorTextInformacio( EstatCasella
				.BUIDA ) );
		g.drawString( "Torn: " + elements_de_control_partida[2], -50, 210 );
		g.drawString( "Torn: " + elements_de_control_partida[2], 580, 0 );

		// Mostrem les dades del jugador A.
		g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorTextInformacio( EstatCasella
				.JUGADOR_A ) );
		if ( ( Integer ) elements_de_control_partida[2] % 2 == 0 )
		{
			g.drawString( "Té el torn", -50, 270 );
		}
		g.drawString( ( ( String ) elements_de_control_jugadors[3][0] ), -50, 290 );
		g.drawString( "D'esquerra a dreta", -50, 310 );

		g.drawString( "Temps: " + ( String ) elements_de_control_jugadors[2][0], -50, 330 );
		g.drawString( "Pistes disponibles: " + ( ( Integer ) elements_de_control_partida[0] -
				                                    ( ( Integer )  elements_de_control_jugadors[1][0] ) ), -50, 350 );

		// Mostrem les dades del jugador B.
		g.setColor( ( ( CombinacionsColors ) elements_de_control_partida[3] ).getColorTextInformacio(
																							EstatCasella.JUGADOR_B ) );
		if ( ( Integer ) elements_de_control_partida[2] % 2 == 1 )
		{
			g.drawString( "Té el torn", 580, 60 );
		}
		g.drawString( ( ( String ) elements_de_control_jugadors[3][1] ), 580, 80 );
		g.drawString( "De dalt a baix", 580, 100 );

		g.drawString( "Temps: " + ( String ) elements_de_control_jugadors[2][1], 580, 120 );
		g.drawString( "Pistes disponibles: " + ( ( Integer ) elements_de_control_partida[0] -
												    ( ( Integer ) elements_de_control_jugadors[1][1] ) ), 580, 140 );

		// Afegim les imatges amb les vores del tauler.
		if ( ( ( CombinacionsColors ) elements_de_control_partida[3] ) == CombinacionsColors.VERMELL_BLAU )
		{
			g.drawImage( ( new ImageIcon( "img/tauler_vb.png" ) ).getImage(), -iniciX, -iniciY, getWidth(),
					getHeight(), null );
		}
		else
		{
			g.drawImage( ( new ImageIcon( "img/tauler_nb.png" ) ).getImage(), -iniciX, -iniciY, getWidth(),
					getHeight(), null );
		}
	}
}
