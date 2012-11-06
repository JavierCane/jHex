package prop.hex.domini.models.enums;

public enum Dificultats
{
	JUGADOR( 0 ), IA_FACIL( 1 ), IA_DIFICIL( 2 );

	public static final int num_dificultats = Dificultats.values().length;

	Dificultats( int posicio_a_array_usuari )
	{
	}
}
