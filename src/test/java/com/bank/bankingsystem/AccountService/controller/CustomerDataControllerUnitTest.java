package com.bank.bankingsystem.AccountService.controller;

import com.bank.bankingsystem.AccountService.dto.CustomerDataDTO;
import com.bank.bankingsystem.AccountService.service.CustomerDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@RestController
@RequestMapping("/api/customers")
public class CustomerDataControllerUnitTest {

    @Mock
    private CustomerDataService customerDataService;
    @InjectMocks
    private CustomerDataController customerDataController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerDataController).build();
    }

    @Test
    void shouldReturnCustomerDataWhenUserIsValid() throws Exception {
        Long userId = 1L;
        CustomerDataDTO customerDataDTO = new CustomerDataDTO(userId, "Test Test", "test@example.com", "Test 123", "123456789");

        when(customerDataService.getCustomerData(userId)).thenReturn(customerDataDTO);

        mockMvc.perform(get("/api/customers/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.fullName").value("Test Test"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.address").value("Test 123"))
                .andExpect(jsonPath("$.phoneNumber").value("123456789"));

        verify(customerDataService, times(1)).getCustomerData(userId);
    }

    @Test
    void shouldCallServiceWithCorrectUserId() throws Exception {
        Long userId = 123L;
        CustomerDataDTO customerDataDTO = new CustomerDataDTO(userId,"Test Test", "test@example.com", "Test 123", "123456789");

        when(customerDataService.getCustomerData(userId)).thenReturn(customerDataDTO);

        mockMvc.perform(get("/api/customers/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerDataService).getCustomerData(userId);
    }

}