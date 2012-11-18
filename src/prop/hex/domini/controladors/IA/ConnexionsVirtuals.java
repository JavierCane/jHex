package prop.hex.domini.controladors.IA;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.util.ArrayList;

/**
 *
 *
 */
public class ConnexionsVirtuals
{

	TaulerHex tauler;
	ArrayList<GrupCaselles> grups;
	EstatCasella jo;
	int connexions_virtuals;
	int connexions_semivirtuals;

	public ConnexionsVirtuals( TaulerHex tauler, EstatCasella jo )
	{
		this.tauler = tauler;
		this.jo = jo;
		connexions_virtuals = 0;
		connexions_semivirtuals = 0;
		grups = new ArrayList<GrupCaselles>();
		buscaGrups();
		buscaConnexions();
	}

	private void buscaConnexions()
	{
		for ( int i = 0; i < grups.size(); i++ )
		{
			for ( int j = i+1; j < grups.size(); j++ )
			{
				int interseccio = grups.get( i ).getVeins().interseca( grups.get( j ).getVeins() );
				if ( interseccio >= 2 )
				{
					connexions_virtuals++;
				}
				else if ( interseccio == 1 )
				{
					connexions_semivirtuals++;
				}
			}
		}
	}

	public ArrayList<GrupCaselles> getGrups()
	{
		return grups;
	}

	public int getConnexions_virtuals()
	{
		return connexions_virtuals;
	}

	public int getConnexions_semivirtuals()
	{
		return connexions_semivirtuals;
	}

	/**
	 * Obté el llistat de tots els grups de caselles de l'usuari especificat.
	 */
	private void buscaGrups()
	{
		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				if ( tauler.getEstatCasella( i, j ) == jo ) //si la casella és meva.
				{
					Casella casella = new Casella( i, j );
					if ( !estaEnUnGrup( casella ) )  //si la casella no esta en cap grup.
					{
						GrupCaselles nou_grup = new GrupCaselles( tauler );
						nou_grup.estendre( casella );
						grups.add( nou_grup );        //creem un nou grup i l'afegim.

					}
				}
			}
		}
	}

	private boolean estaEnUnGrup( Casella casella )
	{

		for ( int i = 0; i < grups.size(); i++ )
		{
			if ( grups.get( i ).pertanyAlGrup( casella ) )
			{
				return true;
			}
		}

		return false;
	}

}
