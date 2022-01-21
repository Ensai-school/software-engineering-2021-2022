package fr.ensai.mutationtesting;

import java.util.Collection;

public class CakeService {

    public boolean isBirthday(Collection<Cake> cakes){
        int chocolate = 0;
        for (Cake cupcake : cakes) {
            if (cupcake.isChololated()){
                chocolate++;
            }
        }
        return chocolate==2;
    }

    public boolean canEveryoneEat(Collection<Cake> cakes, int nbPerson){
        int nbTotalPieces = cakes.stream().mapToInt(Cake::getNbPart).sum();
        return nbTotalPieces>=nbPerson;
    }

    public void eatCake(Cake cake){
        if (cake.getNbPart()>0){
            try {
                cake.getPart();
                System.out.println(
                        "Eating a piece of " + cake.toString()
                );
            } catch (EmptyCakeException e) {
                System.out.println("Nothing to eat");
            }
        }
    }
}
