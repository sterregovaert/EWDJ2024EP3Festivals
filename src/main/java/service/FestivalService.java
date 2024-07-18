package service;

import domain.Festival;
import domain.Genre;
import domain.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.FestivalRepository;
import repository.GenreRepository;
import repository.RegionRepository;

import java.util.Collections;
import java.util.List;

@Service
public class FestivalService {
    @Autowired
    private FestivalRepository festivalRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private RegionRepository regionRepository;

    public List<Festival> fetchFestivalsByGenre(String genreName) {
        return genreRepository.findByName(genreName)
                .map(genre -> festivalRepository.findByGenreOrderByRegionAscStartDateTimeAsc(genre))
                .orElse(Collections.emptyList());
    }

    public List<Festival> fetchFestivalsByRegion(String regionName) {
        return regionRepository.findByName(regionName)
                .map(region -> festivalRepository.findByRegionOrderByGenreAscStartDateTimeAsc(region))
                .orElse(Collections.emptyList());
    }
    public List<Festival> fetchAllFestivals() {
        return festivalRepository.findAllByOrderByGenreAscRegionAscStartDateTimeAsc();
    }
}
