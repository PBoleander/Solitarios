package juegos.visoresMensajes;

import juegos.solitarios.Solitario;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

abstract class VisorMensajes extends JPanel {

    boolean victoria;
    int numPartidas, numVictorias;

    private final Color colorFuenteVictoria;

    VisorMensajes(Color colorFuenteVictoria) {
        super();

        setOpaque(false);

        this.colorFuenteVictoria = colorFuenteVictoria;

        victoria = false;
        numPartidas = 0;
        numVictorias = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.setFont(Solitario.FUENTE_NORMAL);
        FontMetrics fm = g.getFontMetrics();

        /* Mensaje con el nº de partidas jugadas y ganadas (esquina inferior derecha) */
        String texto = "Partidas jugadas: " + numPartidas +
                " - Partidas ganadas: " + numVictorias +
                " (" + porcentaje() + " " + "%)";
        int x = getWidth() - fm.stringWidth(texto) - Solitario.HGAP;
        int y = getHeight() - fm.getAscent();
        g.drawString(texto, x, y);

        /* Mensaje de victoria */
        if (victoria) {
            g.setFont(Solitario.FUENTE_GRANDE);
            g.setColor(colorFuenteVictoria);
            fm = g.getFontMetrics();

            // Sitúa el texto centrado en la ventana
            texto = "¡Has ganado!";
            x = (getWidth() - fm.stringWidth(texto)) / 2;
            y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(texto, x, y);
        }
    }

    public void setNumPartidas(int numPartidas) {
        this.numPartidas = numPartidas;
    }

    public void setVictoria(boolean victoria) {
        if (victoria) numVictorias++;

        this.victoria = victoria;
    }

    private String porcentaje() {
        DecimalFormat df = new DecimalFormat("0.0");
        double porcentaje = (this.numPartidas == 0) ? 0 : (this.numVictorias * 100D / this.numPartidas);

        return df.format(porcentaje);
    }
}
