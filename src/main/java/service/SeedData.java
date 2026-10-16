package service;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeedData {

    private SeedData() {

    }

    public static List<Pokemon> createStarterPokemon() {
        Attack tackle = new DamageAttack("Tackle", 12, 95, Type.NORMAL);
        Attack fist = new DamageAttack("Fist", 20, 100, Type.NORMAL);
        Attack arrow = new DamageAttack("Arrow", 28, 85, Type.FIRE);
        Attack bluntForce = new DamageAttack("Blunt Force", 20, 100, Type.WATER);
        Attack knifeWife = new DamageAttack("Knife Wife", 26, 90, Type.WATER);
        Attack revolver = new DamageAttack("Revolver", 33, 66, Type.FIRE);
        Attack firstAid = new HealAttack("First Aid", 25, 100);
        return List.of(
                new Pokemon("Daryl Dixonemon", Type.ELECTRIC, 78, List.of(fist, arrow, tackle, firstAid)),
                new Pokemon("Maggie Greenemon", Type.WATER, 88, List.of(bluntForce, knifeWife, tackle, firstAid)),
                new Pokemon("Carol Peltierizard", Type.GRASS, 78, List.of(fist, arrow, tackle, firstAid)),
                new Pokemon("Michonnemon", Type.WATER, 88, List.of(bluntForce, knifeWife, tackle, firstAid)),
                new Pokemon("Rickemon Grimes", Type.FIRE, 87, List.of(fist, revolver, tackle, firstAid)),
                new Pokemon("Neganizard", Type.WATER, 88, List.of(bluntForce, knifeWife, tackle, firstAid)));
    }

    private static final Random random = new Random();

    public static Pokemon createWildOpponent(String excludedName) {
        List<Pokemon> pool = new ArrayList<>();
        for (Pokemon pokemon : createStarterPokemon()) {
            if (!pokemon.getName().equalsIgnoreCase(excludedName)) {
                pool.add(pokemon);
            }
        }
        if (pool.isEmpty()) {
            pool = createStarterPokemon();
        }
        return pool.get(random.nextInt(pool.size()));
    }

}
