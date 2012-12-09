package prop.hex.domini.models.enums;

/**
 * Representa els modes d'inici de joc en una partida de Hex.
 *
 * @author Isaac Sánchez Barrera (Grup 7.3, Hex)
 */
public enum ModesInici
{
	/**
	 * Mode d'inici estàndard. Es caracteritza perquè no es poden posar fitxes a les caselles centrals del tauler en
	 * el primer torn.
	 */
	ESTANDARD,

	/**
	 * Mode d'inici amb la regla del pastís. Es caracteritza perquè un cop el jugador A ha posat la primera fitxa al
	 * tauler, l'altre jugador decideix qui és el seu propietari.
	 */
	PASTIS
}
