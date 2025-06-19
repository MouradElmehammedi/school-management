package com.mourad.school_management.service;

import com.mourad.school_management.dto.ScheduleDTO;
import com.mourad.school_management.dto.TeacherDTO;
import com.mourad.school_management.dto.TeacherResponseDTO;
import com.mourad.school_management.entity.Role;
import com.mourad.school_management.entity.Teacher;
import com.mourad.school_management.entity.User;
import com.mourad.school_management.repository.ClasseRepository;
import com.mourad.school_management.repository.RoleRepository;
import com.mourad.school_management.repository.SubjectRepository;
import com.mourad.school_management.repository.TeacherRepository;
import com.mourad.school_management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherResponseDTO createTeacher(TeacherDTO teacherDTO) {
        // Créer l'utilisateur avec le rôle TEACHER
        List<Role> roles = new ArrayList<>();
        List<Role> teacherRoles = roleRepository.findByName(Role.RoleName.ROLE_TEACHER);
        if (teacherRoles.isEmpty()) {
            throw new EntityNotFoundException("Role TEACHER not found");
        }
        roles.add(teacherRoles.get(0));

        User user = User.builder()
                .email(teacherDTO.getEmail())
                .firstname(teacherDTO.getFirstname())
                .lastname(teacherDTO.getLastname())
                .address(teacherDTO.getAddress())
                .phoneNumber(teacherDTO.getPhoneNumber())
                .password(passwordEncoder.encode("defaultPassword"))
                .roles(roles)
                .build();
        user = userRepository.save(user);

        // Créer le professeur
        Teacher teacher = Teacher.builder()
                .user(user)
                .specialization(teacherDTO.getSpecialization())
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
        user.setAddress(teacherDTO.getAddress());
        user.setPhoneNumber(teacherDTO.getPhoneNumber());
        teacher.setSpecialization(teacherDTO.getSpecialization());
        userRepository.save(user);

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
                .address(teacher.getUser().getAddress())
                .phoneNumber(teacher.getUser().getPhoneNumber())
                .specialization(teacher.getSpecialization())
                .schedules(teacher.getSchedules().stream()
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
