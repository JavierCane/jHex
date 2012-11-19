package prop.hex.domini.controladors.drivers;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.cluster.domini.models.estats.EstatPartida;
import prop.hex.domini.controladors.IAHexFacilCtrl;
import prop.hex.domini.controladors.PartidaCtrl;
import prop.hex.domini.controladors.UsuariCtrl;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;
import prop.hex.domini.models.TaulerHex;
import prop.hex.domini.models.UsuariHex;
import prop.hex.domini.models.enums.TipusJugadors;
import prop.hex.presentacio.VisualitzadorPartida;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 18/11/12
 * Time: 20:13
 * To change this template use File | Settings | File Templates.
 */
public class JugarDrvr
{

	public static void IAVsHuma() throws FileNotFoundException, IOException, ClassNotFoundException,
	                                     NullPointerException
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

	public static void HumaVsIA() throws FileNotFoundException, IOException, ClassNotFoundException,
	                                     NullPointerException
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

	public static void HumaVsHuma() throws FileNotFoundException, IOException, ClassNotFoundException,
	                                       NullPointerException
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

	public static void IAVsIA() throws FileNotFoundException, IOException, ClassNotFoundException,
	                                   NullPointerException
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


	private static void instanciaFinestra()
	{
		JFrame f = new JFrame( "Visor partida" );
		//f.setDefaultCloseOperation();
		f.add( new VisualitzadorPartida() );
		f.pack();
		f.setVisible( true );
	}

}
