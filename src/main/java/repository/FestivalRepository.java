package repository;

import domain.Festival;
import domain.Genre;
import domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
    @Query("SELECT f FROM Festival f WHERE " +
            "(:genre IS NULL OR f.genre = :genre) AND " +
            "(:region IS NULL OR f.region = :region)" +
            "ORDER BY f.genre.name ASC, f.region.name ASC, f.startDateTime ASC")
    List<Festival> findByGenreAndRegion(@Param("genre") Genre genre, @Param("region") Region region);

    @Query("SELECT f.availablePlaces FROM Festival f WHERE f.festivalId = :festivalId")
    int findAvailablePlacesByFestivalId(Long festivalId);

    List<Festival> findByGenre(Genre genre);
}