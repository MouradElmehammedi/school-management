package com.mourad.school_management.service;

import com.mourad.school_management.dto.AuthenticationRequest;
import com.mourad.school_management.dto.AuthenticationResponse;
import com.mourad.school_management.dto.RegisterRequest;
import com.mourad.school_management.dto.UserResponseDTO;
import com.mourad.school_management.entity.Role;
import com.mourad.school_management.entity.User;
import com.mourad.school_management.exception.*;
import com.mourad.school_management.repository.RoleRepository;
import com.mourad.school_management.repository.UserRepository;
import com.mourad.school_management.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Attempting to register new user with email: {}", request.getEmail());

        try {
            // Validation des données
            validateRegistrationRequest(request);

            // Vérifier si l'email existe déjà
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ValidationException(ErrorMessages.EMAIL_ALREADY_EXISTS);
            }

            // Créer la liste des rôles
            List<Role> roles = getRolesForRegistration(request);

            // Créer l'utilisateur
            User user = createUserFromRequest(request, roles);

            // Sauvegarder l'utilisateur
            user = userRepository.save(user);
            log.info("User registered successfully with ID: {}", user.getId());

            // Générer le token JWT
            String jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();

        } catch (BaseException e) {
            log.error("Registration failed for email: {}", request.getEmail(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during registration for email: {}", request.getEmail(), e);
            throw new BusinessException(ErrorMessages.OPERATION_FAILED);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Attempting to authenticate user with email: {}", request.getEmail());

        try {
            // Validation des données
            validateAuthenticationRequest(request);

            // Authentifier l'utilisateur
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Récupérer l'utilisateur après authentification réussie
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));

            // Générer le token JWT
            String jwtToken = jwtService.generateToken(user);
            log.info("User authenticated successfully: {}", user.getEmail());

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();

        } catch (ResourceNotFoundException e) {
            log.error("User not found: {}", request.getEmail());
            throw e;
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials for user: {}", request.getEmail());
            throw new AuthenticationException(ErrorMessages.INVALID_CREDENTIALS);
        } catch (Exception e) {
            log.error("Unexpected error during authentication for user: {}", request.getEmail(), e);
            throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED);
        }
    }

    public UserResponseDTO getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("User not authenticated");
        }

        String email = authentication.getName();
        log.info("Fetching current user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToResponseDTO(user);
    }

    private void validateRegistrationRequest(RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }
        if (!isValidEmail(request.getEmail())) {
            throw new ValidationException("Invalid email format");
        }
        if (request.getPassword() == null || request.getPassword().length() < 8) {
            throw new ValidationException(ErrorMessages.PASSWORD_TOO_SHORT);
        }
        if (request.getFirstname() == null || request.getFirstname().trim().isEmpty()) {
            throw new ValidationException("First name is required");
        }
        if (request.getLastname() == null || request.getLastname().trim().isEmpty()) {
            throw new ValidationException("Last name is required");
        }
    }

    private void validateAuthenticationRequest(AuthenticationRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new ValidationException("Password is required");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private List<Role> getRolesForRegistration(RegisterRequest request) {
        List<Role> roles = new ArrayList<>();
        
        // Si des rôles sont spécifiés dans la requête
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (Role roleName : request.getRoles()) {
                List<Role> foundRoles = roleRepository.findByName(roleName.getName());
                if (foundRoles.isEmpty()) {
                    throw new ResourceNotFoundException("Role " + roleName + " not found");
                }
                roles.add(foundRoles.get(0));
            }
        } else {
            // Par défaut, ajouter le rôle STUDENT si aucun rôle n'est spécifié
            List<Role> studentRoles = roleRepository.findByName(Role.RoleName.ROLE_STUDENT);
            if (studentRoles.isEmpty()) {
                throw new ResourceNotFoundException(ErrorMessages.ROLE_NOT_FOUND);
            }
            roles.add(studentRoles.get(0));
        }

        return roles;
    }

    private User createUserFromRequest(RegisterRequest request, List<Role> roles) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .roles(roles)
                .build();
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .enabled(user.getEnabled())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList()))
                .build();
    }
}