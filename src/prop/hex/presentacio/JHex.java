package prop.hex.presentacio;

/**
 * Classe principal del joc Hex. Executa el programa cridant al controlador de presentació, establint prèviament que
 * l'estil de la interfície serà el per defecte del sistema.
 *
 * @author Guillermo Girona San Miguel (Grup 7.3, Hex)
 */
public final class JHex
{

	/**
	 * Funció principal del programa JHex
	 *
	 * @param args Paràmetres del programa quan s'executa en consola.
	 */
	public static void main( String[] args )
	{
		try // Nom de l'aplicació a Mac OS X
		{
			System.setProperty( "apple.laf.useScreenMenuBar", "true" );
			System.setProperty( "com.apple.mrj.application.apple.menu.about.name", "jHex – Joc de Taula Hex" );
		}
		catch ( Exception excepcio )
		{
			// Fora de Mac
		}

		try // Estil per defecte del sistema
		{
			javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getSystemLookAndFeelClassName() );
		}
		catch ( Exception excepcio )
		{
			// No es pot carregar la visualització per defecte del sistema.
		}

		javax.swing.SwingUtilities.invokeLater( new Runnable()
		{

			@Override
			public void run()
			{
				PresentacioCtrl.getInstancia().inicialitzaPresentacio();
			}
		} );
	}
}
