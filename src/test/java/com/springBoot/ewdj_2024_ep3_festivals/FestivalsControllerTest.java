package com.springBoot.ewdj_2024_ep3_festivals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import service.FestivalService;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class FestivalsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FestivalService festivalService;

    @WithMockUser(username = "user", roles = {"NOT_USER"})
    @Test
    void testNoAccessWithWrongUserRole() throws Exception {
        mockMvc.perform(get("/festivals")).andExpect(status().isForbidden());
    }

    // Test for regular user access
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testAccessWithUserRole() throws Exception {
        mockMvc.perform(get("/festivals")).andExpect(status().isOk()).andExpect(view().name("festivals"));
    }

    // Test for admin user access
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAccessWithAdminRole() throws Exception {
        mockMvc.perform(get("/festivals")).andExpect(status().isOk()).andExpect(view().name("festivals"));
    }

    // Test for user with invalid role access
    @WithMockUser(username = "user", roles = {"INVALID_ROLE"})
    @Test
    void testAccessWithInvalidRole() throws Exception {
        mockMvc.perform(get("/festivals")).andExpect(status().isForbidden());
    }

    // Test for regular user access with query parameters
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testAccessWithUserRoleAndQueryParams() throws Exception {
        mockMvc.perform(get("/festivals").param("genre", "Rock").param("region", "Europe")).andExpect(status().isOk()).andExpect(view().name("festivals"));
    }

    // Test for admin user access with query parameters
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAccessWithAdminRoleAndQueryParams() throws Exception {
        mockMvc.perform(get("/festivals").param("genre", "Jazz").param("region", "Asia")).andExpect(status().isOk()).andExpect(view().name("festivals"));
    }

    // Test for regular user access with valid genre and region
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testAccessWithUserRoleAndValidGenreRegion() throws Exception {
        mockMvc.perform(get("/festivals").param("genre", "Rock").param("region", "Europe")).andExpect(status().isOk()).andExpect(view().name("festivals"));
    }

    // Test for admin user access with valid genre and region
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAccessWithAdminRoleAndValidGenreRegion() throws Exception {
        mockMvc.perform(get("/festivals").param("genre", "Jazz").param("region", "Asia")).andExpect(status().isOk()).andExpect(view().name("festivals"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"USER", "ADMIN"})
    @WithMockUser(username = "user", roles = {"USER"})
    void testAccessWithUserRoleAndInvalidGenreButValidRegion(String role) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/festivals")
                        .param("genre", "InvalidGenre")
                        .param("region", "Europe")
                        .with(user("user").roles(role)))
                .andExpect(status().isOk())
                .andExpect(view().name("festivals"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"USER", "ADMIN"})
    void testAccessWithRoleAndInvalidRegion(String role) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/festivals")
                        .param("genre", "Jazz")
                        .param("region", "InvalidRegion")
                        .with(user("user").roles(role)))
                .andExpect(status().isOk())
                .andExpect(view().name("festivals"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"USER", "ADMIN"})
    void testAccessWithRoleAndInvalidGenreRegion(String role) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/festivals")
                        .param("genre", "InvalidGenre")
                        .param("region", "InvalidRegion")
                        .with(user("user").roles(role)))
                .andExpect(status().isOk())
                .andExpect(view().name("festivals"));
    }


    @ParameterizedTest
    @ValueSource(strings = {"USER", "ADMIN"})
    void testAccessWithRoleAndMissingParams(String role) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/festivals")
                        .param("region", "Asia")
                        .with(user("user").roles(role)))
                .andExpect(status().isOk())
                .andExpect(view().name("festivals"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"USER", "ADMIN"})
    void testAccessWithRoleAndNoMissingParams(String role) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/festivals")
                        .param("genre", "Rock")
                        .param("region", "Europe")
                        .with(user("user").roles(role)))
                .andExpect(status().isOk())
                .andExpect(view().name("festivals"));
    }

    @ParameterizedTest
    @CsvSource({
            "user, USER, Rock, Europe",
            "admin, ADMIN, Jazz, Asia"
    })
    void testAccessWithRoleAndServiceException(String username, String role, String genre, String region) throws Exception {
        // Mock the service layer to throw an exception
        when(festivalService.fetchFestivalsWithTickets(anyString(), anyString(), any(Principal.class))).thenThrow(new RuntimeException("Service layer exception"));

        mockMvc.perform(get("/festivals").param("genre", genre).param("region", region).with(user(username).roles(role)))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("message", "Internal Server Error"));
    }
}