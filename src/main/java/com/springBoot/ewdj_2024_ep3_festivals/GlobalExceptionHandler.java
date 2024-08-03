package com.springBoot.ewdj_2024_ep3_festivals;

import exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleRuntimeException(RuntimeException ex, Model model, WebRequest request) {
        String requestUri = request.getDescription(false);
        if (requestUri.contains("/dashboard")) {
            model.addAttribute("genres", Collections.emptyList());
            model.addAttribute("regions", Collections.emptyList());
            model.addAttribute("ticketCount", 0);
            model.addAttribute("errorMessage", "An error occurred while fetching data.");
            return new ModelAndView("dashboard");
        } else {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("message", "Internal Server Error");
            return mav;
        }
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/error";
    }


}