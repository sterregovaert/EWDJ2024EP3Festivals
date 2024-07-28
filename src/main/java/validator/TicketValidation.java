package validator;

import domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import repository.FestivalRepository;
import repository.TicketRepository;

public class TicketValidation implements Validator {

    private static final int FESTIVAL_TICKET_LIMIT = 15;
    private static final int TOTAL_TICKET_LIMIT = 50;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private FestivalRepository festivalRepository;

    @Override
    public boolean supports(Class<?> klass) {
        return Ticket.class.isAssignableFrom(klass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Ticket ticket = (Ticket) target;

        // Check if the quantity of all tickets for a single festival is not above the limit
        Integer totalTicketsForFestival = ticketRepository.sumTicketQuantitiesByUserIdAndFestivalId(ticket.getUser().getUserId(), ticket.getFestival().getFestivalId());
        if (totalTicketsForFestival != null && totalTicketsForFestival + ticket.getQuantity() > FESTIVAL_TICKET_LIMIT) {
            errors.rejectValue("quantity", "quantity.exceedsFestivalLimit", new Object[]{FESTIVAL_TICKET_LIMIT}, "You can buy no more than {limit} tickets for a single festival.");
        }

        // Check if the total number of tickets bought across all festivals is more than the limit
        int totalTicketsBought = ticketRepository.sumTicketQuantitiesByUsername(ticket.getUser().getUsername());
        if (totalTicketsBought + ticket.getQuantity() > TOTAL_TICKET_LIMIT) {
            errors.rejectValue("quantity", "quantity.totalExceedsLimit", new Object[]{TOTAL_TICKET_LIMIT}, "You can buy no more than {limit} tickets in total for all festivals.");
        }

        // Check if the quantity of tickets being bought is more than the available seats
        int availableSeats = festivalRepository.findAvailableSeatsByFestivalId(ticket.getFestival().getFestivalId());
        if (ticket.getQuantity() > availableSeats) {
            errors.rejectValue("quantity", "quantity.exceedsAvailableSeats");
        }
    }
}