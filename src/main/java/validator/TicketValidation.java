package validator;

import domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import repository.FestivalRepository;
import repository.TicketRepository;


public class TicketValidation implements Validator {

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

        // Check if the quantity of all tickets for a single festival is not above 15
        Integer totalTicketsForFestival = ticketRepository.sumTicketQuantitiesByUserIdAndFestivalId(ticket.getUser().getUserId(), ticket.getFestival().getFestivalId());
        if (totalTicketsForFestival != null && totalTicketsForFestival + ticket.getQuantity() > 15) {
            errors.rejectValue("quantity", "quantity.exceedsFestivalLimit", "You can buy no more than 15 tickets for a single festival.");
        }


        // Check if the total number of tickets bought across all festivals is more than 50
        int totalTicketsBought = ticketRepository.sumTicketQuantitiesByUsername(ticket.getUser().getUsername());
        if (totalTicketsBought + ticket.getQuantity() > 50) {
            errors.rejectValue("quantity", "quantity.totalExceedsLimit", "You can buy no more than 50 tickets in total for all festivals.");
        }

        // Check if the quantity of tickets being bought is more than the available seats
        int availableSeats = festivalRepository.findAvailableSeatsByFestivalId(ticket.getFestival().getFestivalId());
        if (ticket.getQuantity() > availableSeats) {
            errors.rejectValue("quantity", "quantity.exceedsAvailableSeats", "You cannot buy more tickets than available seats.");
        }


    }
}