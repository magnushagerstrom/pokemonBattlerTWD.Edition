import battle.BattleEngine;
import battle.RandomStrategy;
import repository.PokemonRepository;
import service.BattleStats;
import service.PokedexService;
import ui.ConsoleMenu;
import ui.InputHelper;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        PokedexService service = new PokedexService();
        InputHelper input = new InputHelper();
        BattleStats stats = new BattleStats();
        BattleEngine battleEngine = new BattleEngine(input, new RandomStrategy(), stats);
        PokemonRepository repository = new PokemonRepository(Path.of("pokedex.json"));

        ConsoleMenu menu = new ConsoleMenu(service, input, battleEngine, stats, repository);
        menu.run();
    }
}