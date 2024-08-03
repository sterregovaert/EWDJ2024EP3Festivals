package exceptions;

public class FestivalNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FestivalNotFoundException(Integer festivalId) {
        super("Festival not found with ID: " + festivalId);
    }
}