import exception.InvalidPokemonDataException;
import model.Attack;
import model.Pokemon;
import model.Type;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Attack ember = new Attack("Ember", 40, 100, Type.FIRE);
        Attack scratch = new Attack("Scratch", 40, 100, Type.NORMAL);
        Pokemon charmander = new Pokemon("Charmander", Type.FIRE, 39, List.of(ember, scratch));

        System.out.println(charmander);
        System.out.println("Attacks: " + charmander.getAttacks());

        charmander.takeDamage(15);
        System.out.println("After 15 damage: " + charmander + " fainted? " + charmander.isFainted());

        charmander.takeDamage(100);
        System.out.println("After 100 damage: " + charmander + " fainted? " + charmander.isFainted());

        charmander.restoreHp();
        System.out.println("After restore: " + charmander);

        try {
            charmander.getAttacks().add(ember);
            System.out.println("NOT protected ← this is a bug!");
        } catch (UnsupportedOperationException e) {
            System.out.println("Attack list is protected");
        }

        try {
            new Pokemon("Pikachu", Type.ELECTRIC, 35, List.of());
            System.out.println("NOT rejected ← this is a bug!");
        } catch (InvalidPokemonDataException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}