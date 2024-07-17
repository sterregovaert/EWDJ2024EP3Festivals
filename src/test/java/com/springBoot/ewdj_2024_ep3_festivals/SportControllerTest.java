package com.springBoot.ewdj_2024_ep3_festivals;

import domain.MyUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import repository.TicketRepository;
import repository.UserRepository;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class SportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SportRepository sportRepository;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MyUser user = new MyUser();
        user.setUsername("testUser");

        Sport sport = new Sport();

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(ticketRepository.countByUser(user)).thenReturn(1L);
        when(sportRepository.findAll()).thenReturn(Collections.singletonList(sport));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testShowSports() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sport"))
                .andExpect(status().isOk())
                .andExpect(view().name("sport"));
    }
}