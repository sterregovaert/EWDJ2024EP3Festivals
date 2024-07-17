package validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class StadiumValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Competition.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Competition competition = (Competition) target;
        if (competition.getStadium().getStadium_id() == null) {
            errors.rejectValue("stadium.stadium_id", "error.stadium", "Please select a stadium.");
        }
    }

    @Override
    public Errors validateObject(Object target) {
        return Validator.super.validateObject(target);
    }
}
