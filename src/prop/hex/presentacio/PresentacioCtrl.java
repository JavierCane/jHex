package prop.hex.presentacio;

import javax.swing.*;
import java.awt.*;

import prop.hex.domini.controladors.PartidaCtrl;
import prop.hex.domini.controladors.UsuariCtrl;

public class PresentacioCtrl
{

	private IniciaSessioVista inicia_sessio_vista = new IniciaSessioVista();


	public void inicialitzaPresentacio()
	{
		inicia_sessio_vista.fesVisible();
	}
}
