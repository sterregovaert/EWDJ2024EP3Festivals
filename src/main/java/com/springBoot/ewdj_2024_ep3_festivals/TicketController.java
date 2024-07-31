package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import domain.MyUser;
import domain.Ticket;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import repository.MyUserRepository;
import service.FestivalService;
import service.PerformanceService;
import service.TicketService;
import validator.PerformanceValidation;
import validator.TicketValidation;
import validator.TicketValidator;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    FestivalService festivalService;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    MyUserRepository myUserRepository;
    @Autowired
    PerformanceValidation performanceValidation;
    @Autowired
    TicketValidation ticketValidation;
    @Autowired
    TicketService ticketService;
    @Autowired
    private TicketValidator ticketValidator;

    @GetMapping
    public String showTickets(Model model, Principal principal) {
        String username = principal.getName();
        MyUser user = myUserRepository.findByUsername(username);

        List<Ticket> tickets = ticketService.findByUserOrderByFestivalDateRegionMusicGenre(user);
        model.addAttribute("tickets", tickets);

        return "tickets";
    }

    // BUY TICKET for festival

    @GetMapping("/buy")
    public String buyFestivalTicketGet(@RequestParam("festivalId") Long festivalId, Model model, Principal principal) {
        setupBuyTicketModel(festivalId, model, new Ticket(), principal);

        return "ticket-buy";
    }

    @PostMapping("/buy")
    public String buyFestivalTicketPost(@RequestParam("festivalId") Long festivalId, @Valid @ModelAttribute Ticket ticket, BindingResult result, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        ticket.setUser(myUserRepository.findByUsername(principal.getName()));
        ticket.setFestival(festivalService.findFestivalById(festivalId));

        ticketValidation.validate(ticket, result);

        if (result.hasErrors()) {
            setupBuyTicketModel(festivalId, model, ticket, principal);
            return "ticket-buy";
        }

        festivalService.updateAvailableSeats(ticket.getFestival().getFestivalId(), ticket.getQuantity());
        ticketService.saveTicket(ticket);

        redirectAttributes.addFlashAttribute("message", ticket.getQuantity() + " tickets were purchased");

        return "redirect:/dashboard";
    }


    private void setupBuyTicketModel(Long festivalId, Model model, Ticket ticket, Principal principal) {
        MyUser user = myUserRepository.findByUsername(principal.getName());
        if (user != null) {
            ticket.setUser(user);
        } else {
            throw new IllegalStateException("User not found");
        }
        ticket.setUser(user);

        Festival festival = festivalService.findFestivalById(festivalId);
        if (festival != null) {
            ticket.setFestival(festival);
            model.addAttribute("festival", festival);
        } else {
            throw new IllegalStateException("Festival not found");
        }

        int ticketsForThisFestival = festivalService.getTicketsForFestivalByUser(festivalId, user.getUserId());
        model.addAttribute("ticketsBought", ticketsForThisFestival);

        model.addAttribute("ticket", ticket);
    }

}