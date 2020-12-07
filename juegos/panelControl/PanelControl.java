package juegos.panelControl;

import juegos.solitarios.Solitario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelControl extends JPanel implements ActionListener {

    private final Boton btnDeshacer, btnRehacer, btnInicio, btnReinicio;
    private final JCheckBox chkMarcadoInferior, chkMarcadoSuperior;
    private final Solitario solitario;

    public PanelControl(Solitario solitario) {
        super(new GridBagLayout());

        setOpaque(false);

        this.solitario = solitario;

        this.btnDeshacer = new Boton("Deshacer");
        this.btnRehacer = new Boton("Rehacer");
        this.btnInicio = new Boton("Nueva");
        this.btnReinicio = new Boton("Reiniciar");

        btnDeshacer.addActionListener(this);
        btnRehacer.addActionListener(this);
        btnInicio.addActionListener(this);
        btnReinicio.addActionListener(this);

        this.chkMarcadoInferior = new JCheckBox("Marcar naipes a inferiores", false);
        this.chkMarcadoSuperior = new JCheckBox("Marcar naipes a superiores", false);

        chkMarcadoInferior.addActionListener(this);
        chkMarcadoSuperior.addActionListener(this);

        chkMarcadoInferior.setOpaque(false);
        chkMarcadoSuperior.setOpaque(false);
        chkMarcadoInferior.setForeground(Color.WHITE);
        chkMarcadoSuperior.setForeground(Color.WHITE);
        chkMarcadoInferior.setFont(Solitario.FUENTE_NORMAL);
        chkMarcadoSuperior.setFont(Solitario.FUENTE_NORMAL);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(Solitario.VGAP, Solitario.HGAP, 0, 0);
        add(btnInicio, c);
        c.gridx = 1;
        add(btnReinicio, c);
        c.gridx = 0;
        c.gridy = 1;
        add(btnDeshacer, c);
        c.gridx = 1;
        add(btnRehacer, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        add(chkMarcadoSuperior, c);
        c.insets = new Insets(Solitario.VGAP / 2, Solitario.HGAP, 0, 0);
        c.gridy = 3;
        add(chkMarcadoInferior, c);

        solitario.setMarcadoInferior(chkMarcadoInferior.isSelected());
        solitario.setMarcadoSuperior(chkMarcadoSuperior.isSelected());
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

        } else if (source.equals(chkMarcadoInferior)) {
            solitario.setMarcadoInferior(chkMarcadoInferior.isSelected());

        } else if (source.equals(chkMarcadoSuperior)) {
            solitario.setMarcadoSuperior(chkMarcadoSuperior.isSelected());

        }

        solitario.revalidate();
        solitario.repaint();
    }

    private int mostrarConfirmacion(boolean reinicio) {
        Object mensaje = "Está a punto de " + (reinicio ? "reiniciar la" : "empezar una nueva") + " partida. ¿Desea " +
                "abandonar ésta?";
        return JOptionPane.showConfirmDialog(null, mensaje, "Aviso", JOptionPane.YES_NO_OPTION);
    }
}
