package juegos;

import javax.swing.*;
import java.awt.*;

class Boton extends JButton {

    //*****************************************************************************************************//
    //******************************************** Constructor ********************************************//
    //*****************************************************************************************************//

    Boton(String texto) {
        super(texto);

        setOpaque(false);

        setPreferredSize(new Dimension(120, 35));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(null);
    }

    //*****************************************************************************************************//
    //****************************************** Métodos default ******************************************//
    //*****************************************************************************************************//

    // Pinta este componente
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setFont(Solitario.FUENTE_NORMAL);
        FontMetrics fm = g.getFontMetrics();

        // Para que la sombra se vea se tiene que disminuir el área pintada de background ya que no pinta nada si
        // está fuera de las dimensiones del botón (width y height)
        final int gap = 5;
        int widthModificado = getWidth() - gap;
        int heightModificado = getHeight() - gap;

        /* Todo menos texto */
        g.setColor(Color.DARK_GRAY);
        g.fillRect(gap, gap, widthModificado, heightModificado); // Sombra (se pinta antes, va debajo)
        g.setColor(getBackground());
        g.fillRect(0, 0, widthModificado, heightModificado); // Botón (se pinta encima)

        /* Texto */
        g.setColor(getForeground());
        final int x = (widthModificado - fm.stringWidth(getText())) / 2;
        final int y = (heightModificado - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(getText(), x, y);
    }
}
