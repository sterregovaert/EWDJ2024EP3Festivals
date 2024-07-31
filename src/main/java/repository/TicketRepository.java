package repository;

import domain.MyUser;
import domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserOrderByFestivalStartDateTimeAscFestivalRegionAscFestivalGenreAsc(MyUser user);

    @Query("SELECT SUM(t.quantity) FROM Ticket t WHERE t.user.username = :username")
    Integer sumTicketQuantitiesByUsername(@Param("username") String username);

    @Query("SELECT SUM(t.quantity) FROM Ticket t WHERE t.user.userId = :userId AND t.festival.festivalId = :festivalId")
    Integer sumTicketQuantitiesByUserIdAndFestivalId(Long userId, Long festivalId);

}

