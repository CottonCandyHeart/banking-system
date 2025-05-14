package com.bank.bankingsystem.AuthorizationService.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidatorUnitTest {
    @Test
    void shouldReturnTrueForValidPassword() {
        assertTrue(PasswordValidator.isStrong("Str0ng@Passw0rd!"));
        assertTrue(PasswordValidator.isStrong("A1b2C3d4@"));
        assertTrue(PasswordValidator.isStrong("Valid123!"));
    }
    @Test
    void shouldReturnFalseWhenPasswordIsTooShort() {
        assertFalse(PasswordValidator.isStrong("A1b!")); // 4 znaki
    }
    @Test
    void shouldReturnFalseWhenNoUppercaseLetter() {
        assertFalse(PasswordValidator.isStrong("weakpassword1!"));
    }
    @Test
    void shouldReturnFalseWhenNoLowercaseLetter() {
        assertFalse(PasswordValidator.isStrong("WEAKPASSWORD1!"));
    }
    @Test
    void shouldReturnFalseWhenNoDigit() {
        assertFalse(PasswordValidator.isStrong("WeakPassword!"));
    }
    @Test
    void shouldReturnFalseWhenNoSpecialCharacter() {
        assertFalse(PasswordValidator.isStrong("WeakPassword1"));
    }
    @Test
    void shouldReturnFalseForEmptyString() {
        assertFalse(PasswordValidator.isStrong(""));
    }
    @Test
    void shouldReturnFalseForNull() {
        assertThrows(NullPointerException.class, () -> PasswordValidator.isStrong(null));
    }
}
