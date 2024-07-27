package com.springBoot.ewdj_2024_ep3_festivals;

import domain.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import repository.MyUserRepository;
import service.FestivalsService;
import service.PerformanceService;
import service.TicketService;
import validator.PerformanceValidation;
import validator.TicketValidation;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/festivals")
public class FestivalsController {

    @Autowired
    FestivalsService festivalsService;
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

// Fetching festivals

    @GetMapping
    public String getFestivals(@RequestParam(name = "genre", required = false) String genre, @RequestParam(name = "region", required = false) String region, Model model, WebRequest request, Principal principal) {

        model.addAttribute("festivals", festivalsService.fetchFestivals(genre, region));

        if (principal != null) {
            MyUser user = myUserRepository.findByUsername(principal.getName());
            Map<Long, Integer> ticketsBoughtPerFestival = festivalsService.getTicketsBoughtPerFestivalForUser(genre, region, user.getUserId());
            model.addAttribute("ticketsBoughtPerFestival", ticketsBoughtPerFestival);
        }

        model.addAttribute("genre", genre);
        model.addAttribute("region", region);

        return "festivals";
    }

// Buying tickets for a festival

    @GetMapping("/buy")
    public String buyFestivalTicketGet(@RequestParam("festivalId") Long festivalId, Model model, Principal principal) {
        setupBuyTicketModel(festivalId, model, new Ticket(), principal);

        return "festival-buy";
    }

    @PostMapping("/buy")
    public String buyFestivalTicketPost(@RequestParam("festivalId") Long festivalId, @Valid @ModelAttribute Ticket ticket, RedirectAttributes redirectAttributes, BindingResult result, Model model, Principal principal) {
        ticketValidation.validate(ticket, result);

        if (result.hasErrors()) {
            setupBuyTicketModel(festivalId, model, ticket, principal);
            return "festival-buy";
        }

        ticketService.saveTicket(ticket);
        redirectAttributes.addFlashAttribute("message", ticket.getQuantity() + " tickets were purchased");

        return "redirect:/dashboard";
    }


    private void setupBuyTicketModel(Long festivalId, Model model, Ticket ticket, Principal principal) {

        System.out.println("Festival ID:");
        System.out.println(festivalId);
        System.out.println("Principal:");
        System.out.println(principal.getName());

        MyUser user = myUserRepository.findByUsername(principal.getName());
        ticket.setUser(user);

        Festival festival = festivalsService.findFestivalById(festivalId);
        ticket.setFestival(festival);
        model.addAttribute("festival", festival);

        int ticketsForThisFestival = festivalsService.getTicketsForFestivalByUser(festivalId, user.getUserId());
        model.addAttribute("ticketsBought", ticketsForThisFestival);

        model.addAttribute("ticket", ticket);

        System.out.println("user of ticket");
        System.out.println(ticket.getUser().getUsername());
        System.out.println("festival of ticket");
        System.out.println(ticket.getFestival().getName());
    }

// Adding a performance to a festival

    @GetMapping("/addPerformance")
    public String showAddPerformanceForm(@RequestParam("festivalId") Long festivalId, Model model) {
        setupAddPerformanceFormModel(festivalId, model, new Performance());
        return "performance-add";
    }

    @PostMapping("/addPerformance")
    public String addPerformance(@RequestParam("festivalId") Long festivalId, @Valid @ModelAttribute Performance performance, BindingResult result, Model model) {
        Festival festival = festivalsService.findFestivalById(festivalId);
        performance.setFestival(festival);

        performanceValidation.validate(performance, result);

        if (result.hasErrors()) {
            setupAddPerformanceFormModel(festivalId, model, performance);
            return "performance-add";
        }

        performanceService.savePerformance(performance);

        return "redirect:/festivals?genre=" + festival.getGenre().getName() + "&region=" + festival.getRegion().getName();
    }

    private void setupAddPerformanceFormModel(Long festivalId, Model model, Performance performance) {
        Festival festival = festivalsService.findFestivalById(festivalId);

        if (festival != null) {
            performance.setFestival(festival);
            model.addAttribute("festival", festival);
            if (performance.getStartDateTime() == null) {
                performance.setStartDateTime(festival.getStartDateTime());
            }
            if (performance.getEndDateTime() == null) {
                performance.setEndDateTime(festival.getStartDateTime().plusHours(1));
            }
        } else {
            model.addAttribute("error", "Festival not found");
        }

        List<SubGenre> subGenres = festivalsService.getSubGenresByGenre(festival.getGenre());
        model.addAttribute("subGenres", subGenres);

        model.addAttribute("performance", performance);
    }

}