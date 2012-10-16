public class Main {

    /**
     * Método principal
     *
     * @param args
     */
    public static void main( String[] args )
    {
        decirHola( 3 );
        decirCosas( "Cosas", 2 );
    }

    /**
     * Saludar a mi madre tantas veces como le especifiquemos
     *
     * @param num_veces Número de veces a decir hola
     */
    private static void decirHola( Integer num_veces )
    {
        for ( Integer i = 0; i < num_veces; i++ )
        {
            System.out.println( i + ".- Hola mama, saluda!" );
        }
    }

    /**
     * Imprimir por pantalla lo que le indiquemos tantas veces como digamos
     *
     * @param num_veces Número de veces a decir el texto del 2º parámetro
     * @param texto     Texto a imprimir por pantalla
     */
    private static void decirCosas( String texto, Integer num_veces )
    {
        for ( Integer i = 0; i < num_veces; i++ )
        {
            System.out.println( i + ".- " + texto );
        }
    }
}