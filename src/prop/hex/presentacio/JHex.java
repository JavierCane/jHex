package prop.hex.presentacio;

public class JHex
{

	public static void main( String[] args )
	{
		javax.swing.SwingUtilities.invokeLater( new Runnable()
		{
			@Override
			public void run()
			{
				PresentacioCtrl presentacio_ctrl = new PresentacioCtrl();
				presentacio_ctrl.inicialitzaPresentacio();
			}
		} );
	}
}
