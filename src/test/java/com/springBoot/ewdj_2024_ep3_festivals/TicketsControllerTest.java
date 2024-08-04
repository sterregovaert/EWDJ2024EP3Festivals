package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import domain.Ticket;
import exceptions.FestivalNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import service.MyUserService;
import service.TicketService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TicketsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;
    @Autowired
    private MyUserService myUserService;

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAdminAccessTicketsPage() throws Exception {
        mockMvc.perform(get("/tickets")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAdminAccessBuyTicketPage() throws Exception {
        mockMvc.perform(get("/tickets/buy").param("festivalId", "1")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testShowTickets() throws Exception {
        when(ticketService.findTicketsByUsername("user")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tickets")).andExpect(status().isOk()).andExpect(view().name("tickets")).andExpect(model().attributeExists("tickets"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testBuyFestivalTicketGet() throws Exception {
        Ticket ticket = new Ticket();
        Festival festival = new Festival();
        festival.setStartDateTime(LocalDateTime.now());
        ticket.setFestival(festival);

        when(ticketService.setupBuyTicketModel(1L, "user")).thenReturn(ticket);
        // TODO fix this
//        when(ticketService.getFestivalById(1L)).thenReturn(festival);
//        MyUser mockUser = new MyUser();
//        when(myUserService.getUserByUsername("user")).thenReturn(mockUser);

        mockMvc.perform(get("/tickets/buy").param("festivalId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket-buy"))
                .andExpect(model().attributeExists("ticket"))
                .andExpect(model().attributeExists("festival"))
                .andExpect(model().attributeExists("ticketsBought"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testBuyFestivalTicketPost_Success() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setQuantity(1);

        mockMvc.perform(post("/tickets/buy").param("festivalId", "1").flashAttr("ticket", ticket)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/dashboard")).andExpect(flash().attributeExists("message"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testBuyFestivalTicketPost_Failure() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setQuantity(0); // Invalid quantity

        mockMvc.perform(post("/tickets/buy").param("festivalId", "1").flashAttr("ticket", ticket)).andExpect(status().isOk()).andExpect(view().name("ticket-buy")).andExpect(model().attributeHasFieldErrors("ticket", "quantity"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testShowTickets_WithTickets() throws Exception {
        Ticket ticket = new Ticket();
        List<Ticket> tickets = List.of(ticket);
        when(ticketService.findTicketsByUsername("user")).thenReturn(tickets);

        mockMvc.perform(get("/tickets")).andExpect(status().isOk()).andExpect(view().name("tickets")).andExpect(model().attributeExists("tickets")).andExpect(model().attribute("tickets", tickets));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testBuyFestivalTicketGet_InvalidFestivalId() throws Exception {
        when(ticketService.setupBuyTicketModel(999L, "user")).thenThrow(new FestivalNotFoundException(999));

        mockMvc.perform(get("/tickets/buy").param("festivalId", "999")).andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testBuyFestivalTicketPost_InsufficientPlaces() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setQuantity(100); // More than available places

        mockMvc.perform(post("/tickets/buy").param("festivalId", "1").flashAttr("ticket", ticket)).andExpect(status().isOk()).andExpect(view().name("ticket-buy")).andExpect(model().attributeHasFieldErrors("ticket", "quantity"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testShowTickets_EmptyModel() throws Exception {
        when(ticketService.findTicketsByUsername("user")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tickets")).andExpect(status().isOk()).andExpect(view().name("tickets")).andExpect(model().attributeExists("tickets")).andExpect(model().attribute("tickets", Collections.emptyList()));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testBuyFestivalTicketPost_InvalidFestivalId() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setQuantity(1);
        when(ticketService.setupBuyTicketModel(999L, "user")).thenThrow(new FestivalNotFoundException(999));

        mockMvc.perform(post("/tickets/buy").param("festivalId", "999").flashAttr("ticket", ticket)).andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testBuyFestivalTicketPost_ExceedingAvailablePlaces() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setQuantity(100); // More than available places

        mockMvc.perform(post("/tickets/buy").param("festivalId", "1").flashAttr("ticket", ticket)).andExpect(status().isOk()).andExpect(view().name("ticket-buy")).andExpect(model().attributeHasFieldErrors("ticket", "quantity"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testShowTickets_WithMultipleTickets() throws Exception {
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        List<Ticket> tickets = List.of(ticket1, ticket2);
        when(ticketService.findTicketsByUsername("user")).thenReturn(tickets);

        mockMvc.perform(get("/tickets")).andExpect(status().isOk()).andExpect(view().name("tickets")).andExpect(model().attributeExists("tickets")).andExpect(model().attribute("tickets", tickets));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testBuyFestivalTicketPost_ZeroQuantity() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setQuantity(0); // Zero quantity

        mockMvc.perform(post("/tickets/buy").param("festivalId", "1").flashAttr("ticket", ticket)).andExpect(status().isOk()).andExpect(view().name("ticket-buy")).andExpect(model().attributeHasFieldErrors("ticket", "quantity"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testBuyFestivalTicketPost_NegativeQuantity() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setQuantity(-1); // Negative quantity

        mockMvc.perform(post("/tickets/buy").param("festivalId", "1").flashAttr("ticket", ticket)).andExpect(status().isOk()).andExpect(view().name("ticket-buy")).andExpect(model().attributeHasFieldErrors("ticket", "quantity"));
    }
}