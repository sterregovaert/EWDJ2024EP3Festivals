package service;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FestivalServiceImpl implements FestivalService {
    @Autowired
    private FestivalRepository festivalRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private SubGenreRepository subGenreRepository;


    public List<Festival> fetchFestivals(String genre, String region) {
        if (genre != null && region != null) {
            return fetchFestivalsByGenreAndRegion(genre, region);
        } else if (genre != null) {
            return fetchFestivalsByGenre(genre);
        } else if (region != null) {
            return fetchFestivalsByRegion(region);
        } else {
            return fetchAllFestivals();
        }
    }

    public List<Festival> fetchFestivalsByGenre(String genreName) {
        return genreRepository.findByName(genreName).map(genre -> festivalRepository.findByGenreOrderByRegionDescStartDateTimeAsc(genre)).orElse(Collections.emptyList());
    }

    public List<Festival> fetchFestivalsByRegion(String regionName) {
        return regionRepository.findByName(regionName).map(region -> festivalRepository.findByRegionOrderByGenreAscStartDateTimeAsc(region)).orElse(Collections.emptyList());
    }

    public List<Festival> fetchFestivalsByGenreAndRegion(String genreName, String regionName) {
        Genre genre = genreRepository.findByName(genreName).orElse(null);
        Region region = regionRepository.findByName(regionName).orElse(null);
        if (genre != null && region != null) {
            return festivalRepository.findByGenreAndRegionOrderByStartDateTimeAsc(genre, region);
        } else {
            return Collections.emptyList();
        }
    }

    public List<Festival> fetchAllFestivals() {
        return festivalRepository.findAllByOrderByGenreAscRegionAscStartDateTimeAsc();
    }

    public Festival findFestivalById(Long festivalId) {
        return festivalRepository.findById(festivalId).orElse(null);
    }

    public int getTicketsForFestivalByUser(Long festivalId, Long userId) {
        Integer ticketsCount = ticketRepository.sumTicketQuantitiesByUserIdAndFestivalId(userId, festivalId);
        return ticketsCount != null ? ticketsCount : 0;
    }

    public Map<Long, Integer> getTicketsBoughtPerFestivalForUser(String genre, String region, Long userId) {
        List<Festival> festivals;
        if (genre != null && region != null) {
            festivals = fetchFestivalsByGenreAndRegion(genre, region);
        } else if (genre != null) {
            festivals = fetchFestivalsByGenre(genre);
        } else if (region != null) {
            festivals = fetchFestivalsByRegion(region);
        } else {
            festivals = fetchAllFestivals();
        }

        Map<Long, Integer> ticketsBoughtPerFestival = new HashMap<>();
        for (Festival festival : festivals) {
            int ticketsBought = getTicketsForFestivalByUser(festival.getFestivalId(), userId);
            ticketsBoughtPerFestival.put(festival.getFestivalId(), ticketsBought);
        }

        return ticketsBoughtPerFestival;
    }


    public List<SubGenre> getSubGenresByGenre(Genre genre) {
        if (genre != null) {
            List<SubGenre> subGenres = subGenreRepository.findByGenre(genre);
            return subGenres;
        }
        return Collections.emptyList();
    }

    public void updateAvailableSeats(Long festivalId, int quantity) {
        Festival festival = findFestivalById(festivalId);
        festival.setAvailableSeats(festival.getAvailableSeats() - quantity);
        festivalRepository.save(festival);
    }

    public List<String> getArtistsByFestival(Long festivalId) {
        return festivalRepository.findById(festivalId)
                .map(festival -> festival.getPerformances().stream()
                        .map(Performance::getArtistName)
                        .distinct()
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public List<String> getFestivalsByGenre(String genre) {
        return genreRepository.findByName(genre)
                .map(festivalRepository::findByGenre)
                .orElse(Collections.emptyList())
                .stream()
                .map(Festival::getName)
                .collect(Collectors.toList());
    }
}
