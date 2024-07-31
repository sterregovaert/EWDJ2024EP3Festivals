package service;

import domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FestivalServiceImpl implements FestivalService {
    @Autowired
    MyUserRepository myUserRepository;
    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private GenreRepository genreRepository;


    // TODO getTicketsBoughtPerFestivalForUser can be added to data of fetchFestivalsByGenreAndRegion or something
    public List<Festival> fetchFestivalsByGenreAndRegion(String genre, String region) {
        Optional<Genre> genreEntity = genreRepository.findByName(genre);
        Optional<Region> regionEntity = regionRepository.findByName(region);
        return festivalRepository.findByGenreAndRegion(genreEntity.orElse(null), regionEntity.orElse(null));
    }

    public Map<Long, Integer> getTicketsBoughtPerFestivalForUser(String genre, String region, Principal principal) {
        List<Festival> festivals = fetchFestivalsByGenreAndRegion(genre, region);

        MyUser user = myUserRepository.findByUsername(principal.getName());

        Map<Long, Integer> ticketsBoughtPerFestival = new HashMap<>();
        for (Festival festival : festivals) {
            int ticketsBought = getTicketsForFestivalByUser(festival.getFestivalId(), user.getUserId());
            ticketsBoughtPerFestival.put(festival.getFestivalId(), ticketsBought);
        }

        return ticketsBoughtPerFestival;
    }

    // ---- ---- ---- ----
    // TODO explanation
    // ---- ---- ---- ----

    public Festival findFestivalById(Long festivalId) {
        return festivalRepository.findById(festivalId).orElse(null);
    }

    public int getTicketsForFestivalByUser(Long festivalId, Long userId) {
        Integer ticketsCount = ticketRepository.sumTicketQuantitiesByUserIdAndFestivalId(userId, festivalId);
        return ticketsCount != null ? ticketsCount : 0;
    }


    public void updateAvailableSeats(Long festivalId, int quantity) {
        Festival festival = findFestivalById(festivalId);
        festival.setAvailableSeats(festival.getAvailableSeats() - quantity);
        festivalRepository.save(festival);
    }

    // ---- ---- ---- ----
    // REST
    // ---- ---- ---- ----
    public List<String> getArtistsByFestival(Long festivalId) {
        return festivalRepository.findById(festivalId)
                .map(festival -> festival.getPerformances().stream()
                        .map(Performance::getArtistName)
                        .distinct()
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public List<Festival> getFestivalsByGenre(String genre) {
        Genre genreEntity = genreRepository.findByName(genre).orElseThrow(() -> new IllegalArgumentException("Genre not found"));

        return festivalRepository.findByGenre(genreEntity);
    }
}
