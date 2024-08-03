package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Genre;
import domain.Region;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import service.DashboardService;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @WithMockUser(username = "user", roles = {})
    @Test
    void testShowDashboardWithNoUserRole() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"NOT_USER"})
    @Test
    void testNoAccessWithWrongUserRole() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isForbidden());
    }


    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testShowDashboardWithValidUserRole() throws Exception {
        List<Genre> genres = List.of(
                Genre.builder().name("Rock").build(),
                Genre.builder().name("Jazz").build()
        );
        List<Region> regions = List.of(
                Region.builder().name("North").build(),
                Region.builder().name("South").build()
        );
        int ticketCount = 5;

        Mockito.when(dashboardService.findAllGenres()).thenReturn(genres);
        Mockito.when(dashboardService.findAllRegions()).thenReturn(regions);
        Mockito.when(dashboardService.findTicketCountForCurrentUser()).thenReturn(ticketCount);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attribute("regions", regions))
                .andExpect(model().attribute("ticketCount", ticketCount));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testShowDashboardWithNoGenres() throws Exception {
        List<Region> regions = List.of(
                Region.builder().name("North").build(),
                Region.builder().name("South").build()
        );
        int ticketCount = 5;

        Mockito.when(dashboardService.findAllGenres()).thenReturn(Collections.emptyList());
        Mockito.when(dashboardService.findAllRegions()).thenReturn(regions);
        Mockito.when(dashboardService.findTicketCountForCurrentUser()).thenReturn(ticketCount);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("genres", Collections.emptyList()))
                .andExpect(model().attribute("regions", regions))
                .andExpect(model().attribute("ticketCount", ticketCount));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testShowDashboardWithNoRegions() throws Exception {
        List<Genre> genres = List.of(
                Genre.builder().name("Rock").build(),
                Genre.builder().name("Jazz").build()
        );
        int ticketCount = 5;

        Mockito.when(dashboardService.findAllGenres()).thenReturn(genres);
        Mockito.when(dashboardService.findAllRegions()).thenReturn(Collections.emptyList());
        Mockito.when(dashboardService.findTicketCountForCurrentUser()).thenReturn(ticketCount);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attribute("regions", Collections.emptyList()))
                .andExpect(model().attribute("ticketCount", ticketCount));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testShowDashboardWithNoTickets() throws Exception {
        List<Genre> genres = List.of(
                Genre.builder().name("Rock").build(),
                Genre.builder().name("Jazz").build()
        );
        List<Region> regions = List.of(
                Region.builder().name("North").build(),
                Region.builder().name("South").build()
        );

        Mockito.when(dashboardService.findAllGenres()).thenReturn(genres);
        Mockito.when(dashboardService.findAllRegions()).thenReturn(regions);
        Mockito.when(dashboardService.findTicketCountForCurrentUser()).thenReturn(0);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("genres", genres))
                .andExpect(model().attribute("regions", regions))
                .andExpect(model().attribute("ticketCount", 0));
    }


    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testShowDashboardWithExceptionInFindAllGenres() throws Exception {
        Mockito.when(dashboardService.findAllGenres()).thenThrow(new RuntimeException("Database error"));
        Mockito.when(dashboardService.findAllRegions()).thenReturn(Collections.emptyList());
        Mockito.when(dashboardService.findTicketCountForCurrentUser()).thenReturn(0);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("genres", Collections.emptyList()))
                .andExpect(model().attribute("regions", Collections.emptyList()))
                .andExpect(model().attribute("ticketCount", 0));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testShowDashboardWithExceptionInFindAllRegions() throws Exception {
        Mockito.when(dashboardService.findAllGenres()).thenReturn(Collections.emptyList());
        Mockito.when(dashboardService.findAllRegions()).thenThrow(new RuntimeException("Database error"));
        Mockito.when(dashboardService.findTicketCountForCurrentUser()).thenReturn(0);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("genres", Collections.emptyList()))
                .andExpect(model().attribute("regions", Collections.emptyList()))
                .andExpect(model().attribute("ticketCount", 0));
    }

}
