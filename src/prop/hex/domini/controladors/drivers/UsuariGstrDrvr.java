package prop.hex.domini.controladors.drivers;

import prop.hex.domini.models.UsuariHex;
import prop.hex.domini.models.enums.TipusJugadors;
import prop.hex.gestors.UsuariHexGstr;

import java.io.FileNotFoundException;
import java.io.IOException;

import static prop.hex.domini.controladors.drivers.UtilsDrvr.llegeixParaula;

/**
 * Proves dels mètodes de la classe UsuariHexGstr. No obstant, també s'estaria provant el correcte funcionament de les
 * classes BaseGstr, UsuariHex i Usuari de forma implícita
 *
 * @author Javier Ferrer Gonzalez (Grup 7.3, Hex)
 */
public final class UsuariGstrDrvr
{

	/**
	 * Fa una instancia nova d'UsuariHex amb el nom i contrasenya que se li especifiqui.
	 *
	 * @return UsuariHex l'Usuari instanciat
	 */
	public static UsuariHex testInstanciaUsuariHex()
	{
		String nom = llegeixParaula( "Escriu el nom de l'usuari a instanciar:\nPer provar casos extrems, " +
		                             "recorda que els noms no permesos son: \n\t" +
		                             UsuariHex.getNomsNoPermesos().toString() );

		String contrasenya = llegeixParaula( "Escriu la contrasenya de l'usuari a instanciar:" );

		try
		{
			UsuariHex usuari_hex = new UsuariHex( nom, contrasenya, TipusJugadors.JUGADOR );
			System.out.println(
					"[OK]\tS'ha instanciat correctament l'usuari. Les seves dades son:\n\t\t" + usuari_hex.toString() );
			return usuari_hex;
		}
		catch ( IllegalArgumentException e )
		{
			System.out.println( "[KO]\t" + e.getMessage() );
			return null;
		}
	}

	/**
	 * Primerament crida a testInstanciaUsuariHex per després intentar guardar aquest UsuariHex a disc.
	 */
	public static void testGuardaUsuariHex()
	{
		UsuariHex usuari_hex = testInstanciaUsuariHex();

		try
		{
			if ( UsuariHexGstr.getInstancia().guardaElement( usuari_hex ) )
			{
				System.out.println( "[OK]\tS'ha guardat correctament el fitxer de l'usuari amb identificador: " +
				                    usuari_hex.getIdentificadorUnic() );
			}
			else
			{
				System.out.println(
						"[KO]\tS'ha produit un error intentant guardar al fitxer de l'usuari amb identificador: " +
						usuari_hex.getIdentificadorUnic() );
			}
		}
		catch ( IOException e )
		{
			System.out.println(
					"[KO]\tS'ha produit un error intentant accedir al fitxer de l'usuari amb identificador: " +
					usuari_hex.getIdentificadorUnic() );
			e.printStackTrace();
		}
	}

	/**
	 * Carrega un UsuariHex desde disc. Tracta totes les possibles excepcions.
	 */
	public static void testCarregaUsuariHex()
	{
		String nom_usuari = llegeixParaula( "Escriu el nom de l'usuari a carregar de disc:" );

		try
		{
			UsuariHex main_user = UsuariHexGstr.getInstancia().carregaElement( nom_usuari );

			System.out.println( "[OK]\tS'ha carregat correctament el fitxer de l'usuari " + nom_usuari + ", " +
			                    "les seves dades son: \n\t\t" + main_user.toString() );
		}
		catch ( FileNotFoundException excepcio )
		{
			System.out.println( "[OK]\tL'usuari que s'intenta carregar (" + nom_usuari + "), " +
			                    "no existeix al sistema: " + excepcio.getMessage() );
		}
		catch ( IOException excepcio )
		{
			System.out.println( "[KO]\tS'ha produit un error intentant accedir al fitxer de l'usuari " + nom_usuari );
		}
		catch ( ClassNotFoundException excepcio )
		{
			System.out.println(
					"[KO]\tError no esperat de tipus ClassNotFoundException intentant accedir al fitxer de l'usuari " +
					nom_usuari );
		}
	}

	/**
	 * Elimina un UsuariHex desde disc.
	 */
	public static void testEliminaUsuariHex()
	{
		String nom_usuari = llegeixParaula( "Escriu el nom de l'usuari a eliminar de disc:" );

		if ( UsuariHexGstr.getInstancia().eliminaElement( nom_usuari ) )
		{
			System.out.println( "[OK]\tS'ha eliminat correctament el fitxer de l'usuari " + nom_usuari + "." );
		}
		else
		{
			System.out.println( "[KO]\tNo s'ha pogut eliminar el fitxer de l'usuari " + nom_usuari + "." );
		}
	}
}
