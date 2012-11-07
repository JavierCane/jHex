package prop.hex.domini.controladors.IA;

import prop.hex.domini.models.Casella;
import prop.hex.domini.models.TaulerHex;
import prop.cluster.domini.models.estats.EstatCasella;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
FALTA TENER EN CUENTA LOS BORDES DEL TABLERO.
*/
/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 31/10/12
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
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
		pendents.add(inici);

		while ( !pendents.isEmpty() )     //mentres hi hagi caselles a la llista, seguir iterant.
		{
			Casella casella = pendents.get( 0 );     //obtenim el vei
			pendents.remove( casella );              //el treiem de pendents
			grup.add( casella );                     //l'afegim al grup.

			List<Casella> veins = tauler.getVeins( casella.getFila(), casella.getColumna() ); //obtenim els veins.
			for ( int i = 0; i < veins.size(); i++ )
			{
				Casella vei = veins.get( i );
				if ( tauler.getEstatCasella( vei.getFila(), vei.getColumna() ) == jo &&
					!grup.contains( vei ) &&
					!pendents.contains( vei ) )
				{
					//si te el mateix estat i no esta afegida al grup, la fiquem a pendents.
					pendents.add( vei );
				}
			}
		}
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
				if ( tauler.getEstatCasella( veins_locals.get( j ).getFila(),
											 veins_locals.get( j ).getColumna() ) == EstatCasella.BUIDA )
				{
					veins.afegirCasella( veins_locals.get( j ) );
				}
			}
		}
		return veins;
	}

	/**
	 * Mira si la casella pertany al grup.
	 * @param casella
	 * @return
	 */
	public boolean pertanyAlGrup( Casella casella )
	{
		return grup.contains( casella );
	}

}
