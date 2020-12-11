package juegos.panelControl;

import juegos.solitarios.Solitario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PanelControlB extends PanelControl {

    private final JCheckBox chkMarcadoInferior;

    public PanelControlB(Solitario solitario) {
        super(solitario);

        this.chkMarcadoInferior = new JCheckBox("Marcar naipes colocables", false);

        chkMarcadoInferior.addActionListener(this);

        chkMarcadoInferior.setOpaque(false);
        chkMarcadoInferior.setForeground(Color.WHITE);
        chkMarcadoInferior.setFont(Solitario.FUENTE_NORMAL);

        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        add(chkMarcadoInferior, c);

        solitario.setMarcadoInferior(chkMarcadoInferior.isSelected());
    }

    public void actionPerformed(ActionEvent actionEvent) {
        super.actionPerformed(actionEvent);

        Object source = actionEvent.getSource();

        if (source.equals(chkMarcadoInferior)) {
            solitario.setMarcadoInferior(chkMarcadoInferior.isSelected());
        }

        solitario.revalidate();
        solitario.repaint();
    }
}
