package com.springBoot.ewdj_2024_ep3_festivals;

import exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/error";
    }

    @ExceptionHandler(FestivalNotFoundException.class)
    public String handleFestivalNotFound(FestivalNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/error";
    }


    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<String> handleGenreNotFoundException(GenreNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoArtistsException.class)
    public ResponseEntity<String> handleNoArtistsException(NoArtistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoFestivalsException.class)
    public ResponseEntity<String> handleNoFestivalsException(NoFestivalsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}