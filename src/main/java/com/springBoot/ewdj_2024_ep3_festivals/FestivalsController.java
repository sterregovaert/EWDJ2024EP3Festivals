package com.springBoot.ewdj_2024_ep3_festivals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import service.FestivalsService;

import java.util.ArrayList;

@Controller
public class FestivalsController {

    @Autowired
    FestivalsService festivalsService;

    @GetMapping("/festivals")
    public String getFestivals(@RequestParam(name = "genre", required = false) String genre,
                               @RequestParam(name = "region", required = false) String region,
                               Model model, WebRequest request) {

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


}