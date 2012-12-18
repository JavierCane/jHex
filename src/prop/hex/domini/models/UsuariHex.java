package prop.hex.domini.models;

import prop.cluster.domini.models.Usuari;
import prop.hex.domini.models.enums.CombinacionsColors;
import prop.hex.domini.models.enums.ModesInici;
import prop.hex.domini.models.enums.TipusJugadors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe UsuariHex. S'esté d'Usuari i simplement conté alguns mètodes per el tractament de atributs especifics d'un
 * Usuari del joc Hex.
 *
 * @author Javier Ferrer Gonzalez (Grup 7.3, Hex)
 */
public final class UsuariHex extends Usuari implements Serializable, Comparable<UsuariHex>
{

	/**
	 * ID de serialització
	 */
	private static final long serialVersionUID = -2893672483693919721L;

	/**
	 * Tipus de jugador
	 */
	protected TipusJugadors tipus_jugador;

	/**
	 * Mode d'inici de les partides seleccionat per l'usuari com a preferent
	 */
	private ModesInici mode_inici;

	/**
	 * Combinació de colors de les fitxes de les partides seleccionada per l'usuari com a preferent
	 */
	private CombinacionsColors combinacio_colors;

	/**
	 * Temps mínim en guanyar una partida
	 */
	private Long temps_minim;

	/**
	 * Fitxes mínimes utilitzades per guanyar una partida
	 */
	private Integer fitxes_minimes;

	/**
	 * Nombre de partides disputades
	 */
	private Integer partides_jugades;

	/**
	 * Nombre de partides guanyades
	 */
	private Integer partides_guanyades;

	/**
	 * Puntuació global de l'usuari
	 */
	private Integer puntuacio_global;

	/**
	 * Llista de noms d'usuari no permesos.
	 */
	private static final Set<String> noms_reservats =
			Collections.unmodifiableSet( new HashSet<String>( Arrays.asList( new String[] {
					"Convidat",
					"Convidat 1",
					"Convidat 2",
					"Mr. X (alg. aleatori, n. X)",
					"Jose Antonio Camacho (n. 0)",
					"Bill Clinton (n. 0.5)",
					"Hillary Clinton (n. 1)",
					"Monica Lewinsky (n. 1.5)",
					"Barney Stinson (n. 2)",
			} ) ) );

	/**
	 * Expressió regular amb els caràcters permesos.
	 */
	private static final String caracters_permesos = "^[A-Za-z0-9,_ ]+$";

	/**
	 * Constructora per defecte, tot i que utilitzem la constructora de la classe pare,
	 * es sobreescriu amb la finalitat de poder comprovar que el nom d'usuari no es tracta d'un nom no permès.
	 *
	 * @param nom           Nom de l'usuari
	 * @param contrasenya   Contrasenya de l'usuari
	 * @param tipus_jugador Tipus de jugador (CONVIDAT o JUGADOR)
	 */
	public UsuariHex( String nom, String contrasenya, TipusJugadors tipus_jugador )
	{
		super( nom, contrasenya, TipusJugadors.getNumDificultats() );

		this.tipus_jugador = tipus_jugador;
		mode_inici = ModesInici.ESTANDARD;
		combinacio_colors = CombinacionsColors.VERMELL_BLAU;

		reiniciaEstadistiques();
	}

	/**
	 * Consulta l'identificador unic de l'usuari. Utilitzada per guardar a disc el fitxer corresponent de l'usuari.
	 *
	 * @return L'identificador únic de l'usuari.
	 */
	public String getIdentificadorUnic()
	{
		return getNom().replace( ' ', '-' );
	}

	/**
	 * Mètode amb la finalitat de poder depurar el programa amb més facilitat. Simplement retorna tots els atributs
	 * de la classe y els seus corresponents valors.
	 *
	 * @return Un String amb tots els atribus de l'usuari.
	 */
	@Override
	public String toString()
	{
		return super.toString() + " [Mode inici: " + mode_inici +
		       ", combinacio colors: " + combinacio_colors + ", temps minim: " + temps_minim +
		       ", fitxes minimes: " + fitxes_minimes + ", partides guanyades: " + partides_guanyades +
		       ", partides jugades: " + partides_jugades + ", puntuacio global: " + puntuacio_global + "]";
	}

	/**
	 * Consulta el llistat de noms d'usuari no permesos
	 *
	 * @return El llistat de noms d'usuari no permesos.
	 */
	public static Set<String> getNomsNoPermesos()
	{
		return noms_reservats;
	}

	/**
	 * Consulta si l'usuari és un usuari registrat o és un usuari del sistema.
	 *
	 * @return Cert, si es tracta d'un usuari registrat. Fals, si és un usuari del sistema.
	 */
	public boolean esUsuariRegistrat()
	{
		return ( !noms_reservats.contains( nom ) );
	}

	/**
	 * Consulta l'expressió regular de noms permesos.
	 *
	 * @return L'expressió regular de noms permesos.
	 */
	public static String getCaractersPermesos()
	{
		return caracters_permesos;
	}

	/**
	 * Consulta el mode d'inici de la partida que té assignat l'usuari.
	 *
	 * @return Un ModeInici corresponent al mode d'inici preferit per l'usuari.
	 */
	public ModesInici getModeInici()
	{
		return mode_inici;
	}

	/**
	 * Consulta el tipus de jugador que és l'usuari.
	 *
	 * @return Un TipusJugador amb el tipus de jugador corresponent a l'usuari.
	 */
	public TipusJugadors getTipusJugador()
	{
		return tipus_jugador;
	}

	/**
	 * Modifica el mode d'inici que té assignat l'usuari.
	 *
	 * @param mode_inici ModeInici que es vol assignar a l'usuari.
	 */
	public boolean setModeInici( ModesInici mode_inici )
	{
		this.mode_inici = mode_inici;
		return true;
	}

	/**
	 * Consulta els colors que té assignat l'usuari.
	 *
	 * @return Un array de dues posicions amb els dos colors de fitxes preferits per l'usuari.
	 */
	public CombinacionsColors getCombinacionsColors()
	{
		return combinacio_colors;
	}

	/**
	 * Modifica un cert color dels que té assignats l'usuari.
	 *
	 * @return Cert si el color es modifica. Fals altrament.
	 */
	public boolean setCombinacionsColors( CombinacionsColors combinacio_colors )
	{
		this.combinacio_colors = combinacio_colors;
		return true;
	}

	/**
	 * Consulta el temps mínim en guanyar una partida de l'usuari.
	 *
	 * @return El temps mínim en guanyar una partida de l'usuari.
	 */
	public Long getTempsMinim()
	{
		return temps_minim;
	}

	/**
	 * Consulta el nombre de fitxes mínim amb què l'usuari ha guanyat una partida.
	 *
	 * @return El nombre de fitxes mínim amb què l'usuari ha guanyat una partida
	 */
	public Integer getFitxesMinimes()
	{
		return fitxes_minimes;
	}

	/**
	 * Consulta el nombre de partides jugades per l'usuari.
	 *
	 * @return El nombre de partides jugades per l'usuari.
	 */
	public Integer getPartidesJugades()
	{
		return partides_jugades;
	}

	/**
	 * Consulta el nombre de partides guanyades per l'usuari.
	 *
	 * @return El nombre de partides guanyades per l'usuari.
	 */
	public Integer getPartidesGuanyades()
	{
		return partides_guanyades;
	}

	/**
	 * Consulta la puntuació global de l'usuari.
	 *
	 * @return La posició global de l'usuari.
	 */
	public Integer getPuntuacioGlobal()
	{
		return puntuacio_global;
	}

	/**
	 * Recalcula les dades de l'usuari en base a una partida finalitzada.
	 * Unicament actualitza les dades d'estadístiques si es tracta d'un usuari de tipus humà o si es tracta d'usuaris
	 * de tipus IA amb nivells de dificultats diferents. Unicament actualitzarem estadístiques si el contrincant
	 * contra el que s'ha jugat no era un convidat.
	 */
	public void recalculaDadesUsuariPartidaFinalitzada( boolean ha_guanyat, TipusJugadors tipus_jugador_contrari,
	                                                    Long temps_emprat, Integer fitxes_usades )
	{
		if ( tipus_jugador_contrari != TipusJugadors.CONVIDAT &&
		     ( tipus_jugador == TipusJugadors.JUGADOR || tipus_jugador != tipus_jugador_contrari ) )
		{
			if ( ha_guanyat )
			{
				if ( num_victories[tipus_jugador_contrari.getPosicioDificultat()] < Integer.MAX_VALUE )
				{
					num_victories[tipus_jugador_contrari.getPosicioDificultat()]++;
				}

				if ( partides_guanyades < Integer.MAX_VALUE )
				{
					partides_guanyades++;
				}

				if ( temps_emprat < temps_minim )
				{
					temps_minim = temps_emprat;
				}

				if ( fitxes_usades < fitxes_minimes )
				{
					fitxes_minimes = fitxes_usades;
				}
			}
			else
			{
				if ( num_derrotes[tipus_jugador_contrari.getPosicioDificultat()] < Integer.MAX_VALUE )
				{
					num_derrotes[tipus_jugador_contrari.getPosicioDificultat()]++;
				}
			}

			if ( partides_jugades < Integer.MAX_VALUE )
			{
				partides_jugades++;
			}

			recalculaPuntuacioGlobal();
		}
	}

	/**
	 * Recalcula la puntuació global d'un jugador tenint en compte el nombre de victories i derrotes en funció de la
	 * dificultat de l'usuari/IA contrari i els punts que orotga/resta aquest tipus de resultat.
	 */
	private void recalculaPuntuacioGlobal()
	{
		Integer sum_victories = 0;
		Integer sum_derrotes = 0;

		for ( TipusJugadors dificultat : TipusJugadors.values() )
		{
			sum_victories += num_victories[dificultat.getPosicioDificultat()] * dificultat.getPuntsPerGuanyar();
			sum_derrotes += num_derrotes[dificultat.getPosicioDificultat()] * dificultat.getPuntsPerPerdre();
		}

		if ( sum_victories - sum_derrotes < Integer.MIN_VALUE )
		{
			puntuacio_global = Integer.MIN_VALUE;
		}
		else if ( sum_victories - sum_derrotes > Integer.MAX_VALUE )
		{
			puntuacio_global = Integer.MAX_VALUE;
		}
		else
		{
			puntuacio_global = sum_victories - sum_derrotes;
		}
	}

	/**
	 * Mètode que reinicia les estadístiques de victòries, empats, derrotes i la puntuació d'un usuari.
	 */
	@Override
	public void reiniciaEstadistiques()
	{
		super.reiniciaEstadistiques();

		temps_minim = Long.MAX_VALUE;
		fitxes_minimes = Integer.MAX_VALUE;
		partides_jugades = 0;
		partides_guanyades = 0;
		puntuacio_global = 0;
	}

	/**
	 * Mètode per comparar dos usuaris en funció de la serva puntuació global.
	 * Utilitzada implícitament per el mètode Collections.sort de la funció actualitzaRanquingUsuari de la classe Ranquing
	 * per ordenar el ràquing en funció de les millors puntuacions globals.
	 *
	 * @param contrincant UsuariHex amb qui es vol comparar
	 * @return -1 si l'usuari actual té més punts, 0 si estan empatats, 1 si l'usuari contrincant té més punts.
	 */
	@Override
	public int compareTo( UsuariHex contrincant )
	{
		return contrincant.getPuntuacioGlobal().compareTo( puntuacio_global );
	}

	/**
	 * Compara la igualtat entre dos Usuaris.
	 * Utilitzada implícitament per la funció indexOf del mètode actualitzaRanquingUsuari de la classe Ranquing per comprovar
	 * si un usuari ja ha estat insertat o no al rànquing.
	 *
	 * @param suposat_usuari_hex Object de tipus UsuariHex a comparar
	 * @return Cert si l'usuari té el mateix identificador. Fals altrament.
	 */
	public boolean equals( Object suposat_usuari_hex )
	{
		if ( suposat_usuari_hex == this )
		{
			return true;
		}
		else if ( suposat_usuari_hex == null )
		{
			return false;
		}
		else if ( !( suposat_usuari_hex instanceof UsuariHex ) )
		{
			return false;
		}
		else
		{
			UsuariHex usuari_hex = ( UsuariHex ) suposat_usuari_hex;

			if ( usuari_hex.getIdentificadorUnic().equals( this.getIdentificadorUnic() ) )
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
}
