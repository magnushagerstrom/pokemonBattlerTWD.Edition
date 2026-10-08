package battle;

import model.Attack;
import model.Pokemon;
import ui.InputHelper;

import java.util.Random;

public class BattleEngine {
    private final InputHelper input;
    private final Random random = new Random();

    public BattleEngine(InputHelper input) {
        this.input = input;
    }

    public void useAttack(Attack attack, Pokemon attacker, Pokemon defender) {
        boolean hit = random.nextInt(100) < attack.getAccuracy();
        if (hit) {
            attack.execute(attacker, defender);
        } else {
            System.out.println(attacker.getName() + " used " + attack.getName() + ".... but missed!");
        }
    }

}
