package com.mourad.school_management.service;


import com.mourad.school_management.dto.NoteDTO;
import com.mourad.school_management.dto.NoteResponseDTO;
import com.mourad.school_management.entity.*;
import com.mourad.school_management.repository.NoteRepository;
import com.mourad.school_management.repository.StudentRepository;
import com.mourad.school_management.repository.SubjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {
    private final NoteRepository noteRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public NoteResponseDTO createNote(NoteDTO noteDTO) {
        // Vérifier si l'étudiant existe
        Student student = studentRepository.findById(noteDTO.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        // Vérifier si la matière existe
        Subject subject = subjectRepository.findById(noteDTO.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));

        // Vérifier si la note est valide
        if (noteDTO.getValue() < 0 || noteDTO.getValue() > 20) {
            throw new IllegalArgumentException("Note must be between 0 and 20");
        }

        // Créer la note
        Note note = Note.builder()
                .student(student)
                .subject(subject)
                .value(noteDTO.getValue())
                .term(noteDTO.getTerm())
                .comment(noteDTO.getComment())
                .build();

        note = noteRepository.save(note);
        return mapToResponseDTO(note);
    }

    public NoteResponseDTO getNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Note not found"));
        return mapToResponseDTO(note);
    }

    public List<NoteResponseDTO> getNotesByStudent(Long studentId) {
        return noteRepository.findByStudentId(studentId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<NoteResponseDTO> getNotesByStudentAndTerm(Long studentId, Term term) {
        return noteRepository.findByStudentIdAndTerm(studentId, term).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<NoteResponseDTO> getNotesBySubject(Long subjectId) {
        return noteRepository.findBySubjectId(subjectId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public NoteResponseDTO updateNote(Long id, NoteDTO noteDTO) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Note not found"));

        // Vérifier si la nouvelle note est valide
        if (noteDTO.getValue() < 0 || noteDTO.getValue() > 20) {
            throw new IllegalArgumentException("Note must be between 0 and 20");
        }

        // Mettre à jour la note
        note.setValue(noteDTO.getValue());
        note.setTerm(noteDTO.getTerm());
        note.setComment(noteDTO.getComment());

        note = noteRepository.save(note);
        return mapToResponseDTO(note);
    }

    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Note not found"));
        noteRepository.delete(note);
    }

    public Double calculateAverage(Long studentId, Term term) {
        List<Note> notes = noteRepository.findByStudentIdAndTerm(studentId, term);
        if (notes.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        double totalCoefficient = 0.0;

        for (Note note : notes) {
            sum += note.getValue() * note.getSubject().getCoefficient();
            totalCoefficient += note.getSubject().getCoefficient();
        }

        return totalCoefficient > 0 ? sum / totalCoefficient : 0.0;
    }

    private NoteResponseDTO mapToResponseDTO(Note note) {
        return NoteResponseDTO.builder()
                .id(note.getId())
                .studentName(note.getStudent().getUser().getFirstname() + " " +
                        note.getStudent().getUser().getLastname())
                .studentMatricule(note.getStudent().getMatricule())
                .subjectName(note.getSubject().getName())
                .value(note.getValue())
                .term(note.getTerm())
                .comment(note.getComment())
                .coefficient(note.getSubject().getCoefficient())
                .build();
    }
}
