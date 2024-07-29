package exceptions;

public class FestivalNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FestivalNotFoundException(Integer id) {
        super(String.format("Could not find festival with festivalId %s", id));
    }
}