package juegos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelControl extends JPanel implements ActionListener {

    private final Boton btnDeshacer, btnRehacer, btnInicio, btnReinicio;
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

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 24, 0, 0);
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
            solitario.iniciar(false);

        } else if (source.equals(btnReinicio)) {
            if (!solitario.isVictoria()) {
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

        solitario.revalidate();
        solitario.repaint();
    }
}
