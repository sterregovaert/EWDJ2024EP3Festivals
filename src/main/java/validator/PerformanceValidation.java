package validator;

import domain.Performance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import repository.PerformanceRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;


public class PerformanceValidation implements Validator {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Override
    public boolean supports(Class<?> klass) {
        return Performance.class.isAssignableFrom(klass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Performance performance = (Performance) target;

        // Check if startDateTime is between 10:00 and 23:00 and minutes are 00
        if (performance.getStartDateTime() != null) {
            LocalTime startTime = performance.getStartDateTime().toLocalTime();
            if (startTime.getMinute() != 0 || startTime.isBefore(LocalTime.of(10, 0)) || startTime.isAfter(LocalTime.of(22, 59))) {
                errors.rejectValue("startDateTime", "startDateTime.invalidTime", "Start time must be between 10:00 and 23:00 with minutes set to 00");
            }
        }

        // Check if the time slot (startDateTime - endDateTime) is not already taken by another performance from the same festival
//        if (performance.getStartDateTime() != null && performance.getEndDateTime() != null) {
//            List<Performance> performances = performanceRepository.findByFestivalFestivalId(performance.getFestival().getFestivalId());
//            for (Performance otherPerformance : performances) {
//                if (otherPerformance.getPerformanceId().equals(performance.getPerformanceId())) {
//                    continue; // Skip the current performance
//                }
//                LocalDateTime otherStart = otherPerformance.getStartDateTime();
//                LocalDateTime otherEnd = otherPerformance.getEndDateTime();
//                if ((performance.getStartDateTime().isBefore(otherEnd) && performance.getEndDateTime().isAfter(otherStart))) {
//                    errors.rejectValue("startDateTime", "startDateTime.timeSlotTaken", "The time slot is already taken by another performance");
//                    break;
//                }
//            }
//        }

        // check if performance.startDateTime is after festival.startDateTime
        if (performance.getStartDateTime() != null && performance.getFestival() != null) {
            LocalDateTime festivalStartDateTime = performance.getFestival().getStartDateTime();
            if (performance.getStartDateTime().isBefore(festivalStartDateTime)) {
                errors.rejectValue("startDateTime", "startDateTime.beforeFestivalStartDateTime", "Start date/time must be after the festival start date/time");
            }
        }

        // check if performance.endDateTime is after performance.startDateTime
        if (performance.getEndDateTime() != null && performance.getStartDateTime() != null) {
            if (!performance.getEndDateTime().isAfter(performance.getStartDateTime())) {
                errors.rejectValue("endDateTime", "endDateTime.beforeStartDateTime", "End date/time must be after start date/time");
            }
        }

        // check if festivalNumber1 is divisible by 3
        if (performance.getFestivalNumber1() % 3 != 0) {
            errors.rejectValue("festivalNumber1", "festivalNumber1.notDivisibleBy3", "Festival number must be divisible by 3");
        }

    }
}