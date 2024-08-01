package exceptions;

public class NoArtistsException extends RuntimeException {
    public NoArtistsException(String message) {
        super(message);
    }
}