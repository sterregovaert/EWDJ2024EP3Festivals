package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimeAfterValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeAfter {
    String value();
    String message() default "Invalid time";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}