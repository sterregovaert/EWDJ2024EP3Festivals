package validator;

import domain.Performance;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FestivalNumber2RangeValidator implements ConstraintValidator<ValidFestivalNumber2RangeConstraint, Performance> {

    private static final int MAX_DIFFERENCE = 1000;

    @Override
    public boolean isValid(Performance performance, ConstraintValidatorContext context) {
        if (performance == null) {
            return true;
        }

        int festivalNumber1 = performance.getFestivalNumber1();
        int festivalNumber2 = performance.getFestivalNumber2();

        if (festivalNumber2 < festivalNumber1 || festivalNumber2 > festivalNumber1 + MAX_DIFFERENCE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{performance.festivalNumber2.range}")
                    .addPropertyNode("festivalNumber2")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    @Override
    public void initialize(ValidFestivalNumber2RangeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}