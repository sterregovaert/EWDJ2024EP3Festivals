package service;

import domain.Festival;
import domain.Genre;
import domain.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import repository.FestivalRepository;
import repository.GenreRepository;
import repository.RegionRepository;
import repository.TicketRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FestivalsService {
    @Autowired
    private FestivalRepository festivalRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private TicketRepository ticketRepository;

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
        Integer ticketsCount = ticketRepository.findTicketQuantitiesSumByUserIdAndFestivalId(userId, festivalId);
        return ticketsCount != null ? ticketsCount : 0;
    }
}
