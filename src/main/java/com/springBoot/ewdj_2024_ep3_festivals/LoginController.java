package com.springBoot.ewdj_2024_ep3_festivals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;


@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout, Model model, Locale locale) {

        if (error != null) {
            model.addAttribute("error", messageSource.getMessage("login.error", null, locale));
        }
        if (logout != null) {
            model.addAttribute("msg", messageSource.getMessage("login.logout", null, locale));
        }

        return "login";
    }

}