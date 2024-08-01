package service;

import domain.Festival;
import domain.MyUser;
import domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import repository.FestivalRepository;
import repository.MyUserRepository;
import repository.TicketRepository;
import validator.TicketQuantityValidator;

import java.util.List;

public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private FestivalRepository festivalRepository;
    @Autowired
    private TicketQuantityValidator ticketQuantityValidator;

    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public List<Ticket> findTicketsByUsername(String username) {
        MyUser user = myUserRepository.findByUsername(username);
        return ticketRepository.findByUserOrderByFestivalStartDateTimeAscFestivalRegionAscFestivalGenreAsc(user);
    }

    public Ticket setupBuyTicketModel(Long festivalId, String username) {
        MyUser user = myUserRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalStateException("User not found");
        }

        Festival festival = festivalRepository.findById(festivalId).orElse(null);
        if (festival == null) {
            throw new IllegalStateException("Festival not found");
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setFestival(festival);
        return ticket;
    }

    public void validateAndBuyTicket(Long festivalId, Ticket ticket, String username, BindingResult result) {
        ticket.setUser(myUserRepository.findByUsername(username));
        ticket.setFestival(festivalRepository.findById(festivalId).orElse(null));

        // TODO fix quantity > available seats
        ticketQuantityValidator.validate(ticket, result);

        if (!result.hasErrors()) {
            updateAvailableSeats(festivalId, ticket.getQuantity());
            saveTicket(ticket);
        }
    }

    public int getTicketsForFestivalByUser(Long festivalId, Long userId) {
        Integer ticketsCount = ticketRepository.sumTicketQuantitiesByUserIdAndFestivalId(userId, festivalId);
        return ticketsCount != null ? ticketsCount : 0;
    }

    public void updateAvailableSeats(Long festivalId, int quantity) {
        Festival festival = festivalRepository.findById(festivalId).orElse(null);
        festival.setAvailableSeats(festival.getAvailableSeats() - quantity);
        festivalRepository.save(festival);
    }

}
