package com.mourad.school_management.service;

import com.mourad.school_management.dto.AuthenticationRequest;
import com.mourad.school_management.dto.AuthenticationResponse;
import com.mourad.school_management.dto.RegisterRequest;
import com.mourad.school_management.entity.Role;
import com.mourad.school_management.entity.User;
import com.mourad.school_management.exception.ErrorMessages;
import com.mourad.school_management.exception.ResourceNotFoundException;
import com.mourad.school_management.repository.UserRepository;
import com.mourad.school_management.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            log.info("Attempting to authenticate user with email: {}", request.getEmail());

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            String jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {}", request.getEmail(), e);
            throw new AuthenticationException(ErrorMessages.INVALID_CREDENTIALS) {
            };
        }
    }
}
