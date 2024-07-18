package repository;

import domain.Festival;
import domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.MyUser;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUser(MyUser user);

//    List<Ticket> findByUserAndCompetition(MyUser user, Competition competition);
//
//    @Query("SELECT SUM(t.quantity) FROM Ticket t WHERE t.user = ?1")
//    Integer getTotalTicketQuantityForUser(MyUser user);
//
//    @Query("SELECT SUM(t.quantity) FROM Ticket t WHERE t.user = :user AND t.competition = :competition")
//    Integer sumQuantityByUserAndCompetition(@Param("user") MyUser user, @Param("competition") Competition competition);
//
//    @Query("SELECT t FROM Ticket t WHERE t.user = :user ORDER BY t.competition.sport.name, t.competition.startDate DESC")
//    List<Ticket> findByUserSortedBySportAndDate(@Param("user") MyUser user);
//
//    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.user = :user")
//    Long countByUser(@Param("user") MyUser user);
//
//    @Query("SELECT SUM(t.quantity) FROM Ticket t WHERE t.user = :user AND t.competition = :competition")
//    Integer getTotalTicketQuantityForUserAndCompetition(@Param("user") MyUser user, @Param("competition") Festival competition);
}

