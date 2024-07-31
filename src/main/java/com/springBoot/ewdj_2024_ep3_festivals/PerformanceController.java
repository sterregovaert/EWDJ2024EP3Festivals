package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import domain.Performance;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.FestivalService;
import service.PerformanceService;
import validator.PerformanceValidation;

@Controller
@RequestMapping("/performance")
public class PerformanceController {
    @Autowired
    PerformanceService performanceService;
    @Autowired
    PerformanceValidation performanceValidation;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private FestivalService festivalService;

    // ---- ---- ---- ----
    // ADDING performance to festival
    // ---- ---- ---- ----
    @GetMapping("/add")
    public String showAddPerformanceForm(@RequestParam("festivalId") Long festivalId, Model model) {
        Performance performance = new Performance();
        try {
            performanceService.setupAddPerformanceFormModel(festivalId, performance, model);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "performance-add";
    }

    @PostMapping("/add")
    public String addPerformance(@RequestParam("festivalId") Long festivalId, @Valid @ModelAttribute Performance performance, BindingResult result, Model model) {
        try {
            performanceService.setupPerformanceForFestival(festivalId, performance);
            performanceValidation.validate(performance, result);

            if (result.hasErrors()) {
                performanceService.setupAddPerformanceFormModel(festivalId, performance, model);
                return "performance-add";
            }

            performanceService.savePerformance(performance);
            Festival festival = performance.getFestival();
            return "redirect:/festivals?genre=" + festival.getGenre().getName() + "&region=" + festival.getRegion().getName();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    // ---- ---- ---- ----
    // REMOVING performance from festival
    // ---- ---- ---- ----
    @GetMapping("/remove")
    public String showRemovePerformanceForm(@RequestParam("festivalId") Long festivalId, Model model) {
        try {
            performanceService.setupRemovePerformanceFormModel(festivalId, model);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "performance-remove";
    }

    @PostMapping("/remove")
    public String removePerformance(@RequestParam("festivalId") Long festivalId, @RequestParam("performanceId") Long performanceId, RedirectAttributes redirectAttributes) {
        try {
            performanceService.deletePerformanceById(performanceId);
            String successMessage = messageSource.getMessage("performanceRemove.successMessage", null, LocaleContextHolder.getLocale());
            redirectAttributes.addFlashAttribute("message", successMessage);
            Festival festival = festivalService.findFestivalById(festivalId);
            return "redirect:/festivals?genre=" + festival.getGenre().getName() + "&region=" + festival.getRegion().getName();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/performance/remove?festivalId=" + festivalId;
        }
    }


}
