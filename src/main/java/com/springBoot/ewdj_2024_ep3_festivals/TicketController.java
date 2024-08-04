package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Ticket;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.FestivalTicketService;
import service.TicketService;

import java.security.Principal;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private FestivalTicketService festivalTicketService;

    @GetMapping
    public String showTickets(Model model, Principal principal) {
        model.addAttribute("tickets", ticketService.findTicketsByUsername(principal.getName()));
        return "tickets";
    }

    private String prepareTicketPurchaseModel(Long festivalId, Ticket ticket, Model model, Principal principal) {
        model.addAttribute("ticket", ticket);
        model.addAttribute("festival", ticket.getFestival());
        model.addAttribute("ticketsBought", festivalTicketService.getTicketsForFestivalByUser(festivalId, ticket.getUser().getUserId()));
        return "ticket-buy";
    }

    @GetMapping("/buy")
    public String buyFestivalTicketGet(@RequestParam("festivalId") Long festivalId, Model model, Principal principal) {
        Ticket ticket = ticketService.setupBuyTicketModel(festivalId, principal.getName());
        return prepareTicketPurchaseModel(festivalId, ticket, model, principal);
    }

    @PostMapping("/buy")
    public String buyFestivalTicketPost(@RequestParam("festivalId") Long festivalId, @Valid @ModelAttribute Ticket ticket, BindingResult result, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        ticketService.validateAndBuyTicket(festivalId, ticket, principal.getName(), result);
        if (result.hasErrors()) {
            return prepareTicketPurchaseModel(festivalId, ticket, model, principal);
        }

        redirectAttributes.addFlashAttribute("message", ticket.getQuantity() + " tickets were purchased");
        return "redirect:/dashboard";
    }
}