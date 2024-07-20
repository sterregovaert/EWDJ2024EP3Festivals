package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.GenreRepository;
import repository.RegionRepository;
import repository.TicketRepository;

@Service
public class DashboardService {

    @Autowired
    GenreRepository genreRepository;
    @Autowired
    RegionRepository regionRepository;
    @Autowired
    TicketRepository ticketRepository;

    public Object findAllGenres() {
        return genreRepository.findAll();
    }

    public Object findAllRegions() {
        return regionRepository.findAll();
    }

    public int findTicketCountForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Integer sum = ticketRepository.sumTicketQuantitiesByUsername(currentUsername);
        return sum != null ? sum : 0;
    }
}
