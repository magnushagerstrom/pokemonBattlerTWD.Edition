package model;

import exception.InvalidPokemonDataException;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    private final String name;
    private final Type type;
    private final int maxHp;
    private int currentHp;
    private final List<Attack> attacks;

    public Pokemon(String name, Type type, int maxHp, List<Attack> attacks) {
        if (name == null || name.isBlank()) {
            throw new InvalidPokemonDataException("Name cannot be null or empty");
        }
        if (type == null) {
            throw new InvalidPokemonDataException("Type cannot be null");
        }
        if (maxHp <= 0 || maxHp > 100) {
            throw new InvalidPokemonDataException("Max HP must be between 1 and 100, was " + maxHp);
        }
        if (attacks == null) {
            throw new InvalidPokemonDataException("Attack list cannot be null");
        }
        if (attacks.isEmpty() || attacks.size() > 4) {
            throw new InvalidPokemonDataException(
                    "A Pokémon must have 1 to 4 attacks, had " + attacks.size());
        }
        boolean hasDamageAttack = false;
        for (Attack attack : attacks) {
            if (attack == null) {
                throw new InvalidPokemonDataException("You need to add a minimum of 1 attack");
            }
            if (attack instanceof DamageAttack) {
                hasDamageAttack = true;
            }
        }
        if (!hasDamageAttack) {
            throw new InvalidPokemonDataException("A Pokémon needs at least one damage attack");
        }

        this.name = name.trim();
        this.type = type;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attacks = new ArrayList<>(attacks);
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public List<Attack> getAttacks() {
        return List.copyOf(attacks);
    }

    public void takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Damage amount must be non-negative, was " + amount);
        }
        currentHp = currentHp - amount;
        if (currentHp < 0) {
            currentHp = 0;
        }
    }

    public void heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Heal amount cannot be negative, was " + amount);
        }
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public boolean isFainted() {
        return currentHp ==0;
    }

    public void restoreHp() {
        currentHp = maxHp;
    }

    @Override
    public String toString() {
        return name + " (" + type.getLabel() + ", HP " + currentHp + "/" + maxHp + ")";
    }

}