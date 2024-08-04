package com.springBoot.ewdj_2024_ep3_festivals;

import domain.*;
import exceptions.FestivalNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import service.FestivalTicketService;
import service.MyUserService;
import service.TicketService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TicketsControllerTest {

    private static final Long VALID_FESTIVAL_ID = 1L;
    private static final Long INVALID_FESTIVAL_ID = 999L;
    private static final String USERNAME = "user";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TicketService ticketService;
    @MockBean
    private MyUserService myUserService;
    @MockBean
    private FestivalTicketService festivalTicketService;

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAdminAccessTicketsPage() throws Exception {
        mockMvc.perform(get("/tickets")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAdminAccessBuyTicketPage() throws Exception {
        mockMvc.perform(get("/tickets/buy").param("festivalId", VALID_FESTIVAL_ID.toString())).andExpect(status().isForbidden());
    }

    @WithMockUser(username = USERNAME, roles = {"USER"})
    @Test
    void testShowTickets_NoTickets() throws Exception {
        when(ticketService.findTicketsByUsername(USERNAME)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tickets")).andExpect(status().isOk()).andExpect(view().name("tickets")).andExpect(model().attributeExists("tickets")).andExpect(model().attribute("tickets", Collections.emptyList()));
    }

    @WithMockUser(username = USERNAME, roles = {"USER"})
    @Test
    void testShowTickets_WithTickets() throws Exception {
        // Create and initialize a Region object
        Region region = new Region();
        region.setName("Test Region");

        // Create and initialize a Genre object
        Genre genre = new Genre();
        genre.setName("Test Genre");

        // Create and initialize a Festival object
        Festival festival = new Festival();
        festival.setStartDateTime(LocalDateTime.now());
        festival.setRegion(region);
        festival.setGenre(genre);

        // Create and initialize a Ticket object
        Ticket ticket = new Ticket();
        ticket.setFestival(festival);

        // Mock the service to return the ticket
        when(ticketService.findTicketsByUsername(USERNAME)).thenReturn(Collections.singletonList(ticket));

        // Perform the request and assert the results
        mockMvc.perform(get("/tickets")).andExpect(status().isOk()).andExpect(view().name("tickets")).andExpect(model().attributeExists("tickets")).andExpect(model().attribute("tickets", Collections.singletonList(ticket)));
    }

    @WithMockUser(username = USERNAME, roles = {"USER"})
    @Test
    void testBuyFestivalTicketGet_InvalidFestivalId() throws Exception {
        when(ticketService.setupBuyTicketModel(INVALID_FESTIVAL_ID, USERNAME)).thenThrow(new FestivalNotFoundException(Math.toIntExact(INVALID_FESTIVAL_ID)));

        mockMvc.perform(get("/tickets/buy").param("festivalId", INVALID_FESTIVAL_ID.toString())).andExpect(status().isNotFound());
    }

    @WithMockUser(username = USERNAME, roles = {"USER"})
    @Test
    void testBuyFestivalTicketGet_ValidFestivalId() throws Exception {
        Ticket ticket = new Ticket();
        Festival festival = new Festival();
        festival.setStartDateTime(LocalDateTime.now());
        ticket.setFestival(festival);

        when(ticketService.setupBuyTicketModel(VALID_FESTIVAL_ID, USERNAME)).thenReturn(ticket);
        when(festivalTicketService.getFestivalById(VALID_FESTIVAL_ID)).thenReturn(festival);
        MyUser mockUser = new MyUser();
        when(myUserService.getUserByUsername(USERNAME)).thenReturn(mockUser);
        when(festivalTicketService.getTicketsForFestivalByUser(VALID_FESTIVAL_ID, mockUser.getUserId())).thenReturn(0);

        mockMvc.perform(get("/tickets/buy").param("festivalId", VALID_FESTIVAL_ID.toString())).andExpect(status().isOk()).andExpect(view().name("ticket-buy")).andExpect(model().attributeExists("ticket")).andExpect(model().attributeExists("festival")).andExpect(model().attributeExists("ticketsBought")).andExpect(model().attribute("ticketsBought", 0));
    }

    @WithMockUser(username = USERNAME, roles = {"USER"})
    @Test
    void testBuyFestivalTicketPost_Success() throws Exception {
        setupMockTicketAndUser(1, 10);

        mockMvc.perform(post("/tickets/buy").param("festivalId", VALID_FESTIVAL_ID.toString()).param("quantity", "1")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/dashboard")).andExpect(flash().attributeExists("message"));
    }

    @WithMockUser(username = USERNAME, roles = {"USER"})
    @Test
    void testBuyFestivalTicketPost_Failure() throws Exception {
        setupMockTicketAndUser(0, 10);

        mockMvc.perform(post("/tickets/buy").param("festivalId", VALID_FESTIVAL_ID.toString()).param("quantity", "0")).andExpect(status().isOk()).andExpect(view().name("ticket-buy")).andExpect(model().attributeHasFieldErrors("ticket", "quantity"));
    }

    @WithMockUser(username = USERNAME, roles = {"USER"})
    @Test
    void testBuyFestivalTicketPost_InsufficientPlaces() throws Exception {
        setupMockTicketAndUser(11, 10);

        mockMvc.perform(post("/tickets/buy").param("festivalId", VALID_FESTIVAL_ID.toString()).param("quantity", "11")).andExpect(status().isOk()).andExpect(view().name("ticket-buy")).andExpect(model().attributeHasFieldErrors("ticket", "quantity"));
    }

    @WithMockUser(username = USERNAME, roles = {"USER"})
    @Test
    void testBuyFestivalTicketPost_InvalidFestivalId() throws Exception {
        when(ticketService.setupBuyTicketModel(INVALID_FESTIVAL_ID, USERNAME)).thenThrow(new FestivalNotFoundException(Math.toIntExact(INVALID_FESTIVAL_ID)));

        mockMvc.perform(post("/tickets/buy").param("festivalId", INVALID_FESTIVAL_ID.toString()).param("quantity", "1")).andExpect(status().isNotFound());
    }

    private void setupMockTicketAndUser(int quantity, int availablePlaces) {
        Ticket ticket = new Ticket();
        ticket.setQuantity(quantity);
        Festival festival = new Festival();
        festival.setAvailablePlaces(availablePlaces);
        ticket.setFestival(festival);

        when(ticketService.setupBuyTicketModel(VALID_FESTIVAL_ID, USERNAME)).thenReturn(ticket);
        when(festivalTicketService.getFestivalById(VALID_FESTIVAL_ID)).thenReturn(festival);
        MyUser mockUser = new MyUser();
        when(myUserService.getUserByUsername(USERNAME)).thenReturn(mockUser);
    }
}
