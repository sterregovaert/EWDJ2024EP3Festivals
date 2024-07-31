package com.springBoot.ewdj_2024_ep3_festivals;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex, Model model) {
        ModelAndView modelAndView = new ModelAndView("dashboard");
        model.addAttribute("error", "Service error");
        model.addAttribute("genres", List.of());
        model.addAttribute("regions", List.of());
        model.addAttribute("ticketCount", 0);
        return modelAndView;
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}