package validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeAfterValidator implements ConstraintValidator<TimeAfter, LocalTime> {

    private LocalTime time;

    @Override
    public void initialize(TimeAfter constraintAnnotation) {
        this.time = LocalTime.parse(constraintAnnotation.value(), DateTimeFormatter.ISO_LOCAL_TIME);
    }

    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
//        return value != null && value.isAfter(time);
        if (value == null || !value.isAfter(time)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate().replace("{value}", time.toString())).addConstraintViolation();
            return false;
        }
        return true;
    }
}