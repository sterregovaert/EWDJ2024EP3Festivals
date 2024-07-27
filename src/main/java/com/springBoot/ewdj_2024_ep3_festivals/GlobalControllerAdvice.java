package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

//    @Autowired
//    private DateFormatter dateFormatter;
//
//    @Autowired
//    private TimeFormatter timeFormatter;
//
//    @ModelAttribute("dateFormatter")
//    public DateFormatter dateFormatter() {
//        return dateFormatter;
//    }
//
//    @ModelAttribute("timeFormatter")
//    public TimeFormatter timeFormatter() {
//        return timeFormatter;
//    }

    @ModelAttribute
    public void addAttributes(Model model, Principal principal, Authentication authentication) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());

            // Get the user role
            String userRole = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(String.valueOf(Role.USER)); // default to "USER" if no role is found
            String simpleUserRole = userRole.substring(5).toLowerCase();
            model.addAttribute("userRole", simpleUserRole);
        }
    }
}