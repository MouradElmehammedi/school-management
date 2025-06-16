package com.mourad.school_management.controller;


import com.mourad.school_management.dto.NoteDTO;
import com.mourad.school_management.dto.NoteResponseDTO;
import com.mourad.school_management.entity.Term;
import com.mourad.school_management.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<NoteResponseDTO> createNote(@Valid @RequestBody NoteDTO noteDTO) {
        return new ResponseEntity<>(noteService.createNote(noteDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<NoteResponseDTO> getNote(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNote(id));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<List<NoteResponseDTO>> getNotesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(noteService.getNotesByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/term/{term}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<List<NoteResponseDTO>> getNotesByStudentAndTerm(
            @PathVariable Long studentId,
            @PathVariable Term term) {
        return ResponseEntity.ok(noteService.getNotesByStudentAndTerm(studentId, term));
    }

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<NoteResponseDTO>> getNotesBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(noteService.getNotesBySubject(subjectId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<NoteResponseDTO> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody NoteDTO noteDTO) {
        return ResponseEntity.ok(noteService.updateNote(id, noteDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}/average/{term}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<Double> calculateAverage(
            @PathVariable Long studentId,
            @PathVariable Term term) {
        return ResponseEntity.ok(noteService.calculateAverage(studentId, term));
    }
}