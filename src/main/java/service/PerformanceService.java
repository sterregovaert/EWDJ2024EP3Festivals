package service;

import domain.Festival;
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

    public void savePerformance(Performance performance) {
        performanceRepository.save(performance);
    }

    public List<Performance> getPerformancesByFestival(Long festivalId) {
        return performanceRepository.findByFestivalFestivalId(festivalId);
    }

    public void deletePerformanceById(Long performanceId) {
        performanceRepository.deleteById(performanceId);
    }

    public void setupAddPerformanceFormModel(Long festivalId, Performance performance, Model model) {
        Festival festival = getFestivalOrThrow(festivalId);
        setupPerformanceDefaults(performance, festival);
        addFestivalAndSubGenresToModel(festival, model);
        model.addAttribute("performance", performance);
        model.addAttribute("performances", getPerformancesByFestival(festivalId));
    }

    public void setupPerformanceForFestival(Long festivalId, Performance performance, BindingResult bindingResult, Model model) {
        Festival festival = getFestivalOrThrow(festivalId);
        performance.setFestival(festival);
    }

    public void setupRemovePerformanceFormModel(Long festivalId, Model model) {
        Festival festival = getFestivalOrThrow(festivalId);
        List<Performance> performances = getPerformancesByFestival(festivalId);
        model.addAttribute("performances", performances);
        model.addAttribute("festival", festival);
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

    private void addFestivalAndSubGenresToModel(Festival festival, Model model) {
        List<SubGenre> subGenres = subGenreService.getSubGenresByGenre(festival.getGenre());
        model.addAttribute("subGenres", subGenres);
        model.addAttribute("festival", festival);
    }


}
