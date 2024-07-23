package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FestivalNumberValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFestivalNumbersConstraint {
    String message() default "festivalNumber2 must be greater than or equal to festivalNumber1 and not higher than festivalNumber1 + 1000";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}