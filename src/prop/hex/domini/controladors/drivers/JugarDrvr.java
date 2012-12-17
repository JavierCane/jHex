package prop.hex.domini.controladors.drivers;

import prop.hex.domini.controladors.PartidaCtrl;
import prop.hex.domini.controladors.UsuariCtrl;
import prop.hex.domini.models.UsuariHex;
import prop.hex.domini.models.enums.TipusJugadors;
import prop.hex.presentacio.drivers.VisualitzadorPartida;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

/**
 * Driver que permet jugar una partida de prova escollint els dos jugadors.
 *
 * @author Marc Junyent Martín (Grup 7.3, Hex)
 */
public final class JugarDrvr
{

	/**
	 * Guarda el primer jugador
	 */
	private static UsuariHex usuari_a;
	/**
	 * Guarda el segón jugador
	 */
	private static UsuariHex usuari_b;
	/**
	 * Vector que guarda els tipus de jugadors
	 */
	private static Vector<TipusJugadors> tipus_jugadors = TipusJugadors.obteLlistatMaquines();

	/**
	 * Demana un jugador d'intel·ligència artificial a l'usuari
	 *
	 * @param missatge Missatge que es vol mostrar per a l'usuari
	 * @return El tipus de jugador que escull l'usuari
	 */
	private static TipusJugadors demanaTipusJugadorIA( String missatge )
	{
		System.out.println( "Jugadors d'intel·ligència artificial:" );

		int i = 1;
		for ( TipusJugadors tipus_jugador : tipus_jugadors )
		{
			System.out.println( i + ".- " + tipus_jugador.getNomUsuari() );
			i++;
		}

		return tipus_jugadors.get( UtilsDrvr.llegeixEnter( missatge ) - 1 );
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
		try
		{
			UsuariCtrl.getInstancia().creaUsuari( nom, "contrasenya", tipus_jugador );
			return UsuariCtrl.getInstancia().carregaUsuari( nom, "contrasenya", tipus_jugador );
		}
		catch ( Exception e )
		{
			return UsuariCtrl.getInstancia().carregaUsuari( nom, "contrasenya", tipus_jugador );
		}
	}

	/**
	 * Crea la instància de la partida obre la seva visualització.
	 */
	private static void creaIVisualitzaPartida()
	{
		try
		{
			UsuariCtrl.getInstancia().entraConvidat();
			PartidaCtrl.getInstancia().preInicialitzaUsuariPartida( 0, usuari_a.getTipusJugador(), usuari_a.getNom(),
					usuari_a.getContrasenya() );
			PartidaCtrl.getInstancia().preInicialitzaUsuariPartida( 1, usuari_b.getTipusJugador(), usuari_b.getNom(),
					usuari_b.getContrasenya() );
			PartidaCtrl.getInstancia().inicialitzaPartida( 7, "Partida de Prova", false );
			instanciaFinestra();
		}
		catch ( Exception e )
		{
			System.out.println( "Excepció al inicialitzar la partida: " + e.getMessage() );
			e.printStackTrace();
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
		TipusJugadors tipus_IA = demanaTipusJugadorIA( "Escriu el número del tipus d'intel·ligència contrincant" );
		usuari_a = agafaUsuari( tipus_IA.getNomUsuari(), tipus_IA );
		usuari_b = agafaUsuari( "Huma", TipusJugadors.JUGADOR );

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
		TipusJugadors tipus_IA = demanaTipusJugadorIA( "Escriu el número del tipus d'intel·ligència contrincant" );
		usuari_a = agafaUsuari( "Huma", TipusJugadors.JUGADOR );
		usuari_b = agafaUsuari( tipus_IA.getNomUsuari(), tipus_IA );

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
		usuari_a = agafaUsuari( "Huma 1", TipusJugadors.JUGADOR );
		usuari_b = agafaUsuari( "Huma 2", TipusJugadors.JUGADOR );

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
		TipusJugadors tipus_IA_A = demanaTipusJugadorIA( "Escriu el número del tipus d'intel·ligència principal" );
		TipusJugadors tipus_IA_B = demanaTipusJugadorIA( "Escriu el número del tipus d'intel·ligència contrincant" );
		usuari_a = agafaUsuari( tipus_IA_A.getNomUsuari(), tipus_IA_A );
		usuari_b = agafaUsuari( tipus_IA_B.getNomUsuari(), tipus_IA_B );

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
