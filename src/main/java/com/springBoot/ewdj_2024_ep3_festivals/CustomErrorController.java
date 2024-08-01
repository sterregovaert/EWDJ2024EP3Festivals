package com.springBoot.ewdj_2024_ep3_festivals;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_CODE = "errorCode";
    private static final String ERROR_MESSAGE = "errorMessage";

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute(ERROR_CODE, "404");
                model.addAttribute(ERROR_MESSAGE, "Page not found");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute(ERROR_CODE, "403");
                model.addAttribute(ERROR_MESSAGE, "Access is denied");
            } else {
                model.addAttribute(ERROR_CODE, statusCode.toString());
                model.addAttribute(ERROR_MESSAGE, "An unexpected error occurred");
            }
        } else {
            model.addAttribute(ERROR_CODE, "Unknown");
            model.addAttribute(ERROR_MESSAGE, "An unexpected error occurred");
        }

        return "error";
    }
}