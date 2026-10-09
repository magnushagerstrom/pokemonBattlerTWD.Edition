package battle;

import model.Attack;
import model.Pokemon;
import ui.InputHelper;

import java.util.List;
import java.util.Random;

public class BattleEngine {
    private final InputHelper input;
    private final Random random = new Random();
    private final CpuStrategy cpuStrategy;

    public BattleEngine(InputHelper input, CpuStrategy cpuStrategy) {
        this.input = input;
        this.cpuStrategy = cpuStrategy;
    }

    public void useAttack(Attack attack, Pokemon attacker, Pokemon defender) {
        boolean hit = random.nextInt(100) < attack.getAccuracy();
        if (hit) {
            attack.execute(attacker, defender);
        } else {
            System.out.println(attacker.getName() + " used " + attack.getName() + ".... but missed!");
        }
    }

    public Attack choosePlayerAttack(Pokemon pokemon) {
        List<Attack> attacks = pokemon.getAttacks();
        System.out.println("Choose an attack for " + pokemon.getName() + ": ");
        for (int i = 0; i < attacks.size(); i++) {
            System.out.println(i + 1 + ". " + attacks.get(i));
        }
        int choice = input.readIntInRange("Attack: ", 1, attacks.size());
        return attacks.get(choice - 1);

    }

    public Attack chooseCpuAttack(Pokemon pokemon) {
        return cpuStrategy.chooseAttack(pokemon);
    }

    public boolean fight(Pokemon player, Pokemon cpu) {
        player.restoreHp();
        cpu.restoreHp();
        System.out.println(player.getName() + " vs " + cpu.getName() + "!");

        boolean playerTurn = random.nextBoolean();
        while (!player.isFainted() && !cpu.isFainted()) {
            if (playerTurn) {
                Attack attack = choosePlayerAttack(player);
                useAttack(attack, player, cpu);
            } else {
                Attack attack = chooseCpuAttack(cpu);
                useAttack(attack, cpu, player);
            }
            playerTurn = !playerTurn;
            System.out.println(player + " | " + cpu);

        }
        boolean playerWon = cpu.isFainted();
        if (playerWon) {
            System.out.println("You won! " + player.getName() + " defeated " + cpu.getName() + ".");
        } else {
            System.out.println("You lost... " + player.getName() + " was defeated by " + cpu.getName() + ".");
        }
        return playerWon;

    }

}