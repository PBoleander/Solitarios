package juegos.registro;

import naipes.Monto;

public class Movimiento {
    
    private final Monto montoOrigen, montoDestino;
    private final int numeroNaipes;

    public Movimiento(Monto montoOrigen, Monto montoDestino) {
        this(montoOrigen, montoDestino, 1);
    }

    public Movimiento(Monto montoOrigen, Monto montoDestino, int numeroNaipes) {
        this.montoOrigen = montoOrigen;
        this.montoDestino = montoDestino;
        this.numeroNaipes = numeroNaipes;
    }

    public Monto getMontoOrigen() {
        return montoOrigen;
    }

    public Monto getMontoDestino() {
        return montoDestino;
    }

    public int getNumeroNaipes() {
        return numeroNaipes;
    }
}
