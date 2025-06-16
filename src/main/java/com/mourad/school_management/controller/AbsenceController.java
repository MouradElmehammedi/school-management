package com.mourad.school_management.controller;


import com.mourad.school_management.dto.AbsenceDTO;
import com.mourad.school_management.dto.AbsenceResponseDTO;
import com.mourad.school_management.service.AbsenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/absences")
@RequiredArgsConstructor
public class AbsenceController {
    private final AbsenceService absenceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<AbsenceResponseDTO> createAbsence(@Valid @RequestBody AbsenceDTO absenceDTO) {
        return new ResponseEntity<>(absenceService.createAbsence(absenceDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<AbsenceResponseDTO> getAbsence(@PathVariable Long id) {
        return ResponseEntity.ok(absenceService.getAbsence(id));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<List<AbsenceResponseDTO>> getAbsencesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(absenceService.getAbsencesByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<List<AbsenceResponseDTO>> getAbsencesByStudentAndDateRange(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(absenceService.getAbsencesByStudentAndDateRange(studentId, startDate, endDate));
    }

    @GetMapping("/classe/{classeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<AbsenceResponseDTO>> getAbsencesByClasse(@PathVariable Long classeId) {
        return ResponseEntity.ok(absenceService.getAbsencesByClasse(classeId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<AbsenceResponseDTO> updateAbsence(
            @PathVariable Long id,
            @Valid @RequestBody AbsenceDTO absenceDTO) {
        return ResponseEntity.ok(absenceService.updateAbsence(id, absenceDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAbsence(@PathVariable Long id) {
        absenceService.deleteAbsence(id);
        return ResponseEntity.noContent().build();
    }
}
