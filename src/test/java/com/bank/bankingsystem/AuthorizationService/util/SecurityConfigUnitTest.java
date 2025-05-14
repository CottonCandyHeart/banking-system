package com.bank.bankingsystem.AuthorizationService.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class SecurityConfigUnitTest {
    @Test
    void passwordEncoder_shouldReturnBCryptPasswordEncoderInstance() {
        SecurityConfig config = new SecurityConfig();

        BCryptPasswordEncoder encoder = config.passwordEncoder();

        assertThat(encoder).isNotNull();
        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);

        String rawPassword = "test";
        String encodedPassword = encoder.encode(rawPassword);
        assertThat(encoder.matches(rawPassword, encodedPassword)).isTrue();
    }
}
