package com.mourad.school_management.service;


import com.mourad.school_management.dto.TeacherDTO;
import com.mourad.school_management.dto.TeacherResponseDTO;
import com.mourad.school_management.entity.Role;
import com.mourad.school_management.entity.Subject;
import com.mourad.school_management.entity.Teacher;
import com.mourad.school_management.entity.User;
import com.mourad.school_management.repository.ClasseRepository;
import com.mourad.school_management.repository.SubjectRepository;
import com.mourad.school_management.repository.TeacherRepository;
import com.mourad.school_management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final ClasseRepository classeRepository;

    public TeacherResponseDTO createTeacher(TeacherDTO teacherDTO) {
        // Créer l'utilisateur
        User user = User.builder()
                .email(teacherDTO.getEmail())
                .firstname(teacherDTO.getFirstname())
                .lastname(teacherDTO.getLastname())
                .role(Role.TEACHER)
                .build();
        user = userRepository.save(user);

        // Récupérer les matières
        List<Subject> subjects = teacherDTO.getSubjectIds().stream()
                .map(subjectId -> subjectRepository.findById(subjectId)
                        .orElseThrow(() -> new EntityNotFoundException("Subject not found with id: " + subjectId)))
                .collect(Collectors.toList());

        // Créer le professeur
        Teacher teacher = Teacher.builder()
                .user(user)
                .subjects(subjects)
                .build();

        teacher = teacherRepository.save(teacher);
        return mapToResponseDTO(teacher);
    }

    public TeacherResponseDTO getTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
        return mapToResponseDTO(teacher);
    }

    public List<TeacherResponseDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TeacherResponseDTO> getTeachersBySubject(Long subjectId) {
        return teacherRepository.findBySubjectsId(subjectId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public TeacherResponseDTO updateTeacher(Long id, TeacherDTO teacherDTO) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        // Mettre à jour les informations de l'utilisateur
        User user = teacher.getUser();
        user.setEmail(teacherDTO.getEmail());
        user.setFirstname(teacherDTO.getFirstname());
        user.setLastname(teacherDTO.getLastname());
        userRepository.save(user);

        // Mettre à jour les matières
        List<Subject> subjects = teacherDTO.getSubjectIds().stream()
                .map(subjectId -> subjectRepository.findById(subjectId)
                        .orElseThrow(() -> new EntityNotFoundException("Subject not found with id: " + subjectId)))
                .collect(Collectors.toList());
        teacher.setSubjects(subjects);

        teacher = teacherRepository.save(teacher);
        return mapToResponseDTO(teacher);
    }

    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
        teacherRepository.delete(teacher);
        userRepository.delete(teacher.getUser());
    }

    private TeacherResponseDTO mapToResponseDTO(Teacher teacher) {
        return TeacherResponseDTO.builder()
                .id(teacher.getId())
                .email(teacher.getUser().getEmail())
                .firstname(teacher.getUser().getFirstname())
                .lastname(teacher.getUser().getLastname())
                .subjects(teacher.getSubjects().stream()
                        .map(subject -> SubjectDTO.builder()
                                .id(subject.getId())
                                .name(subject.getName())
                                .coefficient(subject.getCoefficient())
                                .build())
                        .collect(Collectors.toList()))
                .schedules(teacher.getSchedules().stream()
                        .map(schedule -> ScheduleDTO.builder()
                                .id(schedule.getId())
                                .classeName(schedule.getClasse().getName())
                                .subjectName(schedule.getSubject().getName())
                                .dayOfWeek(schedule.getDayOfWeek())
                                .startTime(schedule.getStartTime())
                                .endTime(schedule.getEndTime())
                                .build())
                        .collect(Collectors.toList()))
                .mainClasses(teacher.getMainClasses().stream()
                        .map(classe -> ClasseDTO.builder()
                                .id(classe.getId())
                                .name(classe.getName())
                                .level(classe.getLevel())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
