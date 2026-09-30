package model;

public enum Type {
    FIRE("Fire"),
    WATER("Water"),
    GRASS("Grass"),
    ELECTRIC("Electric"),
    NORMAL("Normal");

    private final String label;

    Type(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public double effectivenessAgainst(Type defender) {
        if (defender == null) {
            throw new IllegalArgumentException("Defending type cannot be null");
        }

        return switch (this) {
            case FIRE -> switch (defender) {
                case GRASS -> 2.0;
                case WATER -> 0.5;
                case ELECTRIC -> 1.0;
                default -> 1.0;
            };

            case GRASS -> switch (defender) {
                case FIRE -> 0.5;
                case ELECTRIC -> 1.0;
                case WATER -> 2.0;
                default -> 1.0;
            };

            case WATER -> switch (defender) {
                case FIRE -> 2.0;
                case ELECTRIC -> 1.0;
                case GRASS -> 0.5;
                default -> 1.0;
            };

            case ELECTRIC -> switch (defender) {
                case GRASS -> 1.0;
                case WATER -> 2.0;
                case FIRE -> 1.0;
                default -> 1.0;
            };

            case NORMAL -> 1.0;
        };
    }
}

