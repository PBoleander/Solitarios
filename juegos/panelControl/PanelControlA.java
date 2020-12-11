package juegos.panelControl;

import juegos.solitarios.Solitario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PanelControlA extends PanelControl {

    private final JCheckBox chkMarcadoInferior, chkMarcadoSuperior;

    public PanelControlA(Solitario solitario) {
        super(solitario);

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

        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        add(chkMarcadoSuperior, c);
        // Para juntar los dos checkboxes un poco más que el resto
        c.insets = new Insets(Solitario.VGAP / 2, Solitario.HGAP, 0, 0);
        c.gridy++;
        add(chkMarcadoInferior, c);

        solitario.setMarcadoInferior(chkMarcadoInferior.isSelected());
        solitario.setMarcadoSuperior(chkMarcadoSuperior.isSelected());
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        super.actionPerformed(actionEvent);

        Object source = actionEvent.getSource();

         if (source.equals(chkMarcadoInferior)) {
            solitario.setMarcadoInferior(chkMarcadoInferior.isSelected());

        } else if (source.equals(chkMarcadoSuperior)) {
            solitario.setMarcadoSuperior(chkMarcadoSuperior.isSelected());
        }

        solitario.revalidate();
        solitario.repaint();
    }
}
