package naipes;

import java.util.ArrayList;
import java.util.Random;

public class Baraja extends Monto {

    //*******************************************************************************************************//
    //****************************************** Variables miembro ******************************************//
    //*******************************************************************************************************//

    private final ArrayList<Naipe> naipes, naipesAlInicio;
    private final Random random = new Random();

    //*****************************************************************************************************//
    //******************************************** Constructor ********************************************//
    //*****************************************************************************************************//

    public Baraja() {
        super(false,true, 48);
        this.naipes = super.getNaipes();
        this.naipesAlInicio = new ArrayList<>(); // Guardará los naipes barajados antes de repartir

        // Llena la reserva con todos los naipes existentes
        for (Naipe.palo palo: Naipe.palo.values()) {
            for (Naipe.valor valor: Naipe.valor.values()) {
                this.naipesAlInicio.add(new Naipe(palo, valor));
            }
        }

        //barajar();
    }

    //******************************************************************************************************//
    //****************************************** Métodos públicos ******************************************//
    //******************************************************************************************************//

    // Rellena naipes a partir de la reserva y los baraja
    public void barajar() {
        // Baraja las cartas
        while (this.naipesAlInicio.size() > 0) {
            Naipe naipe = this.naipesAlInicio.remove(random.nextInt(this.naipesAlInicio.size()));
            this.naipes.add(naipe);
        }

        // Rellena naipesAlInicio con la reserva barajada
        this.naipesAlInicio.addAll(this.naipes);
    }

    // Recupera la baraja inicial (después de barajar pero antes de repartir)
    public void recuperar() {
        this.naipes.addAll(this.naipesAlInicio);
    }
}
