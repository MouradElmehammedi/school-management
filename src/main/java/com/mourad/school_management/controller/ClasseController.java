package com.mourad.school_management.controller;


import com.mourad.school_management.dto.ClasseDTO;
import com.mourad.school_management.dto.ClasseResponseDTO;
import com.mourad.school_management.service.ClasseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/classes")
@RequiredArgsConstructor
public class ClasseController {
    private final ClasseService classeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClasseResponseDTO> createClasse(@Valid @RequestBody ClasseDTO classeDTO) {
        return new ResponseEntity<>(classeService.createClasse(classeDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT')")
    public ResponseEntity<ClasseResponseDTO> getClasse(@PathVariable Long id) {
        return ResponseEntity.ok(classeService.getClasse(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT')")
    public ResponseEntity<List<ClasseResponseDTO>> getAllClasses() {
        return ResponseEntity.ok(classeService.getAllClasses());
    }

    @GetMapping("/level/{level}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT')")
    public ResponseEntity<List<ClasseResponseDTO>> getClassesByLevel(@PathVariable String level) {
        return ResponseEntity.ok(classeService.getClassesByLevel(level));
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<ClasseResponseDTO>> getClassesByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(classeService.getClassesByTeacher(teacherId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClasseResponseDTO> updateClasse(
            @PathVariable Long id,
            @Valid @RequestBody ClasseDTO classeDTO) {
        return ResponseEntity.ok(classeService.updateClasse(id, classeDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClasse(@PathVariable Long id) {
        classeService.deleteClasse(id);
        return ResponseEntity.noContent().build();
    }
}
