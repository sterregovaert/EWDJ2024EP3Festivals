package exceptions;

public class GenreNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GenreNotFoundException(String message) {
        super(message);
    }
}