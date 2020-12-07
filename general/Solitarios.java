package general;

import juegos.solitarios.SolitarioA;

import javax.swing.*;
import java.awt.*;

class Solitarios extends JFrame {

    Solitarios() {
        super();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Solitarios");
        setLayout(new GridBagLayout());
        setResizable(false);

        add(new SolitarioA());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Solitarios::new);
    }
}
