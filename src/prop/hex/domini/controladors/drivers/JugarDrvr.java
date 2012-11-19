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

		UsuariHex usuari_A;
		UsuariHex usuari_B;
		try
		{
			usuari_A = UsuariCtrl.creaUsuari( "Maquina facil", "contrasenya", TipusJugadors.IA_FACIL, false );
		}
		catch ( Exception e )
		{
			usuari_A = UsuariCtrl.carregaUsuari( "Maquina facil", "contrasenya", TipusJugadors.IA_FACIL );
		}
		try
		{
			usuari_B = UsuariCtrl.creaUsuari( "Huma", "contrasenya", TipusJugadors.JUGADOR, false );
		}
		catch ( Exception e )
		{
			usuari_B = UsuariCtrl.carregaUsuari( "Huma", "contrasenya", TipusJugadors.JUGADOR );
		}

		try
		{
			PartidaCtrl.inicialitzaPartida( 7, usuari_A, usuari_B, "Partida de Prova" );
		}
		catch ( Exception e )
		{
			System.out.println( "Excepció al inicialitzar la partida: " + e.getMessage() );
		}

		instanciaFinestra();
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

		UsuariHex usuari_A;
		UsuariHex usuari_B;
		try
		{
			usuari_A = UsuariCtrl.creaUsuari( "Huma", "contrasenya", TipusJugadors.JUGADOR, false );
		}
		catch ( Exception e )
		{
			usuari_A = UsuariCtrl.carregaUsuari( "Huma", "contrasenya", TipusJugadors.JUGADOR );
		}
		try
		{
			usuari_B = UsuariCtrl.creaUsuari( "Maquina facil", "contrasenya", TipusJugadors.IA_FACIL, false );
		}
		catch ( Exception e )
		{
			usuari_B = UsuariCtrl.carregaUsuari( "Maquina facil", "contrasenya", TipusJugadors.IA_FACIL );
		}

		try
		{
			PartidaCtrl.inicialitzaPartida( 7, usuari_A, usuari_B, "Partida de Prova" );
		}
		catch ( Exception e )
		{
			System.out.println( "Excepció al inicialitzar la partida: " + e.getMessage() );
		}

		instanciaFinestra();
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

		UsuariHex usuari_A;
		UsuariHex usuari_B;
		try
		{
			usuari_A = UsuariCtrl.creaUsuari( "Huma 1", "contrasenya", TipusJugadors.JUGADOR, false );
		}
		catch ( Exception e )
		{
			usuari_A = UsuariCtrl.carregaUsuari( "Huma 1", "contrasenya", TipusJugadors.JUGADOR );
		}
		try
		{
			usuari_B = UsuariCtrl.creaUsuari( "Huma 2", "contrasenya", TipusJugadors.JUGADOR, false );
		}
		catch ( Exception e )
		{
			usuari_B = UsuariCtrl.carregaUsuari( "Huma 2", "contrasenya", TipusJugadors.JUGADOR );
		}

		try
		{
			PartidaCtrl.inicialitzaPartida( 7, usuari_A, usuari_B, "Partida de Prova" );
		}
		catch ( Exception e )
		{
			System.out.println( "Error al inicialitzar la partida." );
		}

		instanciaFinestra();
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

		UsuariHex usuari_A;
		UsuariHex usuari_B;
		try
		{
			usuari_A = UsuariCtrl.creaUsuari( "Maquina facil 2", "contrasenya", TipusJugadors.IA_FACIL, false );
		}
		catch ( Exception e )
		{
			usuari_A = UsuariCtrl.carregaUsuari( "Maquina facil 2", "contrasenya", TipusJugadors.IA_FACIL );
		}
		try
		{
			usuari_B = UsuariCtrl.creaUsuari( "Maquina facil 1", "contrasenya", TipusJugadors.IA_FACIL, false );
		}
		catch ( Exception e )
		{
			usuari_B = UsuariCtrl.carregaUsuari( "Maquina facil 1", "contrasenya", TipusJugadors.IA_FACIL );
		}

		try
		{
			PartidaCtrl.inicialitzaPartida( 7, usuari_A, usuari_B, "Partida de Prova" );
		}
		catch ( Exception e )
		{
			System.out.println( "Excepció al inicialitzar la partida: " + e.getMessage() );
		}

		instanciaFinestra();
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
