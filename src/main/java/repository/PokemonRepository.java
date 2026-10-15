package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.InvalidPokemonDataException;
import model.Pokemon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PokemonRepository {
    private final Path path;
    private final ObjectMapper mapper = new ObjectMapper();

    public PokemonRepository(Path path) {
        this.path = path;
    }

    // Save a list of Pokemon to the specified path
    public void save(List<Pokemon> pokemons) {
        List<PokemonData> dataList = new ArrayList<>();
        for (Pokemon pokemon : pokemons) {
            dataList.add(PokemonMapper.toPokemonData(pokemon));
        }

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), dataList);
            System.out.println("Saved " + pokemons.size() + " Pokémon to " + path);
        } catch (IOException e) {
            System.out.println("Could not save to " + path + ": " + e.getMessage());
        }
    }


    // Load a list of Pokemon from the specified path
    public List<Pokemon> load() {
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }

        try {
            PokemonData[] dataArray = mapper.readValue(path.toFile(), PokemonData[].class);
            List<Pokemon> result = new ArrayList<>();
            if (dataArray == null) {
                return result;
            }
            for (PokemonData data : dataArray) {
                result.add(PokemonMapper.toPokemon(data));
            }
            return result;
        } catch (IOException | InvalidPokemonDataException e) {
            System.out.println("Could not load " + path + " (corrupt or wrong format): " + e.getMessage());
            return new ArrayList<>();
        }
    }
}