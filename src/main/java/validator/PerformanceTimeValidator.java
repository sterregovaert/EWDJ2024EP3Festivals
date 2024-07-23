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
    public boolean isValid(Performance performance, LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        // Check if time is between 10:00 and 23:00 and minutes are 00
        boolean isTimeValid = value.toLocalTime().equals(LocalTime.of(value.getHour(), 0)) && value.getHour() >= 10;

        // Check if the time slot is not already taken by another performance
        // needs to be checked against other performances (which have startDateTime and duration) with the same festival
        List<Performance> performances = performanceRepository.findByFestivalId(performance.getFestival().getId());

        boolean isTimeSlotAvailable = true;
        for (Performance otherPerformance : performances) {
            LocalDateTime otherStart = otherPerformance.getStartDateTime();
            LocalDateTime otherEnd = otherStart.plus(otherPerformance.getDuration());

            // Check if the performance overlaps with any existing performance
            if ((value.isBefore(otherEnd) && value.plus(performance.getDuration()).isAfter(otherStart))) {
                isTimeSlotAvailable = false;
                break;
            }
        }

        return isTimeValid && isTimeSlotAvailable;
    }


}