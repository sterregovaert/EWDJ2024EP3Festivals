package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        }

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                // store the csrf token in the user's session
                http.csrf(csrf -> csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository())).
                // store the csrf token in a cookie
                // http.csrf(csrf ->
                // csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())).
                                authorizeHttpRequests(requests -> requests
                                                // pages that need to be secured
                                                .requestMatchers("/login").permitAll()
                                                .requestMatchers("/dashboard")
                                                .hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                                                .requestMatchers("/tickets").hasAnyRole(Role.USER.name())
                                                .requestMatchers("/tickets/buy").hasRole(Role.USER.name())
                                                .requestMatchers("/festivals")
                                                .hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                                                .requestMatchers("/performance/add").hasRole(Role.ADMIN.name())
                                                .requestMatchers("/performance/remove").hasRole(Role.ADMIN.name())

                                                // rest
                                                .requestMatchers("/api/festival/**").permitAll()
                                                .requestMatchers("/api/festivals").permitAll()

                                                // general stuff
                                                .requestMatchers("/error").permitAll()
                                                .requestMatchers("/css/**").permitAll()
                                                .requestMatchers("/i18n/**").permitAll()
                                                .requestMatchers("/static/favicon.ico").permitAll()
                                                .requestMatchers("/icons/**").permitAll())
                                .formLogin(form -> form.defaultSuccessUrl("/dashboard", true)
                                                .loginPage("/login")
                                                .usernameParameter("username").passwordParameter("password"))
                                .exceptionHandling(handling -> handling
                                                .accessDeniedPage("/error?errorCode=403&errorMessage=Access+Denied"));

                return http.build();
        }
}
