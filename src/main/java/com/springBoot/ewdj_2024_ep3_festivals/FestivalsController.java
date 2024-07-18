package com.springBoot.ewdj_2024_ep3_festivals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.FestivalService;

@Controller
public class FestivalsController {

    @Autowired
    FestivalService festivalService;

    @GetMapping("/festivals")
    public String getFestivals(@RequestParam(name = "genre", required = false) String genre,
                               @RequestParam(name = "region", required = false) String region,
                               Model model) {
        if (genre != null) {
            model.addAttribute("festivals", festivalService.fetchFestivalsByGenre(genre));
        } else if (region != null) {
            model.addAttribute("festivals", festivalService.fetchFestivalsByRegion(region));
        } else {
            model.addAttribute("festivals", festivalService.fetchAllFestivals());
        }

        return "festivals";
    }


}