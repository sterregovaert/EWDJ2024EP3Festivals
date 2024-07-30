package service;

import domain.Festival;
import domain.Genre;
import domain.SubGenre;

import java.util.List;
import java.util.Map;

public interface FestivalService {
    // TODO can be simplified to maybe one method
    List<Festival> fetchFestivals(String genre, String region);

    List<Festival> fetchFestivalsByGenre(String genreName);

    List<Festival> fetchFestivalsByRegion(String regionName);

    List<Festival> fetchFestivalsByGenreAndRegion(String genreName, String regionName);

    List<Festival> fetchAllFestivals();

    Festival findFestivalById(Long festivalId);

    // TODO move this to ticket service?
    int getTicketsForFestivalByUser(Long festivalId, Long userId);

    Map<Long, Integer> getTicketsBoughtPerFestivalForUser(String genre, String region, Long userId);

    // TODO move this to Genre service
    List<SubGenre> getSubGenresByGenre(Genre genre);

    void updateAvailableSeats(Long festivalId, int quantity);

    // REST
    List<String> getArtistsByFestival(Long festivalId);

    List<Festival> getFestivalsByGenre(String genre);
}