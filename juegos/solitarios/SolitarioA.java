package juegos.solitarios;

import juegos.controlMovimientos.ControlMovimientosA;
import juegos.panelControl.PanelControlA;
import juegos.visoresMensajes.VisorMensajesA;
import juegos.visoresMontos.VisorMontosA;
import naipes.Monto;
import naipes.Naipe;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SolitarioA extends Solitario {

    private final ControlMovimientosA controlMovimientos;
    private final Monto[] montosInferiores, montosSuperiores;
    private final Monto montoReserva10, montoManoPorSacar, montoManoSacado;
    private final VisorMensajesA visorMensajes;

    private Component componenteBajoPuntero;

    public SolitarioA() {
        super(new GridBagLayout());

        this.montosInferiores = new Monto[4];
        this.montosSuperiores = new Monto[4];

        for (int i = 0; i < montosInferiores.length; i++) {
            montosInferiores[i] = new Monto(true, false, 11);
            montosInferiores[i].addMouseListener(this);
        }
        for (int i = 0; i < montosSuperiores.length; i++) {
            montosSuperiores[i] = new Monto(false, false, 12);
            montosSuperiores[i].addMouseListener(this);
        }

        this.montoManoPorSacar = new Monto(false, true, 33);
        montoManoPorSacar.addMouseListener(this);

        this.montoManoSacado = new Monto(false, false, 33);
        montoManoSacado.addMouseListener(this);

        this.montoReserva10 = new Monto(false, false, 10);
        montoReserva10.addMouseListener(this);

        this.controlMovimientos = new ControlMovimientosA(montosInferiores, montosSuperiores, montoReserva10,
                montoManoPorSacar, montoManoSacado, registro);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        add(new PanelControlA(this), c);

        c.fill = GridBagConstraints.BOTH; // Para que visorMensajes ocupe toda la ventana
        add(visorMensajes = new VisorMensajesA(), c);
        add(new VisorMontosA(montosInferiores, montosSuperiores, montoReserva10, montoManoPorSacar, montoManoSacado), c);

        iniciar(false); // Empieza la partida
    }

    /*
    Se encarga de:
        - Doble clic (tanto en botón izquierdo como derecho): subir una carta/todas las cartas posibles, respectivamente
        - Clic en montoManoPorSacar
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (componenteBajoPuntero instanceof Monto) {
            int numClics = mouseEvent.getClickCount();
            int boton = mouseEvent.getButton();

            if (!isVictoria()) {

                if (boton == MouseEvent.BUTTON3 && numClics == 2) { // Doble clic secundario
                    reiniciarMontoSeleccionado(); // Evitará que un naipe seleccionado anteriormente se quede
                    // seleccionado

                    controlMovimientos.subirTodosNaipesPosibles();

                    if (isVictoria()) pintarVictoria();

                    revalidate();
                    repaint();

                } else if (boton == MouseEvent.BUTTON1) { // Clic izquierdo

                    if (numClics > 0 && numClics % 2 == 0) { // Doble clic (intenta subir carta a montos superiores)
                        if (!componenteBajoPuntero.equals(montoManoPorSacar)) {
                            Monto monto = (Monto) componenteBajoPuntero;
                            if (Monto.montoSeleccionado == null) // En teoría no debería suceder pero por si acaso
                                monto.cambiarSeleccion(); // Para que colocar pueda hacer la comprobación correctamente

                            /* Prueba si algún monto superior acepta el naipe seleccionado */
                            int i = 0;
                            while (i < montosSuperiores.length && !colocarNaipeSeleccionadoEn(montosSuperiores[i]))
                                i++;

                            revalidate();
                            repaint();
                        }

                    } else if (numClics == 1 && componenteBajoPuntero.equals(montoManoPorSacar)) { // Clic sencillo
                        reiniciarMontoSeleccionado();

                        controlMovimientos.pasarEntreMontosMano();

                        revalidate();
                        repaint();
                    }
                }
            }
        }
    }

    /*
    Se encarga de:
        - Clics en cualquier monto que no sea montoManoPorSacar
            -- Intenta colocar en monto clicado el naipe seleccionado (si lo hay)
            -- Si no, selecciona/deselecciona el monto clicado
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        // No debe interferir con el doble clic (así se evita que se deseleccione el monto doble clicado)
        if (!isVictoria() && mouseEvent.getButton() == MouseEvent.BUTTON1) {

            if (mouseEvent.getClickCount() < 2 && componenteBajoPuntero instanceof Monto) {
                Monto monto = (Monto) componenteBajoPuntero;
                if (!monto.equals(montoManoPorSacar)) {
                    if (!controlMovimientos.colocarNaipeSeleccionadoEn(monto)) // Intenta colocar el naipe seleccionado
                        // (si existe) en el monto clicado
                        monto.cambiarSeleccion(); // Si no puede, cambia la selección del monto
                }

                revalidate();
                repaint();
            }
        }
    }

    /*
    Se encarga de:
        - El fin de un arrastre (intenta colocar el naipe seleccionado hasta el monto destino)
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) { // Después de arrastrar (clickCount = 0)
        if (!isVictoria() && mouseEvent.getButton() == MouseEvent.BUTTON1) {

            if (mouseEvent.getClickCount() == 0 && componenteBajoPuntero instanceof Monto) {
                Monto monto = (Monto) componenteBajoPuntero;
                colocarNaipeSeleccionadoEn(monto);

                revalidate();
                repaint();
            }
        }
    }

    /*
    mouseEntered y mouseExited se usan porque mouseReleased tiene el mismo source que mousePressed (por definición)
    Esta es una manera para saber sobre qué monto se deja de pulsar el ratón
    */
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
        for (Monto montoSuperior: montosSuperiores) montoSuperior.removeAll();
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

        Naipe naipeInicialSuperior = baraja.cogerNaipe();
        controlMovimientos.setNaipeInicialSuperior(naipeInicialSuperior);
        visorMensajes.setValorNaipeSuperiorInicial(naipeInicialSuperior.getValor());
        montosSuperiores[0].meter(naipeInicialSuperior);

        while (baraja.getNumNaipes() > 0) montoManoPorSacar.meter(baraja.cogerNaipe());

        controlMovimientos.actualizarMovimientosPosibles();
    }

    @Override
    public boolean isVictoria() {
        int sumaNaipesSuperiores = 0;

        for (Monto montoSuperior: montosSuperiores)
            sumaNaipesSuperiores += montoSuperior.getNumNaipes();

        return sumaNaipesSuperiores == Naipe.valor.values().length * Naipe.palo.values().length;
    }

    @Override
    public void rehacerMovimiento() {
        saltarEnHistorialCorregido(false);
    }

    @Override
    public void setMarcadoInferior(boolean marcadoInferior) {
        controlMovimientos.setMarcadoInferior(marcadoInferior);
    }

    @Override
    public void setMarcadoSuperior(boolean marcadoSuperior) {
        controlMovimientos.setMarcadoSuperior(marcadoSuperior);
    }

    private boolean colocarNaipeSeleccionadoEn(Monto monto) {
        boolean seHaPodidoColocar = controlMovimientos.colocarNaipeSeleccionadoEn(monto);
        if (seHaPodidoColocar && isVictoria()) pintarVictoria();

        return seHaPodidoColocar;
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
