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

	private TaulerHex tauler;
	private ArrayList<GrupCaselles> grups;
	private EstatCasella jo;
	private int connexions_virtuals;
	private int connexions_semivirtuals;

	public ConnexionsVirtuals( TaulerHex tauler, EstatCasella jo )
	{
		this.tauler = tauler;
		this.jo = jo;
		connexions_virtuals = 0;
		connexions_semivirtuals = 0;
		grups = new ArrayList<GrupCaselles>();
		//Omplim grups amb tots els grups de caselles.
		buscaGrups();
		//Un cop tenim els grups, busquem les connexions virtuals de primer ordre.
		buscaConnexions();
	}

	/**
	 * Busca connexions i semiconnexions de primer ordre.
	 */
	private void buscaConnexions()
	{
		// Comparem tots els grups amb tots (sense repetir).
		for ( int i = 0; i < grups.size(); i++ )
		{
			for ( int j = i+1; j < grups.size(); j++ )
			{
				//interseccio és el nombre de caselles que comparteixen dos grups.
				int interseccio = grups.get( i ).getVeins().interseca( grups.get( j ).getVeins() );
				if ( interseccio >= 2 ) //si és més que 2 la connexió és virtual per la regla OR.
				{
					connexions_virtuals++;
				}
				else if ( interseccio == 1 ) //si és 1 la connexió és semivirtual per la regla AND.
				{
					connexions_semivirtuals++;
				}
			}
		}
	}

	/**
	 * Consultora
	 * @return ArrayList amb els grups de caselles.
	 */
	public ArrayList<GrupCaselles> getGrups()
	{
		return grups;
	}

	/**
	 * Consultora
	 * @return nombre de connexions virtuals.
	 */
	public int getConnexions_virtuals()
	{
		return connexions_virtuals;
	}

	/**
	 * Consultora
	 * @return nombre de connexions semivirtuals.
	 */
	public int getConnexions_semivirtuals()
	{
		return connexions_semivirtuals;
	}

	/**
	 * Obté el llistat de tots els grups de caselles de l'usuari especificat.
	 */
	private void buscaGrups()
	{
		//Iterem per totes les caselles.
		for ( int i = 0; i < tauler.getMida(); i++ )
		{
			for ( int j = 0; j < tauler.getMida(); j++ )
			{
				if ( tauler.getEstatCasella( i, j ) == jo ) //si la casella és meva.
				{
					Casella casella = new Casella( i, j );
					if ( !estaEnUnGrup( casella ) )  //i si la casella no esta en cap grup.
					{
						GrupCaselles nou_grup = new GrupCaselles( tauler );
						nou_grup.estendre( casella );
						grups.add( nou_grup );        //creem un nou grup i l'afegim.
					}
				}
			}
		}
	}

	/**
	 * Mira si la casella està en algun grup.
	 * @param casella
	 * @return true si casella està en un grup, false si no.
	 */
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
