package juegos.solitarioA;

import juegos.registro.Movimiento;
import juegos.registro.Registro;
import naipes.Monto;
import naipes.Naipe;

import java.util.ArrayList;

class ControlMovimientosA {

    // Se requiere guardar estos montos para poder hacer automáticamente la subida de todos los naipes ya que, por
    // como está hecho el programa (a base de montos), no se puede subir naipes directamente sin complicar mucho el
    // código
    private final ArrayList<Monto> montosQuePuedenSubir;
    // Los naipes se usan para marcarlos/desmarcarlos de manera fácil y limpia (con montos sería más complicado)
    private final ArrayList<Naipe> naipesQuePuedenSubir, naipesQueVanAInferiores;
    private final Monto montoReserva, montoManoPorSacar, montoManoSacado;
    private final Monto[] montosInferiores, montosSuperiores;
    private final Registro registro;

    private boolean marcadoInferior, marcadoSuperior;
    private Naipe naipeInicialSuperior;

    ControlMovimientosA(Monto[] montosInferiores, Monto[] montosSuperiores, Monto montoReserva, Monto montoManoPorSacar,
                        Monto montoManoSacado, Registro registro) {
        this.montosInferiores = montosInferiores;
        this.montosSuperiores = montosSuperiores;
        this.montoReserva = montoReserva;
        this.montoManoPorSacar = montoManoPorSacar;
        this.montoManoSacado = montoManoSacado;

        this.montosQuePuedenSubir = new ArrayList<>();
        this.naipesQuePuedenSubir = new ArrayList<>();
        this.naipesQueVanAInferiores = new ArrayList<>();

        this.registro = registro;
    }

    void setMarcadoInferior(boolean marcadoInferior) {
        this.marcadoInferior = marcadoInferior;
        marcarNaipesInferiorPosibles(marcadoInferior);
    }

    void setMarcadoSuperior(boolean marcadoSuperior) {
        this.marcadoSuperior = marcadoSuperior;
        marcarNaipesSubidaPosibles(marcadoSuperior);
    }

    void setNaipeInicialSuperior(Naipe naipeInicialSuperior) {
        this.naipeInicialSuperior = naipeInicialSuperior;
    }

    boolean colocarNaipeSeleccionadoEn(Monto monto) {
        if (Monto.montoSeleccionado != null) {
            Monto montoSeleccionado = Monto.montoSeleccionado; // Variable necesaria (mirar comentarios siguientes)
            if (sePuedeColocar(montoSeleccionado.getUltimoNaipe(), monto)) {
                montoSeleccionado.cambiarSeleccion(); // Cuando el naipe se ha movido ya no se puede cambiar

                monto.meter(montoSeleccionado.cogerNaipe()); // Si aquí usáramos Monto.montoSeleccionado
                // directamente ya sería null por la línea anterior

                registro.registrar(new Movimiento(montoSeleccionado, monto)); // Se guarda el movimiento

                identificarNaipesQuePuedenSubir();
                identificarNaipesQueVanInferiores();

                return true;
            }
        }

        return false;
    }

    void identificarNaipesQuePuedenSubir() {
        /*
        También se tienen que identificar los montos para poder realizar la operación de subida de naipes automática
         */
        if (!naipesQuePuedenSubir.isEmpty()) {
            marcarNaipesSubidaPosibles(false);
            montosQuePuedenSubir.clear();
            naipesQuePuedenSubir.clear();
        }

        for (Monto montoSuperior: montosSuperiores) {
            addMontoYONaipeAArrays(naipesQuePuedenSubir, montosQuePuedenSubir, montoManoSacado, montoSuperior);
            addMontoYONaipeAArrays(naipesQuePuedenSubir, montosQuePuedenSubir, montoReserva, montoSuperior);

            for (Monto montoInferior: montosInferiores)
                addMontoYONaipeAArrays(naipesQuePuedenSubir, montosQuePuedenSubir, montoInferior, montoSuperior);
        }

        marcarNaipesSubidaPosibles(marcadoSuperior);
    }

    void identificarNaipesQueVanInferiores() {
        if (!naipesQueVanAInferiores.isEmpty()) {
            marcarNaipesInferiorPosibles(false);
            naipesQueVanAInferiores.clear();
        }

        for (Monto montoInferior: montosInferiores) {
            if (montoInferior.getNumNaipes() > 0) { // No tiene sentido marcar todos los montos

                addMontoYONaipeAArrays(naipesQueVanAInferiores, null, montoManoSacado, montoInferior);
                addMontoYONaipeAArrays(naipesQueVanAInferiores, null, montoReserva, montoInferior);

                for (Monto montoInferior2 : montosInferiores)
                    addMontoYONaipeAArrays(naipesQueVanAInferiores, null, montoInferior2, montoInferior);
            }
        }

        marcarNaipesInferiorPosibles(marcadoInferior);
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

        identificarNaipesQuePuedenSubir();
        identificarNaipesQueVanInferiores();
    }

    void subirTodosNaipesPosibles() {
        while (!montosQuePuedenSubir.isEmpty()) {
            for (Monto monto: montosQuePuedenSubir) {
                subir(monto);
            }
            identificarNaipesQuePuedenSubir(); // Actualiza la lista para seguir subiendo naipes
        }

        identificarNaipesQueVanInferiores(); // Actualiza para marcar correctamente cómo ha quedado la situación
    }



    private void addMontoYONaipeAArrays(ArrayList<Naipe> naipes, ArrayList<Monto> montos, Monto montoOrigen,
                                        Monto montoDestino) {

        Naipe naipe = montoOrigen.getUltimoNaipe();

        if (sePuedeColocar(naipe, montoDestino)) {
            naipes.add(naipe);
            if (montos != null) montos.add(montoOrigen); // Esto no siempre se usará, viene bien que montos pueda ser
            // null
        }
    }

    // Indica si monto es uno de los superiores
    private boolean esMontoSuperior(Monto monto) {
        for (Monto montoSuperior: montosSuperiores) {
            if (monto == montoSuperior) return true;
        }
        return false;
    }

    private void marcarNaipesInferiorPosibles(boolean marcado) {
        for (Naipe naipe: naipesQueVanAInferiores) {
            naipe.setMarcadoInferior(marcado);
        }
    }

    private void marcarNaipesSubidaPosibles(boolean marcado) {
        for (Naipe naipe: naipesQuePuedenSubir) {
            naipe.setMarcadoSubida(marcado);
        }
    }

    // Indica si naipe se puede colocar en monto
    private boolean sePuedeColocar(Naipe naipe, Monto monto) {
        if (naipe != null && monto != null) {
            if (monto != montoReserva && monto != montoManoPorSacar && monto != montoManoSacado) {
                if (esMontoSuperior(monto)) {
                    if (monto.getNumNaipes() == 0) { // En monto superior vacío sólo puede ir carta con valor igual al
                        // del primer naipe que se ha colocado en el primer monto superior al inicio de la partida
                        return (naipe.getValor() == naipeInicialSuperior.getValor());

                        // Si el monto superior está ocupado, ha de ser del mismo palo y con valor = valor de la última
                        // carta + 1
                    } else return (naipe.getPalo() == monto.getUltimoNaipe().getPalo() &&
                            sonValoresConsecutivos(naipe.getValor(), monto.getUltimoNaipe().getValor()));

                } else { // El monto no es superior
                    // Cuando la carta sea la primera que puede ocupar monto superior no se debe poder meter, ni otra
                    // carta ponerse sobre ésta.
                    if ((monto.getNumNaipes() > 0 &&
                            monto.getUltimoNaipe().getValor() == naipeInicialSuperior.getValor()) ||
                            naipe.getValor() == naipeInicialSuperior.getValor()) return false;

                        // El monto ha de estar vacío o la carta a poner debe ser de distinto palo y con un valor
                        // inmediatamente inferior al de la última carta de ese monto.
                    else return (monto.getNumNaipes() == 0 || (naipe.getPalo() != monto.getUltimoNaipe().getPalo() &&
                            sonValoresConsecutivos(monto.getUltimoNaipe().getValor(), naipe.getValor())));
                }
            }
        }
        return false;
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

    private void subir(Monto monto) {
        for (Monto montoSuperior: montosSuperiores) {
            if (sePuedeColocar(monto.getUltimoNaipe(), montoSuperior)) {
                montoSuperior.meter(monto.cogerNaipe());
                registro.registrar(new Movimiento(monto, montoSuperior));
                return;
            }
        }
    }
}
