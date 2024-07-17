package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DifferentFirstAndLastDigitValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentFirstAndLastDigit {
    String message() default "Het eerste en het laatste cijfer moeten verschillend zijn.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}