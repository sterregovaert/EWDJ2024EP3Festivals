package com.springBoot.ewdj_2024_ep3_festivals;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import repository.*;
import service.MyUserDetailsService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class Ewdj2024EP3FestivalsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyUserDetailsService userDetailsService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TicketRepository ticketRepository;

//    @BeforeEach
//    public void setup() {
//        MyUser normalUser = new MyUser();
//        normalUser.setUsername("user");
//        normalUser.setPassword("password");
//        normalUser.setRole(Role.USER);
//        normalUser.setId(1L);
//        userRepository.save(normalUser);
//
//        Sport sport = new Sport();
//        sport.setSport_id(1L);
//        sport.setName("Netsport");
//        sportRepository.save(sport);
//
//        Stadium stadium = new Stadium();
//        stadium.setStadium_id(1L);
//        stadium.setName("Stade Magnifique");
//        stadium.setCapacity(50000);
//
//        List<Competition> competitions = new ArrayList<>();
//        Discipline netsportDiscipline1 = new Discipline(1L, "Tennis", sport, competitions);
//        Discipline netsportDiscipline2 = new Discipline(2L, "Volleybal", sport, competitions);
//        disciplineRepository.saveAll(List.of(netsportDiscipline1, netsportDiscipline2));
//        List<Discipline> disciplines = new ArrayList<>();
//        disciplines.add(netsportDiscipline1);
//        disciplines.add(netsportDiscipline2);
//
//        Competition competition = new Competition();
//        competition.setCompetition_id(1L);
//        competition.setStartDate(LocalDate.of(2024, 7, 30));
//        competition.setStartTime(LocalTime.of(16, 45));
//        competition.setDisciplines(disciplines);
//        competition.setStadium(stadium);
//        competition.setFreeSeats(30);
//        competition.setSport(sport);
//        competitionRepository.save(competition);
//
//        Ticket ticket = new Ticket();
//        ticket.setTicket_id(5L);
//        ticket.setUser(normalUser);
//        ticket.setCompetition(competition);
//        ticketRepository.save(ticket);
//
//        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(normalUser.getUsername(), normalUser.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
//
//        when(userRepository.findByUsername("user")).thenReturn(normalUser);
//        when(userDetailsService.loadUserByUsername("user")).thenReturn(user);
//    }

    @Test
    public void loginGet() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithAnonymousUser
    public void testNoAccessAnonymous() throws Exception {
        mockMvc.perform(get("/sport"))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    public void accessDeniedPageGet() throws Exception {
        mockMvc.perform(get("/403"))
                .andExpect(status().isOk())
                .andExpect(view().name("403"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testAccessWithUserRole() throws Exception {
        mockMvc.perform(get("/sport"))
                .andExpect(status().isOk())
                .andExpect(view().name("sport"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attribute("username", "user"));
    }

    @WithMockUser(username = "user", roles = {"NOT_USER"})
    @Test
    public void testNoAccessWithWrongUserRole() throws Exception {
        mockMvc.perform(get("/sport"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testWrongPassword() throws Exception {
        mockMvc.perform(formLogin("/login")
                .user("username", "nameUser")
                .password("password", "wrongpassword"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void testCorrectPassword() throws Exception {
        mockMvc.perform(formLogin("/login")
                .user("username", "nameUser")
                .password("password", "123456789"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/sport"));
    }

}
