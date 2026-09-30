import model.Type;

public class Main {
    public static void main(String[] args) {
        for (Type attacker : Type.values()) {
            for (Type defender : Type.values()) {
                System.out.println(attacker + " -> " + defender + ": "
                        + attacker.effectivenessAgainst(defender));
            }
        }
    }
}