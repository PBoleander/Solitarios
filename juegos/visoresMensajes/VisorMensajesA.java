package juegos.visoresMensajes;

import juegos.solitarios.Solitario;
import naipes.Naipe;

import java.awt.*;

public class VisorMensajesA extends VisorMensajes {

    private Naipe.valor valorNaipeSuperiorInicial;

    public VisorMensajesA() {
        super(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setFont(Solitario.FUENTE_NORMAL);

        /* Mensaje con el valor de la carta inicial superior (centrado justo debajo de los montos superiores) */
        String texto = "Valor del naipe superior inicial: " + valorNaipeSuperiorInicial;
        int x = (getWidth() - g.getFontMetrics().stringWidth(texto)) / 2;
        int y = Naipe.ALTO + 3 * Solitario.VGAP;
        g.drawString(texto, x, y);
    }

    public void setValorNaipeSuperiorInicial(Naipe.valor valorNaipeSuperiorInicial) {
        this.valorNaipeSuperiorInicial = valorNaipeSuperiorInicial;
    }
}
