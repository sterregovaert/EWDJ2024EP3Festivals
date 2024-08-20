package validator;

import domain.Performance;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

public class PerformanceDateTimeValidation implements Validator {

    @Override
    public boolean supports(Class<?> klass) {
        return Performance.class.isAssignableFrom(klass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Performance performance = (Performance) target;

        // check if performance.startDateTime is after festival.startDateTime
        if (performance.getStartDateTime() != null && performance.getFestival() != null) {
            LocalDateTime festivalStartDateTime = performance.getFestival().getStartDateTime();

            if (performance.getStartDateTime().isBefore(festivalStartDateTime)) {
                errors.rejectValue("startDateTime", "performance.startDateTime.beforeFestivalStartDateTime");
            }
        }

        // check if performance.endDateTime is after performance.startDateTime
        if (performance.getEndDateTime() != null && performance.getStartDateTime() != null) {
            if (!performance.getEndDateTime().isAfter(performance.getStartDateTime())) {
                errors.rejectValue("endDateTime", "performance.endDateTime.beforeStartDateTime");
            }
        }

    }
}