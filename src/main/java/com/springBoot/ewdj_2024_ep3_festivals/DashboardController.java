package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Genre;
import domain.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.DashboardService;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @GetMapping
    public String showDashboard(Model model) {
        List<Genre> genres = dashboardService.findAllGenres();
        List<Region> regions = dashboardService.findAllRegions();
        Integer ticketCount = dashboardService.findTicketCountForCurrentUser();

        model.addAttribute("genres", genres);
        model.addAttribute("regions", regions);
        model.addAttribute("ticketCount", ticketCount);

        return "dashboard";
    }
}