package service;

import domain.Festival;
import domain.MyUser;
import domain.Ticket;
import exceptions.FestivalNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import repository.FestivalRepository;
import repository.TicketRepository;
import validator.TicketQuantityValidator;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private FestivalRepository festivalRepository;
    @Autowired
    private TicketQuantityValidator ticketQuantityValidator;
    @Autowired
    private MyUserService myUserService;
    @Autowired
    private FestivalTicketService festivalTicketService;

    @Transactional
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
            throw new RuntimeException("Festival not found: " + festivalId, e);
        } catch (Exception e) {
            throw new RuntimeException("Error setting up ticket model for festivalId: " + festivalId, e);
        }
    }

    @Transactional
    public void validateAndBuyTicket(Long festivalId, Ticket ticket, String username, BindingResult result) {
        MyUser user = myUserService.getUserByUsername(username);
        Festival festival = festivalTicketService.getFestivalById(festivalId);

        ticket.setUser(user);
        ticket.setFestival(festival);

        ticketQuantityValidator.validate(ticket, result);

        if (!result.hasErrors()) {
            festivalTicketService.updateAvailablePlaces(festival, ticket.getQuantity());
            saveTicket(ticket);
        }
    }
}