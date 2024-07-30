package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.FestivalService;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class FestivalsRestController {

    @Autowired
    private FestivalService festivalService;

    // overview artists of a festival
    @GetMapping("/festival/{festivalId}/artists")
    public List<String> getArtistsByFestival(@PathVariable Long festivalId) {
        return festivalService.getArtistsByFestival(festivalId);
    }

    // overview of festivals by genre
    @GetMapping("/festivals")
    public List<Festival> getFestivalsByGenre(@RequestParam String genre) {
        return festivalService.getFestivalsByGenre(genre);
    }
}