package prop.hex.domini.models.enums;

import java.awt.Color;

public enum CombinacionsColors
{

	VERMELL_BLAU( new Color[] {
			new Color( 0xFF0000 ),
			new Color( 0x0000FF )
	} ),

	NEGRE_BLANC( new Color[] {
			new Color( 0x000000 ),
			new Color( 0xFFFFFF )
	} );

	CombinacionsColors( Color[] colors )
	{
	}
}
