package service;

import domain.Festival;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.FestivalRepository;

import java.util.List;

@Service
public class FestivalService {
    @Autowired
    private FestivalRepository festivalRepository;

    public List<Festival> fetchFestivalsByGenre(String genre) {
        return festivalRepository.findByGenreOrderByRegionAscStartDateTimeAsc(genre);
    }

    public List<Festival> fetchFestivalsByRegion(String region) {
        return festivalRepository.findByRegionOrderByGenreAscStartDateTimeAsc(region);
    }

    public List<Festival> fetchAllFestivals() {
        return festivalRepository.findAllByOrderByGenreAscRegionAscStartDateTimeAsc();
    }
}
