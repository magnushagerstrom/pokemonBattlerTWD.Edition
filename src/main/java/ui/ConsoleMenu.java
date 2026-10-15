package ui;

import battle.BattleEngine;
import exception.InvalidPokemonDataException;
import exception.PokemonNotFoundException;
import model.Pokemon;
import repository.PokemonRepository;
import service.BattleStats;
import service.PokedexService;
import service.SeedData;

import java.util.List;

public class ConsoleMenu {
    private final PokedexService service;
    private final InputHelper input;
    private final BattleEngine battleEngine;
    private final BattleStats stats;
    private final PokemonRepository repository;


    public ConsoleMenu(PokedexService service, InputHelper input, BattleEngine battleEngine,
                       BattleStats stats, PokemonRepository repository) {
        this.service = service;
        this.input = input;
        this.battleEngine = battleEngine;
        this.stats = stats;
        this.repository = repository;
    }

    public void run() {
        loadOnStartUp();
        boolean running = true;
        while (running) {
            printMenu();
            int choice = input.readIntInRange("Choose: ", 0, 9);
            try {
                switch (choice) {
                    case 1 -> listAll();
                    case 4 -> deletePokemon();
                    case 5 -> saveToFile();
                    case 6 -> loadFromFile();
                    case 7 -> resetPokedex();
                    case 8 -> startBattle();
                    case 9 -> showResults();
                    case 0 -> {
                        printWithDots("Saving then closing the program", 4);
                        saveToFile();
                        running = false;
                    }
                    default -> System.out.println("Not built yet, coming soon!");
                }
            } catch (PokemonNotFoundException | InvalidPokemonDataException e) {
                System.out.println("Something went horribly wrong!: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== POKÉMON BATTLER =====");
        System.out.println("--------TWD:Edition--------");
        System.out.println("1. List all Pokémon");
        System.out.println("2. Add Pokémon");
        System.out.println("3. Edit Pokémon");
        System.out.println("4. Delete Pokémon");
        System.out.println("5. Save to file");
        System.out.println("6. Load from file");
        System.out.println("7. Reset to starting Pokémons");
        System.out.println("8. Pick a fighter & Start battle");
        System.out.println("9. Battle results");
        System.out.println("0. Exit");
    }

    private void listAll() {
        if (service.count() == 0) {
            System.out.println("The pokedex is empty");
            return;
        }
        for (Pokemon pokemon : service.getAll()) {
            System.out.println("  " + pokemon);
        }
    }

    private void deletePokemon() {
        String name = input.readText("Name of the Pokemon to delete: ");
        service.delete(name);
        System.out.println(name + " Was deleted!");
    }

    private void resetPokedex() {
        service.resetToSeed();
        System.out.println("Pokedex reset with seed data!");
    }

    private void startBattle() {
        if (service.count() == 0) {
            System.out.println("The pokedex is empty");
            return;
        }

        List<Pokemon> myPokemon = service.getAll();
        System.out.println("Pick your fighter!");
        for (int i = 0; i < myPokemon.size(); i++) {
            System.out.println((i + 1) + ". " + myPokemon.get(i));
        }

        int choice = input.readIntInRange("Fighter: ", 1, myPokemon.size());
        Pokemon player = myPokemon.get(choice - 1);
        Pokemon wild = SeedData.createWildOpponent();
        System.out.println("Opponent is: " + wild.getName() + ". Fight with honor and glory!");

        for (int i = 3; i >= 1; i--) {
            System.out.println(i + "...");
            pause(1000);
        }
        System.out.println("FIGHT!");

        boolean won = battleEngine.fight(player, wild);
        stats.recordBattle(player.getName(), won);
        player.restoreHp();
    }

    private void showResults() {
        if (stats.getTotalBattles() == 0) {
            System.out.println("No fight has been fought!\nFor honor and glory start a fight with option 8!");
            return;
        }

        int winRate = stats.getWins() * 100 / stats.getTotalBattles();

        System.out.println("===== BATTLE RESULTS =====");
        System.out.println("Battles: " + stats.getTotalBattles());
        System.out.println("Wins:    " + stats.getWins());
        System.out.println("Losses:  " + stats.getLosses());
        System.out.println("Win rate: " + winRate + "%");

        System.out.println("---Per Pokemon---");
        for (String name : stats.getUsedPokemonNames()) {
            int pokemonWins = stats.getWinsFor(name);
            int pokemonLosses = stats.getLossesFor(name);
            int pokemonRate = pokemonWins * 100 / (pokemonWins + pokemonLosses);
            System.out.println(name + ": " + pokemonWins + "W / " + pokemonLosses + "L (" + pokemonRate + "%)");
        }

        System.out.println("Most used Fighter: " + stats.getMostUsedPokemon());
        System.out.println("Most used Attack: " + stats.getMostUsedAttack());
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void loadOnStartUp() {
        try {
            List<Pokemon> loaded = repository.load();
            if (loaded.isEmpty()) {
                printWithDots("Ooops, no saved Pokemon found. Loading default Pokedex", 6);
                service.resetToSeed();
            } else {
                service.replaceAll(loaded);
                printWithDots("Loaded " + loaded.size() + " Pokemons from saved file", 4);
            }
        } catch (InvalidPokemonDataException e) {
            System.out.println("Saved data was invalid (" + e.getMessage() + "). Loading default Pokedex ");
            service.resetToSeed();
        }
    }

    private void saveToFile() {
        repository.save(service.getAll());
        printWithDots("Saving", 4);
        System.out.println("...SUCCESS...");
    }

    private void loadFromFile() {
        List<Pokemon> loaded = repository.load();
        if (loaded.isEmpty()) {
            System.out.println("Sorry! Nothing to load. Keeping the current Pokedex.");
            return;
        }

        service.replaceAll(loaded);
        printWithDots("Loaded " + loaded.size() + " Pokemon from saved file.", 4);
    }


    private void printWithDots(String message, int dots) {
        System.out.print(message);
        for (int i = 0; i < dots; i++) {
            pause(500);
            System.out.print(".");
        }
        System.out.println();
    }
}