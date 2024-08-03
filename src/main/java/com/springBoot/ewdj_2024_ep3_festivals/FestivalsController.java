package com.springBoot.ewdj_2024_ep3_festivals;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import service.FestivalService;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/festivals")
public class FestivalsController {

    @Autowired
    FestivalService festivalService;

    // Fetching festivals
//    @GetMapping
//    public String getFestivals(@RequestParam(name = "genre", required = false) String genre, @RequestParam(name = "region", required = false) String region, Model model, WebRequest request, Principal principal) {
//        // model.addAttribute("festivals", festivalService.fetchFestivalsByGenreAndRegion(genre, region));
//        // model.addAttribute("ticketsBoughtPerFestival", festivalService.getTicketsBoughtPerFestivalForUser(genre, region, principal));
//        model.addAttribute("festivals", festivalService.fetchFestivalsWithTickets(genre, region, principal));
//        model.addAttribute("genre", genre);
//        model.addAttribute("region", region);
//
//        return "festivals";
//    }

    @GetMapping
    public String getFestivals(@RequestParam(name = "genre", required = false) String genre, @RequestParam(name = "region", required = false) String region, Model model, WebRequest request, Principal principal) {
        model.addAttribute("festivals", festivalService.fetchFestivalsWithTickets(genre, region, principal));
        model.addAttribute("genre", genre);
        model.addAttribute("region", region);
        return "festivals";
    }
}