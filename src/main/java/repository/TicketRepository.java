package repository;

import domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.MyUser;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT SUM(t.quantity) FROM Ticket t WHERE t.user.username = :username")
    Integer sumTicketQuantitiesByUsername(@Param("username") String username);

    List<Ticket> findByUserOrderByFestivalStartDateTimeAscFestivalRegionAscFestivalGenreAsc(MyUser user);

    @Query("SELECT SUM(t.quantity) FROM Ticket t WHERE t.user.userId = :userId AND t.festival.festivalId = :festivalId")
    Integer findTicketQuantitiesSumByUserIdAndFestivalId(Long userId, Long festivalId);

//    public interface TicketRepository extends JpaRepository<Ticket, Long> {
//        @Query("SELECT t.festival.id AS festivalId, COUNT(t) AS ticketCount " +
//                "FROM Ticket t WHERE t.user.id = :userId " +
//                "AND (:genreName IS NULL OR t.festival.genre.name = :genreName) " +
//                "AND (:regionName IS NULL OR t.festival.region.name = :regionName) " +
//                "GROUP BY t.festival.id")
//        List<Object[]> findTicketCountsByUserAndOptionalGenreAndRegion(@Param("userId") Long userId,
//                                                                       @Param("genreName") String genreName,
//                                                                       @Param("regionName") String regionName);
//    }
}

