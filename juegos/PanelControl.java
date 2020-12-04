package juegos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelControl extends JPanel implements ActionListener {

    private final Boton btnDeshacer, btnInicio, btnReinicio;
    private final Solitario solitario;

    public PanelControl(Solitario solitario) {
        super(new GridBagLayout());

        setOpaque(false);

        this.solitario = solitario;

        this.btnDeshacer = new Boton("Deshacer");
        this.btnInicio = new Boton("Iniciar partida");
        this.btnReinicio = new Boton("Reiniciar partida");

        btnDeshacer.addActionListener(this);
        btnInicio.addActionListener(this);
        btnReinicio.addActionListener(this);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 24, 0, 0);
        add(btnInicio, c);
        c.gridy = 1;
        add(btnReinicio, c);
        c.gridy = 2;
        add(btnDeshacer, c);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source.equals(btnInicio)) {
            solitario.iniciar(false);

        } else if (source.equals(btnReinicio)) {
            if (!solitario.isVictoria()) solitario.iniciar(true);

        } else if (source.equals(btnDeshacer)) {
            if (!solitario.isVictoria()) solitario.deshacerMovimiento();
        }
    }
}
