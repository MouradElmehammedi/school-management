package com.mourad.school_management.controller;

import com.mourad.school_management.dto.ChangePasswordDTO;
import com.mourad.school_management.dto.UpdateUserDTO;
import com.mourad.school_management.dto.UserDTO;
import com.mourad.school_management.dto.UserResponseDTO;
import com.mourad.school_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserDTO));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> enableUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.enableUser(id));
    }


    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> disableUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.disableUser(id));
    }


    @PatchMapping("/{id}/change-password")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(id, changePasswordDTO);
        return ResponseEntity.ok().build();
    }
}
