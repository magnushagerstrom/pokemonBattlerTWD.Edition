package service;

import model.Attack;
import model.Pokemon;
import model.Type;

import java.util.List;

public class SeedData {

    private SeedData() {

    }

    public static List<Pokemon> createStarterPokemon() {
        Attack tackle = new Attack("Tackle", 12, 95, Type.NORMAL);
        Attack fist = new Attack("Fist", 20, 100, Type.NORMAL);
        Attack arrow = new Attack("Arrow", 28, 85, Type.FIRE);
        Attack bluntForce = new Attack("Blunt Force", 20, 100, Type.WATER);
        Attack knifeWife = new Attack("Knife Wife", 26, 90, Type.WATER);
        Attack revolver = new Attack("Revolver", 66, 99, Type.FIRE);
        return List.of(
                new Pokemon("Daryl Dixon", Type.ELECTRIC, 78, List.of(fist, arrow, tackle)),
                new Pokemon("Maggie Greene", Type.WATER, 88, List.of(bluntForce, knifeWife, tackle)),
                new Pokemon("Carol Peltier", Type.GRASS, 78, List.of(fist, arrow, tackle)),
                new Pokemon("Michonne", Type.WATER, 88, List.of(bluntForce, knifeWife, tackle)),
                new Pokemon("Rick Grimes", Type.FIRE, 78, List.of(fist, revolver, tackle)),
                new Pokemon("Negan", Type.WATER, 88, List.of(bluntForce, knifeWife, tackle))
        );
    }
}
