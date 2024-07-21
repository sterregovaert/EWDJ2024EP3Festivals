package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import domain.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import repository.UserRepository;
import service.FestivalsService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/festivals")
public class FestivalsController {

    @Autowired
    FestivalsService festivalsService;
    @Autowired
    UserRepository userRepository;

    @GetMapping
    public String getFestivals(@RequestParam(name = "genre", required = false) String genre, @RequestParam(name = "region", required = false) String region, Model model, WebRequest request) {

        // Initialize userTickets in session if not present
        if (request.getAttribute("userTickets", WebRequest.SCOPE_SESSION) == null) {
            request.setAttribute("userTickets", new ArrayList<>(), WebRequest.SCOPE_SESSION);
        }

        if (genre != null && region != null) {
            model.addAttribute("festivals", festivalsService.fetchFestivalsByGenreAndRegion(genre, region));
        } else if (genre != null) {
            model.addAttribute("festivals", festivalsService.fetchFestivalsByGenre(genre));
        } else if (region != null) {
            model.addAttribute("festivals", festivalsService.fetchFestivalsByRegion(region));
        } else {
            model.addAttribute("festivals", festivalsService.fetchAllFestivals());
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
        //            redirectAttributes.addFlashAttribute("errorMessage", "You cannot buy more than 15 tickets for a single festival.");
        //            return "redirect:/festivals";
        //        } else if (totalTickets + quantity > 50) {
        //            redirectAttributes.addFlashAttribute("errorMessage", "You cannot buy more than 50 tickets in total.");
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
    public String showAddPerformanceForm(Model model) {
        return "performance-add";
    }
//
//    @PostMapping("/addPerformance")
//    public String addPerformance(@ModelAttribute Performance performance, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            return "addPerformanceForm";
//        }
//
//
//        return "redirect:/festivals";
//    }

}