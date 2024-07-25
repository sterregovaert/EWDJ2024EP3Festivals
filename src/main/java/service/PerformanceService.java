package service;

import domain.Performance;
import org.springframework.beans.factory.annotation.Autowired;
import repository.PerformanceRepository;

public class PerformanceService {

    @Autowired
    PerformanceRepository performanceRepository;

    public void savePerformance(Performance performance) {
        performanceRepository.save(performance);
    }
}
