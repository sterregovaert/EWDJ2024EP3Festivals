package validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateRangeValidator implements ConstraintValidator<DateRange, LocalDate> {

    private LocalDate min;
    private LocalDate max;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.min = LocalDate.parse(constraintAnnotation.min(), DateTimeFormatter.ISO_LOCAL_DATE);
        this.max = LocalDate.parse(constraintAnnotation.max(), DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value != null && (value.isEqual(min) || value.isEqual(max) || (value.isAfter(min) && value.isBefore(max)));
    }
}