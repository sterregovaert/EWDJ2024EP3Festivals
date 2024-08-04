package service;

import domain.Festival;
import exceptions.FestivalNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.FestivalRepository;
import repository.TicketRepository;

@Service
public class FestivalTicketService {
    @Autowired
    private FestivalRepository festivalRepository;
    @Autowired
    private TicketRepository ticketRepository;

    public Festival getFestivalById(Long festivalId) {
        return festivalRepository.findById(festivalId)
                .orElseThrow(() -> new FestivalNotFoundException(festivalId.intValue()));
    }

    public int getTicketsForFestivalByUser(Long festivalId, Long userId) {
        Integer ticketsCount = ticketRepository.sumTicketQuantitiesByUserIdAndFestivalId(userId, festivalId);
        return ticketsCount != null ? ticketsCount : 0;
    }

    @Transactional
    public void updateAvailablePlaces(Festival festival, int quantity) {
        int updatedPlaces = festival.getAvailablePlaces() - quantity;
        if (updatedPlaces < 0) {
            throw new IllegalStateException("Not enough available places for the festival");
        }
        festival.setAvailablePlaces(updatedPlaces);
        festivalRepository.save(festival);
    }
}
