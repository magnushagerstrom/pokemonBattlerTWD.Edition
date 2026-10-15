package repository;

import model.Type;

import java.util.ArrayList;
import java.util.List;

public class PokemonData {
    public String name;
    public Type type;
    public int maxHp;
    public List<AttackData> attacks = new ArrayList<>();

    public PokemonData() {
    }


}
