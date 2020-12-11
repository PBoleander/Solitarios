package general;

import juegos.solitarios.Solitario;
import juegos.solitarios.SolitarioA;
import juegos.solitarios.SolitarioB;

import javax.swing.*;
import java.awt.*;

class Solitarios extends JFrame {

    private final Solitario solitarioA, solitarioB;

    Solitarios() {
        super();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Solitarios");
        setLayout(new CardLayout());
        setResizable(false);

        this.solitarioA = new SolitarioA();
        this.solitarioB = new SolitarioB();

        add(solitarioA);
        add(solitarioB);
        setJMenuBar(new Menu(this));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void mostrarA() {
        solitarioA.setVisible(true);
        solitarioB.setVisible(false);
    }

    void mostrarB() {
        solitarioB.setVisible(true);
        solitarioA.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Solitarios::new);
    }
}
