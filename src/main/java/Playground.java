import battle.BattleEngine;
import battle.RandomStrategy;
import model.Attack;
import model.DamageAttack;
import model.Pokemon;
import model.Type;
import ui.InputHelper;

import java.util.List;

public class Playground {
    public static void main(String[] args) {
        BattleEngine engine = new BattleEngine(new InputHelper(), new RandomStrategy());

        Attack fist = new DamageAttack("Fist", 30, 50, Type.NORMAL);
        Attack arrow = new DamageAttack("Arrow", 30, 50, Type.NORMAL);
        Pokemon daryl = new Pokemon("Daryl Dixon", Type.ELECTRIC, 78, List.of(arrow, fist));
        Pokemon negan = new Pokemon("Negan", Type.WATER, 100, List.of(arrow, fist));

        boolean won = engine.fight(daryl, negan);
        System.out.println("Did the player win? " + won);
    }
}