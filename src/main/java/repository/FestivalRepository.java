package repository;

import domain.Festival;
import domain.Genre;
import domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FestivalRepository  extends JpaRepository<Festival, Long> {
    List<Festival> findByGenreOrderByRegionAscStartDateTimeAsc(Genre genre);
    List<Festival> findByRegionOrderByGenreAscStartDateTimeAsc(Region region);
    List<Festival> findAllByOrderByGenreAscRegionAscStartDateTimeAsc();
}