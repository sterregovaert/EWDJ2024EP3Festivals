package service;

import domain.Festival;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface FestivalService {
    List<Festival> fetchFestivalsByGenreAndRegion(String genre, String region);

    Festival findFestivalById(Long festivalId);

    // TODO move this to ticket service?
    int getTicketsForFestivalByUser(Long festivalId, Long userId);

    Map<Long, Integer> getTicketsBoughtPerFestivalForUser(String genre, String region, Principal principal);

    void updateAvailableSeats(Long festivalId, int quantity);

    // REST
    List<String> getArtistsByFestival(Long festivalId);

    List<Festival> getFestivalsByGenre(String genre);
}