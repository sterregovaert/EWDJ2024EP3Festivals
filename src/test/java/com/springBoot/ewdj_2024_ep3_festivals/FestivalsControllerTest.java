package com.springBoot.ewdj_2024_ep3_festivals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class FestivalsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "user", roles = {"NOT_USER"})
    @Test
    public void testNoAccessWithWrongUserRole() throws Exception {
        mockMvc.perform(get("/festivals")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testAccessUser() throws Exception {
        mockMvc.perform(get("/festivals"))
                .andExpect(status().isFound())
                .andExpect(view().name("festivals"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAccessAdmin() throws Exception {
        mockMvc.perform(get("/festivals"))
                .andExpect(status().isFound())
                .andExpect(view().name("festivals"));
    }


}