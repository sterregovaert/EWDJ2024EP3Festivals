package exceptions;

public class DuplicateCompetitionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateCompetitionException(Integer id) {
        super(String.format("Competition with id %s already exists", id));
    }
}