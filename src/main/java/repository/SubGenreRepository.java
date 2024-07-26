package repository;

import domain.SubGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubGenreRepository extends JpaRepository<SubGenre, Long> {
}