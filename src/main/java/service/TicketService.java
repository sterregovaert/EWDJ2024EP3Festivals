package service;

import domain.MyUser;
import domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import repository.TicketRepository;

import java.util.List;

public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;


    public List<Ticket> findByUserOrderByFestivalDateRegionMusicGenre(MyUser user) {
        return ticketRepository.findByUserOrderByFestivalStartDateTimeAscFestivalRegionAscFestivalGenreAsc(user);
    }

    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);

    }
}
