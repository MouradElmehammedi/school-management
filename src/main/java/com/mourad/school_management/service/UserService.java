package com.mourad.school_management.service;

import com.mourad.school_management.dto.ChangePasswordDTO;
import com.mourad.school_management.dto.UpdateUserDTO;
import com.mourad.school_management.dto.UserDTO;
import com.mourad.school_management.dto.UserResponseDTO;
import com.mourad.school_management.entity.Role;
import com.mourad.school_management.entity.User;
import com.mourad.school_management.exception.AuthenticationException;
import com.mourad.school_management.exception.ResourceNotFoundException;
import com.mourad.school_management.exception.ValidationException;
import com.mourad.school_management.repository.RoleRepository;
import com.mourad.school_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserDTO userDTO) {
        log.info("Creating new user with email: {}", userDTO.getEmail());

        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ValidationException("Email already exists");
        }

        // Récupérer les rôles
        List<Role> roles = getRolesFromDTO(userDTO.getRoles());

        // Créer l'utilisateur
        User user = User.builder()
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .firstname(userDTO.getFirstname())
                .lastname(userDTO.getLastname())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .enabled(userDTO.getEnabled() != null ? userDTO.getEnabled() : true)
                .roles(roles)
                .build();

        user = userRepository.save(user);
        log.info("User created successfully with ID: {}", user.getId());

        return mapToResponseDTO(user);
    }

    public UserResponseDTO getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return mapToResponseDTO(user);
    }

    public UserResponseDTO getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return mapToResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        // Mettre à jour les champs
        if (updateUserDTO.getFirstname() != null) {
            user.setFirstname(updateUserDTO.getFirstname());
        }
        if (updateUserDTO.getLastname() != null) {
            user.setLastname(updateUserDTO.getLastname());
        }
        if (updateUserDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(updateUserDTO.getPhoneNumber());
        }
        if (updateUserDTO.getAddress() != null) {
            user.setAddress(updateUserDTO.getAddress());
        }

        user = userRepository.save(user);
        log.info("User updated successfully with ID: {}", user.getId());

        return mapToResponseDTO(user);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        
        userRepository.delete(user);
        log.info("User deleted successfully with ID: {}", id);
    }

    public UserResponseDTO enableUser(Long id) {
        log.info("Enabling user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        
        user.setEnabled(true);
        user = userRepository.save(user);
        log.info("User enabled successfully with ID: {}", id);

        return mapToResponseDTO(user);
    }

    public UserResponseDTO disableUser(Long id) {
        log.info("Disabling user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        
        user.setEnabled(false);
        user = userRepository.save(user);
        log.info("User disabled successfully with ID: {}", id);

        return mapToResponseDTO(user);
    }

    public void changePassword(Long id, ChangePasswordDTO changePasswordDTO) {
        log.info("Changing password for user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        // Vérifier l'ancien mot de passe
        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new AuthenticationException("Current password is incorrect");
        }

        // Vérifier que les nouveaux mots de passe correspondent
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new ValidationException("New password and confirm password do not match");
        }

        // Vérifier que le nouveau mot de passe est différent de l'ancien
        if (passwordEncoder.matches(changePasswordDTO.getNewPassword(), user.getPassword())) {
            throw new ValidationException("New password must be different from current password");
        }

        // Encoder et sauvegarder le nouveau mot de passe
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed successfully for user with ID: {}", id);
    }

    private List<Role> getRolesFromDTO(List<Role.RoleName> roleNames) {
        List<Role> roles = new ArrayList<>();
        
        if (roleNames != null && !roleNames.isEmpty()) {
            for (Role.RoleName roleName : roleNames) {
                List<Role> foundRoles = roleRepository.findByName(roleName);
                if (foundRoles.isEmpty()) {
                    throw new ResourceNotFoundException("Role " + roleName + " not found");
                }
                roles.add(foundRoles.get(0));
            }
        } else {
            // Rôle par défaut si aucun n'est spécifié
            List<Role> defaultRoles = roleRepository.findByName(Role.RoleName.ROLE_USER);
            if (!defaultRoles.isEmpty()) {
                roles.add(defaultRoles.get(0));
            }
        }

        return roles;
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
