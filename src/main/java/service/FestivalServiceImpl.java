package service;

import domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.FestivalRepository;
import repository.GenreRepository;
import repository.RegionRepository;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FestivalServiceImpl implements FestivalService {
    @Autowired
    private FestivalRepository festivalRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private MyUserService myUserService;

    // TODO getTicketsBoughtPerFestivalForUser can be added to data of fetchFestivalsByGenreAndRegion or something
    public List<Festival> fetchFestivalsByGenreAndRegion(String genre, String region) {
        Optional<Genre> genreEntity = genreRepository.findByName(genre);
        Optional<Region> regionEntity = regionRepository.findByName(region);
        return festivalRepository.findByGenreAndRegion(genreEntity.orElse(null), regionEntity.orElse(null));
    }

    public Map<Long, Integer> getTicketsBoughtPerFestivalForUser(String genre, String region, Principal principal) {
        List<Festival> festivals = fetchFestivalsByGenreAndRegion(genre, region);

        MyUser user = myUserService.getUserByUsername(principal.getName());

        Map<Long, Integer> ticketsBoughtPerFestival = new HashMap<>();
        for (Festival festival : festivals) {
            int ticketsBought = ticketService.getTicketsForFestivalByUser(festival.getFestivalId(), user.getUserId());
            ticketsBoughtPerFestival.put(festival.getFestivalId(), ticketsBought);
        }

        return ticketsBoughtPerFestival;
    }


    // REST
    public List<String> getArtistsByFestival(Long festivalId) {
        return festivalRepository.findById(festivalId).map(festival -> festival.getPerformances().stream().map(Performance::getArtistName).distinct().collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    public List<Festival> getFestivalsByGenre(String genre) {
        Genre genreEntity = genreRepository.findByName(genre).orElseThrow(() -> new IllegalArgumentException("Genre not found"));

        return festivalRepository.findByGenre(genreEntity);
    }
}
