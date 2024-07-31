package service;

import domain.Festival;
import domain.Performance;
import domain.SubGenre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import repository.FestivalRepository;
import repository.PerformanceRepository;

import java.util.List;

public class PerformanceService {

    @Autowired
    PerformanceRepository performanceRepository;
    @Autowired
    FestivalService festivalService;
    @Autowired
    FestivalRepository festivalRepository;
    @Autowired
    SubGenreService subGenreService;

    public void savePerformance(Performance performance) {
        performanceRepository.save(performance);
    }

    public List<Performance> getPerformancesByFestival(Long festivalId) {
        return performanceRepository.findByFestivalFestivalId(festivalId);
    }

    public void deletePerformanceById(Long performanceId) {
        performanceRepository.deleteById(performanceId);
    }

    public void setupAddPerformanceFormModel(Long festivalId, Performance performance, Model model) throws Exception {
        Festival festival = festivalRepository.findById(festivalId).orElse(null);

        if (festival == null) {
            throw new Exception("Festival not found");
        }

        performance.setFestival(festival);
        if (performance.getStartDateTime() == null) {
            performance.setStartDateTime(festival.getStartDateTime());
        }
        if (performance.getEndDateTime() == null) {
            performance.setEndDateTime(festival.getStartDateTime().plusHours(1));
        }

        List<SubGenre> subGenres = subGenreService.getSubGenresByGenre(festival.getGenre());
        model.addAttribute("subGenres", subGenres);
        model.addAttribute("festival", festival);
        model.addAttribute("performance", performance);
    }

    public void setupPerformanceForFestival(Long festivalId, Performance performance) throws Exception {
        Festival festival = festivalRepository.findById(festivalId).orElse(null);

        if (festival == null) {
            throw new Exception("Festival not found");
        }

        performance.setFestival(festival);
    }

    public void setupRemovePerformanceFormModel(Long festivalId, Model model) throws Exception {
        Festival festival = festivalRepository.findById(festivalId).orElse(null);
        if (festival == null) {
            throw new Exception("Festival not found");
        }

        List<Performance> performances = getPerformancesByFestival(festivalId);
        model.addAttribute("performances", performances);
        model.addAttribute("festival", festival);
    }


}
