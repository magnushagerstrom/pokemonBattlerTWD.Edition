package model;

import exception.InvalidPokemonDataException;
import java.util.Random;


public class DamageAttack extends Attack {
    private static final Random random = new Random();
    private static final int CRIT_CHANCE = 16;
    private static final int CRIT_MULTIPLIER = 2;
    private final int baseDamage;
    private final Type type;

    public DamageAttack(String name, int baseDamage, int accuracy, Type type) {
        super(name, accuracy);
        if (baseDamage <= 0) {
            throw new InvalidPokemonDataException("Base damage must be greater than 0, was " + baseDamage);
        }
        if (type == null) {
            throw new InvalidPokemonDataException("Attack type cannot be null");
        }
        this.baseDamage = baseDamage;
        this.type = type;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void execute(Pokemon attacker, Pokemon defender) {
        double multiplier = type.effectivenessAgainst(defender.getType());

        boolean critical = random.nextInt(CRIT_CHANCE) == 0;
        if (critical) {
            multiplier = multiplier * CRIT_MULTIPLIER;
        }

        int damage = (int) Math.round(baseDamage * multiplier);
        defender.takeDamage(damage);
        System.out.println(attacker.getName() + " used " + getName() + "! "
                + defender.getName() + " took " + damage + " damage.");

        if (critical) {
            System.out.println("BAZINGA! That's a critical hit! Damage doubled!");
        }
    }

    @Override
    public String toString() {
        return getName() + " (" + type.getLabel() + ", " + baseDamage + " dmg, "
                + getAccuracy() + "% accuracy)";
    }
}