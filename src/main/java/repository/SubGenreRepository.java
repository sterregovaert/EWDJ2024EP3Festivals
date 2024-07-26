package repository;

import domain.Genre;
import domain.SubGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubGenreRepository extends JpaRepository<SubGenre, Long> {
    List<SubGenre> findByGenre(Genre genre);
}