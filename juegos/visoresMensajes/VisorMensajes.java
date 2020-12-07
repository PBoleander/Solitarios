package juegos.visoresMensajes;

import javax.swing.*;
import java.text.DecimalFormat;

abstract class VisorMensajes extends JPanel {

    boolean victoria;
    int numPartidas, numVictorias;

    VisorMensajes() {
        super();

        setOpaque(false);

        victoria = false;
        numPartidas = 0;
        numVictorias = 0;
    }

    public void setNumPartidas(int numPartidas) {
        this.numPartidas = numPartidas;
    }

    public void setVictoria(boolean victoria) {
        if (victoria) numVictorias++;

        this.victoria = victoria;
    }

    String porcentaje() {
        DecimalFormat df = new DecimalFormat("0.0");
        double porcentaje = (this.numPartidas == 0) ? 0 : (this.numVictorias * 100D / this.numPartidas);

        return df.format(porcentaje);
    }
}
