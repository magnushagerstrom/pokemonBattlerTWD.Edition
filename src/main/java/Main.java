import exception.InvalidPokemonDataException;
import model.Attack;
import model.Type;

public class Main {
    public static void main(String[] args) {
        Attack flamethrower = new Attack("Flamethrower", 90, 100, Type.FIRE);
        System.out.println(flamethrower);

        testInvalid("", 40, 100, Type.NORMAL);
        testInvalid("Tackle", 0, 100, Type.NORMAL);
        testInvalid("Tackle", 40, 101, Type.NORMAL);
        testInvalid("Tackle", 40, 100, null);
    }

    private static void testInvalid(String name, int damage, int accuracy, Type type) {
        try {
            new Attack(name, damage, accuracy, type);
            System.out.println("NOT rejected: " + name + " ← this is a bug!");
        } catch (InvalidPokemonDataException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}