package validator;

import domain.Performance;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.stream.Collectors;

public class PerformanceValidation implements Validator {

    @Override
    public boolean supports(Class<?> klass) {
        return Performance.class.isAssignableFrom(klass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Performance performance = (Performance) target;

        if (performance.getSubGenres() != null && performance.getSubGenres().size() == 2) {
            var subGenresList = performance.getSubGenres().stream().collect(Collectors.toList());
            if (subGenresList.get(0).equals(subGenresList.get(1))) {
                errors.rejectValue("subGenres", "subGenres.same", "Subgenres must be different");
            }
        }
    }
}