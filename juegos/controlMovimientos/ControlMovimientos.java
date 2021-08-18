package juegos.controlMovimientos;

import juegos.registro.Movimiento;
import juegos.registro.Registro;
import naipes.Monto;
import naipes.Naipe;

abstract class ControlMovimientos {

    final Registro registro;

    ControlMovimientos(Registro registro) {
        this.registro = registro;
    }

    abstract boolean sePuedeColocar(Naipe naipe, Monto monto);

    boolean colocarNaipeSeleccionadoEn(Monto monto) {
        if (Monto.montoSeleccionado != null) {
            Monto montoSeleccionado = Monto.montoSeleccionado; // Variable necesaria (mirar comentarios siguientes)
            if (sePuedeColocar(montoSeleccionado.getUltimoNaipe(), monto)) {
                montoSeleccionado.cambiarSeleccion(); // Cuando el naipe se ha movido ya no se puede cambiar

                monto.meter(montoSeleccionado.cogerNaipe()); // Si aquí usáramos Monto.montoSeleccionado
                // directamente ya sería null por la línea anterior

                registro.registrar(new Movimiento(montoSeleccionado, monto)); // Se guarda el movimiento

                return true;
            }
        }

        return false;
    }

    // Devuelve si dos valores pasados por parámetro son consecutivos
    boolean sonValoresConsecutivos(Naipe.valor valorMayor, Naipe.valor valorMenor) {
        Naipe.valor[] valores = Naipe.valor.values();
        // Índices que ocupan el valor mayor y el menor en el array valores
        int iMayor = 0;
        int iMenor = 0;

        while (valorMayor != valores[iMayor]) iMayor++;
        while (valorMenor != valores[iMenor]) iMenor++;

        // Si los índices son consecutivos en el array, también lo son los naipes
        return iMayor == iMenor + 1 || (iMayor == 0 && iMenor == valores.length - 1);
    }
}
