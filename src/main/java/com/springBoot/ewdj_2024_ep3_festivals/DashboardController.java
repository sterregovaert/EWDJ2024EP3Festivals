package com.springBoot.ewdj_2024_ep3_festivals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.DashboardService;

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

}