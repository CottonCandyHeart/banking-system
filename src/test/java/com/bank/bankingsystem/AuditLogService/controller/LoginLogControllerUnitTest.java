package com.bank.bankingsystem.AuditLogService.controller;

import com.bank.bankingsystem.AuditLogService.model.LoginLogEntity;
import com.bank.bankingsystem.AuditLogService.service.LoginLogService;
import com.bank.bankingsystem.AuthorizationService.util.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginLogController.class)
@Import(SecurityConfig.class)
public class LoginLogControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginLogService loginLogService;

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void shouldReturnLoginLogsForUser() throws Exception {
        Long userId = 1L;

        LoginLogEntity log1 = new LoginLogEntity();
        log1.setId(1L);
        log1.setUserId(userId);
        log1.setLoginTime(LocalDateTime.of(2024, 1, 1, 12, 0));
        log1.setIpAddress("192.168.0.1");
        log1.setSuccess(true);

        LoginLogEntity log2 = new LoginLogEntity();
        log2.setId(2L);
        log2.setUserId(userId);
        log2.setLoginTime(LocalDateTime.of(2024, 1, 2, 14, 30));
        log2.setIpAddress("192.168.0.2");
        log2.setSuccess(false);

        when(loginLogService.getLoginHistoryForUser(userId)).thenReturn(List.of(log1, log2));

        mockMvc.perform(get("/api/logins/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].ipAddress", is("192.168.0.1")))
                .andExpect(jsonPath("$[0].success", is(true)))
                .andExpect(jsonPath("$[0].loginTime", notNullValue()))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].userId", is(1)))
                .andExpect(jsonPath("$[1].ipAddress", is("192.168.0.2")))
                .andExpect(jsonPath("$[1].success", is(false)))
                .andExpect(jsonPath("$[1].loginTime", notNullValue()));
    }
}
