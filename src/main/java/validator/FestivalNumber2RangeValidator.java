package validator;

import domain.Performance;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FestivalNumber2RangeValidator implements ConstraintValidator<ValidFestivalNumber2RangeConstraint, Performance> {

    private int maxDifference;

    @Override
    public void initialize(ValidFestivalNumber2RangeConstraint constraintAnnotation) {
        this.maxDifference = constraintAnnotation.maxDifference();
    }

    @Override
    public boolean isValid(Performance performance, ConstraintValidatorContext context) {
        if (performance == null) {
            return true;
        }

        int festivalNumber1 = performance.getFestivalNumber1();
        int festivalNumber2 = performance.getFestivalNumber2();

        if (festivalNumber2 < festivalNumber1) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{performance.festivalNumber2.greaterOrEqual}")
                    .addPropertyNode("festivalNumber2")
                    .addConstraintViolation();
            return false;
        }

        if (festivalNumber2 > festivalNumber1 + maxDifference) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{performance.festivalNumber2.notHigher}")
                    .addPropertyNode("festivalNumber2")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}