package service;

import model.*;

import java.util.List;

public class SeedData {

    private SeedData() {

    }

    public static List<Pokemon> createStarterPokemon() {
        Attack tackle = new DamageAttack("Tackle", 12, 95, Type.NORMAL);
        Attack fist = new DamageAttack("Fist", 20, 100, Type.NORMAL);
        Attack arrow = new DamageAttack("Arrow", 28, 85, Type.FIRE);
        Attack bluntForce = new DamageAttack("Blunt Force", 20, 100, Type.WATER);
        Attack knifeWife = new DamageAttack("Knife Wife", 26, 90, Type.WATER);
        Attack revolver = new DamageAttack("Revolver", 66, 99, Type.FIRE);
        Attack firstAid = new HealAttack("First Aid", 25, 100);
        return List.of(
                new Pokemon("Daryl Dixon", Type.ELECTRIC, 78, List.of(fist, arrow, tackle, firstAid)),
                new Pokemon("Maggie Greene", Type.WATER, 88, List.of(bluntForce, knifeWife, tackle, firstAid)),
                new Pokemon("Carol Peltier", Type.GRASS, 78, List.of(fist, arrow, tackle, firstAid)),
                new Pokemon("Michonne", Type.WATER, 88, List.of(bluntForce, knifeWife, tackle, firstAid)),
                new Pokemon("Rick Grimes", Type.FIRE, 78, List.of(fist, revolver, tackle, firstAid)),
                new Pokemon("Negan", Type.WATER, 88, List.of(bluntForce, knifeWife, tackle, firstAid))
        );
    }
}
