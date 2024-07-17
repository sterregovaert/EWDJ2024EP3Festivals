package validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

public class DisciplineValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Competition competition = (Competition) target;

        List<Discipline> disciplines = competition.getDisciplines();

        // Check if more than 2 disciplines are filled
        if (disciplines.size() > 2) {
            errors.rejectValue("disciplines", "error.competition", "Too many disciplines. Maximum 2 disciplines are allowed.");
        }

        // Check if the same discipline is entered twice
        if (disciplines.size() == 2) {
            String discipline1 = disciplines.get(0).getName();
            String discipline2 = disciplines.get(1).getName();

            if (discipline1 != null && discipline2 != null && !discipline1.isEmpty() && !discipline2.isEmpty() && discipline1.equals(discipline2)) {
                errors.rejectValue("disciplines", "error.competition", "Duplicate disciplines are not allowed.");
            }
        }
    }

    @Override
    public Errors validateObject(Object target) {
        return Validator.super.validateObject(target);
    }
}
