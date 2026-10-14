package service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BattleStats {
    private int wins;
    private int losses;

    private final Map<String, Integer> pokemonUses = new LinkedHashMap<>();
    private final Map<String, Integer> pokemonWins = new LinkedHashMap<>();
    private final Map<String, Integer> pokemonLosses = new LinkedHashMap<>();
    private final Map<String, Integer> attackUses = new LinkedHashMap<>();

    public void recordBattle(String pokemonName, boolean won) {
        addOne(pokemonUses, pokemonName);
        if (won) {
            wins++;
            addOne(pokemonWins, pokemonName);
        } else {
            losses++;
            addOne(pokemonLosses, pokemonName);
        }
    }

    public void recordAttack(String attackName) {
        addOne(attackUses, attackName);

    }

    private void addOne(Map<String, Integer> counter, String key) {
        int current = counter.getOrDefault(key, 0);
        counter.put(key, current + 1);
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getTotalBattles() {
        return wins + losses;
    }

    public int getWinsFor(String pokemonName) {
        return pokemonWins.getOrDefault(pokemonName, 0);
    }

    public int getLossesFor(String pokemonName) {
        return pokemonLosses.getOrDefault(pokemonName, 0);
    }

    public List<String> getUsedPokemonNames() {
        return List.copyOf(pokemonUses.keySet());
    }

    public String getMostUsedAttack() {
        return findMostUsed(attackUses);
    }

    public String getMostUsedPokemon() {
        return findMostUsed(pokemonUses);
    }

    private String findMostUsed(Map<String, Integer> counter) {
        String bestName = "-";
        int bestCount = 0;

        for (String name : counter.keySet()) {
            int count = counter.get(name);
            if (count > bestCount) {
                bestName = name;
                bestCount = count;
            }
        }
        return bestName;
    }
}
