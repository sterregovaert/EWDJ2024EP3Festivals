package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import domain.Genre;
import domain.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import repository.FestivalRepository;
import service.PerformanceService;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PerformanceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FestivalRepository festivalRepository;
    @MockBean
    private PerformanceService performanceService;

    @BeforeEach
    void setup() {
        Genre genre = Genre.builder().name("Rock").build();
        Region region = Region.builder().name("North").build();

        Festival festival = new Festival();
        festival.setFestivalId(1L);
        festival.setName("Test Festival");
        festival.setStartDateTime(LocalDateTime.now());
        festival.setGenre(genre);
        festival.setRegion(region);
        when(festivalRepository.findById(1L)).thenReturn(java.util.Optional.of(festival));
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

//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    @Test
//    void testShowAddPerformanceForm() throws Exception {
//        Festival festival = new Festival();
//        festival.setFestivalId(1L);
//        when(festivalRepository.findById(1L)).thenReturn(java.util.Optional.of(festival));
//
//        mockMvc.perform(get("/performance/add?festivalId=1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("performance-add"))
//                .andExpect(model().attributeExists("performance"))
//                .andExpect(model().attributeExists("performances"));
//    }
//
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    @Test
//    void testAddPerformance_Success() throws Exception {
//        Festival festival = new Festival();
//        festival.setFestivalId(1L);
//        when(festivalRepository.findById(1L)).thenReturn(java.util.Optional.of(festival));
//
//        mockMvc.perform(post("/performance/add")
//                        .param("festivalId", "1")
//                        .param("artistName", "Test Artist")
//                        .param("startDateTime", "2023-10-10T10:00")
//                        .param("endDateTime", "2023-10-10T11:00")
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/festivals?genre=null&region=null"));
//    }
//
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    @Test
//    void testAddPerformance_ValidationErrors() throws Exception {
//        Festival festival = new Festival();
//        festival.setFestivalId(1L);
//        when(festivalRepository.findById(1L)).thenReturn(java.util.Optional.of(festival));
//
//        mockMvc.perform(post("/performance/add")
//                        .param("festivalId", "1")
//                        .param("artistName", "")
//                        .param("startDateTime", "")
//                        .param("endDateTime", "")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("performance-add"))
//                .andExpect(model().attributeHasFieldErrors("performance", "artistName", "startDateTime", "endDateTime"));
//    }
//
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    @Test
//    void testShowRemovePerformanceForm() throws Exception {
//        Festival festival = new Festival();
//        festival.setFestivalId(1L);
//        when(festivalRepository.findById(1L)).thenReturn(java.util.Optional.of(festival));
//        when(performanceService.getPerformancesByFestival(1L)).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/performance/remove?festivalId=1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("performance-remove"))
//                .andExpect(model().attributeExists("performances"))
//                .andExpect(model().attributeExists("festival"));
//    }
//
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    @Test
//    void testRemovePerformance_Success() throws Exception {
//        Festival festival = new Festival();
//        festival.setFestivalId(1L);
//        Performance performance = new Performance();
//        performance.setPerformanceId(1L);
//        performance.setFestival(festival);
//        when(festivalRepository.findById(1L)).thenReturn(java.util.Optional.of(festival));
//
//        mockMvc.perform(post("/performance/remove")
//                        .param("festivalId", "1")
//                        .param("performanceId", "1")
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/festivals?genre=null&region=null"));
//    }
}
