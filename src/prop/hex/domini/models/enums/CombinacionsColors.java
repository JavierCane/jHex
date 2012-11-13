package prop.hex.domini.models.enums;

import prop.cluster.domini.models.estats.EstatCasella;

import java.awt.*;

/**
 * Enum de possibles combinacions de colors.
 * La gràcia d'aquest enum es que es dinàmic i els codis de colors numés figuren a aquest enum,
 * fent així que el fet de modificar aquestos (afegint o cambiant la tonalitat) numés impliqui editar aquest fitxer
 */
public enum CombinacionsColors
{

	VERMELL_BLAU( new Color[] {
			// Color del jugador_a (vermell)
			new Color( 0xFF0000 ),
			// Color del jugador_b (blau)
			new Color( 0x0000FF ),
			// Color de les fitxes buides (blanc)
			new Color( 0xFFFFFF )
	} ),

	NEGRE_BLANC( new Color[] {
			// Color del jugador_a (negre)
			new Color( 0x000000 ),
			// Color del jugador_b (blanc)
			new Color( 0xFFFFFF ),
			// Color de les fitxes buides (turquesa)
			new Color( 0x00AAAA )
	} );

	/**
	 * Atribut que, un cop inicialitzada la constructore de l'enum, contindrá el color del jugador A
	 */
	private Color color_jugador_a;

	/**
	 * Atribut que, un cop inicialitzada la constructore de l'enum, contindrá el color del jugador B
	 */
	private Color color_jugador_b;

	/**
	 * Atribut que, un cop inicialitzada la constructore de l'enum, contindrá el color de les caselles que estiguin
	 * buides
	 */
	private Color color_caselles_buides;

	/**
	 * Constructora de l'enum, simplement estableix el valor de cada un dels atributs privats per després poder-los
	 * obtenir en base al valor de l'enum seleccionat
	 *
	 * @param colors
	 */
	CombinacionsColors( Color[] colors )
	{
		color_jugador_a = colors[0];
		color_jugador_b = colors[1];
		color_caselles_buides = colors[2];
	}

	/**
	 * Mètode públic per poder obtenir el color d'un tipus de casella en base a un valor de l'enum
	 *
	 * @param jugador
	 * @return
	 */
	public Color obteColorCasella( EstatCasella jugador )
	{
		if ( EstatCasella.JUGADOR_A == jugador )
		{
			return color_jugador_a;
		}
		else if ( EstatCasella.JUGADOR_B == jugador )
		{
			return color_jugador_b;
		}
		else if ( EstatCasella.BUIDA == jugador )
		{
			return color_caselles_buides;
		}
		else
		{
			return null;
		}
	}
}
