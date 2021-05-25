package juegos.controlMovimientos;

import juegos.registro.Movimiento;
import juegos.registro.Registro;
import naipes.Monto;
import naipes.Naipe;

import java.util.ArrayList;

public class ControlMovimientosA extends ControlMovimientos {

    // Se requiere guardar estos montos para poder hacer automáticamente la subida de todos los naipes ya que, por
    // como está hecho el programa (a base de montos), no se puede subir naipes directamente sin complicar mucho el
    // código
    private final ArrayList<Monto> montosQuePuedenSubir;
    // Los naipes se usan para marcarlos/desmarcarlos de manera fácil y limpia (con montos sería más complicado)
    private final ArrayList<Naipe> naipesQuePuedenSubir, naipesQueVanAInferiores;
    private final Monto montoReserva, montoManoPorSacar, montoManoSacado;
    private final Monto[] montosInferiores, montosSuperiores;

    private boolean marcadoInferior, marcadoSuperior;
    private Naipe naipeInicialSuperior;

    public ControlMovimientosA(Monto[] montosInferiores, Monto[] montosSuperiores, Monto montoReserva,
                         Monto montoManoPorSacar, Monto montoManoSacado, Registro registro) {
        super(registro);

        this.montosInferiores = montosInferiores;
        this.montosSuperiores = montosSuperiores;
        this.montoReserva = montoReserva;
        this.montoManoPorSacar = montoManoPorSacar;
        this.montoManoSacado = montoManoSacado;

        this.montosQuePuedenSubir = new ArrayList<>();
        this.naipesQuePuedenSubir = new ArrayList<>();
        this.naipesQueVanAInferiores = new ArrayList<>();
    }

    public void actualizarMovimientosPosibles() {
        if (marcadoSuperior) {
            identificarNaipesQuePuedenSubir();
            marcarNaipesSubidaPosibles(true);
        }
        if (marcadoInferior) {
            identificarNaipesQueVanInferiores();
            marcarNaipesInferiorPosibles(true);
        }
    }

    public void setMarcadoInferior(boolean marcadoInferior) {
        this.marcadoInferior = marcadoInferior;

        if (marcadoInferior) {
            identificarNaipesQueVanInferiores();
            marcarNaipesInferiorPosibles(true);

        } else vaciarLosQuePuedenIrInferiores();
    }

    public void setMarcadoSuperior(boolean marcadoSuperior) {
        this.marcadoSuperior = marcadoSuperior;

        if (marcadoSuperior) {
            identificarNaipesQuePuedenSubir();
            marcarNaipesSubidaPosibles(true);

        } else vaciarLosQuePuedenSubir();
    }

    public void setNaipeInicialSuperior(Naipe naipeInicialSuperior) {
        this.naipeInicialSuperior = naipeInicialSuperior;
    }

    public void pasarEntreMontosMano() {
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

        actualizarMovimientosPosibles();
    }

    public void subirTodosNaipesPosibles() {
        while (!montosQuePuedenSubir.isEmpty()) {
            for (Monto monto: montosQuePuedenSubir) {
                subir(monto);
            }
            // Si el montoManoSacado se queda vacío sacará nuevas cartas
            if (montosQuePuedenSubir.contains(montoManoSacado)) {
                if (montoManoSacado.getNumNaipes() == 0) {
                    pasarEntreMontosMano();
                }
            }
            identificarNaipesQuePuedenSubir(); // Actualiza la lista para seguir subiendo naipes
        }

        if (marcadoInferior) identificarNaipesQueVanInferiores(); // Actualiza para marcar correctamente cómo ha
        // quedado la situación
    }

    public boolean colocarNaipeSeleccionadoEn(Monto monto) {
        boolean colocado = super.colocarNaipeSeleccionadoEn(monto);

        if (colocado) actualizarMovimientosPosibles();

        return colocado;
    }

    // Indica si naipe se puede colocar en monto
    @Override
    boolean sePuedeColocar(Naipe naipe, Monto monto) {
        if (naipe != null && monto != null) {
            if (!monto.equals(montoReserva) && !monto.equals(montoManoPorSacar) && !monto.equals(montoManoSacado)) {
                Naipe ultimoNaipeMonto = monto.getUltimoNaipe();

                if (esMontoSuperior(monto)) {
                    if (monto.getNumNaipes() == 0) { // En monto superior vacío sólo puede ir carta con valor igual al
                        // del primer naipe que se ha colocado en el primer monto superior al inicio de la partida
                        return (naipe.getValor().equals(naipeInicialSuperior.getValor()));

                        // Si el monto superior está ocupado, ha de ser del mismo palo y con valor = valor de la última
                        // carta + 1
                    } else return (naipe.getPalo().equals(ultimoNaipeMonto.getPalo()) &&
                            sonValoresConsecutivos(naipe.getValor(), ultimoNaipeMonto.getValor()));

                } else { // El monto no es superior
                    // Cuando la carta sea la primera que puede ocupar monto superior no se debe poder meter, ni otra
                    // carta ponerse sobre ésta.
                    if ((monto.getNumNaipes() > 0 &&
                            ultimoNaipeMonto.getValor().equals(naipeInicialSuperior.getValor())) ||
                            naipe.getValor().equals(naipeInicialSuperior.getValor())) return false;

                        // El monto ha de estar vacío o la carta a poner debe ser de distinto palo y con un valor
                        // inmediatamente inferior al de la última carta de ese monto.
                    else return (monto.getNumNaipes() == 0 || (!naipe.getPalo().equals(ultimoNaipeMonto.getPalo()) &&
                            sonValoresConsecutivos(ultimoNaipeMonto.getValor(), naipe.getValor())));
                }
            }
        }
        return false;
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
            if (monto.equals(montoSuperior)) return true;
        }
        return false;
    }

    private void identificarNaipesQuePuedenSubir() {
        /*
        También se tienen que identificar los montos para poder realizar la operación de subida de naipes automática
         */
        vaciarLosQuePuedenSubir();

        for (Monto montoSuperior: montosSuperiores) {
            addMontoYONaipeAArrays(naipesQuePuedenSubir, montosQuePuedenSubir, montoManoSacado, montoSuperior);
            addMontoYONaipeAArrays(naipesQuePuedenSubir, montosQuePuedenSubir, montoReserva, montoSuperior);

            for (Monto montoInferior: montosInferiores)
                addMontoYONaipeAArrays(naipesQuePuedenSubir, montosQuePuedenSubir, montoInferior, montoSuperior);
        }
    }

    private void identificarNaipesQueVanInferiores() {
        vaciarLosQuePuedenIrInferiores();

        for (Monto montoInferior: montosInferiores) {
            if (montoInferior.getNumNaipes() > 0) { // No tiene sentido marcar todos los montos

                addMontoYONaipeAArrays(naipesQueVanAInferiores, null, montoManoSacado, montoInferior);
                addMontoYONaipeAArrays(naipesQueVanAInferiores, null, montoReserva, montoInferior);

                for (Monto montoInferior2 : montosInferiores)
                    addMontoYONaipeAArrays(naipesQueVanAInferiores, null, montoInferior2, montoInferior);
            }
        }
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

    private void subir(Monto monto) {
        for (Monto montoSuperior: montosSuperiores) {
            if (sePuedeColocar(monto.getUltimoNaipe(), montoSuperior)) {
                montoSuperior.meter(monto.cogerNaipe());
                registro.registrar(new Movimiento(monto, montoSuperior));
                return;
            }
        }
    }

    private void vaciarLosQuePuedenIrInferiores() {
        if (!naipesQueVanAInferiores.isEmpty()) {
            marcarNaipesInferiorPosibles(false);
            naipesQueVanAInferiores.clear();
        }
    }

    private void vaciarLosQuePuedenSubir() {
        if (!naipesQuePuedenSubir.isEmpty()) {
            marcarNaipesSubidaPosibles(false);
            montosQuePuedenSubir.clear();
            naipesQuePuedenSubir.clear();
        }
    }
}
