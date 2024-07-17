package validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OlympicNumbers implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Competition.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Competition competition = (Competition) target;
        if (competition.getOlympicNumber2() < competition.getOlympicNumber1() - 1000 || competition.getOlympicNumber2() > competition.getOlympicNumber1() + 1000) {
            errors.rejectValue("olympicNumber2", "olympic.number2.must.be.greater", "Olympic number 2 must in range of 1000 from Olympic number 1. ");
        }

    }

    @Override
    public Errors validateObject(Object target) {
        return Validator.super.validateObject(target);
    }
}
