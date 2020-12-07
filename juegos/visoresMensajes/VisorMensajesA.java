package juegos.visoresMensajes;

import juegos.solitarios.Solitario;
import naipes.Naipe;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class VisorMensajesA extends VisorMensajes {

    private Naipe.valor valorNaipeSuperiorInicial;

    public VisorMensajesA() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.setFont(Solitario.FUENTE_NORMAL);
        FontMetrics fm = g.getFontMetrics();

        /* Mensaje con el nº de partidas jugadas y ganadas */
        String texto = "Partidas jugadas: " + numPartidas +
                        " - Partidas ganadas: " + numVictorias +
                        " (" + porcentaje() + " " + "%)";
        int x = getWidth() - fm.stringWidth(texto) - 20;
        int y = getHeight() - fm.getAscent();
        g.drawString(texto, x, y);

        /* Mensaje con el valor de la carta inicial superior */
        texto = "Valor del naipe superior inicial: " + valorNaipeSuperiorInicial;
        x = (getWidth() - fm.stringWidth(texto)) / 2;
        y = Naipe.ALTO + 60;
        g.drawString(texto, x, y);

        /* Mensaje de victoria */
        if (victoria) {
            g.setFont(Solitario.FUENTE_GRANDE);
            fm = g.getFontMetrics();

            // Sitúa el texto centrado en la ventana
            texto = "¡Has ganado!";
            x = (getWidth() - fm.stringWidth(texto)) / 2;
            y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(texto, x, y);
        }
    }

    public void setValorNaipeSuperiorInicial(Naipe.valor valorNaipeSuperiorInicial) {
        this.valorNaipeSuperiorInicial = valorNaipeSuperiorInicial;
    }
}
