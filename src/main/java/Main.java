import service.PokedexService;
import ui.ConsoleMenu;
import ui.InputHelper;

public class Main {
    public static void main(String[] args) {
        PokedexService service = new PokedexService();
        service.resetToSeed();

        InputHelper input = new InputHelper();
        ConsoleMenu menu = new ConsoleMenu(service, input);
        menu.run();
    }
}