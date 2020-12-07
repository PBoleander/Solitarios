package naipes;

import javax.swing.*;
import java.awt.*;

public class Monto extends JLayeredPane {

    /* Píxeles mostrados de un naipe que no está arriba del monto según su visibilidad */
    public static final int VGAP_INVISIBLE = 2;
    public static final int VGAP_VISIBLE = 20;

    public static Monto montoSeleccionado = null; // Sólo puede haber un monto seleccionado como máximo

    private static final Color VERDE_OSCURO = new Color(0, 100, 0);

    private final boolean bocaAbajo;
    private final int gap;

    public Monto(boolean todosNaipesIdentificables, boolean bocaAbajo, int maxNaipes) {
        super();

        this.bocaAbajo = bocaAbajo;
        this.gap = todosNaipesIdentificables ? VGAP_VISIBLE : VGAP_INVISIBLE;

        // Con esto el tamaño de VisorMontos será el correcto y estrictamente necesario
        setPreferredSize(new Dimension(Naipe.ANCHO, Naipe.ALTO + maxNaipes * gap));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getNumNaipes() == 0) {
            g.setColor(VERDE_OSCURO);
            g.fillRoundRect(0, 0, Naipe.ANCHO, Naipe.ALTO, 14, 14);
        }
    }

    // Añade un naipe al monto
    public boolean meter(Naipe naipe) {
        if (naipe != null) {
            naipe.setBocaAbajo(bocaAbajo);

            naipe.setBounds(0, getNumNaipes() * gap, Naipe.ANCHO, Naipe.ALTO);
            add(naipe, Integer.valueOf(getNumNaipes()));

            return true;
        }
        return false;
    }

    // Devuelve la carta de arriba del monto y la quita de éste
    public Naipe cogerNaipe() {
        int size = getNumNaipes();
        if (size > 0) {
            Naipe naipe = getUltimoNaipe();

            remove(naipe);

            return naipe;
        }
        return null;
    }

    // Devuelve el número de naipes del monto
    public int getNumNaipes() {
        return getComponentCount();
    }

    // Devuelve el naipe de arriba del monto sin quitárselo
    public Naipe getUltimoNaipe() {
        if (getNumNaipes() == 0) return null;
        else return getNaipe(0);
    }

    // Selecciona o deselecciona
    // Devuelve si el naipe ha sido seleccionado
    public void cambiarSeleccion() {
        if (getNumNaipes() > 0) {
            if (montoSeleccionado == this) montoSeleccionado = null;
            else {
                if (montoSeleccionado != null)
                    montoSeleccionado.cambiarSeleccion();

                montoSeleccionado = this;
            }
            getUltimoNaipe().cambiarSeleccion();
        }
    }

    protected Naipe getNaipe(int i) {
        return (Naipe) getComponent(i);
    }
}
