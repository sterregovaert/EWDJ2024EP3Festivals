package service;

import domain.Genre;
import domain.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.GenreRepository;
import repository.RegionRepository;
import repository.TicketRepository;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    GenreRepository genreRepository;
    @Autowired
    RegionRepository regionRepository;
    @Autowired
    TicketRepository ticketRepository;


    public List<Genre> findAllGenres() {
        List<Genre> genres = genreRepository.findAllByOrderByNameAsc();
        return genres != null ? genres : List.of();
    }

    public List<Region> findAllRegions() {
        List<Region> regions = regionRepository.findAllByOrderByNameAsc();
        return regions != null ? regions : List.of();
    }

    public int findTicketCountForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Integer sum = ticketRepository.sumTicketQuantitiesByUsername(currentUsername);
        return sum != null ? sum : 0;
    }
}
