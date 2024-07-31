package service;

import domain.Festival;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface FestivalService {
    List<Festival> fetchFestivalsByGenreAndRegion(String genre, String region);
    
    Map<Long, Integer> getTicketsBoughtPerFestivalForUser(String genre, String region, Principal principal);

    // REST
    List<String> getArtistsByFestival(Long festivalId);

    List<Festival> getFestivalsByGenre(String genre);
}