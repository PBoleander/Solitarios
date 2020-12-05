package juegos.solitarioA;

import juegos.PanelControl;
import juegos.Solitario;
import juegos.registro.Movimiento;
import juegos.registro.Registro;
import naipes.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SolitarioA extends Solitario {

    private final Baraja baraja;
    private final Registro registro;
    private final Monto[] montosInferiores, montosSuperiores;
    private final Monto montoReserva10, montoManoPorSacar, montoManoSacado;
    private final VisorMensajesA visorMensajes;

    private Component componenteBajoPuntero;
    private Naipe naipeInicialSuperior;

    private int numPartidas;

    public SolitarioA() {
        super(new GridBagLayout());

        this.baraja = new Baraja();
        this.registro = super.getRegistro();

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
                if (!colocarNaipeSeleccionadoEn(monto)) // Intenta colocar el naipeEnMano (si existe) en el monto clicado
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
    protected void iniciar(boolean reinicio) {
        if (!isVictoria()) visorMensajes.setNumPartidas(numPartidas++); // Se incrementa después porque las partidas
        // se cuentan cuando acaban

        // Se vacían todos los montos
        for (Monto montoSuperior: montosSuperiores) montoSuperior.clear();
        for (Monto montoInferior: montosInferiores) montoInferior.clear();
        montoManoPorSacar.clear();
        montoManoSacado.clear();
        montoReserva10.clear();

        registro.vaciar(); // Se vacía el registro

        visorMensajes.setVictoria(false); // Quita el mensaje de victoria si estaba

        if (reinicio) baraja.recuperar();
        else baraja.barajar();

        /* Se rellenan los montos con los naipes de inicio */
        while (montoReserva10.getNumNaipes() != 10) montoReserva10.meter(baraja.cogerNaipe());

        for (Monto monto: montosInferiores) monto.meter(baraja.cogerNaipe());

        montosSuperiores[0].meter(naipeInicialSuperior = baraja.cogerNaipe());

        while (baraja.getNumNaipes() > 0) montoManoPorSacar.meter(baraja.cogerNaipe());
    }

    @Override
    protected boolean isVictoria() {
        int sumaNaipesSuperiores = 0;

        for (Monto montoSuperior: montosSuperiores)
            sumaNaipesSuperiores += montoSuperior.getNumNaipes();

        return sumaNaipesSuperiores == Naipe.valor.values().length * Naipe.palo.values().length;
    }

    @Override
    // Coloca, si puede, el naipe seleccionado (si existe) en monto y devuelve si lo ha conseguido
    protected boolean colocarNaipeSeleccionadoEn(Monto monto) {
        if (monto != null && Monto.montoSeleccionado != null) {
            if (monto != montoReserva10 && monto != montoManoPorSacar && monto != montoManoSacado) {
                Monto montoSeleccionado = Monto.montoSeleccionado; // Variable necesaria (mirar comentarios siguientes)
                if (sePuedeColocar(montoSeleccionado.getUltimoNaipe(), monto)) {
                    montoSeleccionado.cambiarSeleccion(); // Cuando el naipe se ha movido ya no se puede cambiar
                    monto.meter(montoSeleccionado.cogerNaipe()); // Si aquí usáramos Monto.montoSeleccionado
                    // directamente ya sería null por la línea anterior

                    registro.registrar(new Movimiento(montoSeleccionado, monto)); // Se guarda el movimiento

                    if (isVictoria()) {
                        visorMensajes.setVictoria(true); // Si se ha ganado, se muestra un mensaje
                        visorMensajes.setNumPartidas(numPartidas++);
                    }

                    return true;
                }
            }
        }
        return false;
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

            // Si el monto no es superior, el monto ha de estar vacío o la carta a poner debe ser de distinto palo y
            // con un valor inmediatamente inferior al de la última carta de ese monto
        } else return (monto.getNumNaipes() == 0 || (naipe.getPalo() != monto.getUltimoNaipe().getPalo() &&
                sonValoresConsecutivos(monto.getUltimoNaipe().getValor(), naipe.getValor())));
    }
}
