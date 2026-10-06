import model.Attack;
import model.DamageAttack;
import model.HealAttack;
import model.Pokemon;
import model.Type;

import java.util.List;

public class Playground {
    public static void main(String[] args) {
        Attack arrow = new DamageAttack("Arrow", 28, 85, Type.FIRE);
        Attack firstAid = new HealAttack("First Aid", 25, 100);

        Pokemon rick = new Pokemon("Rick Grimes", Type.FIRE, 78, List.of(arrow));
        Pokemon carol = new Pokemon("Carol Peltier", Type.GRASS, 78, List.of(firstAid));

        System.out.println("Start:   " + carol);

        arrow.execute(rick, carol);
        System.out.println("         " + carol);

        firstAid.execute(carol, rick);
        System.out.println("         " + carol);

        firstAid.execute(carol, rick);
        System.out.println("         " + carol);

        firstAid.execute(carol, rick);
        System.out.println("         " + carol);
    }
}