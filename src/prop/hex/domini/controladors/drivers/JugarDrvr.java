package prop.hex.domini.controladors.drivers;

import prop.hex.domini.controladors.PartidaCtrl;
import prop.hex.domini.controladors.UsuariCtrl;
import prop.hex.domini.models.UsuariHex;
import prop.hex.domini.models.enums.TipusJugadors;
import prop.hex.presentacio.VisualitzadorPartida;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Permet jugar una partida de prova escollint els tipus d'oponent.
 */
public class JugarDrvr
{

	static UsuariHex usuari_A;
	static UsuariHex usuari_B;
	static TipusJugadors[] tipus_jugadors = TipusJugadors.values();

	/**
	 * Demana un jugador d'intel·ligència artificial a l'usuari
	 *
	 * @param missatge Missatge que es vol mostrar per a l'usuari
	 * @return El tipus de jugador que escull l'usuari
	 */
	private static TipusJugadors demanaTipusJugadorIA( String missatge )
	{
		System.out.println( "Jugadors d'intel·ligència artificial:" );
		for ( int i = 1; i < tipus_jugadors.length; i++ )
		{
			System.out.println( i + ".- " + tipus_jugadors[i].getNomUsuari() );
		}

		return tipus_jugadors[UtilsDrvr.llegeixEnter( missatge )];
	}

	/**
	 * Obté un usuari amb les dades indicades pels paràmetres
	 *
	 * @param nom           Nom de l'usuari
	 * @param tipus_jugador Tipus de
	 * @return L'usuari
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NullPointerException
	 */
	private static UsuariHex agafaUsuari( String nom, TipusJugadors tipus_jugador )
			throws FileNotFoundException, IOException, ClassNotFoundException, NullPointerException
	{
		UsuariHex usuari;
		try
		{
			usuari = UsuariCtrl.creaUsuari( nom, "contrasenya", tipus_jugador, false );
		}
		catch ( Exception e )
		{
			usuari = UsuariCtrl.carregaUsuari( nom, "contrasenya", tipus_jugador );
		}

		return usuari;
	}

	/**
	 * Crea la instància de la partida obre la seva visualització.
	 */
	private static void creaIVisualitzaPartida()
	{
		try
		{
			PartidaCtrl.getInstancia().inicialitzaPartida( 7, usuari_A, usuari_B, "Partida de Prova" );
			instanciaFinestra();
		}
		catch ( Exception e )
		{
			System.out.println( "Excepció al inicialitzar la partida: " + e.getMessage() );
		}
	}

	/**
	 * Crea 2 usuaris, carrega una partida amb ells i instancia una finestra amb VisualitzadorPartida.
	 * Permet jugar una partida IA vs Huma.
	 *
	 * @throws FileNotFoundException  Si el fitxer no s'ha generat i no s'han pogut escriure les dades.
	 * @throws IOException            IOException Si ha succeït un error d'entrada/sortida inesperat.
	 * @throws ClassNotFoundException Si hi ha un problema de classes quan es carrega l'usuari.
	 * @throws NullPointerException   Es dona si el fitxer està buit.
	 */
	public static void IAVsHuma()
			throws FileNotFoundException, IOException, ClassNotFoundException, NullPointerException
	{
		usuari_A = agafaUsuari( "Maquina",
				demanaTipusJugadorIA( "Escriu el número del tipus d'intel·ligència contrincant" ) );
		usuari_B = agafaUsuari( "Huma", TipusJugadors.JUGADOR );

		creaIVisualitzaPartida();
	}

	/**
	 * Crea 2 usuaris, carrega una partida amb ells i instancia una finestra amb VisualitzadorPartida.
	 * Permet jugar una partida Huma vs IA.
	 *
	 * @throws FileNotFoundException  Si el fitxer no s'ha generat i no s'han pogut escriure les dades.
	 * @throws IOException            IOException Si ha succeït un error d'entrada/sortida inesperat.
	 * @throws ClassNotFoundException Si hi ha un problema de classes quan es carrega l'usuari.
	 * @throws NullPointerException   Es dona si el fitxer està buit.
	 */
	public static void HumaVsIA()
			throws FileNotFoundException, IOException, ClassNotFoundException, NullPointerException
	{

		usuari_A = agafaUsuari( "Huma", TipusJugadors.JUGADOR );
		usuari_B = agafaUsuari( "Maquina",
				demanaTipusJugadorIA( "Escriu el número del tipus d'intel·ligència contrincant" ) );

		creaIVisualitzaPartida();
	}

	/**
	 * Crea 2 usuaris, carrega una partida amb ells i instancia una finestra amb VisualitzadorPartida.
	 * Permet jugar una partida Huma vs Huma.
	 *
	 * @throws FileNotFoundException  Si el fitxer no s'ha generat i no s'han pogut escriure les dades.
	 * @throws IOException            IOException Si ha succeït un error d'entrada/sortida inesperat.
	 * @throws ClassNotFoundException Si hi ha un problema de classes quan es carrega l'usuari.
	 * @throws NullPointerException   Es dona si el fitxer està buit.
	 */
	public static void HumaVsHuma()
			throws FileNotFoundException, IOException, ClassNotFoundException, NullPointerException
	{
		usuari_A = agafaUsuari( "Huma 1", TipusJugadors.JUGADOR );
		usuari_B = agafaUsuari( "Huma 2", TipusJugadors.JUGADOR );

		creaIVisualitzaPartida();
	}

	/**
	 * Crea 2 usuaris, carrega una partida amb ells i instancia una finestra amb VisualitzadorPartida.
	 * Permet jugar una partida IA vs IA.
	 *
	 * @throws FileNotFoundException  Si el fitxer no s'ha generat i no s'han pogut escriure les dades.
	 * @throws IOException            IOException Si ha succeït un error d'entrada/sortida inesperat.
	 * @throws ClassNotFoundException Si hi ha un problema de classes quan es carrega l'usuari.
	 * @throws NullPointerException   Es dona si el fitxer està buit.
	 */
	public static void IAVsIA() throws FileNotFoundException, IOException, ClassNotFoundException, NullPointerException
	{
		usuari_A = agafaUsuari( "Maquina",
				demanaTipusJugadorIA( "Escriu el número del tipus d'intel·ligència principal" ) );
		usuari_B = agafaUsuari( "Maquina",
				demanaTipusJugadorIA( "Escriu el número del tipus d'intel·ligència contrincant" ) );

		creaIVisualitzaPartida();
	}

	/**
	 * Crea un JFrame i obre la finestra utilitzant com a contingut VisualitzadorPartida.
	 */
	private static void instanciaFinestra()
	{
		JFrame f = new JFrame( "Visor partida" );
		f.add( new VisualitzadorPartida() );
		f.pack();
		f.setVisible( true );
	}
}
