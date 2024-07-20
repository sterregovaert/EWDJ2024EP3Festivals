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
import repository.TicketRepository;
import repository.UserRepository;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
public class TicketsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TicketRepository ticketRepository;


//    @BeforeEach
//    public void setup() {
//        MyUser user = new MyUser();
//        user.setUsername("testUser");
//
//        Competition competition = new Competition();
//        competition.setCompetition_id(1L);
//
//        Ticket ticket = new Ticket();
//        ticket.setCompetition(competition);
//        ticket.setUser(user);
//
//        when(userRepository.findByUsername("testUser")).thenReturn(user);
//        when(ticketRepository.findByUserSortedBySportAndDate(user)).thenReturn(Collections.singletonList(ticket));
//        when(competitionRepository.findById(1L)).thenReturn(java.util.Optional.of(competition));
//        when(ticketRepository.findByUserAndCompetition(user, competition)).thenReturn(Collections.singletonList(ticket));
//    }

    @Test
    @WithMockUser(username = "testUser")
    public void testShowTickets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ticket"))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testNoAccessTickets() throws Exception {
        mockMvc.perform(get("/tickets/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testBuyTicket() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ticket/buy/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket-buy"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testBuyTickets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/ticket/buy/1").param("quantity", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user")
    public void testAccessWithUserRole2() throws Exception {
        mockMvc.perform(get("/ticket/buy/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket-buy"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("userId"))
                .andExpect(model().attributeExists("ticketCount"))
                .andExpect(model().attributeExists("sportName"))
                .andExpect(model().attributeExists("stadiumName"))
                .andExpect(model().attribute("username", "user"));
    }
}