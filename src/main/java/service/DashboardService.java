package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.GenreRepository;
import repository.RegionRepository;

@Service
public class DashboardService {

    @Autowired
    GenreRepository genreRepository;
    @Autowired
    RegionRepository regionRepository;

    public Object findAllGenres() {
        return genreRepository.findAll();
    }

    public Object findAllRegions() {
        return regionRepository.findAll();
    }
}
