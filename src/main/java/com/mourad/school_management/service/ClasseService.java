package com.mourad.school_management.service;


import com.mourad.school_management.dto.*;
import com.mourad.school_management.entity.Classe;
import com.mourad.school_management.entity.Teacher;
import com.mourad.school_management.repository.ClasseRepository;
import com.mourad.school_management.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClasseService {
    private final ClasseRepository classeRepository;
    private final TeacherRepository teacherRepository;

    public ClasseResponseDTO createClasse(ClasseDTO classeDTO) {
        // Vérifier si le nom de la classe existe déjà
        if (classeRepository.findByName(classeDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Class name already exists");
        }

        // Récupérer le professeur principal
        Teacher mainTeacher = teacherRepository.findById(classeDTO.getMainTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        // Créer la classe
        Classe classe = Classe.builder()
                .name(classeDTO.getName())
                .level(classeDTO.getLevel())
                .teacher(mainTeacher)
                .build();

        classe = classeRepository.save(classe);
        return mapToResponseDTO(classe);
    }

    public ClasseResponseDTO getClasse(Long id) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Class not found"));
        return mapToResponseDTO(classe);
    }

    public List<ClasseResponseDTO> getAllClasses() {
        return classeRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ClasseResponseDTO> getClassesByLevel(String level) {
        return classeRepository.findByLevel(level).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ClasseResponseDTO> getClassesByTeacher(Long teacherId) {
        return classeRepository.findByTeacherId(teacherId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ClasseResponseDTO updateClasse(Long id, ClasseDTO classeDTO) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Class not found"));

        // Vérifier si le nouveau nom existe déjà (si le nom a changé)
        if (!classe.getName().equals(classeDTO.getName()) &&
                classeRepository.findByName(classeDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Class name already exists");
        }

        // Mettre à jour les informations de base
        classe.setName(classeDTO.getName());
        classe.setLevel(classeDTO.getLevel());

        // Mettre à jour le professeur principal si nécessaire
        if (!classe.getTeacher().getId().equals(classeDTO.getMainTeacherId())) {
            Teacher mainTeacher = teacherRepository.findById(classeDTO.getMainTeacherId())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
            classe.setTeacher(mainTeacher);
        }

        classe = classeRepository.save(classe);
        return mapToResponseDTO(classe);
    }

    public void deleteClasse(Long id) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Class not found"));

        // Vérifier si la classe a des étudiants
        if (!classe.getStudents().isEmpty()) {
            throw new IllegalStateException("Cannot delete class with associated students");
        }

        classeRepository.delete(classe);
    }

    private ClasseResponseDTO mapToResponseDTO(Classe classe) {
        return ClasseResponseDTO.builder()
                .id(classe.getId())
                .name(classe.getName())
                .level(classe.getLevel())
                .mainTeacher(TeacherDTO.builder()
                        .id(classe.getTeacher().getId())
                        .email(classe.getTeacher().getUser().getEmail())
                        .firstname(classe.getTeacher().getUser().getFirstname())
                        .lastname(classe.getTeacher().getUser().getLastname())
                        .build())
                .students(classe.getStudents().stream()
                        .map(student -> StudentDTO.builder()
                                .id(student.getId())
                                .matricule(student.getMatricule())
                                .email(student.getUser().getEmail())
                                .firstname(student.getUser().getFirstname())
                                .lastname(student.getUser().getLastname())
                                .build())
                        .collect(Collectors.toList()))
                .schedules(classe.getSchedules().stream()
                        .map(schedule -> ScheduleDTO.builder()
                                .id(schedule.getId())
                                .dayOfWeek(schedule.getDayOfWeek())
                                .startTime(schedule.getStartTime())
                                .endTime(schedule.getEndTime())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}