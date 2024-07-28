package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FestivalNumberValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFestivalNumbersConstraint {
    String message() default "{performance.validFestivalNumbersConstraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}