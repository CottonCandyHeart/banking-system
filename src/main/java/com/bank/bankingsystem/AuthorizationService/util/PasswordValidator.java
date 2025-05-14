package com.bank.bankingsystem.AuthorizationService.util;

import java.util.regex.Pattern;

public class PasswordValidator {
    // Minimum 8 znaków
    // Co najmniej jedna wielka litera
    // Co najmniej jedna mała litera
    // Co najmniej jedna cyfra
    // Co najmniej jeden znak specjalny
    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isStrong(String password) {
        return pattern.matcher(password).matches();
    }
}
