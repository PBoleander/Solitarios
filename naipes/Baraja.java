package naipes;

import java.util.ArrayList;
import java.util.Random;

public class Baraja extends Monto {

    //*******************************************************************************************************//
    //****************************************** Variables miembro ******************************************//
    //*******************************************************************************************************//

    private final ArrayList<Naipe> naipesAlInicio; // Si lo hago Monto no funciona, la ventana no aparece
    private final Random random = new Random();

    //*****************************************************************************************************//
    //******************************************** Constructor ********************************************//
    //*****************************************************************************************************//

    public Baraja() {
        super(false,true, 48);

        this.naipesAlInicio = new ArrayList<>(); // Guardará los naipes barajados antes de repartir

        // Llena la reserva con todos los naipes existentes
        for (Naipe.palo palo: Naipe.palo.values()) {
            for (Naipe.valor valor: Naipe.valor.values()) {
                this.naipesAlInicio.add(new Naipe(palo, valor));
            }
        }
    }

    //******************************************************************************************************//
    //****************************************** Métodos públicos ******************************************//
    //******************************************************************************************************//

    // Rellena naipes a partir de la reserva y los baraja
    public void barajar() {
        // Baraja las cartas
        while (this.naipesAlInicio.size() > 0) {
            Naipe naipe = this.naipesAlInicio.remove(random.nextInt(this.naipesAlInicio.size()));
            meter(naipe);
        }

        // Rellena naipesAlInicio con la reserva barajada
        int i = getNumNaipes();
        while (i > 0) {
            naipesAlInicio.add(getNaipe(--i));
        }
    }

    // Recupera la baraja inicial (después de barajar pero antes de repartir)
    public void recuperar() {
        int i = 0;
        while (i < naipesAlInicio.size()) {
            meter(naipesAlInicio.get(i++));
        }
    }
}
