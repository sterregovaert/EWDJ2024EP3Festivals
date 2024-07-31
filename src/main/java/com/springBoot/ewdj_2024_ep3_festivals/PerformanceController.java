package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import domain.Performance;
import domain.SubGenre;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import repository.MyUserRepository;
import service.FestivalService;
import service.PerformanceService;
import validator.PerformanceValidation;

import java.util.List;

@Controller
@RequestMapping("/performance")
public class PerformanceController {
    // TODO remove festival service
    @Autowired
    FestivalService festivalService;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    MyUserRepository myUserRepository;
    @Autowired
    PerformanceValidation performanceValidation;
    @Autowired
    private MessageSource messageSource;

    // ADDING performance to festival

    @GetMapping("/addPerformance")
    public String showAddPerformanceForm(@RequestParam("festivalId") Long festivalId, Model model) {
        setupAddPerformanceFormModel(festivalId, model, new Performance());
        return "performance-add";
    }

    @PostMapping("/addPerformance")
    public String addPerformance(@RequestParam("festivalId") Long festivalId, @Valid @ModelAttribute Performance performance, BindingResult result, Model model) {
        Festival festival = festivalService.findFestivalById(festivalId);
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
        Festival festival = festivalService.findFestivalById(festivalId);

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

        List<SubGenre> subGenres = festivalService.getSubGenresByGenre(festival.getGenre());
        model.addAttribute("subGenres", subGenres);

        model.addAttribute("performance", performance);
    }

    // REMOVING performance from festival

    @GetMapping("/removePerformance")
    public String showRemovePerformanceForm(@RequestParam("festivalId") Long festivalId, Model model) {
        Festival festival = festivalService.findFestivalById(festivalId);
        if (festival != null) {
            List<Performance> performances = performanceService.getPerformancesByFestival(festivalId);

            model.addAttribute("performances", performances);
            model.addAttribute("festival", festival);
        } else {
            model.addAttribute("error", "Festival not found");
        }

        return "performance-remove";
    }

    @PostMapping("/removePerformance")
    public String removePerformance(@RequestParam("festivalId") Long festivalId, @RequestParam("performanceId") Long performanceId, RedirectAttributes redirectAttributes) {
        performanceService.deletePerformanceById(performanceId);
        String successMessage = messageSource.getMessage("performanceRemove.successMessage", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("message", successMessage);
        Festival festival = festivalService.findFestivalById(festivalId);
        return "redirect:/festivals?genre=" + festival.getGenre().getName() + "&region=" + festival.getRegion().getName();
    }
}
