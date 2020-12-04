package juegos.registro;

import java.util.ArrayList;

public class Registro {

    private final ArrayList<Movimiento> movimientos;

    public Registro() {
        this.movimientos = new ArrayList<>();
    }

    public Movimiento quitarUltimoMovimiento() {
        if (movimientos.isEmpty()) return null;
        else return movimientos.remove(movimientos.size() - 1);
    }

    public void registrar(Movimiento movimiento) {
        movimientos.add(movimiento);
    }

    public void vaciar() {
        if (!movimientos.isEmpty()) movimientos.clear();
    }
}
