package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Ticket;
import exceptions.FestivalNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import service.TicketService;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TicketsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Test
    public void testShowTickets() throws Exception {
        Principal principal = () -> "user";
        when(ticketService.findTicketsByUsername("user")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tickets").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("tickets"))
                .andExpect(model().attributeExists("tickets"));
    }

    @Test
    public void testBuyFestivalTicketGet() throws Exception {
        Principal principal = () -> "user";
        Ticket ticket = new Ticket();
        when(ticketService.setupBuyTicketModel(1L, "user")).thenReturn(ticket);

        mockMvc.perform(get("/tickets/buy").param("festivalId", "1").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket-buy"))
                .andExpect(model().attributeExists("ticket"))
                .andExpect(model().attributeExists("festival"))
                .andExpect(model().attributeExists("ticketsBought"));
    }

    @Test
    public void testBuyFestivalTicketPost_Success() throws Exception {
        Principal principal = () -> "user";
        Ticket ticket = new Ticket();
        ticket.setQuantity(1);

        mockMvc.perform(post("/tickets/buy")
                        .param("festivalId", "1")
                        .flashAttr("ticket", ticket)
                        .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    public void testBuyFestivalTicketPost_Failure() throws Exception {
        Principal principal = () -> "user";
        Ticket ticket = new Ticket();
        ticket.setQuantity(0); // Invalid quantity

        mockMvc.perform(post("/tickets/buy")
                        .param("festivalId", "1")
                        .flashAttr("ticket", ticket)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket-buy"))
                .andExpect(model().attributeHasFieldErrors("ticket", "quantity"));
    }


    @Test
    public void testShowTickets_WithTickets() throws Exception {
        Principal principal = () -> "user";
        Ticket ticket = new Ticket();
        List<Ticket> tickets = List.of(ticket);
        when(ticketService.findTicketsByUsername("user")).thenReturn(tickets);

        mockMvc.perform(get("/tickets").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("tickets"))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(model().attribute("tickets", tickets));
    }


    @Test
    public void testBuyFestivalTicketGet_InvalidFestivalId() throws Exception {
        Principal principal = () -> "user";
        when(ticketService.setupBuyTicketModel(999L, "user")).thenThrow(new FestivalNotFoundException(999));

        mockMvc.perform(get("/tickets/buy").param("festivalId", "999").principal(principal))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testBuyFestivalTicketPost_InsufficientPlaces() throws Exception {
        Principal principal = () -> "user";
        Ticket ticket = new Ticket();
        ticket.setQuantity(100); // More than available places

        mockMvc.perform(post("/tickets/buy")
                        .param("festivalId", "1")
                        .flashAttr("ticket", ticket)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket-buy"))
                .andExpect(model().attributeHasFieldErrors("ticket", "quantity"));
    }

    @Test
    public void testShowTickets_EmptyModel() throws Exception {
        Principal principal = () -> "user";
        when(ticketService.findTicketsByUsername("user")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tickets").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("tickets"))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(model().attribute("tickets", Collections.emptyList()));
    }

    @Test
    public void testBuyFestivalTicketPost_InvalidFestivalId() throws Exception {
        Principal principal = () -> "user";
        Ticket ticket = new Ticket();
        ticket.setQuantity(1);
        when(ticketService.setupBuyTicketModel(999L, "user")).thenThrow(new FestivalNotFoundException(999));

        mockMvc.perform(post("/tickets/buy")
                        .param("festivalId", "999")
                        .flashAttr("ticket", ticket)
                        .principal(principal))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testBuyFestivalTicketPost_ExceedingAvailablePlaces() throws Exception {
        Principal principal = () -> "user";
        Ticket ticket = new Ticket();
        ticket.setQuantity(100); // More than available places

        mockMvc.perform(post("/tickets/buy")
                        .param("festivalId", "1")
                        .flashAttr("ticket", ticket)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket-buy"))
                .andExpect(model().attributeHasFieldErrors("ticket", "quantity"));
    }

    @Test
    public void testShowTickets_WithMultipleTickets() throws Exception {
        Principal principal = () -> "user";
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        List<Ticket> tickets = List.of(ticket1, ticket2);
        when(ticketService.findTicketsByUsername("user")).thenReturn(tickets);

        mockMvc.perform(get("/tickets").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("tickets"))
                .andExpect(model().attributeExists("tickets"))
                .andExpect(model().attribute("tickets", tickets));
    }

    @Test
    public void testBuyFestivalTicketPost_ZeroQuantity() throws Exception {
        Principal principal = () -> "user";
        Ticket ticket = new Ticket();
        ticket.setQuantity(0); // Zero quantity

        mockMvc.perform(post("/tickets/buy")
                        .param("festivalId", "1")
                        .flashAttr("ticket", ticket)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket-buy"))
                .andExpect(model().attributeHasFieldErrors("ticket", "quantity"));
    }

    @Test
    public void testBuyFestivalTicketPost_NegativeQuantity() throws Exception {
        Principal principal = () -> "user";
        Ticket ticket = new Ticket();
        ticket.setQuantity(-1); // Negative quantity

        mockMvc.perform(post("/tickets/buy")
                        .param("festivalId", "1")
                        .flashAttr("ticket", ticket)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("ticket-buy"))
                .andExpect(model().attributeHasFieldErrors("ticket", "quantity"));
    }
}