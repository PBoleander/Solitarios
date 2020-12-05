package naipes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Naipe extends JLabel {

    //*****************************************************************************************************//
    //*********************************************** Enums ***********************************************//
    //*****************************************************************************************************//

    public enum palo {
        BASTOS, COPAS, ESPADAS, OROS
    }

    public enum valor {
        AS, DOS, TRES, CUATRO, CINCO, SEIS, SIETE, OCHO, NUEVE, SOTA, CABALLO, REY
    }

    //*******************************************************************************************************//
    //****************************************** Variables miembro ******************************************//
    //*******************************************************************************************************//

    public static final int ALTO = 159;
    public static final int ANCHO = 104;

    private static final BufferedImage imagenReverso = setImagenReverso();
    private static final Color SELECCION = new Color(0, 100, 255, 150);
    private static final Color MARCA = new Color(255, 120, 0, 150);

    private final BufferedImage imagenAnverso;
    private final palo palo;
    private final valor valor;

    private boolean bocaAbajo, marcado, seleccionado;

    //*****************************************************************************************************//
    //******************************************** Constructor ********************************************//
    //*****************************************************************************************************//

    Naipe(palo palo, valor valor) {
        super();

        this.palo = palo;
        this.valor = valor;
        this.imagenAnverso = setImagenAnverso();
        this.bocaAbajo = false;

        setPreferredSize(new Dimension(ANCHO, ALTO));
    }

    //******************************************************************************************************//
    //****************************************** Métodos públicos ******************************************//
    //******************************************************************************************************//

    @Override
    public String toString() {
        return this.valor + " DE " + this.palo;
    }

    public palo getPalo() {
        return this.palo;
    }

    public valor getValor() {
        return this.valor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bocaAbajo) g.drawImage(imagenReverso, 0, 0, null);
        else g.drawImage(this.imagenAnverso, 0, 0, null);

        if (seleccionado) {
            g.setColor(SELECCION);
            g.fillRoundRect(0, 0, ANCHO, ALTO, 14, 14);
        } else if (marcado) {
            g.setColor(MARCA);
            g.fillRoundRect(0, 0, ANCHO, ALTO, 14, 14);
        }
    }

    //*****************************************************************************************************//
    //****************************************** Métodos default ******************************************//
    //*****************************************************************************************************//

    // Selecciona el naipe si no hay ningún otro seleccionado o lo deselecciona si ya estaba seleccionado
    void cambiarSeleccion() {
        seleccionado = !seleccionado;
    }

    void setBocaAbajo(boolean bocaAbajo) {
        this.bocaAbajo = bocaAbajo;
    }

    void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    //******************************************************************************************************//
    //****************************************** Métodos privados ******************************************//
    //******************************************************************************************************//

    // Establece la imagen del anverso del naipe (diferente para cada uno)
    private BufferedImage setImagenAnverso() {
        char valor = switch (this.valor) {
            case AS, SOTA, CABALLO, REY -> this.valor.toString().toLowerCase().charAt(0);
            case DOS, TRES, CUATRO, CINCO, SEIS, SIETE, OCHO, NUEVE -> String.valueOf(this.valor.ordinal() + 1).charAt(0);
        };
        String nombreArchivo = valor + String.valueOf(this.palo.toString().toLowerCase().charAt(0)) + ".png";

        try {
            return ImageIO.read(getClass().getResourceAsStream("imagenes/" + nombreArchivo));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Establece la imagen del reverso (la misma para todos los naipes)
    private static BufferedImage setImagenReverso() {
        try {
            return ImageIO.read(Naipe.class.getResourceAsStream("imagenes/bt.png"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
