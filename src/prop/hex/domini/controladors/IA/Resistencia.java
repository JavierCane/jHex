package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 18/11/12
 * Time: 18:30
 * To change this template use File | Settings | File Templates.
 */
public class Resistencia
{

	TaulerHex tauler;
	EstatCasella jugador;
	boolean[][] checkedFields;
	ArrayList<Integer> pathResistances;

	public Resistencia( TaulerHex tauler, EstatCasella jugador )
	{
		this.tauler = tauler;
		this.jugador = jugador;
	}

	public int evalua()
	{
		double valor = 0.0;
		checkedFields = new boolean[tauler.getMida()][tauler.getMida()];
		pathResistances = new ArrayList<Integer>();
		resistenciaB(new Casella(-1, 0), 0);
		return getMinPath();
	}

	public void resistenciaB( Casella actual, int puntuacio )
	{
		if ( actual.getFila() != -1 )
		{
			checkedFields[actual.getFila()][actual.getColumna()] = true;
		}

		List<Casella> veins;

		if ( actual.getFila() == -1 )
		{
			veins = new ArrayList<Casella>();
			for ( int i = 0; i < tauler.getMida(); i++ )
			{
				veins.add( new Casella( 0, i ) );
			}
		}
		else
		{
			veins = tauler.getVeins( actual );
		}

		if ( actual.getFila() != -1 && tauler.getEstatCasella( actual ) == EstatCasella.BUIDA )
		{
			puntuacio++;
		}

		if ( actual.getColumna() == tauler.getMida() - 1 )
		{
			pathResistances.add( puntuacio );
		}

		for ( Casella vei : veins )
		{
			if ( checkedFields[vei.getFila()][vei.getColumna()] == false && tauler.getEstatCasella( vei ) == jugador && vei.getColumna() > actual.getColumna() )
			{
				resistenciaB(vei, puntuacio);
			}
		}

		for (Casella vei: veins)
		{
			if ( checkedFields[vei.getFila()][vei.getColumna()] == false && tauler.getEstatCasella( vei ) == jugador )
			{
				resistenciaB(vei, puntuacio);
			}
		}

		for ( Casella vei : veins )
		{
			if ( checkedFields[vei.getFila()][vei.getColumna()] == false && tauler.getEstatCasella( vei ) == EstatCasella.BUIDA && vei.getColumna() > actual.getColumna() )
			{
				resistenciaB(vei, puntuacio);
			}
		}

		for (Casella vei: veins)
		{
			if ( checkedFields[vei.getFila()][vei.getColumna()] == false && tauler.getEstatCasella( vei ) == EstatCasella.BUIDA )
			{
				resistenciaB(vei, puntuacio);
			}
		}

	}

	private int getMinPath() {
		int min = Integer.MAX_VALUE;
		for(Integer i : pathResistances)
		{
			System.out.println("VALI: " + i);
			if(i < min)
			{
				min = i;
			}
		}
		return min;
	}

}
