package com.springBoot.ewdj_2024_ep3_festivals;

import exceptions.FestivalNotFoundException;
import exceptions.NoArtistsException;
import exceptions.NoFestivalsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
class FestivalErrorAdvice {

    @ResponseBody
    @ExceptionHandler(FestivalNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> festivalNotFoundHandler(FestivalNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return response;
    }

    @ResponseBody
    @ExceptionHandler(NoFestivalsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> noFestivalsFoundHandler(NoFestivalsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return response;
    }

    @ResponseBody
    @ExceptionHandler(NoArtistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> noArtistsFoundHandler(NoArtistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return response;
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> illegalArgumentHandler(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return response;
    }
}