package service;

public class BattleStats {
    private int wins;
    private int losses;

    public void recordBattle(boolean won) {
        if (won) {
            wins++;
        } else {
            losses++;
        }
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getTotalBattles() {
        return wins+losses;
    }


}
