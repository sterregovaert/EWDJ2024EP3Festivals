package service;

import domain.Performance;
import org.springframework.beans.factory.annotation.Autowired;
import repository.PerformanceRepository;

import java.util.List;

public class PerformanceService {

    @Autowired
    PerformanceRepository performanceRepository;

    public void savePerformance(Performance performance) {
        performanceRepository.save(performance);
    }

    public List<Performance> getPerformancesByFestival(Long festivalId) {
        return performanceRepository.findByFestivalFestivalId(festivalId);
    }

    public void deletePerformanceById(Long performanceId) {
        performanceRepository.deleteById(performanceId);
    }
}
