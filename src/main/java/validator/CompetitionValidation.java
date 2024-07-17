package validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Locale;

@Component
public class CompetitionValidation implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Competition.class.isAssignableFrom(clazz);
    }

    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private MessageSource messageSource;

    @Override
    public void validate(Object target, Errors errors) {
        Competition competition = (Competition) target;

        Long competition_id = competition.getCompetition_id();
        if (competition_id == null) {
            errors.rejectValue("competition_id", "nullValue.competition.competition_id", messageSource.getMessage("nullValue.competition.competition_id", null, Locale.getDefault()));
        }

        if (competitionRepository.findByOlympicNumber1(competition.getOlympicNumber1()) != null) {
            errors.rejectValue("olympicNumber1", "error.competition", messageSource.getMessage("error.competition.exists", null, Locale.getDefault()));
        }

        if (competition.getOlympicNumber2() < competition.getOlympicNumber1() - 1000 || competition.getOlympicNumber2() > competition.getOlympicNumber1() + 1000) {
            errors.rejectValue("olympicNumber2", "olympic.number2.must.be.greater", messageSource.getMessage("olympic.number2.must.be.greater", null, Locale.getDefault()));
        }

        if (competition.getStadium() == null || competition.getStadium().getStadium_id() == null) {
            errors.rejectValue("stadium.stadium_id", "error.stadium", messageSource.getMessage("error.stadium", null, Locale.getDefault()));
        }

        List<Discipline> disciplines = competition.getDisciplines();

        if (disciplines.size() > 2) {
            errors.rejectValue("disciplines", "error.competition", messageSource.getMessage("error.competition.tooManyDisciplines", null, Locale.getDefault()));
        }

        if (disciplines.size() == 2) {
            String discipline1 = disciplines.get(0).getName();
            String discipline2 = disciplines.get(1).getName();

            if (discipline1 != null && discipline2 != null && !discipline1.isEmpty() && !discipline2.isEmpty() && discipline1.equals(discipline2)) {
                errors.rejectValue("disciplines", "error.competition", messageSource.getMessage("error.competition.duplicateDisciplines", null, Locale.getDefault()));
            }
        }
    }

    @Override
    public Errors validateObject(Object target) {
        return Validator.super.validateObject(target);
    }
}
