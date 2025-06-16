package com.mourad.school_management.service;


import com.mourad.school_management.dto.AbsenceDTO;
import com.mourad.school_management.dto.AbsenceResponseDTO;
import com.mourad.school_management.entity.Absence;
import com.mourad.school_management.entity.Student;
import com.mourad.school_management.entity.User;
import com.mourad.school_management.repository.AbsenceRepository;
import com.mourad.school_management.repository.StudentRepository;
import com.mourad.school_management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AbsenceService {
    private final AbsenceRepository absenceRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public AbsenceResponseDTO createAbsence(AbsenceDTO absenceDTO) {
        // Vérifier si l'étudiant existe
        Student student = studentRepository.findById(absenceDTO.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        // Récupérer l'utilisateur connecté
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Current user not found"));

        // Créer l'absence
        Absence absence = Absence.builder()
                .student(student)
                .date(absenceDTO.getDate())
                .reason(absenceDTO.getReason())
                .build();

        absence = absenceRepository.save(absence);
        return mapToResponseDTO(absence);
    }

    public AbsenceResponseDTO getAbsence(Long id) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Absence not found"));
        return mapToResponseDTO(absence);
    }

    public List<AbsenceResponseDTO> getAbsencesByStudent(Long studentId) {
        return absenceRepository.findByStudentId(studentId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AbsenceResponseDTO> getAbsencesByStudentAndDateRange(
            Long studentId,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        return absenceRepository.findByStudentIdAndDateBetween(studentId, startDate, endDate).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AbsenceResponseDTO> getAbsencesByClasse(Long classeId) {
        return absenceRepository.findByStudentClasseId(classeId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public AbsenceResponseDTO updateAbsence(Long id, AbsenceDTO absenceDTO) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Absence not found"));

        // Mettre à jour l'absence
        absence.setDate(absenceDTO.getDate());
        absence.setReason(absenceDTO.getReason());

        absence = absenceRepository.save(absence);
        return mapToResponseDTO(absence);
    }

    public void deleteAbsence(Long id) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Absence not found"));
        absenceRepository.delete(absence);
    }

    private AbsenceResponseDTO mapToResponseDTO(Absence absence) {
        return AbsenceResponseDTO.builder()
                .id(absence.getId())
                .studentName(absence.getStudent().getUser().getFirstname() + " " +
                        absence.getStudent().getUser().getLastname())
                .studentMatricule(absence.getStudent().getMatricule())
                .classeName(absence.getStudent().getClasse().getName())
                .date(absence.getDate())
                .reason(absence.getReason())
                .build();
    }
}
