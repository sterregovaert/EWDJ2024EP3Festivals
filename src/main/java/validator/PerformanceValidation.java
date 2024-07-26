package validator;

import domain.Performance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        // check if performances subgenres are different
        if (performance.getSubGenres() != null && performance.getSubGenres().size() == 2) {
            var subGenresList = performance.getSubGenres().stream().collect(Collectors.toList());

            if (subGenresList.get(0).equals(subGenresList.get(1))) {
                errors.rejectValue("subGenres", "subGenres.same", "Subgenres must be different");
            }
        }

        // check if festivalNumber1 is divisible by 3
        if (performance.getFestivalNumber1() % 3 != 0) {
            errors.rejectValue("festivalNumber1", "festivalNumber1.notDivisibleBy3", "Festival number must be divisible by 3");
        }

    }
}