package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import domain.Genre;
import domain.Performance;
import domain.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import repository.FestivalRepository;
import service.PerformanceService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FestivalRepository festivalRepository;

    @MockBean
    private PerformanceService performanceService;

    private Festival festival;
    private Performance performance;

    @BeforeEach
    void setup() {
        Genre genre = Genre.builder().name("Rock").build();
        Region region = Region.builder().name("North").build();

        festival = new Festival();
        festival.setFestivalId(festivalId);
        festival.setName("Test Festival");
        festival.setStartDateTime(LocalDateTime.now());
        festival.setGenre(genre);
        festival.setRegion(region);

        performance = new Performance();
        performance.setPerformanceId(performanceId);
        performance.setFestival(festival);

        when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testUserCannotAccessAddPerformance() throws Exception {
        mockMvc.perform(get("/performance/add?festivalId=1")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testUserCannotAccessRemovePerformance() throws Exception {
        mockMvc.perform(get("/performance/remove?festivalId=1")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testShowAddPerformanceForm() throws Exception {
        mockMvc.perform(get("/performance/add?festivalId=1")).andExpect(status().isOk()).andExpect(view().name("performance-add")).andExpect(model().attributeExists("performance")).andExpect(model().attributeExists("performances"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAddPerformance_Success() throws Exception {
        mockMvc.perform(post("/performance/add").param("festivalId", "1").param("artistName", "Test Artist").param("startDateTime", "2023-10-10T10:00").param("endDateTime", "2023-10-10T11:00").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/festivals?genre=null&region=null"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAddPerformance_ValidationErrors() throws Exception {
        mockMvc.perform(post("/performance/add").param("festivalId", "1").param("artistName", "").param("startDateTime", "").param("endDateTime", "").with(csrf())).andExpect(status().isOk()).andExpect(view().name("performance-add")).andExpect(model().attributeHasFieldErrors("performance", "artistName", "startDateTime", "endDateTime"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testShowRemovePerformanceForm() throws Exception {
        when(performanceService.getPerformancesByFestival(festivalId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/performance/remove?festivalId=1")).andExpect(status().isOk()).andExpect(view().name("performance-remove")).andExpect(model().attributeExists("performances")).andExpect(model().attributeExists("festival"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testRemovePerformance_Success() throws Exception {
        mockMvc.perform(post("/performance/remove").param("festivalId", "1").param("performanceId", "1").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/festivals?genre=null&region=null"));
    }

    @Test
    public void testAddPerformanceWithValidationErrors() throws Exception {
        performance.setArtistName("");
        performance.setStartDateTime(null);
        performance.setEndDateTime(null);

        mockMvc.perform(post("/performance/add").param("festivalId", festivalId.toString()).flashAttr("performance", performance)).andExpect(status().isOk()).andExpect(view().name("performance-add")).andExpect(model().attributeExists("performance")).andExpect(model().attributeHasFieldErrors("performance", "artistName", "startDateTime", "endDateTime"));
    }

    @Test
    public void testRemovePerformanceWithInvalidPerformanceId() throws Exception {
        when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
        Mockito.doThrow(new IllegalArgumentException("Performance not found")).when(performanceService).deletePerformanceById(invalidPerformanceId);

        mockMvc.perform(post("/performance/remove").param("festivalId", festivalId.toString()).param("performanceId", invalidPerformanceId.toString())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }

    @Test
    public void testAddPerformanceWithMissingRequiredFields() throws Exception {
        performance.setArtistName("");
        performance.setStartDateTime(null);
        performance.setEndDateTime(null);

        mockMvc.perform(post("/performance/add").param("festivalId", festivalId.toString()).flashAttr("performance", performance)).andExpect(status().isOk()).andExpect(view().name("performance-add")).andExpect(model().attributeExists("performance")).andExpect(model().attributeHasFieldErrors("performance", "artistName", "startDateTime", "endDateTime"));
    }

    @Test
    public void testRemovePerformanceWithNonExistentFestivalId() throws Exception {
        when(festivalRepository.findById(invalidFestivalId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/performance/remove").param("festivalId", invalidFestivalId.toString()).param("performanceId", performanceId.toString())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }

    @Test
    public void testAddPerformanceWithInvalidDateRange() throws Exception {
        performance.setStartDateTime(LocalDateTime.now().plusDays(1));
        performance.setEndDateTime(LocalDateTime.now());

        mockMvc.perform(post("/performance/add").param("festivalId", festivalId.toString()).flashAttr("performance", performance)).andExpect(status().isOk()).andExpect(view().name("performance-add")).andExpect(model().attributeExists("performance")).andExpect(model().attributeHasFieldErrors("performance", "endDateTime"));
    }

    @Test
    public void testRemovePerformanceWithValidPerformanceIdButInvalidFestivalId() throws Exception {
        when(festivalRepository.findById(invalidFestivalId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/performance/remove").param("festivalId", invalidFestivalId.toString()).param("performanceId", performanceId.toString())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }

    @Test
    public void testAddPerformanceWithDuplicatePerformance() throws Exception {
        performance.setArtistName("Duplicate Artist");

        when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
        Mockito.doThrow(new IllegalArgumentException("Performance already exists")).when(performanceService).savePerformance(performance);

        mockMvc.perform(post("/performance/add").param("festivalId", festivalId.toString()).flashAttr("performance", performance)).andExpect(status().isOk()).andExpect(view().name("performance-add")).andExpect(model().attributeExists("performance")).andExpect(model().attributeHasFieldErrors("performance", "artistName"));
    }

    @Test
    public void testRemovePerformanceWithValidPerformanceIdAndValidFestivalId() throws Exception {
        when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
        Mockito.doNothing().when(performanceService).deletePerformanceById(performanceId);

        mockMvc.perform(post("/performance/remove").param("festivalId", festivalId.toString()).param("performanceId", performanceId.toString())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }
}
