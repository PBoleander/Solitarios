package juegos.visoresMontos;

import juegos.solitarios.Solitario;
import naipes.Monto;
import naipes.Naipe;

import javax.swing.*;
import java.awt.*;

public class VisorMontosA extends JPanel {

    private final JLabel etiquetaReserva, etiquetaManoPorSacar, etiquetaManoSacado;
    private final Monto montoReserva, montoManoPorSacar, montoManoSacado;

    public VisorMontosA(Monto[] montosInferiores, Monto[] montosSuperiores, Monto montoReserva, Monto montoManoPorSacar,
                 Monto montoManoSacado) {
        super(new GridBagLayout());

        setOpaque(false);

        this.montoManoPorSacar = montoManoPorSacar;
        this.montoManoSacado = montoManoSacado;
        this.montoReserva = montoReserva;

        final int hGap = Solitario.HGAP, vGap = Solitario.VGAP;
        final GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(vGap, hGap, vGap, hGap);
        c.anchor = GridBagConstraints.NORTH;

        c.gridy = 0;

        for (int i = 0; i < montosSuperiores.length; i++) {
            c.gridx = 2 + i;
            add(montosSuperiores[i], c);
        }

        c.gridy = 1;
        c.gridheight = 2;

        for (int i = 0; i < montosInferiores.length; i++) {
            c.gridx = 2 + i;
            add(montosInferiores[i], c);
        }

        // Para los montos reserva y de mano se hace un panel para cada uno porque irán acompañados del nº de naipes
        etiquetaReserva = nuevoLabel();
        etiquetaManoPorSacar = nuevoLabel();
        etiquetaManoSacado = nuevoLabel();

        c.gridheight = 1;
        c.gridx = 0;
        c.insets = new Insets(1, hGap, vGap, hGap); // Para mantener alineado montoReserva con los inferiores a pesar
        // de la etiqueta del nº de naipes
        add(rellenarPanel(etiquetaReserva, montoReserva), c);

        c.gridy = 2;
        c.insets = new Insets(vGap, hGap, vGap, hGap);
        add(rellenarPanel(etiquetaManoPorSacar, montoManoPorSacar), c);

        c.gridx = 1;
        add(rellenarPanel(etiquetaManoSacado, montoManoSacado), c);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        etiquetaReserva.setText("Naipes: " + montoReserva.getNumNaipes());
        etiquetaManoPorSacar.setText("Naipes: " + montoManoPorSacar.getNumNaipes());
        etiquetaManoSacado.setText("Naipes: " + montoManoSacado.getNumNaipes());
    }

    private JLabel nuevoLabel() {
        JLabel label = new JLabel("Naipe:"); // Darle texto inicial le da tamaño para que no colapse el GUI
        label.setForeground(Color.WHITE);
        label.setFont(Solitario.FUENTE_NORMAL);
        label.setPreferredSize(new Dimension(Naipe.ANCHO, (int) label.getPreferredSize().getHeight())); // Para que
        // cuando pasa de una cifra a dos no tarde

        return label;
    }

    private JPanel rellenarPanel(JLabel etiqueta, Monto monto) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST; // Etiqueta comienza en el lado izquierdo
        panel.add(etiqueta, c);
        c.gridy = 1;
        c.insets = new Insets(2, 0, 0, 0); // Espacio vertical entre monto y etiqueta
        panel.add(monto, c);

        return panel;
    }
}
