package validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OlympicNumber1 implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Competition.class.isAssignableFrom(clazz);
    }

    @Autowired
    private CompetitionRepository competitionRepository;

    @Override
    public void validate(Object target, Errors errors) {
        Competition competition = (Competition) target;
        if (competitionRepository.findByOlympicNumber1(competition.getOlympicNumber1()) != null) {
            errors.rejectValue("olympicNumber1", "error.competition", "A competition with this Olympic Number 1 already exists.");
        }
    }

    @Override
    public Errors validateObject(Object target) {
        return Validator.super.validateObject(target);
    }
}
