package model;

import exception.InvalidPokemonDataException;

public class Attack {
    private final String name;
    private final int baseDamage;
    private final int accuracy;
    private final Type type;

    public Attack(String name, int baseDamage, int accuracy, Type type) {
        if (name == null || name.isBlank()) {
            throw new InvalidPokemonDataException("Attack name cannot be null or empty");
        }

        if (baseDamage <= 0) {
            throw new InvalidPokemonDataException("Base damage must be greater than 0");
        }

        if (accuracy < 1 || accuracy > 100) {
            throw new InvalidPokemonDataException("Accuracy must be between 1 and 100");
        }

        if (type == null) {
            throw new InvalidPokemonDataException("Attack type cannot be null");
        }


        this.name = name.trim();
        this.baseDamage = baseDamage;
        this.accuracy = accuracy;
        this.type = type;


    }

    public String getName() {
        return name;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + "(" + type.getLabel() + ", " + baseDamage + "dmg, " + accuracy + "% accuracy)";
    }

}
