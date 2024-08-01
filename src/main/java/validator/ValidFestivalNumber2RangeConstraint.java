package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FestivalNumber2RangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFestivalNumber2RangeConstraint {
    String message() default "{performance.festivalNumber2.range}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}