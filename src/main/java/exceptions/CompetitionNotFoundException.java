package exceptions;

public class CompetitionNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CompetitionNotFoundException(Integer id) {
        super(String.format("Could not find competition %s", id));
    }
}