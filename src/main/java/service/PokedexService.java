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

    public void update(String oldName, Pokemon updated) {
        if (updated == null) {
            throw new IllegalArgumentException("Updated Pokemon cannot be null");
        }
        Pokemon existing = findByName(oldName);
        String oldKey = existing.getName().toLowerCase();
        String newKey = updated.getName().toLowerCase();

        if (newKey.equals(oldKey)) {
            pokedex.put(oldKey, updated);
            return;
        }

        if (pokedex.containsKey(newKey)) {
            throw new InvalidPokemonDataException("A Pokemon named " + updated.getName() + " already exists");
        }
        pokedex.remove(oldKey);
        pokedex.put(newKey, updated);
    }

    public List<Pokemon> getAll() {
        return List.copyOf(pokedex.values());
    }

    public void replaceAll(List<Pokemon> pokemons) {
        Map<String, Pokemon> fresh = new LinkedHashMap<>();
        for (Pokemon pokemon : pokemons) {
            String key = pokemon.getName().toLowerCase();
            if (fresh.containsKey(key)) {
                throw new InvalidPokemonDataException("Duplicate Pokémon in data: " + pokemon.getName());
            }
            fresh.put(key, pokemon);
        }
        pokedex.clear();
        pokedex.putAll(fresh);
    }

    public void resetToSeed() {
        replaceAll(SeedData.createStarterPokemon());
    }
}