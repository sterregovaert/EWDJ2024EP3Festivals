package com.springBoot.ewdj_2024_ep3_festivals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testLoginWithError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testLoginWithLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login").param("logout", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithAnonymousUser
    public void testNoAccessAnonymous() throws Exception {
        String[] urls = {"/dashboard", "/festivals", "/tickets"};
        for (String url : urls) {
            mockMvc.perform(get(url))
                    .andExpect(redirectedUrlPattern("**/login"));
        }
    }

    @Test
    public void accessDeniedPageGet() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
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
                .andExpect(redirectedUrl("/dashboard"));
    }
}