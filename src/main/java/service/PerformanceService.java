package service;

import domain.Festival;
import domain.Genre;
import domain.Performance;
import domain.SubGenre;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import repository.FestivalRepository;
import repository.PerformanceRepository;

import java.util.List;

@Slf4j
@Service
public class PerformanceService {

    @Autowired
    PerformanceRepository performanceRepository;
    @Autowired
    FestivalService festivalService;
    @Autowired
    FestivalRepository festivalRepository;
    @Autowired
    SubGenreService subGenreService;

    public Performance savePerformance(Performance performance) {
        return performanceRepository.save(performance);
    }

    public List<Performance> getPerformancesByFestival(Long festivalId) {
        return performanceRepository.findByFestivalFestivalId(festivalId);
    }

    public void deletePerformanceById(Long performanceId) {
        performanceRepository.deleteById(performanceId);
    }

    public Festival setupAddPerformanceWithDefaults(Long festivalId, Performance performance) {
        Festival festival = getFestivalOrThrow(festivalId);

        setupPerformanceDefaults(performance, festival);

        return festival;
    }

    public List<SubGenre> setupSubGenresForFestivalGenre(Genre festivalGenre) {
        List<SubGenre> subGenres = subGenreService.getSubGenresByGenre(festivalGenre);

        return subGenres;
    }

    public List<Performance> setupPerformances(Long festivalId) {
        List<Performance> performances = getPerformancesByFestival(festivalId);

        return performances;
    }

    public Performance setupPerformanceForFestival(Long festivalId, Performance performance) {

        Festival festival = getFestivalOrThrow(festivalId);
        performance.setFestival(festival);

        return performance;
    }

    public Festival setupFestival(Long festivalId) {
        Festival festival = getFestivalOrThrow(festivalId);

        return festival;
    }

    private Festival getFestivalOrThrow(Long festivalId) {
        return festivalRepository.findById(festivalId)
                .orElseThrow(() -> new IllegalArgumentException("Festival not found"));
    }

    private void setupPerformanceDefaults(Performance performance, Festival festival) {
        performance.setFestival(festival);

        if (performance.getStartDateTime() == null) {
            performance.setStartDateTime(festival.getStartDateTime());
        }

        if (performance.getEndDateTime() == null) {
            performance.setEndDateTime(festival.getStartDateTime().plusHours(1));
        }
    }
}
