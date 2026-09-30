package exception;

public class InvalidPokemonDataException extends RuntimeException {
    public InvalidPokemonDataException(String message) {
        super(message);
    }
}