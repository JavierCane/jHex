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
 * Color de la vora de les caselles,
 * Color de les caselles pista,
 * Color de les caselles inhabilitades,
 * Color del text de la informació del jugador_a,
 * Color del text de Mou Fitxa IA del jugador_a,
 * Color del text de la informació del jugador_b,
 * Color del text de Mou Fitxa IA del jugador_b,
 * Color de la resta de textos
 *
 * @author Javier Ferrer Gonzalez (Grup 7.3, Hex)
 */
public enum CombinacionsColors
{

	VERMELL_BLAU( new Color[] {
			new Color( 0xAA0000 ),
			new Color( 0x0000AA ),
			new Color( 0xCCCCCC ),
			new Color( 0xFFFFFF ),
			new Color( 0x000000 ),
			new Color( 0x666666 ),
			new Color( 0x333333 ),
			new Color( 0xAA0000 ),
			new Color( 0xFFFFFF ),
			new Color( 0x0000AA ),
			new Color( 0xFFFFFF ),
			new Color( 0x000000 )
	} ),

	NEGRE_BLANC( new Color[] {
			new Color( 0x000000 ),
			new Color( 0xFFFFFF ),
			new Color( 0x00AAAA ),
			new Color( 0x007777 ),
			new Color( 0x000000 ),
			new Color( 0x005E5E ),
			new Color( 0x002E2E ),
			new Color( 0x000000 ),
			new Color( 0xFFFFFF ),
			new Color( 0xFFFFFF ),
			new Color( 0x000000 ),
			new Color( 0x000000 )
	} );

	/**
	 * Atribut que, un cop inicialitzada la constructora de l'enum, contindrà el color del jugador A
	 */
	private Color color_jugador_a;

	/**
	 * Atribut que, un cop inicialitzada la constructora de l'enum, contindrà el color del jugador B
	 */
	private Color color_jugador_b;

	/**
	 * Atribut que, un cop inicialitzada la constructora de l'enum, contindrà el color de les caselles que estiguin
	 * buides
	 */
	private Color color_caselles_buides;

	/**
	 * Atribut que, un cop inicialitzada la constructora de l'enum, contindrà el color del fons de la finestra del joc
	 */
	private Color color_fons_finestra;

	/**
	 * Atribut que, un cop inicialitzada la constructora de l'enum, contindrà el color de la vora de les caselles
	 * buides
	 */
	private Color color_vora_caselles;

	/**
	 * Atribut que, un cop inicialitzada la constructora de l'enum, contindrà el color de les caselles marcades al
	 * demanar una pista.
	 */
	private Color color_caselles_pista;

	/**
	 * Atribut que, un cop inicialitzada la constructora de l'enum, contindrà el color de les caselles que estiguin
	 * inhabilitades.
	 */
	private Color color_caselles_inhabilitades;

	/**
	 * Atribut que, un cop inicialitzada la constructora de l'enum, contindrà el color del text amb la informació
	 * del jugador A
	 */
	private Color color_text_informacio_jugador_a;

	/**
	 * Atribut que, un cop inicialitzada la constructora de l'enum, contindrà el color del text Mou Fitxa IA per al
	 * jugador A
	 */
	private Color color_text_mou_ia_jugador_a;

	/**
	 * Atribut que, un cop inicialitzada la constructora de l'enum, contindrà el color del text amb la informació
	 * del jugador B
	 */
	private Color color_text_informacio_jugador_b;

	/**
	 * Atribut que, un cop inicialitzada la constructora de l'enum, contindrà el color del text Mou Fitxa IA per al
	 * jugador B
	 */
	private Color color_text_mou_ia_jugador_b;

	/**
	 * Atribut que, un cop inicialitzada la constructora de l'enum, contindrà el color dels textos genèrics del
	 * visor de partides
	 */
	private Color color_text_generic;

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
		color_caselles_pista = colors[5];
		color_caselles_inhabilitades = colors[6];
		color_text_informacio_jugador_a = colors[7];
		color_text_mou_ia_jugador_a = colors[8];
		color_text_informacio_jugador_b = colors[9];
		color_text_mou_ia_jugador_b = colors[10];
		color_text_generic = colors[11];
	}

	/**
	 * Mètode públic per poder obtenir el color d'un tipus de casella en base a un valor de l'enum de possibles estats
	 *
	 * @param estat_casella Estat de la casella de la qual es vol saber el color
	 * @return El color relacionat amb l'estat.
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
	 * @return El color de fons de la finestra del joc.
	 */
	public Color getColorFonsFinestra()
	{
		return color_fons_finestra;
	}

	/**
	 * Mètode públic per poder obtenir el color del fons de la vora de les caselles
	 *
	 * @return El color de la vora de les caselles.
	 */
	public Color getColorVoraCaselles()
	{
		return color_vora_caselles;
	}

	/**
	 * Mètode públic per poder obtenir el color de les caselles que es marquen al demanar una pista.
	 *
	 * @return El color de les caselles marcades per una pista.
	 */
	public Color getColorCasellesPista()
	{
		return color_caselles_pista;
	}

	/**
	 * Mètode públic per poder obtenir el color de les caselles inhabilitades.
	 *
	 * @return El color de les caselles inhabilitades.
	 */
	public Color getColorCasellesInhabilitades()
	{
		return color_caselles_inhabilitades;
	}

	/**
	 * Mètode públic per poder obtenir el color del text d'un jugador de l'enum de possibles estats de les
	 * caselles (que coincideixen amb els possibles jugadors)
	 *
	 * @param estat_casella Jugador del qual es vol saber el color del text d'informació.
	 * @return El color relacionat amb l'estat.
	 */
	public Color getColorTextInformacio( EstatCasella estat_casella )
	{
		if ( EstatCasella.JUGADOR_A == estat_casella )
		{
			return color_text_informacio_jugador_a;
		}
		else if ( EstatCasella.JUGADOR_B == estat_casella )
		{
			return color_text_informacio_jugador_b;
		}
		else if ( EstatCasella.BUIDA == estat_casella )
		{
			return color_text_generic;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Mètode públic per poder obtenir el color del text de Mou Fitxa IA d'un jugador de l'enum de possibles estats
	 * de les
	 * caselles (que coincideixen amb els possibles jugadors)
	 *
	 * @param estat_casella Jugador del qual es vol saber el color del text de Mou Fitxa IA.
	 * @return El color relacionat amb l'estat.
	 */
	public Color getColorTextMouFitxaIA( EstatCasella estat_casella )
	{
		if ( EstatCasella.JUGADOR_A == estat_casella )
		{
			return color_text_mou_ia_jugador_a;
		}
		else if ( EstatCasella.JUGADOR_B == estat_casella )
		{
			return color_text_mou_ia_jugador_b;
		}
		else if ( EstatCasella.BUIDA == estat_casella )
		{
			return color_text_generic;
		}
		else
		{
			return null;
		}
	}
}
