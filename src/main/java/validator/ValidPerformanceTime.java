package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PerformanceTimeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPerformanceTime {
    String message() default "Invalid performance time";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}