package repository;

import domain.Festival;
import domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository  extends JpaRepository<Festival, Long> {
}
