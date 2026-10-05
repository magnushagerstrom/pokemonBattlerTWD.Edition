package model;

import exception.InvalidPokemonDataException;

public abstract class Attack {
    private final String name;
    private final int accuracy;

    protected Attack(String name, int accuracy) {
        if (name == null || name.isBlank()) {
            throw new InvalidPokemonDataException("Attack name cannot be null or empty");
        }
        if (accuracy < 1 || accuracy > 100) {
            throw new InvalidPokemonDataException("Accuracy must be between 1 and 100, was " + accuracy);
        }
        this.name = name.trim();
        this.accuracy = accuracy;
    }

    public String getName() {
        return name;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public abstract void execute(Pokemon attacker, Pokemon defender);

    @Override
    public String toString() {
        return name + " (" + accuracy + "% accuracy)";
    }
}