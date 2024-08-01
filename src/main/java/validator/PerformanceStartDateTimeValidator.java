package validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class PerformanceStartDateTimeValidator implements ConstraintValidator<ValidPerformanceStartDateTime, LocalDateTime> {

    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public void initialize(ValidPerformanceStartDateTime constraintAnnotation) {
        this.startTime = LocalTime.parse(constraintAnnotation.start());
        this.endTime = LocalTime.parse(constraintAnnotation.end());
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        LocalTime time = value.toLocalTime();
        boolean isTimeValid = time.equals(LocalTime.of(time.getHour(), 0)) && !time.isBefore(startTime) && !time.isAfter(endTime);
        if (!isTimeValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{performance.startDateTime.invalidTime}")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}