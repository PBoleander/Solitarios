package juegos.solitarios;

import juegos.controlMovimientos.ControlMovimientosB;
import juegos.panelControl.PanelControlB;
import juegos.visoresMensajes.VisorMensajesB;
import juegos.visoresMontos.VisorMontosB;
import naipes.Monto;
import naipes.Naipe;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SolitarioB extends Solitario {

    private final ControlMovimientosB controlMovimientos;
    private final Monto[] montosInferiores;
    private final Monto montoReserva10, montoManoPorSacar, montoManoSacado;
    private final VisorMensajesB visorMensajes;

    private Component componenteBajoPuntero;

    public SolitarioB() {
        super(new GridBagLayout());

        this.montosInferiores = new Monto[5];

        for (int i = 0; i < montosInferiores.length; i++) {
            // 27 naipes hacen que los solitarios A y B sean igual de altos
            montosInferiores[i] = new Monto(true, false, 27);
            montosInferiores[i].addMouseListener(this);
        }

        this.montoManoPorSacar = new Monto(false, true, 33);
        montoManoPorSacar.addMouseListener(this);

        this.montoManoSacado = new Monto(false, false, 33);
        montoManoSacado.addMouseListener(this);

        this.montoReserva10 = new Monto(false, false, 10);
        montoReserva10.addMouseListener(this);

        this.controlMovimientos = new ControlMovimientosB(montosInferiores, montoReserva10, montoManoPorSacar,
                montoManoSacado, registro);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        add(new PanelControlB(this), c);

        c.fill = GridBagConstraints.BOTH; // Para que visorMensajes ocupe toda la ventana
        add(visorMensajes = new VisorMensajesB(), c);
        add(new VisorMontosB(montosInferiores, montoReserva10, montoManoPorSacar, montoManoSacado), c);

        iniciar(false); // Empieza la partida
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (!isVictoria() && componenteBajoPuntero instanceof Monto) {

            if (mouseEvent.getClickCount() == 1 && componenteBajoPuntero.equals(montoManoPorSacar)) { // Clic sencillo
                reiniciarMontoSeleccionado();

                controlMovimientos.pasarEntreMontosMano();

                revalidate();
                repaint();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        // No debe interferir con el doble clic (así se evita que se deseleccione el monto doble clicado)
        if (!isVictoria() && mouseEvent.getButton() == MouseEvent.BUTTON1) {

            if (mouseEvent.getClickCount() < 2 && componenteBajoPuntero instanceof Monto) {
                Monto monto = (Monto) componenteBajoPuntero;
                if (!monto.equals(montoManoPorSacar)) {
                    if (controlMovimientos.colocarNaipeSeleccionadoEn(monto)) { // Intenta colocar el naipe seleccionado
                        // (si existe) en el monto clicado
                        if (isVictoria()) pintarVictoria();

                    } else
                        monto.cambiarSeleccion(); // Si no puede colocarlo, cambia la selección del monto
                }

                revalidate();
                repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (!isVictoria() && mouseEvent.getButton() == MouseEvent.BUTTON1) {

            if (mouseEvent.getClickCount() == 0 && componenteBajoPuntero instanceof Monto) {
                Monto monto = (Monto) componenteBajoPuntero;
                if (controlMovimientos.colocarNaipeSeleccionadoEn(monto) && isVictoria())
                    pintarVictoria();

                revalidate();
                repaint();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        componenteBajoPuntero = mouseEvent.getComponent();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        componenteBajoPuntero = null;
    }

    @Override
    public void deshacerMovimiento() {
        saltarEnHistorialCorregido(true);
    }

    @Override
    public void iniciar(boolean reinicio) {
        if (isVictoria()) visorMensajes.setVictoria(false); // Quita el mensaje de victoria si estaba
        else visorMensajes.setNumPartidas(numPartidas++); // Se incrementa después porque las partidas
        // se cuentan cuando acaban

        super.reiniciarMontoSeleccionado(); // Si hay monto seleccionado, lo deselecciona

        // Se vacían todos los montos
        for (Monto montoInferior: montosInferiores) montoInferior.removeAll();
        montoManoPorSacar.removeAll();
        montoManoSacado.removeAll();
        montoReserva10.removeAll();

        registro.vaciar(); // Se vacía el registro

        if (reinicio) baraja.recuperar();
        else baraja.barajar();

        /* Se rellenan los montos con los naipes de inicio */
        while (montoReserva10.getNumNaipes() != 10) montoReserva10.meter(baraja.cogerNaipe());

        for (Monto monto: montosInferiores) monto.meter(baraja.cogerNaipe());

        while (baraja.getNumNaipes() > 0) montoManoPorSacar.meter(baraja.cogerNaipe());

        controlMovimientos.actualizarMovimientosPosibles();
    }

    @Override
    public boolean isVictoria() {
        int sumaNaipes = 0;
        for (Monto montoInferior: montosInferiores) {
            sumaNaipes += montoInferior.getNumNaipes();
        }

        return sumaNaipes == Naipe.palo.values().length * Naipe.valor.values().length;
    }

    @Override
    public void rehacerMovimiento() {
        saltarEnHistorialCorregido(false);
    }

    @Override
    public void setMarcadoInferior(boolean marcadoInferior) {
        controlMovimientos.setMarcadoInferior(marcadoInferior);
    }

    // En este solitario no sirve de nada
    @Override
    public void setMarcadoSuperior(boolean marcadoSuperior) {

    }

    // Devuelve si el monto de mano sacado ha llegado a 0 además de haberse provocado por un pase de naipes entre los
    // dos montos de mano
    private boolean montoManoSacadoHaLlegadoAlFinal(int numNaipesMontoPorSacarAntes, int numNaipesMontoSacadoAntes) {
        int numNaipesMontoPorSacarActual = montoManoPorSacar.getNumNaipes();
        int numNaipesMontoSacadoActual = montoManoSacado.getNumNaipes();

        return (numNaipesMontoPorSacarActual != numNaipesMontoPorSacarAntes &&
                numNaipesMontoSacadoActual != numNaipesMontoSacadoAntes &&
                numNaipesMontoSacadoActual == 0);
    }

    private void pintarVictoria() {
        visorMensajes.setVictoria(true); // Si se ha ganado, se muestra un mensaje
        visorMensajes.setNumPartidas(numPartidas++);
    }

    // Se realiza el salto en el historial de movimientos propiamente dicho
    private void saltarEnHistorial(boolean haciaAtras) {
        if (haciaAtras) super.deshacerMovimiento();
        else super.rehacerMovimiento();
    }

    // Realiza el salto en el historial de movimientos además de no dejar el monto de mano sacado a 0 si eso ha sido
    // provocado por un pase de cartas entre los dos montos de mano
    private void saltarEnHistorialCorregido(boolean haciaAtras) {
        int numNaipesMontoPorSacarAntes = montoManoPorSacar.getNumNaipes();
        int numNaipesMontoSacadoAntes = montoManoSacado.getNumNaipes();

        saltarEnHistorial(haciaAtras);

        // Si el monto de mano sacado llega al final (naipes = 0), se procede a volver a saltar en el historial en el
        // mismo sentido, siempre y cuando eso haya sido provocado por un pase de cartas entre los montos de mano
        if (montoManoSacadoHaLlegadoAlFinal(numNaipesMontoPorSacarAntes, numNaipesMontoSacadoAntes)) {
            saltarEnHistorial(haciaAtras);
        }

        controlMovimientos.actualizarMovimientosPosibles();
    }
}
