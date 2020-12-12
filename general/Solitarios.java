package general;

import juegos.solitarios.SolitarioA;
import juegos.solitarios.SolitarioB;

import javax.swing.*;

class Solitarios extends JFrame {

    Solitarios() {
        super();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Solitarios");
        setResizable(false);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Solitario A", new SolitarioA());
        tabbedPane.addTab("Solitario B", new SolitarioB());

        add(tabbedPane);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Solitarios::new);
    }
}
