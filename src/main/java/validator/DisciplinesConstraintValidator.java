package validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Collectors;

public class DisciplinesConstraintValidator implements ConstraintValidator<DisciplinesConstraint, List<Discipline>> {

    @Override
    public boolean isValid(List<Discipline> disciplines, ConstraintValidatorContext context) {
        if (disciplines == null || disciplines.isEmpty()) {
            return true;
        }

        if (disciplines.size() > 2) {
            return false;
        }

        for (Discipline discipline : disciplines) {
            if (discipline.getName() == null || discipline.getName().isEmpty()) {
                return true;
            }
        }

        List<Discipline> distinctDisciplines = disciplines.stream().distinct().collect(Collectors.toList());
        return distinctDisciplines.size() == disciplines.size();
    }
}