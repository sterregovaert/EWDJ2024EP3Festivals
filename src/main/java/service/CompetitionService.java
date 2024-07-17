package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompetitionService {

    @Autowired
    private CompetitionRepository competitionRepository;

    public List<Competition> getCompetitionsBySport(Long sport_id) {
        return competitionRepository.findCompetitionsBySportId(sport_id);
    }

    public int getFreeSeatsOfCompetition(Long competition_id) {
        Optional<Competition> competitionOptional = competitionRepository.findById(competition_id);

        return competitionOptional.map(Competition::getFreeSeats).orElse(0);
    }
}