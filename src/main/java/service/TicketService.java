package service;

import domain.Festival;
import domain.MyUser;
import domain.Ticket;
import exceptions.FestivalNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.TicketRepository;

import java.util.List;

@Slf4j
@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private MyUserService myUserService;
    @Autowired
    private FestivalTicketService festivalTicketService;

    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public List<Ticket> findTicketsByUsername(String username) {
        MyUser user = myUserService.getUserByUsername(username);
        return ticketRepository.findByUserOrderByFestivalStartDateTimeAscFestivalRegionAscFestivalGenreAsc(user);
    }

    public Ticket setupBuyTicketModel(Long festivalId, String username) {
        try {
            MyUser user = myUserService.getUserByUsername(username);
            Festival festival = festivalTicketService.getFestivalById(festivalId);

            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setFestival(festival);
            return ticket;
        } catch (FestivalNotFoundException e) {
            log.error("Festival not found: " + festivalId, e);
            throw new RuntimeException("Festival not found: " + festivalId, e);
        } catch (Exception e) {
            log.error("Error setting up ticket model for festivalId: " + festivalId, e);
            throw new RuntimeException("Error setting up ticket model for festivalId: " + festivalId, e);
        }
    }

}