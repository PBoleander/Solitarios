package juegos.solitarios;

import juegos.registro.Movimiento;
import juegos.registro.Registro;
import naipes.Baraja;
import naipes.Monto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public abstract class Solitario extends JPanel implements MouseListener {

    //*****************************************************************************************************//
    //**************************************** Variables generales ****************************************//
    //*****************************************************************************************************//

    public static final Font FUENTE_GRANDE = new Font(Font.DIALOG, Font.BOLD, 50);
    public static final Font FUENTE_NORMAL = new Font(Font.DIALOG, Font.PLAIN, 14);
    public static final int HGAP = 24;
    public static final int VGAP = 20;

    final Baraja baraja;
    final Registro registro;

    int numPartidas;

    Solitario(LayoutManager layoutManager) {
        super(layoutManager);

        this.baraja = new Baraja();
        this.registro = new Registro();
        this.numPartidas = 0;

        setBackground(new Color(0, 150, 0));
    }

    public abstract void iniciar(boolean reinicio);
    public abstract boolean isVictoria();
    public abstract void setMarcadoInferior(boolean marcadoInferior);
    public abstract void setMarcadoSuperior(boolean marcadoSuperior);

    public void deshacerMovimiento() {
        baseMoversePorRegistro(true);
    }

    public void rehacerMovimiento() {
        baseMoversePorRegistro(false);
    }

    void reiniciarMontoSeleccionado() {
        if (Monto.montoSeleccionado != null) Monto.montoSeleccionado.cambiarSeleccion();
    }

    private void baseMoversePorRegistro(boolean haciaAtras) {
        // Si hay monto seleccionado, se quita antes de proceder
        reiniciarMontoSeleccionado();

        Movimiento movimiento;
        if (haciaAtras) movimiento = registro.getMovimientoAnterior();
        else movimiento = registro.getMovimientoPosterior();

        if (movimiento != null) {
            Monto montoOrigen = movimiento.getMontoOrigen();
            Monto montoDestino = movimiento.getMontoDestino();
            int numNaipes = movimiento.getNumeroNaipes();

            int i = 0;
            while (i < numNaipes) {
                if (haciaAtras) montoOrigen.meter(montoDestino.cogerNaipe());
                else montoDestino.meter(montoOrigen.cogerNaipe());

                i++;
            }
        }
    }
}
