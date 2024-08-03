package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import exceptions.FestivalNotFoundException;
import exceptions.GenreNotFoundException;
import exceptions.NoArtistsException;
import exceptions.NoFestivalsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import service.FestivalService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FestivalsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FestivalService festivalService;

    @Test
    void testGetArtistsByFestival() throws Exception {
        Long festivalId = 1L;
        List<String> artists = Arrays.asList("Artist1", "Artist2");
        when(festivalService.getArtistsByFestival(festivalId)).thenReturn(artists);

        mockMvc.perform(get("/api/festival/{festivalId}/artists", festivalId)).andExpect(status().isOk()).andExpect(jsonPath("$[0]").value("Artist1")).andExpect(jsonPath("$[1]").value("Artist2"));
    }

    @Test
    void testGetFestivalsByGenre() throws Exception {
        String genre = "Rock";
        Festival festival1 = new Festival();
        festival1.setName("Rock Festival 1");
        Festival festival2 = new Festival();
        festival2.setName("Rock Festival 2");
        List<Festival> festivals = Arrays.asList(festival1, festival2);
        when(festivalService.getFestivalsByGenre(genre)).thenReturn(festivals);

        mockMvc.perform(get("/api/festivals").param("genre", genre)).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Rock Festival 1")).andExpect(jsonPath("$[1].name").value("Rock Festival 2"));
    }

    @Test
    void testGetArtistsByFestival_NoArtists() throws Exception {
        Long festivalId = 2L;
        when(festivalService.getArtistsByFestival(festivalId)).thenThrow(new NoArtistsException("No artists found for festival with ID: " + festivalId));

        mockMvc.perform(get("/api/festival/{festivalId}/artists", festivalId)).andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("No artists found for festival with ID: " + festivalId));
    }

    @Test
    void testGetFestivalsByGenre_NoFestivals() throws Exception {
        String genre = "Jazz";
        when(festivalService.getFestivalsByGenre(genre)).thenThrow(new NoFestivalsException("No festivals found for genre: " + genre));

        mockMvc.perform(get("/api/festivals").param("genre", genre)).andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("No festivals found for genre: " + genre));
    }

    @Test
    void testGetArtistsByFestival_NonExistentFestival() throws Exception {
        Long festivalId = 999L;
        when(festivalService.getArtistsByFestival(festivalId)).thenThrow(new FestivalNotFoundException(festivalId.intValue()));

        mockMvc.perform(get("/api/festival/{festivalId}/artists", festivalId)).andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Festival not found with ID: " + festivalId));
    }

    @Test
    void testGetFestivalsByGenre_NonExistentGenre() throws Exception {
        String genre = "NonExistentGenre";
        when(festivalService.getFestivalsByGenre(genre)).thenThrow(new GenreNotFoundException("Genre not found"));

        mockMvc.perform(get("/api/festivals").param("genre", genre)).andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Genre not found"));
    }

  
    @Test
    void testGetFestivalsByGenre_MissingGenreParameter() throws Exception {
        mockMvc.perform(get("/api/festivals")).andExpect(status().isBadRequest());
    }

    @Test
    void testGetArtistsByFestival_MissingFestivalIdParameter() throws Exception {
        mockMvc.perform(get("/api/festival//artists")).andExpect(status().isBadRequest());
    }

    @Test
    void testGetArtistsByFestival_EmptyArtistsList() throws Exception {
        Long festivalId = 3L;
        when(festivalService.getArtistsByFestival(festivalId)).thenReturn(List.of());

        mockMvc.perform(get("/api/festival/{festivalId}/artists", festivalId)).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    }
}