package com.springBoot.ewdj_2024_ep3_festivals;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import repository.*;

@Component
public class InitDataConfig implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private RegionRepository regionRepository;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final String BCRYPTED_PASWOORD = "$2a$12$pEsi.Zg3VSZ7WJcyfpObE.zW6bsgIZV965XSE3kFcqR9nRr0XKC8G";
    // string 'paswoord': https://bcrypt-generator.com


    @Override
    public void run(String... args) {
        // -------- -------- --------
        // -------- USERS --------
        // -------- -------- --------
        var user = MyUser.builder().username("nameUser").role(Role.USER).password(BCRYPTED_PASWOORD).build();
        var user2 = MyUser.builder().username("nameUser2").role(Role.USER).password(BCRYPTED_PASWOORD).build();
        var admin = MyUser.builder().username("nameAdmin").role(Role.ADMIN).password(encoder.encode("123456789")).build();

        List<MyUser> userList = Arrays.asList(admin, user, user2);
        userRepository.saveAll(userList);

        // -------- -------- --------
        // -------- Genres --------
        // -------- -------- --------
        List<Genre> genres = Arrays.asList(new Genre(null, "Rock"), new Genre(null, "Pop"), new Genre(null, "Jazz"), new Genre(null, "Electronic"));
        genreRepository.saveAll(genres);
        // -------- -------- --------
        // -------- Regions --------
        // -------- -------- --------
        List<Region> regions = Arrays.asList(new Region(null, "North America"), new Region(null, "South America"), new Region(null, "Europe"), new Region(null, "Asia"), new Region(null, "Africa"), new Region(null, "Australia"));
        regionRepository.saveAll(regions);
        // -------- -------- --------
        // -------- TICKETS --------
        // -------- -------- --------
        /*MyUser nameUser = userRepository.findByUsername("nameUser");

        List<Ticket> tickets = new ArrayList<>();

        for (Competition competition : competitions) {
            Ticket ticket = new Ticket();
            ticket.setUser(nameUser);
            ticket.setQuantity(3);
            ticket.setCompetition(competition);
            tickets.add(ticket);
        }

        ticketRepository.saveAll(tickets);*/

    }

    private static byte[] loadImage(String imageName) throws IOException {
        return Files.readAllBytes(Paths.get("src/main/resources/static/images/sport", imageName));
    }

}
