package prop.hex.domini.controladors.IA;

import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;
import prop.cluster.domini.models.estats.EstatCasella;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 31/10/12
 */
public class GrupCaselles
{

	TaulerHex tauler;
	ArrayList<Casella> grup;

	public GrupCaselles( TaulerHex tauler )
	{
		this.tauler = tauler;
		grup = new ArrayList<Casella>();
	}

	/**
	 * A partir de la casella inicial, busca totes les caselles colindants amb el mateix estat.
	 *
	 * @param inici Casella inicial per buscar tot el grup.
	 */
	public void estendre( Casella inici )
	{
		EstatCasella jo = tauler.getEstatCasella( inici.getFila(), inici.getColumna() );

		List<Casella> pendents = new LinkedList<Casella>();
		pendents.add( inici );

		while ( !pendents.isEmpty() )     //mentres hi hagi caselles a la llista, seguir iterant.
		{
			Casella casella = pendents.get( 0 );     //obtenim el vei

			pendents.remove( casella );              //el treiem de pendents
			grup.add( casella );                     //l'afegim al grup.

			//Comprovem si esta a la cantonada, primer pel jugador A i despres pel B i si ho està mirem totes les cantonades.
			if ( jo == EstatCasella.JUGADOR_A && ( casella.getColumna() == 0 || casella.getColumna() == tauler.getMida() - 1 ) )
			{
				for ( int i = 0; i < tauler.getMida(); i++ )
				{
					Casella casellaLateral = new Casella( i, casella.getColumna() );
					if ( esPotAfegir( casellaLateral, pendents, jo ) )
					{
						pendents.add( casellaLateral );
					}
				}
			}
			else if ( jo == EstatCasella.JUGADOR_B && ( casella.getFila() == 0 || casella.getFila() == tauler.getMida() - 1 ) )
			{
				for ( int i = 0; i < tauler.getMida(); i++ )
				{
					Casella casellaLateral = new Casella( casella.getFila(), i );
					if ( esPotAfegir( casellaLateral, pendents, jo ) )
					{
						pendents.add( casellaLateral );
					}
				}
			}

			List<Casella> veins = tauler.getVeins( casella.getFila(), casella.getColumna() ); //obtenim els veins.
			for ( int i = 0; i < veins.size(); i++ )
			{
				Casella vei = veins.get( i );
				if ( esPotAfegir( vei, pendents, jo ) )
				{
					//si te el mateix estat i no està afegida al grup, la fiquem a pendents.
					pendents.add( vei );
				}
			}
		}
	}

	/**
	 * Métode auxiliar per a estendre, comprova si una casella es pot afegir a grup.
	 * Mira si te el mateix estat, no està afegida al grup i no està a pendents.
	 *
	 * @param casella
	 * @param pendents
	 * @param jo
	 * @return
	 */
	private boolean esPotAfegir( Casella casella, List<Casella> pendents, EstatCasella jo )
	{
		if ( tauler.getEstatCasella( casella.getFila(), casella.getColumna() ) == jo &&
				!grup.contains( casella ) &&
				!pendents.contains( casella ) )
		{
			return true;
		}
		return false;
	}


	/**
	 * Afegeix la casella "afegir" al grup.
	 *
	 * @param afegir casella a afegir
	 * @return
	 */
	public boolean afegirCasella( Casella afegir )
	{
		if ( grup.contains( afegir ) )
		{
			return false;
		}
		grup.add( afegir );
		return true;

	}

	/**
	 * Retorna un ArrayList de caselles amb totes les caselles del grup.
	 *
	 * @return
	 */
	public ArrayList<Casella> getGrup()
	{
		return grup;
	}

	/**
	 * Retorna un GrupCaselles amb totes les caselles veines.
	 *
	 * @return
	 */
	public GrupCaselles getVeins()
	{
		GrupCaselles veins = new GrupCaselles( this.tauler );

		List<Casella> visitats = new LinkedList<Casella>();

		for ( int i = 0; i < grup.size(); i++ )
		{
			List<Casella> veins_locals = tauler.getVeins( grup.get( i ).getFila(), grup.get( i ).getColumna() );
			for ( int j = 0; j < veins_locals.size(); j++ )
			{
				if ( tauler.getEstatCasella( veins_locals.get( j ).getFila(), veins_locals.get( j ).getColumna() ) == EstatCasella.BUIDA )
				{
					veins.afegirCasella( veins_locals.get( j ) );
				}
			}
		}
		return veins;
	}

	/**
	 * Mira si la casella pertany al grup.
	 *
	 * @param casella
	 * @return
	 */
	public boolean pertanyAlGrup( Casella casella )
	{
		return grup.contains( casella );
	}

	public int interseca( GrupCaselles grupB )
	{
		int contador = 0;
		ArrayList<Casella> CasellesB = grupB.getGrup();
		for ( int i = 0; i < grup.size(); i++ )
		{
			for ( int j = 0; j < CasellesB.size(); j++ )
			{
				if ( grup.get( i ).equals( CasellesB.get( j ) ) )
				{
					contador++;
				}
			}
		}
		return contador;
	}
}
