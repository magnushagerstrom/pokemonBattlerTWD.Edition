import battle.BattleEngine;
import battle.RandomStrategy;
import service.BattleStats;
import service.PokedexService;
import ui.ConsoleMenu;
import ui.InputHelper;

public class Main {
    public static void main(String[] args) {
        PokedexService service = new PokedexService();
        service.resetToSeed();

        InputHelper input = new InputHelper();
        BattleEngine battleEngine = new BattleEngine(input, new RandomStrategy());
        BattleStats stats = new BattleStats();
        ConsoleMenu menu = new ConsoleMenu(service, input, battleEngine, stats);
        menu.run();
    }
}