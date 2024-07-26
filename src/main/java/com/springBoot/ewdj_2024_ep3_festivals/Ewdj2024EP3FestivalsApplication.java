package com.springBoot.ewdj_2024_ep3_festivals;

import java.util.Locale;

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
import validator.*;


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
        registry.addViewController("/403").setViewName("403");
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
    public FormatterService formatterService() {
        return new FormatterService();
    }

    @Bean
    DateFormatter dateFormatter() {
        return new DateFormatter();
    }

    @Bean
    TimeFormatter timeformatter() {
        return new TimeFormatter();
    }


    /*SERVICE*/
    @Bean
    UserDetailsService myUserDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    DashboardService dashboardService() {
        return new DashboardService();
    }

    @Bean
    FestivalsService festivalService() {
        return new FestivalsService();
    }

    @Bean
    TicketsService ticketsService() {
        return new TicketsService();
    }

    @Bean
    PerformanceService performanceService() {
        return new PerformanceService();
    }

    /*VALIDATOR*/
    @Bean
    TicketValidator ticketValidator() {
        return new TicketValidator();
    }

    @Bean
    PerformanceValidation performanceValidation() {
        return new PerformanceValidation();
    }

}
