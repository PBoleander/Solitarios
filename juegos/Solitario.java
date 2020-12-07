package juegos;

import juegos.registro.Movimiento;
import juegos.registro.Registro;
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

    private final Registro registro;

    public Solitario(LayoutManager layoutManager) {
        super(layoutManager);

        this.registro = new Registro();
    }

    protected Registro getRegistro() {
        return registro;
    }

    protected abstract void iniciar(boolean reinicio);
    protected abstract boolean isVictoria();
    protected abstract void setMarcadoInferior(boolean marcadoInferior);
    protected abstract void setMarcadoSuperior(boolean marcadoSuperior);

    protected void deshacerMovimiento() {
        // Si hay monto seleccionado, se quita antes de proceder
        reiniciarMontoSeleccionado();

        Movimiento movimiento = registro.getMovimientoAnterior();
        if (movimiento != null) {
            Monto montoOrigen = movimiento.getMontoOrigen();
            Monto montoDestino = movimiento.getMontoDestino();
            int numNaipes = movimiento.getNumeroNaipes();

            int i = 0;
            while (i < numNaipes) {
                montoOrigen.meter(montoDestino.cogerNaipe());
                i++;
            }
        }
    }

    protected void rehacerMovimiento() {
        // Si hay monto seleccionado, se quita antes de proceder
        reiniciarMontoSeleccionado();

        Movimiento movimiento = registro.getMovimientoPosterior();
        if (movimiento != null) {
            Monto montoOrigen = movimiento.getMontoOrigen();
            Monto montoDestino = movimiento.getMontoDestino();
            int numNaipes = movimiento.getNumeroNaipes();

            int i = 0;
            while (i < numNaipes) {
                montoDestino.meter(montoOrigen.cogerNaipe());
                i++;
            }
        }
    }

    protected void reiniciarMontoSeleccionado() {
        if (Monto.montoSeleccionado != null) Monto.montoSeleccionado.cambiarSeleccion();
    }
}
