package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Genre;
import domain.Region;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import service.DashboardService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @WithMockUser(username = "user", roles = {"NOT_USER"})
    @Test
    public void testNoAccessWithWrongUserRole() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isForbidden());
    }

    // -------------------------------------------------------
    private ResultActions performCommonAssertions() throws Exception {
        return mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attributeExists("regions"))
                .andExpect(model().attributeExists("ticketCount"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testShowDashboard() throws Exception {
        when(dashboardService.findAllGenres()).thenReturn(List.of(
                Genre.builder().name("Genre1").build(),
                Genre.builder().name("Genre2").build()
        ));
        when(dashboardService.findAllRegions()).thenReturn(List.of(
                Region.builder().name("Region1").build(),
                Region.builder().name("Region2").build()
        ));
        when(dashboardService.findTicketCountForCurrentUser()).thenReturn(5);

        performCommonAssertions()
                .andExpect(model().attribute("genres", List.of(
                        Genre.builder().name("Genre1").build(),
                        Genre.builder().name("Genre2").build()
                )))
                .andExpect(model().attribute("regions", List.of(
                        Region.builder().name("Region1").build(),
                        Region.builder().name("Region2").build()
                )))
                .andExpect(model().attribute("ticketCount", 5));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testShowDashboardWithEmptyGenresAndRegions() throws Exception {
        when(dashboardService.findAllGenres()).thenReturn(List.of());
        when(dashboardService.findAllRegions()).thenReturn(List.of());
        when(dashboardService.findTicketCountForCurrentUser()).thenReturn(0);

        performCommonAssertions()
                .andExpect(model().attribute("genres", List.of()))
                .andExpect(model().attribute("regions", List.of()))
                .andExpect(model().attribute("ticketCount", 0));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testShowDashboardWithNullGenresAndRegions() throws Exception {
        when(dashboardService.findAllGenres()).thenReturn(List.of());
        when(dashboardService.findAllRegions()).thenReturn(List.of());
        when(dashboardService.findTicketCountForCurrentUser()).thenReturn(0);

        performCommonAssertions()
                .andExpect(model().attribute("genres", List.of()))
                .andExpect(model().attribute("regions", List.of()))
                .andExpect(model().attribute("ticketCount", 0));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testShowDashboardWithException() throws Exception {
        when(dashboardService.findAllGenres()).thenThrow(new RuntimeException("Service error"));
        when(dashboardService.findAllRegions()).thenReturn(List.of());
        when(dashboardService.findTicketCountForCurrentUser()).thenReturn(0);

        performCommonAssertions()
                .andExpect(model().attribute("genres", List.of()))
                .andExpect(model().attribute("regions", List.of()))
                .andExpect(model().attribute("ticketCount", 0));
    }


}
