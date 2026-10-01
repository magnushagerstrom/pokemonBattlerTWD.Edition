import model.Pokemon;
import service.PokedexService;

public class Main {
    public static void main(String[] args) {
        PokedexService service = new PokedexService();
        service.resetToSeed();

        System.out.println("Seeded " + service.count() + " Pokémon:");
        for (Pokemon pokemon : service.getAll()) {
            System.out.println("  " + pokemon + " " + pokemon.getAttacks());
        }

        service.delete("Negan");
        System.out.println("After delete: " + service.count());

        service.resetToSeed();
        System.out.println("After reset: " + service.count());
    }
}