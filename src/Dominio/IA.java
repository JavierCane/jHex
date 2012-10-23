package Dominio;

import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 22/10/12
 * Time: 16:44
 * To change this template use File | Settings | File Templates.
 */
public class IA {

    private Casilla mejor_jugada;
    private Integer max_profundidad;
    private Integer MaxPlayer;

    public IA(int level) {

    }


    public Casilla minimax(Tablero p, Integer jugador, Integer profundidad) {

        return mejor_jugada;
    }

    private Double recursion(Tablero p, Integer jugador, Integer profundidad, Double alpha, Double beta) {
        Vector<Casilla> moves_list = moves_available(p);

        if (profundidad == max_profundidad || moves_list.size() == 0) {    //end recursion if we reach the maximum depth or if we can't keep playing.
            return heuristic_value(p, jugador);
        } else {
            if (jugador == MaxPlayer) {       //if player is max do max    (cambia maxplayer por me)
                for (int i = 0; i < moves_list.size(); i++) {
                    p.colocarFicha(jugador, moves_list.get(i).getFila(), moves_list.get(i).getColumna());  //put the piece
                    alpha = Math.max(alpha, recursion(p, 1 - jugador, profundidad + 1, alpha, beta));         //do recursion
                    p.colocarFicha(null, moves_list.get(i).getFila(), moves_list.get(i).getColumna());    //remove the piece
                    if (alpha >= beta) break;
                }
                return alpha;
            } else {
                for (int i = 0; i < moves_list.size(); i++) {
                    p.colocarFicha(jugador, moves_list.get(i).getFila(), moves_list.get(i).getColumna());  //put the piece
                    beta = Math.min(beta, recursion(p, 1 - jugador, profundidad + 1, alpha, beta));         //do recursion
                    p.colocarFicha(null, moves_list.get(i).getFila(), moves_list.get(i).getColumna());    //remove the piece
                    if (beta <= alpha) break;
                }
                return beta;
            }
        }


    }

    private Double heuristic_value(Tablero p, Integer jugador) {
        return 0.0;
    }

    private Vector<Casilla> moves_available(Tablero p) {
        Vector<Casilla> moves_list = new Vector<Casilla>();

        for (int i = 0; i < p.getTamano(); i++) {
            for (int j = 0; j < p.getTamano(); j++) {
                if (p.getNumJugadorCasilla(i, j) == null) moves_list.add(new Casilla(i, j));
            }
        }

        return moves_list;
    }
}