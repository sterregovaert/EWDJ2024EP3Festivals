package service;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import repository.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FestivalService {
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

    public List<Festival> fetchFestivalsByGenre(String genreName) {
        return genreRepository.findByName(genreName).map(genre -> festivalRepository.findByGenreOrderByRegionDescStartDateTimeAsc(genre)).orElse(Collections.emptyList());
    }

    public List<Festival> fetchFestivalsByRegion(String regionName) {
        return regionRepository.findByName(regionName).map(region -> festivalRepository.findByRegionOrderByGenreAscStartDateTimeAsc(region)).orElse(Collections.emptyList());
    }

    public List<Festival> fetchAllFestivals() {
        return festivalRepository.findAllByOrderByGenreAscRegionAscStartDateTimeAsc();
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

    public int getAvailableSeatsForFestival(Long festivalId) {
        return festivalRepository.findById(festivalId).map(Festival::getAvailableSeats).orElse(0);
    }

    public int buyTicketForFestival(Long festivalId, int quantity, WebRequest request) {
        Festival festival = festivalRepository.findById(festivalId).orElse(null);
        if (festival == null || quantity <= 0) {
            return 0;
        }

        Map<Long, Integer> userTickets = (Map<Long, Integer>) request.getAttribute("userTickets", WebRequest.SCOPE_SESSION);
        if (userTickets == null) {
            userTickets = new HashMap<>();
        }

        int ticketsForThisFestival = userTickets.getOrDefault(festivalId, 0);
        int availableSeats = festival.getAvailableSeats();

        if (ticketsForThisFestival + quantity > availableSeats) {
            return 0;
        }

        festival.setAvailableSeats(availableSeats - quantity);
        festivalRepository.save(festival);

        userTickets.put(festivalId, ticketsForThisFestival + quantity);
        request.setAttribute("userTickets", userTickets, WebRequest.SCOPE_SESSION);

        return quantity;
    }

    public Festival findFestivalById(Long festivalId) {
        return festivalRepository.findById(festivalId).orElse(null);
    }

    public int getTicketsForFestivalByUser(Long festivalId, Long userId) {
        Integer ticketsCount = ticketRepository.sumTicketQuantitiesByUserIdAndFestivalId(userId, festivalId);
        return ticketsCount != null ? ticketsCount : 0;
    }

    //    public Map<Long, Integer> getTicketsBoughtPerFestivalForUser(String genre, String region, Long userId) {
    //        List<Object[]> ticketsData = ticketRepository.findTicketCountsByUserAndOptionalGenreAndRegion(userId, genre, region);
    //        Map<Long, Integer> ticketsBoughtPerFestival = new HashMap<>();
    //        for (Object[] data : ticketsData) {
    //            Long festivalId = (Long) data[0];
    //            Integer ticketCount = ((Long) data[1]).intValue(); // Assuming COUNT returns Long
    //            ticketsBoughtPerFestival.put(festivalId, ticketCount);
    //        }
    //        return ticketsBoughtPerFestival;
    //    }

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

    public void addPerformanceToFestival(Long festivalId, Performance performance) {
        Festival festival = festivalRepository.findById(festivalId).orElseThrow(() -> new IllegalArgumentException("Invalid festival ID: " + festivalId));

        festival.getPerformances().add(performance);

        festivalRepository.save(festival);
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
}
