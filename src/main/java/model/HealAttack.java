package model;

import exception.InvalidPokemonDataException;

public class HealAttack extends Attack {
    private final int healAmount;

    public HealAttack(String name, int healAmount, int accuracy) {
        super(name, accuracy);
        if (healAmount <= 0) {
            throw new InvalidPokemonDataException("Heal amount must be greater than 0");
        }
        this.healAmount = healAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }

    @Override
    public void execute(Pokemon attacker, Pokemon defender) {
        int before = attacker.getCurrentHp();
        attacker.heal(healAmount);
        int healed = attacker.getCurrentHp() - before;
        System.out.println(attacker.getName() + " used " + getName() + "! "
                + attacker.getName() + " healed " + healed + " HP.");
    }

    @Override
    public String toString() {
        return getName() + " (heals " + healAmount + " HP, " + getAccuracy() + "% accuracy)";
    }
}