package fr.ensai.mutationtesting;

import java.util.Collection;
import java.util.LinkedList;

public class Cake {

    private Collection<Ingredient> toppings;
    private Ingredient base;
    private int nbPart;

    public Cake(Ingredient base, int nbPart) {
        this.toppings = new LinkedList<>();
        this.base = base;
        this.nbPart = nbPart;
    }

    public Cake(Cake cake, int nbPart){
        this.toppings = cake.toppings;
        this.base = cake.base;
        this.nbPart = nbPart;
    }

    public void addTopping(Ingredient ingredient){
        this.toppings.add(ingredient);
    }

    public boolean isChololated(){
        if (base==Ingredient.CHOCOLATE) {
            return true;
        }
        return this.toppings.contains(Ingredient.CHOCOLATE);
    }

    public Cake getPart() throws EmptyCakeException {
        if (nbPart>0) {
            this.nbPart --;
            return new Cake(this, 1);
        }else {
            throw new EmptyCakeException();
        }
    }

    @Override
    public String toString() {
        return "fr.insee.mutation_testing.Cake{" +
                "toppings=" + toppings +
                ", base=" + base +
                ", nbPart=" + nbPart +
                '}';
    }

    /*
    GETTER : nothing really cool bellow
     */

    public Collection<Ingredient> getToppings() {
        return toppings;
    }

    public Ingredient getBase() {
        return base;
    }

    public int getNbPart() {
        return nbPart;
    }

}
