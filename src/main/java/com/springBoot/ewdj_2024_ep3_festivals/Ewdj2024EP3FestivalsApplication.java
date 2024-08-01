package com.springBoot.ewdj_2024_ep3_festivals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import perform.PerformRestFestival;
import service.*;
import validator.PerformanceValidation;
import validator.TicketQuantityValidator;

import java.util.Locale;


@SpringBootApplication
@EnableJpaRepositories("repository")
@EntityScan("domain")
//@ComponentScan({"controllers", "config", "service"})
public class Ewdj2024EP3FestivalsApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(Ewdj2024EP3FestivalsApplication.class, args);

        try {
            new PerformRestFestival();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/dashboard");
        registry.addViewController("/error").setViewName("error");
    }

    /*LOCALE*/
    @Bean
    LocaleResolver localeResolver() {
        CookieLocaleResolver slr = new CookieLocaleResolver();
//        slr.setDefaultLocale(Locale.ENGLISH);
        slr.setDefaultLocale(new Locale("nl"));
        return slr;
    }

    @Bean
    DateFormatter dateFormatter() {
        return new DateFormatter();
    }

    /*SERVICE*/
    // TODO check if both user service can be combined into 1
    @Bean
    MyUserService myUserService() {
        return new MyUserService();
    }

    @Bean
    UserDetailsService myUserDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    DashboardService dashboardService() {
        return new DashboardService();
    }

    @Bean
    FestivalService festivalService() {
        return new FestivalServiceImpl();
    }

    @Bean
    PerformanceService performanceService() {
        return new PerformanceService();
    }

    @Bean
    SubGenreService subGenreService() {
        return new SubGenreService();
    }

    @Bean
    TicketService ticketsService() {
        return new TicketService();
    }

    /*VALIDATOR*/
    @Bean
    PerformanceValidation performanceValidation() {
        return new PerformanceValidation();
    }

    @Bean
    TicketQuantityValidator ticketQuantityValidator() {
        return new TicketQuantityValidator();
    }

}
