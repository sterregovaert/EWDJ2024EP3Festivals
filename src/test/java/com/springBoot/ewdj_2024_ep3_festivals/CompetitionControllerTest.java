package com.springBoot.ewdj_2024_ep3_festivals;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import repository.*;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompetitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SportRepository sportRepository;

    @MockBean
    private CompetitionRepository competitionRepository;

    @MockBean
    private StadiumRepository stadiumRepository;

    @MockBean
    private DisciplineRepository disciplineRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TicketRepository ticketRepository;

    @BeforeEach
    public void setup() {
        Sport sport = new Sport();
        sport.setSport_id(1L);

        Competition competition = new Competition();
        competition.setSport(sport);

        when(sportRepository.findById(1L)).thenReturn(java.util.Optional.of(sport));
        when(competitionRepository.findBySportOrderByStartDateDescStartTimeDesc(sport)).thenReturn(Collections.singletonList(competition));
    }

    @Test
    public void testShowCompetitions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/competition/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    public void testNoAccessAddCompetition() throws Exception {
        mockMvc.perform(get("/competition-add/1"))
                .andExpect(status().isForbidden());
    }

}