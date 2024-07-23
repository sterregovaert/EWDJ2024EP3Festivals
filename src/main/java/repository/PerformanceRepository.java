package repository;

import domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    List<Performance> findByFestivalId(Long festivalId);
}