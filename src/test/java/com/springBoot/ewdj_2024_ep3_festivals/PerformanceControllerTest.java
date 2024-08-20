package com.springBoot.ewdj_2024_ep3_festivals;

import domain.Festival;
import domain.Genre;
import domain.Performance;
import domain.Region;
import domain.Ticket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import service.PerformanceService;
import validator.PerformanceDateTimeValidation;
import validator.PerformanceTimeSlotValidation;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PerformanceControllerTest {

        private final Long festivalId = 1L;
        private final Long performanceId = 1L;
        private final Long invalidPerformanceId = 999L;

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PerformanceService performanceService;
        @MockBean
        private PerformanceDateTimeValidation performanceDateTimeValidation;
        @MockBean
        private PerformanceTimeSlotValidation performanceTimeSlotValidation;

        private Festival festival;
        private Performance performance;

        @BeforeEach
        void setup() {
                Genre genre = Genre.builder().name("Rock").build();
                Region region = Region.builder().name("North").build();

                festival = new Festival();
                festival.setFestivalId(festivalId);
                festival.setName("Test Festival");
                festival.setStartDateTime(
                                LocalDateTime.now().plusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0));
                festival.setGenre(genre);
                festival.setRegion(region);
                festival.setAvailablePlaces(10);
                festival.setTicketPrice(10.0);

                performance = new Performance();
                performance.setPerformanceId(performanceId);
                performance.setFestival(festival);
                performance.setArtistName("Test Artist");
                performance.setFestivalNumber1(3333);
                performance.setFestivalNumber2(3333);
                performance.setStartDateTime(
                                LocalDateTime.now().plusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0)
                                                .plusHours(1));
                performance.setEndDateTime(
                                LocalDateTime.now().plusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0)
                                                .plusHours(2));
        }

        @WithMockUser(username = "user", roles = { "USER" })
        @Test
        void testUserCannotAccessAddPerformance() throws Exception {
                mockMvc.perform(get("/performance/add?festivalId=1")).andExpect(status().isForbidden());
        }

        @WithMockUser(username = "user", roles = { "USER" })
        @Test
        void testUserCannotAccessRemovePerformance() throws Exception {
                mockMvc.perform(get("/performance/remove?festivalId=1")).andExpect(status().isForbidden());
        }

        @WithMockUser(username = "admin", roles = { "ADMIN" })
        @Test
        public void testShowAddPerformanceForm() throws Exception {
                when(performanceService.setupAddPerformanceWithDefaults(eq(festivalId), any(Performance.class)))
                                .thenReturn(festival);

                mockMvc.perform(
                                get("/performance/add")
                                                .param("festivalId", festivalId.toString()))
                                .andExpect(status().isOk()).andExpect(view().name("performance-add"));
        }

        @WithMockUser(username = "admin", roles = { "ADMIN" })
        @Test
        public void testAddPerformanceWithValidData() throws Exception {
                when(performanceService.setupPerformanceForFestival(eq(festivalId), any(Performance.class)))
                                .thenReturn(performance);

                when(performanceService.savePerformance(any(Performance.class))).thenReturn(performance);

                mockMvc.perform(
                                post("/performance/add")
                                                .param("festivalId", festivalId.toString())
                                                .flashAttr("performance", performance)
                                                .with(csrf()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
        }

        @WithMockUser(username = "admin", roles = { "ADMIN" })
        @Test
        public void testAddPerformanceWithInvalidData() throws Exception {
                when(performanceService.setupPerformanceForFestival(eq(festivalId), any(Performance.class)))
                                .thenReturn(performance);
                when(performanceService.setupAddPerformanceWithDefaults(eq(festivalId), any(Performance.class)))
                                .thenReturn(festival);

                performance.setEndDateTime(null); // Setting invalid data

                doAnswer(invocation -> {
                        BindingResult bindingResult = invocation.getArgument(1);
                        bindingResult.rejectValue("endDateTime", "error.performance.endDateTime",
                                        "End date/time must be provided");
                        return null;
                }).when(performanceDateTimeValidation).validate(any(Performance.class), any(BindingResult.class));

                mockMvc.perform(
                                post("/performance/add")
                                                .param("festivalId", festivalId.toString())
                                                .flashAttr("performance", performance)
                                                .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(view().name("performance-add"))
                                .andExpect(model().attributeExists("performance"))
                                .andExpect(model().attributeHasFieldErrors("performance", "endDateTime"));
        }

        @WithMockUser(username = "admin", roles = { "ADMIN" })
        @Test
        void testShowRemovePerformanceForm() throws Exception {
                when(performanceService.setupPerformances(festivalId)).thenReturn(Collections.emptyList());
                when(performanceService.setupFestival(festivalId)).thenReturn(festival);

                mockMvc.perform(get("/performance/remove?festivalId=1"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("performance-remove"))
                                .andExpect(model().attributeExists("performances"))
                                .andExpect(model().attributeExists("festival"));
        }

        @WithMockUser(username = "admin", roles = { "ADMIN" })
        @Test
        void testRemovePerformance_Success() throws Exception {
                doNothing().when(performanceService).deletePerformanceById(1L);
                when(performanceService.setupFestival(festivalId)).thenReturn(festival);

                String expectedResultUrl = "/festivals?genre="
                                + festival.getGenre().getName()
                                + "&region="
                                + festival.getRegion().getName();

                mockMvc.perform(post("/performance/remove")
                                .param("festivalId", festival.getFestivalId().toString())
                                .param("performanceId", performance.getPerformanceId().toString())
                                .with(csrf())).andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl(expectedResultUrl));
        }

        @WithMockUser(username = "admin", roles = { "ADMIN" })
        @Test
        public void testRemovePerformanceWithInvalidPerformanceId() throws Exception {
                doThrow(new IllegalArgumentException("Performance not found")).when(performanceService)
                                .deletePerformanceById(invalidPerformanceId);
                when(performanceService.setupFestival(festivalId)).thenReturn(festival);

                mockMvc.perform(post("/performance/remove").param("festivalId", festivalId.toString())
                                .param("performanceId", invalidPerformanceId.toString()).with(csrf()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
        }

        @WithMockUser(username = "admin", roles = { "ADMIN" })
        @Test
        public void testRemovePerformanceWithValidPerformanceIdAndValidFestivalId() throws Exception {
                doNothing().when(performanceService).deletePerformanceById(performanceId);
                when(performanceService.setupFestival(festivalId)).thenReturn(festival);

                mockMvc.perform(post("/performance/remove").param("festivalId", festivalId.toString())
                                .param("performanceId", performanceId.toString()).with(csrf()))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrlPattern("/festivals?genre=*&region=*"));
        }

}