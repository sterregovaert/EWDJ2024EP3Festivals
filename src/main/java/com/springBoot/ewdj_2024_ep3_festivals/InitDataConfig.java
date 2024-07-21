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
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private TicketRepository ticketRepository;

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
        // -------- GENRES --------
        // -------- -------- --------
        List<Genre> genres = Arrays.asList(Genre.builder().name("Rock").build(), Genre.builder().name("Pop").build(), Genre.builder().name("Jazz").build(), Genre.builder().name("Electronic").build());
        genreRepository.saveAll(genres);

        // -------- -------- --------
        // -------- REGIONS --------
        // -------- -------- --------
        List<Region> regions = Arrays.asList(Region.builder().name("North America").build(), Region.builder().name("South America").build(), Region.builder().name("Europe").build(), Region.builder().name("Asia").build(), Region.builder().name("Africa").build(), Region.builder().name("Australia").build());
        regionRepository.saveAll(regions);

        // -------- -------- --------
        // -------- FESTIVALS --------
        // -------- -------- --------
        List<Region> allRegions = regionRepository.findAll();
        List<Genre> allGenres = genreRepository.findAll();

        List<String> adjectives = Arrays.asList("Epic", "Mega", "Ultra", "Magic", "Super", "Fantastic", "Awesome", "Great", "Cool", "Amazing", "Incredible", "Unbelievable", "Spectacular", "Wonderful", "Fabulous", "Terrific", "Marvelous", "Phenomenal", "Astounding", "Mind-blowing", "Mind-boggling", "Mind-bending");
        List<String> types = Arrays.asList("Fest", "Festival", "Gala", "Concert", "Celebration", "Jamboree");
        List<String> whimsicalWords = Arrays.asList("Twilight", "Odyssey", "Voyage", "Quest", "Safari", "Expedition", "Journey", "Escape");

        Map<String, List<String>> genreToArtistsMap = new HashMap<>();
        genreToArtistsMap.put("Rock", Arrays.asList("The Eternal Rockers", "Granite Sound", "Quartz Vibes"));
        genreToArtistsMap.put("Pop", Arrays.asList("Bubblegum Beats", "Candy Melodies", "Poppy Harmonics"));
        genreToArtistsMap.put("Jazz", Arrays.asList("Smooth Saxophones", "Jazzy Jive", "Brass Echoes"));
        genreToArtistsMap.put("Electronic", Arrays.asList("Digital Dreams", "Circuit Symphony", "Electric Essence"));

        List<Festival> festivals = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String adjective = adjectives.get(ThreadLocalRandom.current().nextInt(adjectives.size()));
            String type = types.get(ThreadLocalRandom.current().nextInt(types.size()));
            String whimsicalWord = whimsicalWords.get(ThreadLocalRandom.current().nextInt(whimsicalWords.size()));
            String festivalName = String.format("%s %s %s", adjective, whimsicalWord, type);


            festivals.add(Festival.builder().startDateTime(LocalDateTime.of(2024, ThreadLocalRandom.current().nextInt(7, 13), ThreadLocalRandom.current().nextInt(1, 29), ThreadLocalRandom.current().nextInt(0, 24), 0)).region(allRegions.get(ThreadLocalRandom.current().nextInt(allRegions.size()))).genre(allGenres.get(ThreadLocalRandom.current().nextInt(allGenres.size()))).availableSeats(ThreadLocalRandom.current().nextInt(0, 31)).ticketPrice(ThreadLocalRandom.current().nextDouble(1, 150.1)).name(festivalName).build());

        }
        festivalRepository.saveAll(festivals);


        // -------- -------- --------
        // -------- PERFORMANCES --------
        // -------- -------- --------
        for (Festival festival : festivals) {
            List<Performance> performances = new ArrayList<>();
            LocalDateTime performanceStartTime = festival.getStartDateTime().plusHours(1); // Start 1 hour after festival starts

            for (int i = 0; i < 2; i++) {
                Performance performance = new Performance();
                List<String> artistNames = genreToArtistsMap.get(festival.getGenre().getName());
                String artistName = artistNames.get(new Random().nextInt(artistNames.size()));

                performance.setArtistName(artistName);
                performance.setStartDateTime(performanceStartTime);
                performance.setEndDateTime(performanceStartTime.plusHours(2)); // Each performance lasts 2 hours
                performance.setFestival(festival);

                performances.add(performance);

                performanceStartTime = performance.getEndDateTime().plusHours(1); // Next performance starts 1 hour after the previous ends
            }

            performanceRepository.saveAll(performances);
        }

        // -------- -------- --------
        // -------- TICKETS --------
        // -------- -------- --------
        MyUser nameUser = userRepository.findByUsername("nameUser");

        List<Ticket> tickets = new ArrayList<>();
        for (Festival festival : festivals) {
            if ("Europe".equals(festival.getRegion().getName())) {
                Ticket ticket = new Ticket();
                ticket.setUser(nameUser);
                ticket.setQuantity(2);
                ticket.setFestival(festival);
                tickets.add(ticket);
            }
        }
        ticketRepository.saveAll(tickets);

    }

    private static byte[] loadImage(String imageName) throws IOException {
        return Files.readAllBytes(Paths.get("src/main/resources/static/images/sport", imageName));
    }

}
