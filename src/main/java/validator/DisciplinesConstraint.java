package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DisciplinesConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DisciplinesConstraint {
    String message() default "Disciplines must meet the specified conditions.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}