package ui;

import model.Attack;
import model.DamageAttack;
import model.HealAttack;
import model.Type;

import java.util.NoSuchElementException;

import battle.BattleEngine;
import exception.InvalidPokemonDataException;
import exception.PokemonNotFoundException;
import model.Pokemon;
import repository.PokemonRepository;
import repository.StatsRepository;
import service.BattleStats;
import service.PokedexService;
import service.SeedData;

import java.util.List;
import java.util.ArrayList;

public class ConsoleMenu {
    private final PokedexService service;
    private final InputHelper input;
    private final BattleEngine battleEngine;
    private final BattleStats stats;
    private final PokemonRepository repository;
    private final StatsRepository statsRepository;


    public ConsoleMenu(PokedexService service, InputHelper input, BattleEngine battleEngine,
                       BattleStats stats, PokemonRepository repository, StatsRepository statsRepository) {
        this.service = service;
        this.input = input;
        this.battleEngine = battleEngine;
        this.stats = stats;
        this.repository = repository;
        this.statsRepository = statsRepository;
    }

    public void run() {
        loadOnStartUp();
        boolean running = true;
        try {
            while (running) {
                printMenu();
                int choice = input.readIntInRange("Choose: ", 0, 9);
                try {
                    switch (choice) {
                        case 1 -> listAll();
                        case 2 -> addPokemon();
                        case 4 -> deletePokemon();
                        case 5 -> saveToFile();
                        case 6 -> loadFromFile();
                        case 7 -> resetPokedex();
                        case 8 -> startBattle();
                        case 9 -> showResults();
                        case 0 -> {
                            saveToFile();
                            System.out.println("Closing the program. See you, survivor!");
                            running = false;
                        }
                        default -> System.out.println("Not built yet, coming soon!");
                    }
                } catch (PokemonNotFoundException | InvalidPokemonDataException e) {
                    System.out.println("Something went horribly wrong!: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("Input was closed. Saving and exiting...");
            saveToFile();
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

    private void addPokemon() {
        System.out.println("--- Add a new Pokémon ---");
        String name = input.readText("Name: ");
        Type type = readType();
        int maxHp = input.readIntInRange("Max HP (1-100): ", 1, 100);
        List<Attack> attacks = readAttacks();

        Pokemon pokemon = new Pokemon(name, type, maxHp, attacks);
        service.add(pokemon);
        System.out.println(pokemon.getName() + " joined the Pokédex!");
    }

    private Type readType() {
        Type[] types = Type.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i].getLabel());
        }
        int choice = input.readIntInRange("Type: ", 1, types.length);
        return types[choice - 1];
    }

    private List<Attack> readAttacks() {
        int count = input.readIntInRange("Number of attacks (1-4): ", 1, 4);
        List<Attack> attacks = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            System.out.println("--- Attack " + i + " of " + count + " ---");
            attacks.add(readAttack());
        }
        return attacks;
    }

    private Attack readAttack() {
        String name = input.readText("Attack name: ");
        int kind = input.readIntInRange("1. Damage attack  2. Heal attack: ", 1, 2);
        int accuracy = input.readIntInRange("Accuracy (1-100): ", 1, 100);

        if (kind == 1) {
            int baseDamage = input.readIntInRange("Base damage (1-100): ", 1, 100);
            System.out.println("Attack type:");
            Type attackType = readType();
            return new DamageAttack(name, baseDamage, accuracy, attackType);
        }

        int healAmount = input.readIntInRange("Heal amount (1-100): ", 1, 100);
        return new HealAttack(name, healAmount, accuracy);
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
        statsRepository.loadInto(stats);
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
        printWithDots("Saving", 4);
        repository.save(service.getAll());
        statsRepository.save(stats);

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