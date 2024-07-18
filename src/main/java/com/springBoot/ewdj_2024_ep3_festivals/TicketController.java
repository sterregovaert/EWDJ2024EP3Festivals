package com.springBoot.ewdj_2024_ep3_festivals;

import domain.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import repository.FestivalRepository;
import repository.TicketRepository;
import repository.UserRepository;
import validator.TicketValidator;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private TicketValidator ticketValidator;

    @GetMapping
    public String showTickets(Model model, Principal principal) {
        String username = principal.getName();
        MyUser user = userRepository.findByUsername(username);

//        List<Ticket> tickets = ticketRepository.findByUserSortedBySportAndDate(user);
//        model.addAttribute("tickets", tickets);

        return "tickets";
    }

    @GetMapping("/buy/{id}")
    public String buyTicket(@PathVariable Long id, Model model, Principal principal) {
        Ticket ticket = new Ticket();

        MyUser user = userRepository.findByUsername(principal.getName());
        Festival festival = festivalRepository.findById(id).orElse(null);
        if (festival == null) {
            return "redirect:/error";
        }
        ticket.setFestival(festival);

//        List<Ticket> tickets = ticketRepository.findByUserAndFestival(user, festival);
//        model.addAttribute("tickets", tickets);
//        model.addAttribute("ticketCount", tickets.size());

        ticket.setQuantity(1);
        model.addAttribute("ticket", ticket);
        return "ticket-buy";
    }

    @PostMapping("/buy/{id}")
    public String buyTickets(@PathVariable Long id, @Valid Ticket ticket, BindingResult result, Model model, Principal principal, @RequestParam int quantity) {
        MyUser user = userRepository.findByUsername(principal.getName());
        Festival festival = festivalRepository.findById(id).orElse(null);

        ticket.setFestival(festival);
        ticket.setUser(user);
        ticket.setQuantity(quantity);
        ticket.setFestival(festival);

        ticketValidator.validate(ticket, result);

        if (result.hasErrors()) {
            model.addAttribute("ticket", ticket);
            return "ticket-buy";
        }

        festival.setAvailableSeats(festival.getAvailableSeats() - quantity);
        festivalRepository.save(festival);

        ticketRepository.save(ticket);

        return "redirect:/festival/" + String.valueOf(ticket.getFestival());
    }
}