package prop.hex.domini.controladors.drivers;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.controladors.IA.GrupCaselles;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 07/11/12
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
public class IAGrupCasellesDrvr
{
	public static void main( String[] args ) {
		System.out.println("Provant GrupCaselles");
		TaulerHex tauler = new TaulerHex(7);
		tauler.mouFitxa(EstatCasella.JUGADOR_A, 4, 3);
		//tauler.mouFitxa(EstatCasella.JUGADOR_A, 3, 3);

		GrupCaselles jugadorA = new GrupCaselles(tauler);
		jugadorA.estendre(new Casella(4,3));
		ArrayList<Casella> grup = jugadorA.getGrup();
		System.out.println("Grup A creat, hi ha " + grup.size() + " caselles: ");

		for(int i=0; i<grup.size(); i++) {
			System.out.println("Casella: " + grup.get(i).getFila() + "," + grup.get(i).getColumna() );
		}

		GrupCaselles veines = jugadorA.getVeins();
		ArrayList<Casella> grup_veines = veines.getGrup();

		System.out.println("Veins creats, hi ha " + grup_veines.size() + " caselles: ");
		for(int i=0; i<grup_veines.size(); i++) {
			System.out.println("Casella: " + grup_veines.get(i).getFila() + "," + grup_veines.get(i).getColumna());
		}

	}

}
