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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import service.PerformanceService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
        festival.setStartDateTime(LocalDateTime.now().plusDays(1).withMinute(0).withNano(0));
        festival.setGenre(genre);
        festival.setRegion(region);
        festival.setAvailablePlaces(10);
        festival.setTicketPrice(10.0);

        performance = new Performance();
        performance.setPerformanceId(performanceId);
        performance.setFestival(festival);
        performance.setArtistName("Test Artist");
        performance.setFestivalNumber1(3333);
        performance.setFestivalNumber2(3333);
        performance.setStartDateTime(LocalDateTime.now().plusDays(1).withMinute(0).withNano(0).plusHours(1));
        performance.setEndDateTime(LocalDateTime.now().plusDays(1).withMinute(0).withNano(0).plusHours(2));

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
    public void testShowAddPerformanceForm() throws Exception {
        when(performanceService.setupAddPerformanceFormModel(eq(festivalId), any(Performance.class), any(Model.class))).thenReturn(festival);

        mockMvc.perform(get("/performance/add").param("festivalId", festivalId.toString())).andExpect(status().isOk()).andExpect(view().name("performance-add")).andExpect(model().attributeExists("performance")).andExpect(model().attributeExists("performances"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAddPerformanceWithValidData() throws Exception {
        when(performanceService.savePerformance(any(Performance.class))).thenReturn(performance);
        when(performanceService.setupPerformanceForFestival(eq(festivalId), any(Performance.class), any(BindingResult.class), any(Model.class))).thenReturn(performance);

        mockMvc.perform(post("/performance/add").param("festivalId", festivalId.toString()).flashAttr("performance", performance).with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAddPerformanceWithInvalidData() throws Exception {
        performance.setEndDateTime(null); // Setting invalid data

        mockMvc.perform(post("/performance/add").param("festivalId", festivalId.toString()).flashAttr("performance", performance).with(csrf())).andExpect(status().isOk()).andExpect(view().name("performance-add")).andExpect(model().attributeExists("performance")).andExpect(model().attributeHasFieldErrors("performance", "endDateTime"));
    }


    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAddPerformance_Success() throws Exception {
        mockMvc.perform(post("/performance/add").param("festivalId", "1").param("artistName", "Test Artist").param("startDateTime", "2023-10-10T10:00").param("endDateTime", "2023-10-10T11:00").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/festivals?genre=Jazz&region=South America"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAddPerformance_ValidationErrors() throws Exception {
        mockMvc.perform(post("/performance/add").param("festivalId", "1").param("artistName", "").param("startDateTime", "").param("endDateTime", "").with(csrf())).andExpect(status().isOk()).andExpect(view().name("performance-add")).andExpect(model().attributeHasFieldErrors("performance", "artistName", "startDateTime", "endDateTime"));
    }


    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAddPerformanceWithValidationErrors() throws Exception {
        performance.setArtistName("");
        performance.setStartDateTime(null);
        performance.setEndDateTime(null);

        mockMvc.perform(post("/performance/add").param("festivalId", festivalId.toString()).flashAttr("performance", performance)).andExpect(status().isOk()).andExpect(view().name("performance-add")).andExpect(model().attributeExists("performance")).andExpect(model().attributeHasFieldErrors("performance", "artistName", "startDateTime", "endDateTime"));
    }


    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAddPerformanceWithMissingRequiredFields() throws Exception {
        performance.setArtistName("");
        performance.setStartDateTime(null);
        performance.setEndDateTime(null);

        mockMvc.perform(post("/performance/add").param("festivalId", festivalId.toString()).flashAttr("performance", performance).with(csrf())).andExpect(status().isOk()).andExpect(view().name("performance-add")).andExpect(model().attributeExists("performance")).andExpect(model().attributeHasFieldErrors("performance", "artistName", "startDateTime", "endDateTime"));
    }


    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAddPerformanceWithInvalidDateRange() throws Exception {
        performance.setStartDateTime(LocalDateTime.now().plusDays(1));
        performance.setEndDateTime(LocalDateTime.now());

        mockMvc.perform(post("/performance/add").param("festivalId", festivalId.toString()).flashAttr("performance", performance)).andExpect(status().isOk()).andExpect(view().name("performance-add")).andExpect(model().attributeExists("performance")).andExpect(model().attributeHasFieldErrors("performance", "endDateTime"));
    }


    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAddPerformanceWithDuplicatePerformance() throws Exception {
        performance.setArtistName("Duplicate Artist");

        Mockito.doThrow(new IllegalArgumentException("Performance already exists")).when(performanceService).savePerformance(performance);

        mockMvc.perform(post("/performance/add").param("festivalId", festivalId.toString()).flashAttr("performance", performance).with(csrf())).andExpect(status().isOk()).andExpect(view().name("performance-add")).andExpect(model().attributeExists("performance")).andExpect(model().attributeHasFieldErrors("performance", "artistName"));
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
        Mockito.doNothing().when(performanceService).deletePerformanceById(1L);

        mockMvc.perform(post("/performance/remove").param("festivalId", "1").param("performanceId", "1").with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/festivals?genre=Jazz&region=South America"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testRemovePerformanceWithInvalidPerformanceId() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Performance not found")).when(performanceService).deletePerformanceById(invalidPerformanceId);

        mockMvc.perform(post("/performance/remove").param("festivalId", festivalId.toString()).param("performanceId", invalidPerformanceId.toString()).with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testRemovePerformanceWithNonExistentFestivalId() throws Exception {
        mockMvc.perform(post("/performance/remove").param("festivalId", invalidFestivalId.toString()).param("performanceId", performanceId.toString()).with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testRemovePerformanceWithValidPerformanceIdButInvalidFestivalId() throws Exception {
        mockMvc.perform(post("/performance/remove").param("festivalId", invalidFestivalId.toString()).param("performanceId", performanceId.toString()).with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testRemovePerformanceWithValidPerformanceIdAndValidFestivalId() throws Exception {
        Mockito.doNothing().when(performanceService).deletePerformanceById(performanceId);

        mockMvc.perform(post("/performance/remove").param("festivalId", festivalId.toString()).param("performanceId", performanceId.toString()).with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
    }

}
