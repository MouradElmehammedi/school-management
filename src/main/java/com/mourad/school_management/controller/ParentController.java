package com.mourad.school_management.controller;


import com.mourad.school_management.dto.ParentDTO;
import com.mourad.school_management.dto.ParentResponseDTO;
import com.mourad.school_management.service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parents")
@RequiredArgsConstructor
public class ParentController {
    private final ParentService parentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParentResponseDTO> createParent(@Valid @RequestBody ParentDTO parentDTO) {
        return new ResponseEntity<>(parentService.createParent(parentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ParentResponseDTO> getParent(@PathVariable Long id) {
        return ResponseEntity.ok(parentService.getParent(id));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<ParentResponseDTO> getCurrentParent(@AuthenticationPrincipal UserDetails userDetails) {
        // Supposons que userDetails contient l'ID de l'utilisateur
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(parentService.getParentByUserId(userId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<ParentResponseDTO>> getAllParents() {
        return ResponseEntity.ok(parentService.getAllParents());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PARENT')")
    public ResponseEntity<ParentResponseDTO> updateParent(
            @PathVariable Long id,
            @Valid @RequestBody ParentDTO parentDTO) {
        return ResponseEntity.ok(parentService.updateParent(id, parentDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteParent(@PathVariable Long id) {
        parentService.deleteParent(id);
        return ResponseEntity.noContent().build();
    }
}
