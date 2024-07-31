package com.springBoot.ewdj_2024_ep3_festivals;

import domain.MyUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import repository.MyUserRepository;
import service.FestivalService;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/festivals")
public class FestivalsController {

    @Autowired
    FestivalService festivalService;
    @Autowired
    MyUserRepository myUserRepository;

    // Fetching festivals
    @GetMapping
    public String getFestivals(@RequestParam(name = "genre", required = false) String genre, @RequestParam(name = "region", required = false) String region, Model model, WebRequest request, Principal principal) {

        model.addAttribute("festivals", festivalService.fetchFestivals(genre, region));

        MyUser user = myUserRepository.findByUsername(principal.getName());
        Map<Long, Integer> ticketsBoughtPerFestival = festivalService.getTicketsBoughtPerFestivalForUser(genre, region, user.getUserId());
        model.addAttribute("ticketsBoughtPerFestival", ticketsBoughtPerFestival);

        model.addAttribute("genre", genre);
        model.addAttribute("region", region);

        return "festivals";
    }


}