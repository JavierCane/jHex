package prop.hex.domini.controladors;

import prop.cluster.domini.models.estats.EstatCasella;
import prop.hex.domini.models.Casella;
import prop.hex.domini.models.PartidaHex;

/**
 * Interfície per poder cridar als moviments de la intel·ligència artificial de manera genèrica.
 *
 * @author Isaac Sánchez Barrera (Grup 7.3, Hex)
 */
public interface MouFitxaIA
{

	/**
	 * Configura la instància de partida per a la intel·ligència artificial.
	 *
	 * @param partida Partida on es vol jugar amb la intel·ligència artificial.
	 * @return Cert si s'ha canviat de partida. Fals altrament.
	 */
	public boolean setPartida( PartidaHex partida );

	/**
	 * @param fitxa Fitxa que vol col·locar-se al tauler de la partida del paràmetre implícit.
	 * @return La casella on es mouria la fitxa.
	 */
	public Casella mouFitxa( EstatCasella fitxa );
}
