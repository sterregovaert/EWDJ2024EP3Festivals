package service;

import domain.Genre;
import domain.SubGenre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.SubGenreRepository;

import java.util.Collections;
import java.util.List;

@Service
public class SubGenreService {

    @Autowired
    private SubGenreRepository subGenreRepository;

    public List<SubGenre> getSubGenresByGenre(Genre genre) {
        if (genre != null) {
            List<SubGenre> subGenres = subGenreRepository.findByGenre(genre);
            return subGenres;
        }
        return Collections.emptyList();
    }
}
