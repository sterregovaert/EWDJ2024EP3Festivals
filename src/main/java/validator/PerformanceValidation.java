package validator;

import domain.Performance;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class PerformanceValidation implements Validator {

    @Override
    public boolean supports(Class<?> klass) {
        return Performance.class.isAssignableFrom(klass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Performance performance = (Performance) target;

        // check if festivalNumber1 is divisible by 3
        if (performance.getFestivalNumber1() % 3 != 0) {
            errors.rejectValue("festivalNumber1", "festivalNumber1.notDivisibleBy3", "Festival number must be divisible by 3");
        }

    }
}