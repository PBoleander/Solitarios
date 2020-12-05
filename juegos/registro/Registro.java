package juegos.registro;

import java.util.ArrayList;

public class Registro {

    private final ArrayList<Movimiento> movimientos;
    private int indiceActual;

    public Registro() {
        this.movimientos = new ArrayList<>();
        this.indiceActual = -1;
    }

    public Movimiento getMovimientoAnterior() {
        // Al ser índiceActual el último índice de movimientos visible, se ha de hacer un post-decremento
        if (indiceActual == -1) return null;
        else return movimientos.get(indiceActual--);
    }

    public Movimiento getMovimientoPosterior() {
        // Aquí, a diferencia de getMovimientoAnterior, se necesita pre-incrementar para obtener el movimiento posterior
        if (indiceActual == movimientos.size() - 1) return null;
        else return movimientos.get(++indiceActual);
    }

    public void registrar(Movimiento movimiento) {
        // Si se registra nuevo movimiento, se borran movimientos posteriores a posición actual en el historial
        if (indiceActual < movimientos.size() - 1) {
            while (movimientos.size() - 1 > indiceActual)
                movimientos.remove(movimientos.size() - 1);
        }

        movimientos.add(movimiento);
        indiceActual = movimientos.size() - 1;
    }

    public void vaciar() {
        if (!movimientos.isEmpty()) {
            movimientos.clear();
            indiceActual = -1;
        }
    }
}
