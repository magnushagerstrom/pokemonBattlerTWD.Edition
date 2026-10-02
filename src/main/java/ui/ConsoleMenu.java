package ui;

import model.Pokemon;
import service.PokedexService;

public class ConsoleMenu {
    private final PokedexService service;
    private final InputHelper input;

    public ConsoleMenu(PokedexService service, InputHelper input) {
        this.service = service;
        this.input = input;
    }

    public void run() {
      boolean running = true;
      while (running) {
          printMenu();
          int choice = input.readIntInRange("Choose ",0, 9);
          switch (choice) {
              case 1 -> listAll();
              case 0 -> running = false;
              default -> System.out.println("Not built yet, coming soon!");
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
        System.out.println("7. Reset to starting Pokémon");
        System.out.println("8. Start battle");
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

}
