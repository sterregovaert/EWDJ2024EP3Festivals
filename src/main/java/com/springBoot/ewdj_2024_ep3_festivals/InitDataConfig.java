package com.springBoot.ewdj_2024_ep3_festivals;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Autowired
    private FestivalRepository festivalRepository;

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
        List<Genre> genres = Arrays.asList(
                Genre.builder().name("Rock").build(),
                Genre.builder().name("Pop").build(),
                Genre.builder().name("Jazz").build(),
                Genre.builder().name("Electronic").build()
        );
        genreRepository.saveAll(genres);

        // -------- -------- --------
        // -------- Regions --------
        // -------- -------- --------
        List<Region> regions = Arrays.asList(
                Region.builder().name("North America").build(),
                Region.builder().name("South America").build(),
                Region.builder().name("Europe").build(),
                Region.builder().name("Asia").build(),
                Region.builder().name("Africa").build(),
                Region.builder().name("Australia").build()
        );
        regionRepository.saveAll(regions);
        // -------- -------- --------
        // -------- Festivals --------
        // -------- -------- --------
        Region europe = regionRepository.findByName("Europe")
                .orElseThrow(() -> new RuntimeException("Region not found"));
        Genre rock = genreRepository.findByName("Rock")
                .orElseThrow(() -> new RuntimeException("Genre not found"));

        List<Festival> festivals = Arrays.asList(
                Festival.builder()
                        .startDateTime(LocalDateTime.of(2024, 3, 15, 14, 0))
                        .region(europe)
                        .genre(rock)
                        .availableSeats(5000)
                        .ticketPrice(49.99)
                        .build(),
                Festival.builder()
                        .startDateTime(LocalDateTime.of(2024, 7, 20, 16, 0))
                        .region(europe)
                        .genre(rock)
                        .availableSeats(8000)
                        .ticketPrice(99.99)
                        .build()
        );

        festivalRepository.saveAll(festivals);

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
