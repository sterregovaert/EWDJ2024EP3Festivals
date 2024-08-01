package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Ticket;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.TicketService;

import java.security.Principal;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @GetMapping
    public String showTickets(Model model, Principal principal) {
        model.addAttribute("tickets", ticketService.findTicketsByUsername(principal.getName()));
        return "tickets";
    }


    private String setupBuyTicketModel(Long festivalId, Ticket ticket, Model model, Principal principal) {
        model.addAttribute("ticket", ticket);
        model.addAttribute("festival", ticket.getFestival());
        model.addAttribute("ticketsBought", ticketService.getTicketsForFestivalByUser(festivalId, ticket.getUser().getUserId()));
        return "ticket-buy";
    }

    @GetMapping("/buy")
    public String buyFestivalTicketGet(@RequestParam("festivalId") Long festivalId, Model model, Principal principal) {
        Ticket ticket = ticketService.setupBuyTicketModel(festivalId, principal.getName());
//        model.addAttribute("ticket", ticket);
//        model.addAttribute("festival", ticket.getFestival());
//        model.addAttribute("ticketsBought", ticketService.getTicketsForFestivalByUser(festivalId, ticket.getUser().getUserId()));
//        return "ticket-buy";
        return setupBuyTicketModel(festivalId, ticket, model, principal);
    }

    @PostMapping("/buy")
    public String buyFestivalTicketPost(@RequestParam("festivalId") Long festivalId, @Valid @ModelAttribute Ticket ticket, BindingResult result, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        ticketService.validateAndBuyTicket(festivalId, ticket, principal.getName(), result);
        if (result.hasErrors()) {
//            model.addAttribute("ticket", ticket);
//            model.addAttribute("festival", ticket.getFestival());
//            model.addAttribute("ticketsBought", ticketService.getTicketsForFestivalByUser(festivalId, ticket.getUser().getUserId()));
//            return "ticket-buy";
            return setupBuyTicketModel(festivalId, ticket, model, principal);
        }

        redirectAttributes.addFlashAttribute("message", ticket.getQuantity() + " tickets were purchased");
        return "redirect:/dashboard";
    }
}