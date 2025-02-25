package validator;

import domain.Performance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import repository.PerformanceRepository;

import java.time.LocalDateTime;
import java.util.List;

public class PerformanceTimeSlotValidation implements Validator {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Override
    public boolean supports(Class<?> klass) {
        return Performance.class.isAssignableFrom(klass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Performance performance = (Performance) target;

        // Check if the time slot (startDateTime - endDateTime) is not already taken by
        // another performance from the same festival
        if (performance.getStartDateTime() != null && performance.getEndDateTime() != null) {
            List<Performance> performances = performanceRepository
                    .findByFestivalFestivalId(performance.getFestival().getFestivalId());

            for (Performance otherPerformance : performances) {
                if (otherPerformance.getPerformanceId().equals(performance.getPerformanceId())) {
                    continue; // Skip the current performance
                }

                LocalDateTime otherStart = otherPerformance.getStartDateTime();
                LocalDateTime otherEnd = otherPerformance.getEndDateTime();

                if ((performance.getStartDateTime().isBefore(otherEnd)
                        && performance.getEndDateTime().isAfter(otherStart))) {
                    errors.rejectValue("startDateTime", "performance.startDateTime.timeSlotTaken");
                    break;
                }
            }
        }

    }
}