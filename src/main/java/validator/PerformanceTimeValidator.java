package validator;

import domain.Performance;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import repository.PerformanceRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class PerformanceTimeValidator implements ConstraintValidator<ValidPerformanceTime, Performance> {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Override
    public boolean isValid(Performance performance, ConstraintValidatorContext context) {
        if (performance == null || performance.getStartDateTime() == null || performance.getFestival() == null) {
            return false;
        }

        LocalDateTime value = performance.getStartDateTime();

        // Check if time is between 10:00 and 23:00 and minutes are 00
        boolean isTimeValid = value.toLocalTime().equals(LocalTime.of(value.getHour(), 0)) && value.getHour() >= 10 && value.getHour() < 23;
        if (!isTimeValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{performance.startDateTime.invalidTime}")
                    .addPropertyNode("startDateTime")
                    .addConstraintViolation();
            return false;
        }

        // Check if the time slot is not already taken by another performance
        List<Performance> performances = performanceRepository.findByFestivalFestivalId(performance.getFestival().getFestivalId());

        for (Performance otherPerformance : performances) {
            LocalDateTime otherStart = otherPerformance.getStartDateTime();
            LocalDateTime otherEnd = otherPerformance.getEndDateTime();

            // TODO check if it shouldn't be displayed somewhere else the error instead of startdatetime
            // Check if the performance overlaps with any existing performance
            if ((value.isAfter(otherStart) || value.isEqual(otherStart)) && value.isBefore(otherEnd)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{performance.startDateTime.timeSlotTaken}")
                        .addPropertyNode("startDateTime")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }

    @Override
    public void initialize(ValidPerformanceTime constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}