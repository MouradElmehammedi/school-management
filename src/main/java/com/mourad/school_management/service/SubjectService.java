package com.mourad.school_management.service;


import com.mourad.school_management.dto.*;
import com.mourad.school_management.entity.Subject;
import com.mourad.school_management.entity.Teacher;
import com.mourad.school_management.repository.SubjectRepository;
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
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    public SubjectResponseDTO createSubject(SubjectDTO subjectDTO) {
        // Vérifier si le nom de la matière existe déjà
        if (subjectRepository.findByName(subjectDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Subject name already exists");
        }

        // Récupérer les professeurs
        List<Teacher> teachers = subjectDTO.getTeacherIds().stream()
                .map(teacherId -> teacherRepository.findById(teacherId)
                        .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + teacherId)))
                .collect(Collectors.toList());

        // Créer la matière
        Subject subject = Subject.builder()
                .name(subjectDTO.getName())
                .coefficient(subjectDTO.getCoefficient())
                .teachers(teachers)
                .build();

        subject = subjectRepository.save(subject);
        return mapToResponseDTO(subject);
    }

    public SubjectResponseDTO getSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));
        return mapToResponseDTO(subject);
    }

    public List<SubjectResponseDTO> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public SubjectResponseDTO updateSubject(Long id, SubjectDTO subjectDTO) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));

        // Vérifier si le nouveau nom existe déjà (si le nom a changé)
        if (!subject.getName().equals(subjectDTO.getName()) &&
                subjectRepository.findByName(subjectDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Subject name already exists");
        }

        // Mettre à jour les informations de base
        subject.setName(subjectDTO.getName());
        subject.setCoefficient(subjectDTO.getCoefficient());

        // Mettre à jour les professeurs
        List<Teacher> teachers = subjectDTO.getTeacherIds().stream()
                .map(teacherId -> teacherRepository.findById(teacherId)
                        .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + teacherId)))
                .collect(Collectors.toList());
        subject.setTeachers(teachers);

        subject = subjectRepository.save(subject);
        return mapToResponseDTO(subject);
    }

    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));


        subjectRepository.delete(subject);
    }

    private SubjectResponseDTO mapToResponseDTO(Subject subject) {
        return SubjectResponseDTO.builder()
                .id(subject.getId())
                .name(subject.getName())
                .coefficient(subject.getCoefficient())
                .teachers(subject.getTeachers().stream()
                        .map(teacher -> TeacherDTO.builder()
                                .id(teacher.getId())
                                .email(teacher.getUser().getEmail())
                                .firstname(teacher.getUser().getFirstname())
                                .lastname(teacher.getUser().getLastname())
                                .build())
                        .collect(Collectors.toList()))

                .build();
    }
}
