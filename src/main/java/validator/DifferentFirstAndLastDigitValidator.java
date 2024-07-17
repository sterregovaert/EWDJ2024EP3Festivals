package validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DifferentFirstAndLastDigitValidator implements ConstraintValidator<DifferentFirstAndLastDigit, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        int number = value;
        int lastDigit = number % 10;

        while (number >= 10) {
            number /= 10;
        }
        int firstDigit = number;

        return firstDigit != lastDigit;
    }
}