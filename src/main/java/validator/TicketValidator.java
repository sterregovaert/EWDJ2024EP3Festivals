package validator;

import domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import repository.TicketRepository;

@Component
public class TicketValidator implements Validator {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Ticket ticket = (Ticket) target;

        if (ticket.getFestival() == null) {
            errors.rejectValue("competition.name", "Competition.null", "Competition can not be null.");
        }

//        Integer totalTicketsForThisCompetition = ticketRepository.getTotalTicketQuantityForUserAndCompetition(ticket.getUser(), ticket.getFestival());
//        if (totalTicketsForThisCompetition == null) {
//            totalTicketsForThisCompetition = 0;
//        }
//        if (totalTicketsForThisCompetition + ticket.getQuantity() > 20) {
//            errors.rejectValue("quantity", "Quantity.exceeded", "You can not buy more than 20 tickets for one competition.");
//        }
//
//        Integer totalTicketsBought = ticketRepository.getTotalTicketQuantityForUser(ticket.getUser());
//        if (totalTicketsBought == null) {
//            totalTicketsBought = 0;
//        }
//        if (totalTicketsBought + ticket.getQuantity() > 100) {
//            errors.rejectValue("quantity", "Quantity.exceeded", "You can not buy more than 100 tickets in total.");
//        }
//
//        if (ticket.getQuantity() > ticket.getFestival().getAvailableSeats()) {
//            System.out.println("seats: " + ticket.getFestival().getAvailableSeats());
//            errors.rejectValue("quantity", "Quantity.exceeded", "You can not buy more tickets than available seats.");
//        }
    }

    @Override
    public Errors validateObject(Object target) {
        return Validator.super.validateObject(target);
    }
}
