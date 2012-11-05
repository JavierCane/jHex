package prop.hex.domini.models.enums;

public enum Colors
{
	VERMELL( "FF0000" ),
	BLAU( "0000FF" ),
	NEGRE( "000000" ),
	BLANC( "FFFFFF" );

	private final String rgb;

	Colors( String rgb )
	{
		this.rgb = rgb;
	}

	Colors()
	{
		rgb = "";
	}

	public String getRGB()
	{
		return rgb;
	}
}
