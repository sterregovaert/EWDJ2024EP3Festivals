package repository;

import domain.Festival;
import domain.Genre;
import domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
    List<Festival> findByGenreOrderByRegionDescStartDateTimeAsc(Genre genre);

    List<Festival> findByRegionOrderByGenreAscStartDateTimeAsc(Region region);

    List<Festival> findAllByOrderByGenreAscRegionAscStartDateTimeAsc();

    List<Festival> findByGenreAndRegionOrderByStartDateTimeAsc(Genre genre, Region region);


    @Query("SELECT f.availableSeats FROM Festival f WHERE f.festivalId = :festivalId")
    int findAvailableSeatsByFestivalId(Long festivalId);

    List<Festival> findByGenre(Genre genre);
}