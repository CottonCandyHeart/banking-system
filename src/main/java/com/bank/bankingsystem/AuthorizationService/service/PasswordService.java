package com.bank.bankingsystem.AuthorizationService.service;

import com.bank.bankingsystem.AuthorizationService.dto.ChangePasswordRequest;
import com.bank.bankingsystem.AuthorizationService.model.UserEntity;
import com.bank.bankingsystem.AuthorizationService.util.PasswordValidator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public PasswordService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public void changePassword(ChangePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New passwords do not match.");
        }

        String username = getCurrentUsername();
        UserEntity user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found."));

        if (!PasswordValidator.isStrong(request.getNewPassword())) {
            throw new IllegalArgumentException("Password is too weak.");
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.save(user);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            return principal.toString();
        }
    }
}
