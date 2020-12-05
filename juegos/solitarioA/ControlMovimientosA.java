package juegos.solitarioA;

import juegos.registro.Movimiento;
import juegos.registro.Registro;
import naipes.Monto;
import naipes.Naipe;

class ControlMovimientosA {

    private final Monto montoReserva, montoManoPorSacar, montoManoSacado;
    private final Monto[] montosSuperiores;
    private final Registro registro;

    private Naipe naipeInicialSuperior;

    ControlMovimientosA(Monto[] montosSuperiores, Monto montoReserva, Monto montoManoPorSacar, Monto montoManoSacado,
                        Registro registro) {
        this.montosSuperiores = montosSuperiores;
        this.montoReserva = montoReserva;
        this.montoManoPorSacar = montoManoPorSacar;
        this.montoManoSacado = montoManoSacado;

        this.registro = registro;
    }

    void setNaipeInicialSuperior(Naipe naipeInicialSuperior) {
        this.naipeInicialSuperior = naipeInicialSuperior;
    }

    boolean colocarNaipeSeleccionadoEn(Monto monto) {
        if (monto != null && Monto.montoSeleccionado != null) {
            if (monto != montoReserva && monto != montoManoPorSacar && monto != montoManoSacado) {
                Monto montoSeleccionado = Monto.montoSeleccionado; // Variable necesaria (mirar comentarios siguientes)
                if (sePuedeColocar(montoSeleccionado.getUltimoNaipe(), monto)) {
                    montoSeleccionado.cambiarSeleccion(); // Cuando el naipe se ha movido ya no se puede cambiar
                    monto.meter(montoSeleccionado.cogerNaipe()); // Si aquí usáramos Monto.montoSeleccionado
                    // directamente ya sería null por la línea anterior

                    registro.registrar(new Movimiento(montoSeleccionado, monto)); // Se guarda el movimiento

                    return true;
                }
            }
        }
        return false;
    }

    void pasarEntreMontosMano() {
        int numNaipes = 0;
        Naipe naipe = montoManoPorSacar.getUltimoNaipe();
        if (naipe == null) { // Devuelve todas las cartas de nuevo al montoManoPorSacar
            while ((naipe = montoManoSacado.cogerNaipe()) != null) {
                if (montoManoPorSacar.meter(naipe)) numNaipes++;
            }
            // Registra el movimiento
            registro.registrar(new Movimiento(montoManoSacado, montoManoPorSacar, numNaipes));
        }

        // Pasa tres (o los que haya) naipes de montoManoPorSacar a montoManoSacado
        numNaipes = 0;
        for (int i = 0; i < 3; i++) {
            naipe = montoManoPorSacar.cogerNaipe();
            if (montoManoSacado.meter(naipe)) numNaipes++;
        }
        // Registra el movimiento
        registro.registrar(new Movimiento(montoManoPorSacar, montoManoSacado, numNaipes));
    }

    // Indica si monto es uno de los superiores
    private boolean esMontoSuperior(Monto monto) {
        for (Monto montoSuperior: montosSuperiores) {
            if (monto == montoSuperior) return true;
        }
        return false;
    }

    // Indica si naipe se puede colocar en monto
    private boolean sePuedeColocar(Naipe naipe, Monto monto) {
        if (esMontoSuperior(monto)) {
            if (monto.getNumNaipes() == 0) { // En monto superior vacío sólo puede ir carta con valor igual al del
                // primer naipe que se ha colocado en el primer monto superior al inicio de la partida
                return (naipe.getValor() == naipeInicialSuperior.getValor());

                // Si el monto superior está ocupado, ha de ser del mismo palo y con valor = valor de la última carta
                // + 1
            } else return (naipe.getPalo() == monto.getUltimoNaipe().getPalo() &&
                    sonValoresConsecutivos(naipe.getValor(), monto.getUltimoNaipe().getValor()));

        } else { // El monto no es superior
            // Cuando la carta sea la primera que puede ocupar monto superior no se debe poder meter, ni otra carta
            // ponerse sobre ésta.
            if ((monto.getNumNaipes() > 0 && monto.getUltimoNaipe().getValor() == naipeInicialSuperior.getValor()) ||
                    naipe.getValor() == naipeInicialSuperior.getValor()) return false;

                // El monto ha de estar vacío o la carta a poner debe ser de distinto palo y con un valor inmediatamente
                // inferior al de la última carta de ese monto.
            else return (monto.getNumNaipes() == 0 || (naipe.getPalo() != monto.getUltimoNaipe().getPalo() &&
                    sonValoresConsecutivos(monto.getUltimoNaipe().getValor(), naipe.getValor())));
        }
    }

    // Devuelve si dos valores pasados por parámetro son consecutivos
    private boolean sonValoresConsecutivos(Naipe.valor valorMayor, Naipe.valor valorMenor) {
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
