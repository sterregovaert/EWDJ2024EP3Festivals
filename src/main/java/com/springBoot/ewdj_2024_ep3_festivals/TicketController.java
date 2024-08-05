package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import domain.MyUser;
import domain.Ticket;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.FestivalTicketService;
import service.MyUserService;
import service.TicketService;
import validator.TicketQuantityValidator;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private FestivalTicketService festivalTicketService;
    @Autowired
    private MyUserService myUserService;
    @Autowired
    private TicketQuantityValidator ticketQuantityValidator;


    @GetMapping
    public String showTickets(Model model, Principal principal) {
        model.addAttribute("tickets", ticketService.findTicketsByUsername(principal.getName()));
        return "tickets";
    }

    private String prepareTicketPurchaseModel(Long festivalId, Ticket ticket, Model model) {
        model.addAttribute("ticket", ticket);
        model.addAttribute("festival", ticket.getFestival());
        model.addAttribute("ticketsBought", festivalTicketService.getTicketsForFestivalByUser(festivalId, ticket.getUser().getUserId()));

        return "ticket-buy";
    }

    @GetMapping("/buy")
    public String buyFestivalTicketGet(@RequestParam("festivalId") Long festivalId, Model model, Principal principal) {
        Ticket ticket = ticketService.setupBuyTicketModel(festivalId, principal.getName());

        return prepareTicketPurchaseModel(festivalId, ticket, model);
    }

    @PostMapping("/buy")
    public String buyFestivalTicketPost(@RequestParam("festivalId") Long festivalId, @Valid @ModelAttribute Ticket ticket, BindingResult result, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        MyUser user = myUserService.getUserByUsername(principal.getName());
        Festival festival = festivalTicketService.getFestivalById(festivalId);
        ticket.setUser(user);
        ticket.setFestival(festival);

        ticketQuantityValidator.validate(ticket, result);

        if (result.hasErrors()) {
            return prepareTicketPurchaseModel(festivalId, ticket, model);
        }

        festivalTicketService.updateAvailablePlaces(festival, ticket.getQuantity());
        ticketService.saveTicket(ticket);

        redirectAttributes.addFlashAttribute("message", ticket.getQuantity() + " tickets were purchased");
        return "redirect:/dashboard";
    }
}