package com.springBoot.ewdj_2024_ep3_festivals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

//@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class Ewdj2024EP3FestivalsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

}
