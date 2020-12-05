package juegos;

import juegos.registro.Movimiento;
import juegos.registro.Registro;
import naipes.Monto;
import naipes.Naipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public abstract class Solitario extends JPanel implements MouseListener {

    private final Registro registro;

    public Solitario(LayoutManager layoutManager) {
        super(layoutManager);

        this.registro = new Registro();
    }

    protected Registro getRegistro() {
        return registro;
    }

    protected abstract void iniciar(boolean reinicio);
    protected abstract boolean isVictoria();
    // Coloca, si puede, el naipe seleccionado (si existe) en monto y devuelve si lo ha conseguido
    protected abstract boolean colocarNaipeSeleccionadoEn(Monto monto);


    void deshacerMovimiento() {
        // Si hay monto seleccionado, se quita antes de proceder
        if (Monto.montoSeleccionado != null) Monto.montoSeleccionado.cambiarSeleccion();

        Movimiento movimiento = registro.getMovimientoAnterior();
        if (movimiento != null) {
            Monto montoOrigen = movimiento.getMontoOrigen();
            Monto montoDestino = movimiento.getMontoDestino();
            int numNaipes = movimiento.getNumeroNaipes();

            int i = 0;
            while (i < numNaipes) {
                montoOrigen.meter(montoDestino.cogerNaipe());
                i++;
            }
        }
    }

    void rehacerMovimiento() {
        // Si hay monto seleccionado, se quita antes de proceder
        if (Monto.montoSeleccionado != null) Monto.montoSeleccionado.cambiarSeleccion();

        Movimiento movimiento = registro.getMovimientoPosterior();
        if (movimiento != null) {
            Monto montoOrigen = movimiento.getMontoOrigen();
            Monto montoDestino = movimiento.getMontoDestino();
            int numNaipes = movimiento.getNumeroNaipes();

            int i = 0;
            while (i < numNaipes) {
                montoDestino.meter(montoOrigen.cogerNaipe());
                i++;
            }
        }
    }

    // Devuelve si dos valores pasados por parámetro son consecutivos
    protected boolean sonValoresConsecutivos(Naipe.valor valorMayor, Naipe.valor valorMenor) {
        Naipe.valor[] valores = Naipe.valor.values();
        // Índices que ocupan el valor mayor y el menor en el array valores
        int iMayor = 0;
        int iMenor = 0;

        while (valorMayor != valores[iMayor] || valorMenor != valores[iMenor]) {
            if (valorMayor != valores[iMayor]) iMayor++;
            if (valorMenor != valores[iMenor]) iMenor++;
        }

        // Si los índices son consecutivos en el array, también lo son los naipes
        return iMayor == iMenor + 1 || (iMayor == 0 && iMenor == valores.length - 1);
    }
}
