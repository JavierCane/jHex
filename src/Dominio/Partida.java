package Dominio;

import java.util.Date;

public class Partida
{

	private Date fecha_hora;
	private Jugador[] jugadores;
	private String[] colores;
	private Tauler tauler;
	private Integer turno;
	private Integer total_turnos;
	private ModosInicio modo_inicio;


	/**
	 * Constructor por defecto. Inicializa la fecha y la hora de la partida a las actuales.
	 */
	public Partida()
	{
		// Por defecto, las instancias de Date() tienen el valor del instante en que se crean.
		fecha_hora = new Date();
		total_turnos = new Integer( 0 );
	}

	public Date getFechaHora()
	{
		return fecha_hora;
	}

	public Jugador[] getJugadores()
	{
		return jugadores;
	}

	public void setJugadores( Integer j, Jugador jugador )
	{
		this.jugadores[j] = jugador;
	}

	public void setJugadores( Jugador[] jugadores )
	{
		this.jugadores = jugadores;
	}

	public String[] getColores()
	{
		return colores;
	}

	public String getColores( Integer c )
	{
		return colores[c];
	}

	public void setColores( Integer c, String color )
	{
		this.colores[c] = color;
	}

	public void setColores( String[] colores )
	{
		this.colores = colores;
	}

	public Tauler getTauler()
	{
		return tauler;
	}

	public void setTauler( Tauler tauler )
	{
		this.tauler = tauler;
	}

	public Integer getTurno()
	{
		return turno;
	}

	public void setTurno( Integer turno )
	{
		this.turno = turno;
	}

	public Integer getTotalTurnos()
	{
		return total_turnos;
	}

	public void incrementarTotalTurnos()
	{
		total_turnos++;
	}

	public void cambiarTurno()
	{
		if ( turno.intValue() == 0 )
		{
			turno = 1;
		}
		else
		{
			turno = 0;
		}
	}

	public Jugador getJugadorCasilla( Integer fila, Integer columna )
	{
		Integer res = tauler.getNumJugadorCasilla( fila, columna );
		return ( res != null ? jugadores[res] : null );
	}

	public void setModoInicio( ModosInicio modo_inicio )
	{
		this.modo_inicio = modo_inicio;
	}

	public ModosInicio getModoInicio()
	{
		return modo_inicio;
	}

	public void intercambiarFichas()
	{
		Jugador j_aux = jugadores[0];
		jugadores[0] = jugadores[1];
		jugadores[1] =j_aux;

		String c_aux = colores[0];
		colores[0] = colores[1];
		colores[1] = c_aux;
	}
}
