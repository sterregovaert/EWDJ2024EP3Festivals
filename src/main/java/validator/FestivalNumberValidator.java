package validator;

import domain.Performance;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FestivalNumberValidator implements ConstraintValidator<ValidFestivalNumbersConstraint, Performance> {
    @Override
    public boolean isValid(Performance performance, ConstraintValidatorContext context) {
        if (performance == null) {
            return true;
        }
        return performance.getFestivalNumber2() >= performance.getFestivalNumber1() &&
               performance.getFestivalNumber2() <= performance.getFestivalNumber1() + 1000;
    }
}