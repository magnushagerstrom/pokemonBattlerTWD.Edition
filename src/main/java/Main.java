import battle.BattleEngine;
import battle.RandomStrategy;
import service.PokedexService;
import ui.ConsoleMenu;
import ui.InputHelper;

public class Main {
    public static void main(String[] args) {
        PokedexService service = new PokedexService();
        service.resetToSeed();

        InputHelper input = new InputHelper();
        BattleEngine battleEngine = new BattleEngine(input, new RandomStrategy());
        ConsoleMenu menu = new ConsoleMenu(service, input, battleEngine);
        menu.run();
    }
}