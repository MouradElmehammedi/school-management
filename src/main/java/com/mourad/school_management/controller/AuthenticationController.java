package com.mourad.school_management.controller;

import com.mourad.school_management.dto.AuthenticationRequest;
import com.mourad.school_management.dto.AuthenticationResponse;
import com.mourad.school_management.dto.RegisterRequest;
import com.mourad.school_management.dto.UserResponseDTO;
import com.mourad.school_management.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> getMe() {
        return ResponseEntity.ok(service.getMe());
    }
}
