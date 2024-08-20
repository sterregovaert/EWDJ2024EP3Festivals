package validator;

import domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import repository.FestivalRepository;
import repository.TicketRepository;

public class TicketQuantityValidator implements Validator {

    private static final int FESTIVAL_TICKET_LIMIT = 15;
    private static final int TOTAL_TICKET_LIMIT = 50;
    private static final String QUANTITY_FIELD = "quantity";

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

        // Check if the quantity of all tickets for a single festival is not above the
        // limit
        Integer totalTicketsForFestival = ticketRepository.sumTicketQuantitiesByUserIdAndFestivalId(
                ticket.getUser().getUserId(), ticket.getFestival().getFestivalId());
        if (totalTicketsForFestival != null && totalTicketsForFestival + ticket.getQuantity() > FESTIVAL_TICKET_LIMIT) {
            errors.rejectValue(QUANTITY_FIELD, "quantity.exceedsFestivalLimit", new Object[] { FESTIVAL_TICKET_LIMIT },
                    "You can buy no more than {0} tickets for a single festival.");
        }

        // Check if the total number of tickets bought across all festivals is more than
        // the limit
        String ticketUserUsername = ticket.getUser().getUsername();
        int totalTicketsBought = ticketRepository.sumTicketQuantitiesByUsername(ticketUserUsername);
        if (totalTicketsBought + ticket.getQuantity() > TOTAL_TICKET_LIMIT) {
            errors.rejectValue(QUANTITY_FIELD, "quantity.totalExceedsLimit", new Object[] { TOTAL_TICKET_LIMIT },
                    "You can buy no more than {0} tickets in total for all festivals.");
        }

        // Check if the quantity of tickets being bought is more than the available
        // places
        int availablePlaces = festivalRepository.findAvailablePlacesByFestivalId(ticket.getFestival().getFestivalId());
        if (ticket.getQuantity() > availablePlaces) {
            errors.rejectValue(QUANTITY_FIELD, "quantity.exceedsAvailablePlaces");
        }
    }
}