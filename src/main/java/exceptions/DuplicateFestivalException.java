package exceptions;

public class DuplicateFestivalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateFestivalException(Integer id) {
        super(String.format("Festival with id %s already exists", id));
    }
}