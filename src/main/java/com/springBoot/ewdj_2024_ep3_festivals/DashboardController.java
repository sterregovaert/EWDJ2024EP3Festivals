package com.springBoot.ewdj_2024_ep3_festivals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.DashboardService;

import java.util.Collections;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @GetMapping
    public String showDashboard(Model model) {
        model.addAttribute("genres", dashboardService.findAllGenres());
        model.addAttribute("regions", dashboardService.findAllRegions());
        model.addAttribute("ticketCount", dashboardService.findTicketCountForCurrentUser());

        return "dashboard";
    }


    @ExceptionHandler(RuntimeException.class)
    public String handleException(RuntimeException e, Model model) {
        model.addAttribute("genres", Collections.emptyList());
        model.addAttribute("regions", Collections.emptyList());
        model.addAttribute("ticketCount", 0);
        model.addAttribute("errorMessage", "An error occurred while fetching data.");
        return "dashboard";
    }
}