package repository;

import java.util.LinkedHashMap;
import java.util.Map;

public class StatsData {
    public int wins;
    public int losses;
    public Map<String, Integer> pokemonUses = new LinkedHashMap<>();
    public Map<String, Integer> pokemonWins = new LinkedHashMap<>();
    public Map<String, Integer> pokemonLosses = new LinkedHashMap<>();
    public Map<String, Integer> attackUses = new LinkedHashMap<>();

    public StatsData() {
    }
}