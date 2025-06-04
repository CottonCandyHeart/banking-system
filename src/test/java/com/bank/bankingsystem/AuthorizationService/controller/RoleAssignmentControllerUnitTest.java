package com.bank.bankingsystem.AuthorizationService.controller;

import com.bank.bankingsystem.AuthorizationService.dto.RoleUpdateRequest;
import com.bank.bankingsystem.AuthorizationService.service.RoleAssignmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoleAssignmentControllerUnitTest {

    private MockMvc mockMvc;
    private RoleAssignmentService roleAssignmentService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        roleAssignmentService = Mockito.mock(RoleAssignmentService.class);
        RoleAssignmentController controller = new RoleAssignmentController(roleAssignmentService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }


    @Test
    void shouldAssignRoleSuccessfully() throws Exception {
        RoleUpdateRequest request = new RoleUpdateRequest();
        request.setEmployeeId(1L);
        request.setNewRole("MANAGER");

        mockMvc.perform(post("/admin/assign-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(roleAssignmentService).assignRole(1L, "MANAGER");
    }
}
