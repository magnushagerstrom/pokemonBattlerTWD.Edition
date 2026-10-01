package service;

import exception.InvalidPokemonDataException;
import exception.PokemonNotFoundException;
import model.Pokemon;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PokedexService {

    private final Map<String, Pokemon> pokedex = new LinkedHashMap<>();

    public void add(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon cannot be null");
        }


        String key = pokemon.getName().toLowerCase();
        if (pokedex.containsKey(key)) {
            throw new InvalidPokemonDataException("Pokemon already exists in the pokedex");
        }

        pokedex.put(key, pokemon);
    }

    public int count() {
        return pokedex.size();
    }

    public Pokemon findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new PokemonNotFoundException("No name was given, enter name");
        }

        Pokemon found = pokedex.get(name.trim().toLowerCase());
        if (found == null) {
            throw new PokemonNotFoundException("No Pokemon named " + name.trim() + " in the Pokedex.");
        }
        return found;
    }

    public void delete(String name) {
        findByName(name);
        pokedex.remove(name.trim().toLowerCase());
    }

    public List<Pokemon> getAll() {
        return List.copyOf(pokedex.values());
    }
}
