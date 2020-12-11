package juegos.panelControl;

import juegos.solitarios.Solitario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

abstract class PanelControl extends JPanel implements ActionListener {

    final GridBagConstraints c = new GridBagConstraints();
    final Solitario solitario;

    private final Boton btnDeshacer, btnRehacer, btnInicio, btnReinicio;

    public PanelControl(Solitario solitario) {
        super(new GridBagLayout());

        setOpaque(false);

        this.solitario = solitario;

        this.btnDeshacer = new Boton("Deshacer");
        this.btnRehacer = new Boton("Rehacer");
        this.btnInicio = new Boton("Nuevo");
        this.btnReinicio = new Boton("Reiniciar");

        btnDeshacer.addActionListener(this);
        btnRehacer.addActionListener(this);
        btnInicio.addActionListener(this);
        btnReinicio.addActionListener(this);

        c.insets = new Insets(Solitario.VGAP, Solitario.HGAP, 0, 0);
        add(btnInicio, c);
        c.gridx = 1;
        add(btnReinicio, c);
        c.gridx = 0;
        c.gridy = 1;
        add(btnDeshacer, c);
        c.gridx = 1;
        add(btnRehacer, c);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source.equals(btnInicio)) {
            if (solitario.isVictoria() || mostrarConfirmacion(false) == JOptionPane.YES_OPTION)
                solitario.iniciar(false);

        } else if (source.equals(btnReinicio)) {
            if (!solitario.isVictoria() && mostrarConfirmacion(true) == JOptionPane.YES_OPTION) {
                solitario.iniciar(true);
            }

        } else if (source.equals(btnDeshacer)) {
            if (!solitario.isVictoria()) {
                solitario.deshacerMovimiento();
            }

        } else if (source.equals(btnRehacer)) {
            if (!solitario.isVictoria()) {
                solitario.rehacerMovimiento();
            }
        }
    }

    private int mostrarConfirmacion(boolean reinicio) {
        Object mensaje = "Está a punto de " + (reinicio ? "reiniciar la" : "empezar una nueva") + " partida. ¿Desea " +
                "abandonar ésta?";
        return JOptionPane.showConfirmDialog(null, mensaje, "Aviso", JOptionPane.YES_NO_OPTION);
    }
}
