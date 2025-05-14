package com.bank.bankingsystem.AuthorizationService.controller;

import com.bank.bankingsystem.AuthorizationService.dto.AuthorizationDTO;
import com.bank.bankingsystem.AuthorizationService.exception.UnauthorizedException;
import com.bank.bankingsystem.AuthorizationService.service.AuthorizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorizationController.class)
public class AuthorizationControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthorizationService authorizationService;

    @Test
    void shouldLReturnMockTokenWhenLogInSuccessful() throws Exception{
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername("testuser");
        dto.setPassword("testpassword");

        when(authorizationService.authenticate(any())).thenReturn("mock-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("mock-token"));
    }
    @Test
    void shouldReturn401WhenLoginFails() throws Exception {
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername("wrongUser");
        dto.setPassword("wrongPass");

        when(authorizationService.authenticate(any())).thenThrow(new UnauthorizedException("Invalid username or password"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }
}
