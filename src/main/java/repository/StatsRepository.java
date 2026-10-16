package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.InvalidPokemonDataException;
import service.BattleStats;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StatsRepository {
    private final Path path;
    private final ObjectMapper mapper = new ObjectMapper();

    public StatsRepository(Path path) {
        this.path = path;
    }

    // Save the battle stats to the file
    public void save(BattleStats stats) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), stats.toData());
            System.out.println("Saved battle stats to " + path);
        } catch (IOException e) {
            System.out.println("Could not save battle stats to " + path + ": " + e.getMessage());
        }
    }

    // Load the battle stats from the file into the existing BattleStats object
    public void loadInto(BattleStats stats) {
        if (!Files.exists(path)) {
            return;
        }
        try {
            StatsData data = mapper.readValue(path.toFile(), StatsData.class);
            stats.loadFrom(data);
            System.out.println("Loaded battle stats from " + path);
        } catch (IOException | InvalidPokemonDataException e) {
            System.out.println("Could not load battle stats from " + path + ": " + e.getMessage());
        }
    }


}
