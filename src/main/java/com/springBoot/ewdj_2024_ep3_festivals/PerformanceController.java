package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import domain.Genre;
import domain.Performance;
import domain.SubGenre;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.PerformanceService;
import validator.PerformanceDateTimeValidation;
import validator.PerformanceTimeSlotValidation;

@Controller
@RequestMapping("/performance")
public class PerformanceController {
    @Autowired
    PerformanceService performanceService;
    @Autowired
    PerformanceDateTimeValidation performanceDateTimeValidation;
    @Autowired
    PerformanceTimeSlotValidation performanceTimeSlotValidation;
    @Autowired
    private MessageSource messageSource;

    // ---- ---- ---- ----
    // ADDING performance to festival
    // ---- ---- ---- ----
    @GetMapping("/add")
    public String showAddPerformanceForm(@RequestParam("festivalId") Long festivalId, Model model) {
        Performance performance = new Performance();
        Festival festival = performanceService.setupAddPerformanceWithDefaults(festivalId, performance);
        model.addAttribute("festival", festival);
        model.addAttribute("performance", performance);

        Genre festivalGenre = festival.getGenre();
        List<SubGenre> subGenres = performanceService.setupSubGenresForFestivalGenre(festivalGenre);
        model.addAttribute("subGenres", subGenres);

        List<Performance> performances = performanceService.setupPerformances(festivalId);
        model.addAttribute("performances", performances);

        return "performance-add";
    }

    @PostMapping("/add")
    public String addPerformance(
            @RequestParam("festivalId") Long festivalId,
            @Valid @ModelAttribute Performance performance,
            BindingResult result,
            Model model) {
        performance = performanceService.setupPerformanceForFestival(festivalId, performance);

        performanceTimeSlotValidation.validate(performance, result);
        performanceDateTimeValidation.validate(performance, result);

        if (result.hasErrors()) {
            Festival festival = performanceService.setupAddPerformanceWithDefaults(festivalId, performance);
            model.addAttribute("festival", festival);
            model.addAttribute("performance", performance);

            Genre festivalGenre = festival.getGenre();
            List<SubGenre> subGenres = performanceService.setupSubGenresForFestivalGenre(festivalGenre);
            model.addAttribute("subGenres", subGenres);

            List<Performance> performances = performanceService.setupPerformances(festivalId);
            model.addAttribute("performances", performances);

            return "performance-add";
        }

        performance = performanceService.savePerformance(performance);
        Festival festival = performance.getFestival();

        return "redirect:/festivals?genre=" + festival.getGenre().getName() + "&region="
                + festival.getRegion().getName();
    }

    // ---- ---- ---- ----
    // REMOVING performance from festival
    // ---- ---- ---- ----
    @GetMapping("/remove")
    public String showRemovePerformanceForm(@RequestParam("festivalId") Long festivalId, Model model) {
        Festival festival = performanceService.setupFestival(festivalId);
        model.addAttribute("festival", festival);

        List<Performance> performances = performanceService.setupPerformances(festivalId);
        model.addAttribute("performances", performances);

        return "performance-remove";
    }

    @PostMapping("/remove")
    public String removePerformance(@RequestParam("festivalId") Long festivalId,
            @RequestParam("performanceId") Long performanceId, RedirectAttributes redirectAttributes) {
        try {
            performanceService.deletePerformanceById(performanceId);

            String successMessage = messageSource.getMessage("performanceRemove.successMessage", null,
                    LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("message", successMessage);
        } catch (IllegalArgumentException e) {
            String errorMessage = messageSource.getMessage("performanceRemove.errorMessage", null,
                    LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("message", errorMessage);
        }

        try {
            Festival festival = performanceService.setupFestival(festivalId);

            return "redirect:/festivals?genre=" + festival.getGenre().getName() + "&region="
                    + festival.getRegion().getName();
        } catch (IllegalArgumentException e) {
            String errorMessage = messageSource.getMessage("performanceRemove.errorMessage", null,
                    LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("message", errorMessage);

            return "dashboard";
        }
    }

}
