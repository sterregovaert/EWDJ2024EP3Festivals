package com.springBoot.ewdj_2024_ep3_festivals;

import exceptions.DuplicateFestivalException;
import exceptions.FestivalNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class FestivalErrorAdvice {

    @ResponseBody
    @ExceptionHandler(FestivalNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String festivalNotFoundHandler(FestivalNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(DuplicateFestivalException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String duplicateFestivalHandler(DuplicateFestivalException ex) {
        return ex.getMessage();
    }
}
