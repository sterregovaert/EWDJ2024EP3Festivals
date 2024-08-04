package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import domain.Genre;
import domain.Performance;
import domain.Region;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import repository.FestivalRepository;
import service.PerformanceService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PerformanceControllerTest {

    private final Long festivalId = 1L;
    private final Long invalidFestivalId = 999L;
    private final Long performanceId = 1L;
    private final Long invalidPerformanceId = 999L;
    private final Festival festival = new Festival();
    private final Performance performance = new Performance();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PerformanceService performanceService;
    @MockBean
    private FestivalRepository festivalRepository;

    private void setupFestival() {
        festival.setGenre(new Genre());
        festival.setRegion(new Region());
    }

    @Test
    public void testShowAddPerformanceForm() throws Exception {
        Mockito.doNothing().when(performanceService).setupAddPerformanceFormModel(festivalId, performance, null);
        mockMvc.perform(get("/performance/add").param("festivalId", festivalId.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("performance-add"))
                .andExpect(model().attributeExists("performance"));
    }

    @Test
    public void testAddPerformance() throws Exception {
        setupFestival();

        Mockito.when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
        Mockito.doNothing().when(performanceService).setupPerformanceForFestival(festivalId, performance, null, null);
        Mockito.doNothing().when(performanceService).savePerformance(performance);

        mockMvc.perform(post("/performance/add")
                        .param("festivalId", festivalId.toString())
                        .flashAttr("performance", performance))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }

    @Test
    public void testShowRemovePerformanceForm() throws Exception {
        Mockito.doNothing().when(performanceService).setupRemovePerformanceFormModel(festivalId, null);
        mockMvc.perform(get("/performance/remove").param("festivalId", festivalId.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("performance-remove"))
                .andExpect(model().attributeExists("performances"));
    }

    @Test
    public void testRemovePerformance() throws Exception {
        setupFestival();

        Mockito.when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
        Mockito.doNothing().when(performanceService).deletePerformanceById(performanceId);

        mockMvc.perform(post("/performance/remove")
                        .param("festivalId", festivalId.toString())
                        .param("performanceId", performanceId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }

    @Test
    public void testAddPerformanceWithValidationErrors() throws Exception {
        setupFestival();

        Mockito.when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
        Mockito.doNothing().when(performanceService).setupPerformanceForFestival(festivalId, performance, null, null);
        Mockito.doNothing().when(performanceService).setupAddPerformanceFormModel(festivalId, performance, null);

        mockMvc.perform(post("/performance/add")
                        .param("festivalId", festivalId.toString())
                        .flashAttr("performance", performance))
                .andExpect(status().isOk())
                .andExpect(view().name("performance-add"))
                .andExpect(model().attributeExists("performance"))
                .andExpect(model().attributeHasFieldErrors("performance", "artistName", "startDateTime", "endDateTime"));
    }

    @Test
    public void testRemovePerformanceWithInvalidPerformanceId() throws Exception {
        setupFestival();

        Mockito.when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
        Mockito.doThrow(new IllegalArgumentException("Performance not found")).when(performanceService).deletePerformanceById(invalidPerformanceId);

        mockMvc.perform(post("/performance/remove")
                        .param("festivalId", festivalId.toString())
                        .param("performanceId", invalidPerformanceId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }

    @Test
    public void testAddPerformanceWithMissingRequiredFields() throws Exception {
        setupFestival();

        Mockito.when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
        Mockito.doNothing().when(performanceService).setupPerformanceForFestival(festivalId, performance, null, null);
        Mockito.doNothing().when(performanceService).setupAddPerformanceFormModel(festivalId, performance, null);

        mockMvc.perform(post("/performance/add")
                        .param("festivalId", festivalId.toString())
                        .flashAttr("performance", performance))
                .andExpect(status().isOk())
                .andExpect(view().name("performance-add"))
                .andExpect(model().attributeExists("performance"))
                .andExpect(model().attributeHasFieldErrors("performance", "artistName", "startDateTime", "endDateTime"));
    }

    @Test
    public void testRemovePerformanceWithNonExistentFestivalId() throws Exception {
        Mockito.when(festivalRepository.findById(invalidFestivalId)).thenReturn(Optional.empty());
        Mockito.doNothing().when(performanceService).deletePerformanceById(performanceId);

        mockMvc.perform(post("/performance/remove")
                        .param("festivalId", invalidFestivalId.toString())
                        .param("performanceId", performanceId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }

    @Test
    public void testAddPerformanceWithInvalidDateRange() throws Exception {
        performance.setStartDateTime(LocalDateTime.now().plusDays(1));
        performance.setEndDateTime(LocalDateTime.now());
        setupFestival();

        Mockito.when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
        Mockito.doNothing().when(performanceService).setupPerformanceForFestival(festivalId, performance, null, null);
        Mockito.doNothing().when(performanceService).setupAddPerformanceFormModel(festivalId, performance, null);

        mockMvc.perform(post("/performance/add")
                        .param("festivalId", festivalId.toString())
                        .flashAttr("performance", performance))
                .andExpect(status().isOk())
                .andExpect(view().name("performance-add"))
                .andExpect(model().attributeExists("performance"))
                .andExpect(model().attributeHasFieldErrors("performance", "endDateTime"));
    }

    @Test
    public void testRemovePerformanceWithValidPerformanceIdButInvalidFestivalId() throws Exception {
        Mockito.when(festivalRepository.findById(invalidFestivalId)).thenReturn(Optional.empty());
        Mockito.doNothing().when(performanceService).deletePerformanceById(performanceId);

        mockMvc.perform(post("/performance/remove")
                        .param("festivalId", invalidFestivalId.toString())
                        .param("performanceId", performanceId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }

    @Test
    public void testAddPerformanceWithDuplicatePerformance() throws Exception {
        performance.setArtistName("Duplicate Artist");
        setupFestival();

        Mockito.when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
        Mockito.doNothing().when(performanceService).setupPerformanceForFestival(festivalId, performance, null, null);
        Mockito.doNothing().when(performanceService).setupAddPerformanceFormModel(festivalId, performance, null);
        Mockito.doThrow(new IllegalArgumentException("Performance already exists")).when(performanceService).savePerformance(performance);

        mockMvc.perform(post("/performance/add")
                        .param("festivalId", festivalId.toString())
                        .flashAttr("performance", performance))
                .andExpect(status().isOk())
                .andExpect(view().name("performance-add"))
                .andExpect(model().attributeExists("performance"))
                .andExpect(model().attributeHasFieldErrors("performance", "artistName"));
    }

    @Test
    public void testRemovePerformanceWithValidPerformanceIdAndValidFestivalId() throws Exception {
        setupFestival();

        Mockito.when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
        Mockito.doNothing().when(performanceService).deletePerformanceById(performanceId);

        mockMvc.perform(post("/performance/remove")
                        .param("festivalId", festivalId.toString())
                        .param("performanceId", performanceId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }
}
