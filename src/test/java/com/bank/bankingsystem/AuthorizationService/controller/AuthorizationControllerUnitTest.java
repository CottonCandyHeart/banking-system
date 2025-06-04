package com.bank.bankingsystem.AuthorizationService.controller;

import com.bank.bankingsystem.AuthorizationService.dto.AuthorizationDTO;
import com.bank.bankingsystem.AuthorizationService.exception.GlobalExceptionHandler;
import com.bank.bankingsystem.AuthorizationService.exception.UnauthorizedException;
import com.bank.bankingsystem.AuthorizationService.service.AuthorizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthorizationControllerUnitTest {

    private MockMvc mockMvc;
    private AuthorizationService authorizationService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        authorizationService = mock(AuthorizationService.class);
        AuthorizationController controller = new AuthorizationController(authorizationService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

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
