package prop.hex.domini.controladors;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;

/**
 * Interface per poder cridar als moviments de la intel·ligència artificial de manera genèrica.
 */
public interface MouFitxaIA
{
	public boolean setPartida(PartidaHex partida);
	public Casella mouFitxa(EstatCasella fitxa);
}
