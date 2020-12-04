package naipes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Monto extends JLayeredPane {

    /* Píxeles mostrados de un naipe que no está arriba del monto según su visibilidad */
    public static final int VGAP_INVISIBLE = 2;
    public static final int VGAP_VISIBLE = 20;

    public static Monto montoSeleccionado = null;

    private static final Color VERDE_OSCURO = new Color(0, 100, 0);

    private final boolean bocaAbajo;
    private final ArrayList<Naipe> naipes;
    private final int gap;

    public Monto(boolean todosNaipesIdentificables, boolean bocaAbajo, int maxNaipes) {
        super();

        this.bocaAbajo = bocaAbajo;
        this.gap = todosNaipesIdentificables ? VGAP_VISIBLE : VGAP_INVISIBLE;
        this.naipes = new ArrayList<>();

        setPreferredSize(new Dimension(Naipe.ANCHO, Naipe.ALTO + maxNaipes * gap));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (naipes.isEmpty()) {
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
            setSize(new Dimension(Naipe.ANCHO, Naipe.ALTO + getNumNaipes() * gap));

            return this.naipes.add(naipe);
        }
        return false;
    }

    // Vacía el monto
    public void clear() {
        this.naipes.clear();

        removeAll();

        setSize(new Dimension(Naipe.ANCHO, Naipe.ALTO));
    }

    // Devuelve la carta de arriba del monto y la quita de éste
    public Naipe cogerNaipe() {
        int size = getNumNaipes();
        if (size > 0) {
            Naipe naipe = this.naipes.remove(size - 1);

            remove(naipe);
            setSize(new Dimension(Naipe.ANCHO, Naipe.ALTO + (size - 1) * gap));

            return naipe;
        } else return null;
    }

    // Devuelve la lista de naipes del monto
    ArrayList<Naipe> getNaipes() {
        return this.naipes;
    }

    // Devuelve el número de naipes del monto
    public int getNumNaipes() {
        return this.naipes.size();
    }

    // Devuelve el naipe de arriba del monto sin quitárselo
    public Naipe getUltimoNaipe() {
        if (naipes.isEmpty()) return null;
        else return this.naipes.get(naipes.size() - 1);
    }

    // Selecciona o deselecciona
    // Devuelve si el naipe ha sido seleccionado
    public boolean cambiarSeleccion() {
        if (getNumNaipes() > 0) {
            if (montoSeleccionado == this) montoSeleccionado = null;
            else {
                if (montoSeleccionado != null)
                    montoSeleccionado.cambiarSeleccion();

                montoSeleccionado = this;
            }
            return getUltimoNaipe().cambiarSeleccion();
        }
        return false;
    }
}
