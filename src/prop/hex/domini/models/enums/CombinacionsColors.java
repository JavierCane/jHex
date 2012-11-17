package prop.hex.domini.models.enums;

import prop.cluster.domini.models.estats.EstatCasella;

import java.awt.*;

/**
 * Enum de possibles combinacions de colors.
 * La gràcia d'aquest enum es que es dinàmic i els codis de colors numés figuren aquí,
 * fent així que el fet de modificar aquestos (afegint o cambiant la tonalitat) numés impliqui editar aquest fitxer.
 * <p/>
 * L'ordre dels valors de l'array de colors de cada uns dels valors de l'enum es important y ha de ser el seguent:
 * Color de les caselles del jugador_a,
 * Color de les caselles del jugador_b,
 * Color de les caselles buides,
 * Color del fons de la finestra del joc,
 * Color de la vora de les caselles
 */
public enum CombinacionsColors
{

	VERMELL_BLAU( new Color[] {
			new Color( 0xAA0000 ),
			new Color( 0x0000AA ),
			new Color( 0xCCCCCC ),
			new Color( 0xFFFFFF ),
			new Color( 0x000000 )
	} ),

	NEGRE_BLANC( new Color[] {
			new Color( 0x000000 ),
			new Color( 0xFFFFFF ),
			new Color( 0x00AAAA ),
			new Color( 0x007777 ),
			new Color( 0x000000 )
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
	 * Atribut que, un cop inicialitzada la constructore de l'enum, contindrá el color del fons de la finestra del joc
	 */
	private Color color_fons_finestra;

	/**
	 * Atribut que, un cop inicialitzada la constructore de l'enum, contindrá el color de la vora de les caselles
	 * buides
	 */
	private Color color_vora_caselles;

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
		color_fons_finestra = colors[3];
		color_vora_caselles = colors[4];
	}

	/**
	 * Mètode públic per poder obtenir el color d'un tipus de casella en base a un valor de l'enum de possibles estats
	 *
	 * @param estat_casella
	 * @return
	 */
	public Color getColorCasella( EstatCasella estat_casella )
	{
		if ( EstatCasella.JUGADOR_A == estat_casella )
		{
			return color_jugador_a;
		}
		else if ( EstatCasella.JUGADOR_B == estat_casella )
		{
			return color_jugador_b;
		}
		else if ( EstatCasella.BUIDA == estat_casella )
		{
			return color_caselles_buides;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Mètode públic per poder obtenir el color del fons de la finestra del joc
	 *
	 * @return
	 */
	public Color getColorFonsFinestra()
	{
		return color_fons_finestra;
	}

	/**
	 * Mètode públic per poder obtenir el color del fons de la vora de les caselles
	 *
	 * @return
	 */
	public Color getColorVoraCaselles()
	{
		return color_vora_caselles;
	}
}
