package battle;

import model.Attack;
import model.Pokemon;

import java.util.List;
import java.util.Random;

public class RandomStrategy implements CpuStrategy {

    private final Random random = new Random();

    @Override
    public Attack chooseAttack(Pokemon pokemon) {
        List<Attack> attacks = pokemon.getAttacks();
        int index = random.nextInt(attacks.size());
        return attacks.get(index);
    }
}