package juegos.solitarioA;

import juegos.PanelControl;
import juegos.Solitario;
import naipes.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SolitarioA extends Solitario {

    private final Baraja baraja;
    private final ControlMovimientosA controlMovimientos;
    private final Monto[] montosInferiores, montosSuperiores;
    private final Monto montoReserva10, montoManoPorSacar, montoManoSacado;
    private final VisorMensajesA visorMensajes;

    private Component componenteBajoPuntero;

    private int numPartidas;

    public SolitarioA() {
        super(new GridBagLayout());

        this.baraja = new Baraja();

        this.numPartidas = 0;

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
                montoManoPorSacar, montoManoSacado, super.getRegistro());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        add(new PanelControl(this), c);

        c.fill = GridBagConstraints.BOTH; // Para que visorMensajes ocupe toda la ventana
        add(visorMensajes = new VisorMensajesA(), c);
        add(new VisorMontosA(montosInferiores, montosSuperiores, montoReserva10, montoManoPorSacar, montoManoSacado), c);

        setBackground(new Color(0, 150, 0));
    }

    /*
    Se encarga de:
        - Doble clic
        - Clic en montoManoPorSacar
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (componenteBajoPuntero instanceof Monto) {
            int numClics = mouseEvent.getClickCount();
            
            if (numClics > 0 && numClics % 2 == 0) { // Doble clic (intenta subir carta a montos superiores)
                if (componenteBajoPuntero != montoManoPorSacar) {
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

            } else if (numClics == 1 && componenteBajoPuntero == montoManoPorSacar) { // Clic sencillo
                if (Monto.montoSeleccionado != null) Monto.montoSeleccionado.cambiarSeleccion();

                controlMovimientos.pasarEntreMontosMano();

                revalidate();
                repaint();
            }
        }
    }

    /*
    Se encarga de:
        - Clics en cualquier monto que no sea montoManoPorSacar
            -- Intenta colocar en monto clicado el naipeEnMano (si lo hay)
            -- Si no, selecciona/deselecciona el monto clicado
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        // No debe interferir con el doble clic (así se evita que se deseleccione el monto doble clicado)
        if (mouseEvent.getClickCount() < 2 && componenteBajoPuntero instanceof Monto) {
            Monto monto = (Monto) componenteBajoPuntero;
            if (monto != montoManoPorSacar) {
                if (!controlMovimientos.colocarNaipeSeleccionadoEn(monto)) // Intenta colocar el naipe seleccionado (si
                    // existe) en el monto clicado
                    monto.cambiarSeleccion(); // Si no puede, cambia la selección del monto
            }

            revalidate();
            repaint();
        }
    }

    /*
    Se encarga de:
        - El fin de un arrastre (intenta colocar el naipe seleccionado hasta el monto destino)
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) { // Después de arrastrar (clickCount = 0)
        if (mouseEvent.getClickCount() == 0 && componenteBajoPuntero instanceof Monto) {
            Monto monto = (Monto) componenteBajoPuntero;
            colocarNaipeSeleccionadoEn(monto);

            revalidate();
            repaint();
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
    protected void deshacerMovimiento() {
        super.deshacerMovimiento();

        controlMovimientos.identificarNaipesQuePuedenSubir();
        controlMovimientos.identificarNaipesQueVanInferiores();
    }

    @Override
    protected void rehacerMovimiento() {
        super.rehacerMovimiento();

        controlMovimientos.identificarNaipesQuePuedenSubir();
        controlMovimientos.identificarNaipesQueVanInferiores();
    }

    @Override
    protected void iniciar(boolean reinicio) {
        if (!isVictoria()) visorMensajes.setNumPartidas(numPartidas++); // Se incrementa después porque las partidas
        // se cuentan cuando acaban

        // Se vacían todos los montos
        for (Monto montoSuperior: montosSuperiores) montoSuperior.clear();
        for (Monto montoInferior: montosInferiores) montoInferior.clear();
        montoManoPorSacar.clear();
        montoManoSacado.clear();
        montoReserva10.clear();

        super.getRegistro().vaciar(); // Se vacía el registro

        visorMensajes.setVictoria(false); // Quita el mensaje de victoria si estaba

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

        controlMovimientos.identificarNaipesQuePuedenSubir();
        controlMovimientos.identificarNaipesQueVanInferiores();
    }

    @Override
    protected boolean isVictoria() {
        int sumaNaipesSuperiores = 0;

        for (Monto montoSuperior: montosSuperiores)
            sumaNaipesSuperiores += montoSuperior.getNumNaipes();

        return sumaNaipesSuperiores == Naipe.valor.values().length * Naipe.palo.values().length;
    }

    @Override
    protected void setMarcadoInferior(boolean marcadoInferior) {
        controlMovimientos.setMarcadoInferior(marcadoInferior);
    }

    @Override
    protected void setMarcadoSuperior(boolean marcadoSuperior) {
        controlMovimientos.setMarcadoSuperior(marcadoSuperior);
    }


    private boolean colocarNaipeSeleccionadoEn(Monto monto) {
        boolean seHaPodidoColocar = controlMovimientos.colocarNaipeSeleccionadoEn(monto);
        if (seHaPodidoColocar && isVictoria()) pintarVictoria();

        return seHaPodidoColocar;
    }

    private void pintarVictoria() {
        visorMensajes.setVictoria(true); // Si se ha ganado, se muestra un mensaje
        visorMensajes.setNumPartidas(numPartidas++);
    }
}
