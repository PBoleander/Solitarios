package juegos.solitarioA;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

class VisorMensajesA extends JLabel {

    private final Font fuenteGrande = new Font(Font.DIALOG, Font.BOLD, 50);
    private final Font fuenteNormal = new Font(Font.DIALOG, Font.PLAIN, 14);

    private boolean victoria;
    private int numPartidas, numVictorias;

    VisorMensajesA() {
        super();

        victoria = false;
        numPartidas = 0;
        numVictorias = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.setFont(fuenteNormal);
        FontMetrics fm = g.getFontMetrics();

        String texto = "Partidas jugadas: " + numPartidas +
                        " - Partidas ganadas: " + numVictorias +
                        " (" + porcentaje() + " " + "%)";
        int x = getWidth() - fm.stringWidth(texto) - 20;
        int y = getHeight() - fm.getAscent();
        g.drawString(texto, x, y);

        if (victoria) {
            g.setFont(fuenteGrande);
            fm = g.getFontMetrics();

            // Sitúa el texto centrado en la ventana
            texto = "¡Has ganado!";
            x = (getWidth() - fm.stringWidth(texto)) / 2;
            y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(texto, x, y);
        }
    }

    void setNumPartidas(int numPartidas) {
        this.numPartidas = numPartidas;
    }

    void setVictoria(boolean victoria) {
        if (victoria) numVictorias++;

        this.victoria = victoria;
    }

    private String porcentaje() {
        DecimalFormat df = new DecimalFormat("0.0");
        double porcentaje = (this.numPartidas == 0) ? 0 : (this.numVictorias * 100D / this.numPartidas);

        return df.format(porcentaje);
    }
}
