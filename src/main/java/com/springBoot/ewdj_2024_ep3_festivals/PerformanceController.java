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
import repository.FestivalRepository;
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
    private FestivalRepository festivalRepository;

    // ---- ---- ---- ----
    // ADDING performance to festival
    // ---- ---- ---- ----
    @GetMapping("/add")
    public String showAddPerformanceForm(@RequestParam("festivalId") Long festivalId, Model model) {
        Performance performance = new Performance();
        performanceService.setupAddPerformanceFormModel(festivalId, performance, model);
        return "performance-add";
    }

    @PostMapping("/add")
    public String addPerformance(@RequestParam("festivalId") Long festivalId, @Valid @ModelAttribute Performance performance, BindingResult result, Model model) {
        performanceService.setupPerformanceForFestival(festivalId, performance, result, model);
        // TODO split up the validate in sub validations
        performanceValidation.validate(performance, result);

        if (result.hasErrors()) {
            performanceService.setupAddPerformanceFormModel(festivalId, performance, model);
            return "performance-add";
        }

        performanceService.savePerformance(performance);
        Festival festival = performance.getFestival();
        return "redirect:/festivals?genre=" + festival.getGenre().getName() + "&region=" + festival.getRegion().getName();
    }

    // ---- ---- ---- ----
    // REMOVING performance from festival
    // ---- ---- ---- ----
    @GetMapping("/remove")
    public String showRemovePerformanceForm(@RequestParam("festivalId") Long festivalId, Model model) {
        performanceService.setupRemovePerformanceFormModel(festivalId, model);
        return "performance-remove";
    }

    @PostMapping("/remove")
    public String removePerformance(@RequestParam("festivalId") Long festivalId, @RequestParam("performanceId") Long performanceId, RedirectAttributes redirectAttributes) {
        performanceService.deletePerformanceById(performanceId);
        String successMessage = messageSource.getMessage("performanceRemove.successMessage", null, LocaleContextHolder.getLocale());
        redirectAttributes.addFlashAttribute("message", successMessage);
        Festival festival = festivalRepository.findById(festivalId).orElse(null);
        return "redirect:/festivals?genre=" + festival.getGenre().getName() + "&region=" + festival.getRegion().getName();
    }


}
