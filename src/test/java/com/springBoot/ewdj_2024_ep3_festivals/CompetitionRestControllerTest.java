package com.springBoot.ewdj_2024_ep3_festivals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import service.CompetitionService;

@SpringBootTest
class CompetitionRestControllerTest {

    @Mock
    private CompetitionService mock;

    @Mock
    private CompetitionRestController controller;

    private MockMvc mockMvc;

    private final int ID = 10;
    private final String NAME = "Test";

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(controller).build();
        ReflectionTestUtils.setField(controller, "competitionService", mock);
    }

    private Competition aCompetition(Long competition_id) {
        Competition competition = new Competition();
        return competition.builder().competition_id(competition_id).build();
    }

    private void performRest(String uri) throws Exception {
        mockMvc.perform(get(uri)).andExpect(status().isOk()).andExpect(jsonPath("$.competition_id").value(ID)).andExpect(jsonPath("$.competition_name").value(NAME));
    }

    @Test
    public void testGetCompetition_isOk() throws Exception {
//        // Arrange
//        Competition expectedCompetition = new Competition();
//        expectedCompetition.setCompetition_id((long) ID);
//        expectedCompetition.setName(NAME);
//
//        Mockito.when(mock.getCompetitionDetail(ID)).thenReturn(expectedCompetition);
//
//        // Act and Assert
//        mockMvc.perform(get("/competition/" + ID))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.competition_id", is(ID)))
//                .andExpect(jsonPath("$.name", is(NAME)));
//
//        Mockito.verify(mock).getCompetitionDetail(ID);
    }

    @Test
    public void testGetCompetition_notFound() throws Exception {
//        Mockito.when(mock.getCompetitionDetail(ID)).thenThrow(new CompetitionNotFoundException(ID));
//        Exception exception = assertThrows(Exception.class, () -> {
//            mockMvc.perform(get("/competition/" + ID)).andReturn();
//        });
//        assertTrue(exception.getCause() instanceof CompetitionNotFoundException);
//        Mockito.verify(mock).getCompetitionDetail(ID);
    }

    @Test
    public void testGetAllCompetitions_emptyList() throws Exception {
//        Mockito.when(mock.getAllCompetitions()).thenReturn(new ArrayList<>());
//
//        mockMvc.perform(get("/competition/all")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$").isEmpty());
//
//        Mockito.verify(mock).getAllCompetitions();
    }

    @Test
    public void testGetAllCompetitions_noEmptyList() throws Exception {
//        Competition competition1 = aCompetition(ID, NAME);
//        Competition competition2 = aCompetition(5678, "Test2");
//        List<Competition> listCompetition = List.of(competition1, competition2);
//        Mockito.when(mock.getAllCompetitions()).thenReturn(listCompetition);
//
//        mockMvc.perform(get("/competition/all")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$").isNotEmpty()).andExpect(jsonPath("$[0].competition_id").value(ID)).andExpect(jsonPath("$[0].competition_name").value(NAME)).andExpect(jsonPath("$[1].competition_id").value(5678)).andExpect(jsonPath("$[1].competition_name").value("Test2"));
//
//        Mockito.verify(mock).getAllCompetitions();
    }

}