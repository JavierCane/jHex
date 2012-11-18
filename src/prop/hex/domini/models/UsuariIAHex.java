package prop.hex.domini.models;

import prop.hex.domini.models.enums.TipusJugadors;

import java.io.Serializable;

/**
 * Classe UsuariHex. S'esté d'Usuari i simplement conté alguns mètodes per el tractament de atributs especifics d'un
 * Usuari del joc Hex.
 */
public final class UsuariIAHex extends UsuariHex implements Serializable
{

	/**
	 * ID de serialització
	 */
	private static final long serialVersionUID = -3033434414921865680L;

	/**
	 * Constructora per usuaris de tipus IA
	 *
	 * @param tipus_jugador
	 */
	public UsuariIAHex( TipusJugadors tipus_jugador )
	{
		super( tipus_jugador.getNomUsuari(), "" );

		this.tipus_jugador = tipus_jugador;
	}
}
