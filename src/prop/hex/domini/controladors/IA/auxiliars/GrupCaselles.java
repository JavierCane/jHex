package prop.hex.domini.controladors.IA.auxiliars;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Busquem grups de caselles contigues, tenint en compte les cantonades.
 * El JugadorA va d'est a oest, el JugadorB de nord a sud.
 */
public final class GrupCaselles
{

	/**
	 * Tauler sobre el que es busquen els grups.
	 */
	private TaulerHex tauler;

	/**
	 * Grup de caselles.
	 */
	private ArrayList<Casella> grup;

	private EstatCasella jo;

	/**
	 * Instancia un GrupCaselles, amb el grup buit.
	 *
	 * @param tauler tauler sobre el que buscar grups.
	 */
	public GrupCaselles( TaulerHex tauler )
	{
		this.tauler = tauler;
		grup = new ArrayList<Casella>();
	}

	/**
	 * A partir de la casella inicial donada, busca totes les caselles colindants amb el mateix estat, omplint el grup.
	 *
	 * @param inici Casella inicial per buscar tot el grup.
	 */
	public void estendre( Casella inici )
	{
		jo = tauler.getEstatCasella( inici );

		List<Casella> pendents = new LinkedList<Casella>();
		pendents.add( inici );

		while ( !pendents.isEmpty() )     //mentres hi hagi caselles a la llista, seguir iterant.
		{
			Casella casella = pendents.get( 0 );     //obtenim el vei

			pendents.remove( casella );              //el treiem de pendents
			grup.add( casella );                     //l'afegim al grup.

			//Comprovem si esta a la cantonada, primer pel jugador A i despres pel B i si ho està mirem totes les cantonades.
			if ( jo == EstatCasella.JUGADOR_A &&
			     ( casella.getColumna() == 0 || casella.getColumna() == tauler.getMida() - 1 ) )
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
			else if ( jo == EstatCasella.JUGADOR_B &&
			          ( casella.getFila() == 0 || casella.getFila() == tauler.getMida() - 1 ) )
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

			List<Casella> veins = tauler.getVeins( casella ); //obtenim els veins.
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
	 * @param casella  casella a mirar si es pot afegir.
	 * @param pendents llistat de caselles pendents per analitzar.
	 * @param jo       llistat de la meva casella.
	 * @return true si es pot afegir, false altrament.
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
	 * @return return false si no es pot afegir, altrament return true.
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
	 * @return Grup de caselles.
	 */
	public ArrayList<Casella> getGrup()
	{
		return grup;
	}

	/**
	 * Consulta les caselles veines.
	 *
	 * @return Un GrupCaselles amb les caselles veïnes al grup.
	 */
	public GrupCaselles getVeins()
	{
		GrupCaselles veins = new GrupCaselles( this.tauler );

		List<Casella> visitats = new LinkedList<Casella>();

		for ( Casella casella : grup )
		{
			//Mirem si els veins es poden afegir.
			List<Casella> veins_locals = tauler.getVeins( casella );
			for ( Casella vei : veins_locals )
			{
				if ( tauler.getEstatCasella( vei ) == EstatCasella.BUIDA )
				{
					veins.afegirCasella( vei );
				}
			}

			//Mirem si la casella pertany a la cantonada d'un jugador i si es pot afegir.
			if ( jo == EstatCasella.JUGADOR_A )
			{
				//Si es jugador A mirem les columnes.
				if ( casella.getColumna() == 0 || casella.getColumna() == tauler.getMida() - 1 )
				{
					//Iterem per totes les de la mateixa columna i les afegim si es pot.
					for ( int i = 0; i < tauler.getMida(); i++ )
					{
						if ( tauler.getEstatCasella( i, casella.getColumna() ) == EstatCasella.BUIDA )
						{
							veins.afegirCasella( new Casella( i, casella.getColumna() ) );
						}
					}
				}
			}
			//Fem el mateix pel jugador B
			else if ( jo == EstatCasella.JUGADOR_B )
			{
				if ( casella.getFila() == 0 || casella.getFila() == tauler.getMida() - 1 )
				{
					for ( int j = 0; j < tauler.getMida(); j++ )
					{
						if ( tauler.getEstatCasella( casella.getFila(), j ) == EstatCasella.BUIDA )
						{
							veins.afegirCasella( new Casella( casella.getFila(), j ) );
						}
					}
				}
			}
		}
		return veins;
	}

	/**
	 * Mira si la casella pertany al grup.
	 *
	 * @param casella
	 * @return Cert si la casella pertany al grup. Fals altrament.
	 */
	public boolean pertanyAlGrup( Casella casella )
	{
		return grup.contains( casella );
	}

	/**
	 * Obté el nombre de caselles d'intersecció entre dos grups de caselles.
	 *
	 * @param grupB grup amb el que intersequem.
	 * @return nombre de caselles intersecades.
	 */
	public int interseca( GrupCaselles grupB )
	{
		int contador = 0;
		ArrayList<Casella> CasellesB = grupB.getGrup();
		for ( int i = 0; i < grup.size(); i++ )
		{
			if ( tauler.getEstatCasella( grup.get( i ) ) == EstatCasella.BUIDA )
			{
				for ( int j = 0; j < CasellesB.size(); j++ )
				{
					if ( grup.get( i ).equals( CasellesB.get( j ) ) )
					{
						contador++;
					}
				}
			}
		}
		return contador;
	}
}
