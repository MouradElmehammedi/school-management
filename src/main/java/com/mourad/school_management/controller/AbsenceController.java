package com.mourad.school_management.controller;

import com.mourad.school_management.dto.AbsenceDTO;
import com.mourad.school_management.dto.AbsenceResponseDTO;
import com.mourad.school_management.service.AbsenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AbsenceController {
    private final AbsenceService absenceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<AbsenceResponseDTO> createAbsence(
            @Valid @RequestBody AbsenceDTO absenceDTO) {
        log.info("Creating new absence for student ID: {}", absenceDTO.getStudentId());
        try {
            AbsenceResponseDTO response = absenceService.createAbsence(absenceDTO);
            log.info("Absence created successfully with ID: {}", response.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating absence for student ID: {}", absenceDTO.getStudentId(), e);
            throw e;
        }
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<AbsenceResponseDTO> getAbsence(
            @PathVariable Long id) {
        log.info("Fetching absence with ID: {}", id);
        try {
            AbsenceResponseDTO response = absenceService.getAbsence(id);
            log.info("Absence retrieved successfully with ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching absence with ID: {}", id, e);
            throw e;
        }
    }


    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<List<AbsenceResponseDTO>> getAbsencesByStudent(
            @PathVariable Long studentId) {
        log.info("Fetching absences for student ID: {}", studentId);
        try {
            List<AbsenceResponseDTO> response = absenceService.getAbsencesByStudent(studentId);
            log.info("Retrieved {} absences for student ID: {}", response.size(), studentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching absences for student ID: {}", studentId, e);
            throw e;
        }
    }


    @GetMapping("/student/{studentId}/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<List<AbsenceResponseDTO>> getAbsencesByStudentAndDateRange(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Fetching absences for student ID: {} between {} and {}", studentId, startDate, endDate);
        try {
            List<AbsenceResponseDTO> response = absenceService.getAbsencesByStudentAndDateRange(studentId, startDate, endDate);
            log.info("Retrieved {} absences for student ID: {} in date range", response.size(), studentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching absences for student ID: {} in date range", studentId, e);
            throw e;
        }
    }


    @GetMapping("/classe/{classeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<AbsenceResponseDTO>> getAbsencesByClasse(
            @PathVariable Long classeId) {
        log.info("Fetching absences for class ID: {}", classeId);
        try {
            List<AbsenceResponseDTO> response = absenceService.getAbsencesByClasse(classeId);
            log.info("Retrieved {} absences for class ID: {}", response.size(), classeId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching absences for class ID: {}", classeId, e);
            throw e;
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<AbsenceResponseDTO> updateAbsence(
            @PathVariable Long id,
            @Valid @RequestBody AbsenceDTO absenceDTO) {
        log.info("Updating absence with ID: {}", id);
        try {
            AbsenceResponseDTO response = absenceService.updateAbsence(id, absenceDTO);
            log.info("Absence updated successfully with ID: {}", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating absence with ID: {}", id, e);
            throw e;
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAbsence(
            @PathVariable Long id) {
        log.info("Deleting absence with ID: {}", id);
        try {
            absenceService.deleteAbsence(id);
            log.info("Absence deleted successfully with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting absence with ID: {}", id, e);
            throw e;
        }
    }
}
