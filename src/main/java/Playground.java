import model.Attack;
import model.DamageAttack;
import model.Pokemon;
import model.Type;

import java.util.List;

public class Playground {
    public static void main(String[] args) {
        Attack arrow = new DamageAttack("Arrow", 28, 85, Type.FIRE);
        Attack bluntForce = new DamageAttack("Blunt Force", 20, 100, Type.WATER);
        Attack tackle = new DamageAttack("Tackle", 12, 95, Type.NORMAL);

        Pokemon rick = new Pokemon("Rick Grimes", Type.FIRE, 78, List.of(arrow));
        Pokemon carol = new Pokemon("Carol Peltier", Type.GRASS, 78, List.of(tackle));
        Pokemon maggie = new Pokemon("Maggie Greene", Type.WATER, 88, List.of(bluntForce));

        List<Attack> moves = List.of(arrow, bluntForce, tackle);
        List<Pokemon> targets = List.of(carol, rick, maggie);

        for (int i = 0; i < moves.size(); i++) {
            moves.get(i).execute(rick, targets.get(i));
            System.out.println("  → " + targets.get(i));
        }
    }
}
