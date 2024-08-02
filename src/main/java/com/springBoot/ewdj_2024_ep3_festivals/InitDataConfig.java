package com.springBoot.ewdj_2024_ep3_festivals;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import repository.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class InitDataConfig implements CommandLineRunner {
    private static final String BCRYPTED_PASWOORD = "$2a$12$pEsi.Zg3VSZ7WJcyfpObE.zW6bsgIZV965XSE3kFcqR9nRr0XKC8G";
    // string 'paswoord': https://bcrypt-generator.com
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private SubGenreRepository subGenreRepository;
    @Autowired
    private FestivalRepository festivalRepository;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private TicketRepository ticketRepository;


    @Override
    public void run(String... args) {
        // -------- -------- --------
        // -------- USERS --------
        // -------- -------- --------
        var user = MyUser.builder().username("nameUser").role(Role.USER).password(BCRYPTED_PASWOORD).build();
        var user2 = MyUser.builder().username("nameUser2").role(Role.USER).password(BCRYPTED_PASWOORD).build();
        var admin = MyUser.builder().username("nameAdmin").role(Role.ADMIN).password(encoder.encode("123456789")).build();

        List<MyUser> userList = Arrays.asList(admin, user, user2);
        myUserRepository.saveAll(userList);

        // -------- -------- --------
        // -------- GENRES --------
        // -------- -------- --------
        List<Genre> genres = Arrays.asList(Genre.builder().name("Rock").build(), Genre.builder().name("Pop").build(), Genre.builder().name("Jazz").build(), Genre.builder().name("Electronic").build());
        genreRepository.saveAll(genres);

        // -------- -------- --------
        // -------- SUBGENRES --------
        // -------- -------- --------
        List<SubGenre> subGenres = genres.stream().flatMap(genre -> {
            return switch (genre.getName()) {
                case "Rock" ->
                        Stream.of(SubGenre.builder().name("Alternative Rock").genre(genre).build(), SubGenre.builder().name("Classic Rock").genre(genre).build(), SubGenre.builder().name("Hard Rock").genre(genre).build());
                case "Pop" ->
                        Stream.of(SubGenre.builder().name("Synth Pop").genre(genre).build(), SubGenre.builder().name("Electro Pop").genre(genre).build(), SubGenre.builder().name("Indie Pop").genre(genre).build());
                case "Jazz" ->
                        Stream.of(SubGenre.builder().name("Smooth Jazz").genre(genre).build(), SubGenre.builder().name("Bebop").genre(genre).build(), SubGenre.builder().name("Swing").genre(genre).build());
                case "Electronic" ->
                        Stream.of(SubGenre.builder().name("House").genre(genre).build(), SubGenre.builder().name("Techno").genre(genre).build(), SubGenre.builder().name("Trance").genre(genre).build());
                default -> Stream.empty();
            };
        }).collect(Collectors.toList());

        subGenreRepository.saveAll(subGenres);

        // -------- -------- --------
        // -------- REGIONS --------
        // -------- -------- --------
        List<Region> regions = Arrays.asList(Region.builder().name("North America").build(), Region.builder().name("South America").build(), Region.builder().name("Europe").build(), Region.builder().name("Asia").build(), Region.builder().name("Africa").build(), Region.builder().name("Australia").build());
        regionRepository.saveAll(regions);

        // -------- -------- --------
        // -------- FESTIVALS & PERFORMANCES --------
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
        List<Performance> performances = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            // festival
            String adjective = adjectives.get(ThreadLocalRandom.current().nextInt(adjectives.size()));
            String type = types.get(ThreadLocalRandom.current().nextInt(types.size()));
            String whimsicalWord = whimsicalWords.get(ThreadLocalRandom.current().nextInt(whimsicalWords.size()));
            String festivalName = String.format("%s %s %s", adjective, whimsicalWord, type);

            LocalDateTime startOfFestival = LocalDateTime.of(2024, ThreadLocalRandom.current().nextInt(7, 13), ThreadLocalRandom.current() // month
                    .nextInt(1, 29), ThreadLocalRandom.current() // day
                    .nextInt(9, 20), 0) // hour and minutes
                    ;

            Festival festival = Festival.builder().startDateTime(startOfFestival).region(allRegions.get(ThreadLocalRandom.current().nextInt(allRegions.size()))).genre(allGenres.get(ThreadLocalRandom.current().nextInt(allGenres.size()))).availablePlaces(ThreadLocalRandom.current().nextInt(0, 31)).ticketPrice(ThreadLocalRandom.current().nextDouble(1, 150.1)).name(festivalName).build();

            festivals.add(festival);

            // 2 performances per festival
            for (int j = 0; j < 2; j++) {
                Performance performance = new Performance();
                List<String> artistNames = genreToArtistsMap.get(festival.getGenre().getName());
                String artistName = artistNames.get(new Random().nextInt(artistNames.size()));

                LocalDateTime startOfPerformance = festival.getStartDateTime().plusHours(j + 1);

                performance.setArtistName(artistName);
                performance.setStartDateTime(startOfPerformance);
                performance.setEndDateTime(startOfPerformance.plusHours(1));
                performance.setFestival(festival);
                performance.setFestivalNumber1(3333);
                performance.setFestivalNumber2(3333);

                performances.add(performance);
            }


        }
        festivalRepository.saveAll(festivals);
        performanceRepository.saveAll(performances);


        // -------- -------- --------
        // -------- TICKETS --------
        // -------- -------- --------
        MyUser nameUser = myUserRepository.findByUsername("nameUser");

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


}
