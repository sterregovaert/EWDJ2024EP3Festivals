package repository;

import domain.Festival;
import domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FestivalRepository  extends JpaRepository<Festival, Long> {
    List<Festival> findByGenreOrderByRegionAscStartDateTimeAsc(String genre);
    List<Festival> findByRegionOrderByGenreAscStartDateTimeAsc(String region);
    List<Festival> findAllByOrderByGenreAscRegionAscStartDateTimeAsc();
}