package validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PerformanceFestivalNumber1Validator implements ConstraintValidator<ValidFestivalNumber1, Integer> {

    @Override
    public void initialize(ValidFestivalNumber1 constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Use @NotNull for null checks
        }
        return value % 3 == 0;
    }

}