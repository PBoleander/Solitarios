package juegos.controlMovimientos;

import juegos.registro.Movimiento;
import juegos.registro.Registro;
import naipes.Monto;
import naipes.Naipe;

import java.util.ArrayList;

public class ControlMovimientosB extends ControlMovimientos {

    private final ArrayList<Naipe> naipesQueVanAInferiores;
    private final Monto montoReserva, montoManoPorSacar, montoManoSacado;
    private final Monto[] montosInferiores;

    private boolean marcadoInferior;

    public ControlMovimientosB(Monto[] montosInferiores, Monto montoReserva, Monto montoManoPorSacar,
                               Monto montoManoSacado, Registro registro) {
        super(registro);

        this.montosInferiores = montosInferiores;
        this.montoReserva = montoReserva;
        this.montoManoPorSacar = montoManoPorSacar;
        this.montoManoSacado = montoManoSacado;

        this.naipesQueVanAInferiores = new ArrayList<>();
    }

    public void actualizarMovimientosPosibles() {
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

    public boolean colocarNaipeSeleccionadoEn(Monto monto) {
        boolean colocado = super.colocarNaipeSeleccionadoEn(monto);

        if (colocado) actualizarMovimientosPosibles();

        return colocado;
    }

    @Override
    boolean sePuedeColocar(Naipe naipe, Monto monto) {
        if (naipe != null && monto != null) {
            if (!monto.equals(montoReserva) && !monto.equals(montoManoPorSacar) && !monto.equals(montoManoSacado)) {
                // Si está vacío
                if (monto.getNumNaipes() == 0) return true;
                // Si no lo está, el palo ha de ser distinto y el valor inmediatamente inferior a la última carta
                else {
                    Naipe ultimoNaipeMonto = monto.getUltimoNaipe();
                    return !ultimoNaipeMonto.getPalo().equals(naipe.getPalo()) &&
                            sonValoresConsecutivos(ultimoNaipeMonto.getValor(), naipe.getValor());
                }
            }
        }
        return false;
    }

    private void addMontoYONaipeAArrays(ArrayList<Naipe> naipes, Monto montoOrigen, Monto montoDestino) {
        Naipe naipe = montoOrigen.getUltimoNaipe();

        if (sePuedeColocar(naipe, montoDestino)) {
            naipes.add(naipe);
        }
    }

    private void identificarNaipesQueVanInferiores() {
        vaciarLosQuePuedenIrInferiores();

        for (Monto montoInferior: montosInferiores) {
            if (montoInferior.getNumNaipes() > 0) { // No tiene sentido marcar todos los montos

                addMontoYONaipeAArrays(naipesQueVanAInferiores, montoManoSacado, montoInferior);
                addMontoYONaipeAArrays(naipesQueVanAInferiores, montoReserva, montoInferior);

                for (Monto montoInferior2 : montosInferiores)
                    addMontoYONaipeAArrays(naipesQueVanAInferiores, montoInferior2, montoInferior);
            }
        }
    }

    private void marcarNaipesInferiorPosibles(boolean marcado) {
        for (Naipe naipe: naipesQueVanAInferiores) {
            naipe.setMarcadoInferior(marcado);
        }
    }

    private void vaciarLosQuePuedenIrInferiores() {
        if (!naipesQueVanAInferiores.isEmpty()) {
            marcarNaipesInferiorPosibles(false);
            naipesQueVanAInferiores.clear();
        }
    }
}
