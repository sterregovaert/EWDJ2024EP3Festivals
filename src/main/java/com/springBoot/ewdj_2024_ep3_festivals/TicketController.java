package com.springBoot.ewdj_2024_ep3_festivals;

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
        List<Ticket> tickets = ticketService.findTicketsByUsername(username);
        model.addAttribute("tickets", tickets);
        return "tickets";
    }

    @GetMapping("/buy")
    public String buyFestivalTicketGet(@RequestParam("festivalId") Long festivalId, Model model, Principal principal) {
        Ticket ticket = ticketService.setupBuyTicketModel(festivalId, principal.getName());
        model.addAttribute("ticket", ticket);
        model.addAttribute("festival", ticket.getFestival());
        model.addAttribute("ticketsBought", ticketService.getTicketsForFestivalByUser(festivalId, ticket.getUser().getUserId()));
        return "ticket-buy";
    }

    @PostMapping("/buy")
    public String buyFestivalTicketPost(@RequestParam("festivalId") Long festivalId, @Valid @ModelAttribute Ticket ticket, BindingResult result, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        ticketService.validateAndBuyTicket(festivalId, ticket, principal.getName(), result);
        if (result.hasErrors()) {
            model.addAttribute("ticket", ticket);
            model.addAttribute("festival", ticket.getFestival());
            model.addAttribute("ticketsBought", ticketService.getTicketsForFestivalByUser(festivalId, ticket.getUser().getUserId()));
            return "ticket-buy";
        }

        redirectAttributes.addFlashAttribute("message", ticket.getQuantity() + " tickets were purchased");
        return "redirect:/dashboard";
    }
}