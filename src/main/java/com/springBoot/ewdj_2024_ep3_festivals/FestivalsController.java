package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import domain.MyUser;
import domain.Performance;
import domain.SubGenre;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import repository.UserRepository;
import service.FestivalsService;
import service.PerformanceService;
import validator.PerformanceValidation;

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
    UserRepository userRepository;
    @Autowired
    PerformanceValidation performanceValidation;

    @GetMapping
    public String getFestivals(@RequestParam(name = "genre", required = false) String genre, @RequestParam(name = "region", required = false) String region, Model model, WebRequest request, Principal principal) {

        model.addAttribute("festivals", festivalsService.fetchFestivals(genre, region));

        if (principal != null) {
            MyUser user = userRepository.findByUsername(principal.getName());
            Map<Long, Integer> ticketsBoughtPerFestival = festivalsService.getTicketsBoughtPerFestivalForUser(genre, region, user.getUserId());
            model.addAttribute("ticketsBoughtPerFestival", ticketsBoughtPerFestival);
        }

        model.addAttribute("genre", genre);
        model.addAttribute("region", region);

        return "festivals";
    }


    @GetMapping("/buy")
    public String buyFestivalTicketGet(@RequestParam("festivalId") Long festivalId, WebRequest request, RedirectAttributes redirectAttributes, Model model, Principal principal) {
        //        int totalTickets = userTickets.values().stream().mapToInt(Integer::intValue).sum();
        //        int availableSeats = festivalsService.getAvailableSeatsForFestival(festivalId);
        //
        //        if (quantity > 15) {
        //            redirectAttributes.addFlashAttribute("errorMessage", "You can not buy more than 15 tickets for a single festival.");
        //            return "redirect:/festivals";
        //        } else if (totalTickets + quantity > 50) {
        //            redirectAttributes.addFlashAttribute("errorMessage", "You can not buy more than 50 tickets in total.");
        //            return "redirect:/festivals";
        //        } else if (ticketsForThisFestival + quantity > availableSeats) {
        //            redirectAttributes.addFlashAttribute("errorMessage", "Not enough available seats.");
        //            return "redirect:/festivals";
        //        } else {
        //            int purchasedTickets = festivalsService.buyTicketForFestival(festivalId, quantity, request);
        //            if (purchasedTickets > 0) {
        //                userTickets.put(festivalId, ticketsForThisFestival + purchasedTickets);
        //                request.setAttribute("userTickets", userTickets, WebRequest.SCOPE_SESSION);
        //                redirectAttributes.addFlashAttribute("successMessage", purchasedTickets + " tickets were successfully purchased!");
        //            } else {
        //                redirectAttributes.addFlashAttribute("errorMessage", "Failed to purchase tickets.");
        //            }
        //        }
        MyUser user = userRepository.findByUsername(principal.getName());
        Festival festival = festivalsService.findFestivalById(festivalId);
        int ticketsForThisFestival = festivalsService.getTicketsForFestivalByUser(festivalId, user.getUserId());

        model.addAttribute("festivalId", festivalId);
        model.addAttribute("festivalName", festival.getName());
        model.addAttribute("ticketsBought", ticketsForThisFestival);

        return "festival-buy";
    }

    @PostMapping("/buy")
    public String buyFestivalTicketPost(@RequestParam("festivalId") Long festivalId, WebRequest request, RedirectAttributes redirectAttributes, Model model) {

        return "dashboard";
    }


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

        model.addAttribute("performance", performance);

        List<SubGenre> subGenres = festivalsService.getSubGenresByGenre(festival.getGenre());
        model.addAttribute("subGenres", subGenres);
    }

}