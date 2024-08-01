package service;

import domain.Festival;
import domain.MyUser;
import domain.Ticket;
import exceptions.FestivalNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import repository.FestivalRepository;
import repository.TicketRepository;
import validator.TicketQuantityValidator;

import java.util.List;

public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private FestivalRepository festivalRepository;
    @Autowired
    private TicketQuantityValidator ticketQuantityValidator;
    @Autowired
    private MyUserService myUserService;

    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    private Festival getFestivalById(Long festivalId) {
        return festivalRepository.findById(festivalId)
                .orElseThrow(() -> new FestivalNotFoundException(festivalId.intValue()));
    }

    public List<Ticket> findTicketsByUsername(String username) {
        return ticketRepository.findByUserOrderByFestivalStartDateTimeAscFestivalRegionAscFestivalGenreAsc(myUserService.getUserByUsername(username));
    }

    public Ticket setupBuyTicketModel(Long festivalId, String username) {
        MyUser user = myUserService.getUserByUsername(username);
        Festival festival = getFestivalById(festivalId);

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setFestival(festival);
        return ticket;
    }

    public void validateAndBuyTicket(Long festivalId, Ticket ticket, String username, BindingResult result) {
        ticket.setUser(myUserService.getUserByUsername(username));
        ticket.setFestival(getFestivalById(festivalId));

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
        if (festival == null) {
            throw new IllegalStateException("Festival not found");
        }
        festival.setAvailableSeats(festival.getAvailableSeats() - quantity);
        festivalRepository.save(festival);
    }

}
