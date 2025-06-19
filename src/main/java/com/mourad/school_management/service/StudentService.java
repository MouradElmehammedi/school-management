package com.mourad.school_management.service;

import com.mourad.school_management.dto.AbsenceDTO;
import com.mourad.school_management.dto.NoteDTO;
import com.mourad.school_management.dto.StudentDTO;
import com.mourad.school_management.dto.StudentResponseDTO;
import com.mourad.school_management.entity.*;
import com.mourad.school_management.repository.*;
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
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ClasseRepository classeRepository;
    private final ParentRepository parentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentResponseDTO createStudent(StudentDTO studentDTO) {
        // Vérifier si le matricule existe déjà
        if (studentRepository.findByMatricule(studentDTO.getMatricule()).isPresent()) {
            throw new IllegalArgumentException("Matricule already exists");
        }

        // Récupérer le rôle STUDENT
        List<Role> roles = new ArrayList<>();
        List<Role> teacherRoles = roleRepository.findByName(Role.RoleName.ROLE_STUDENT);
        if (teacherRoles.isEmpty()) {
            throw new EntityNotFoundException("Role STUDENT not found");
        }
        roles.add(teacherRoles.get(0));

        // Créer l'utilisateur
        User user = User.builder()
                .email(studentDTO.getEmail())
                .firstname(studentDTO.getFirstname())
                .lastname(studentDTO.getLastname())
                .address(studentDTO.getAddress())
                .phoneNumber(studentDTO.getPhoneNumber())
                .password(passwordEncoder.encode("defaultPassword"))
                .roles(roles)
                .build();
        user = userRepository.save(user);

        // Récupérer la classe et le parent
        Classe classe = classeRepository.findById(studentDTO.getClasseId())
                .orElseThrow(() -> new EntityNotFoundException("Classe not found"));
        Parent parent = parentRepository.findById(studentDTO.getParentId())
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        // Créer l'étudiant
        Student student = Student.builder()
                .matricule(studentDTO.getMatricule())
                .user(user)
                .classe(classe)
                .parent(parent)
                .build();

        student = studentRepository.save(student);
        return mapToResponseDTO(student);
    }

    public StudentResponseDTO getStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return mapToResponseDTO(student);
    }

    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StudentResponseDTO> getStudentsByClasse(Long classeId) {
        return studentRepository.findByClasseId(classeId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public StudentResponseDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        // Mettre à jour les informations de l'utilisateur
        User user = student.getUser();
        user.setEmail(studentDTO.getEmail());
        user.setFirstname(studentDTO.getFirstname());
        user.setLastname(studentDTO.getLastname());
        user.setAddress(studentDTO.getAddress());
        user.setPhoneNumber(studentDTO.getPhoneNumber());
        userRepository.save(user);

        // Mettre à jour la classe et le parent si nécessaire
        if (!student.getClasse().getId().equals(studentDTO.getClasseId())) {
            Classe classe = classeRepository.findById(studentDTO.getClasseId())
                    .orElseThrow(() -> new EntityNotFoundException("Classe not found"));
            student.setClasse(classe);
        }

        if (!student.getParent().getId().equals(studentDTO.getParentId())) {
            Parent parent = parentRepository.findById(studentDTO.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent not found"));
            student.setParent(parent);
        }

        student.setMatricule(studentDTO.getMatricule());
        student = studentRepository.save(student);
        return mapToResponseDTO(student);
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        studentRepository.delete(student);
        userRepository.delete(student.getUser());
    }

    private StudentResponseDTO mapToResponseDTO(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .matricule(student.getMatricule())
                .email(student.getUser().getEmail())
                .firstname(student.getUser().getFirstname())
                .lastname(student.getUser().getLastname())
                .address(student.getUser().getAddress())
                .phoneNumber(student.getUser().getPhoneNumber())
                .classeName(student.getClasse().getName())
                .parentName(student.getParent().getUser().getFirstname() + " " +
                        student.getParent().getUser().getLastname())
                .notes(student.getNotes().stream()
                        .map(note -> NoteDTO.builder()
                                .id(note.getId())
                                .value(note.getValue())
                                .build())
                        .collect(Collectors.toList()))
                .absences(student.getAbsences().stream()
                        .map(absence -> AbsenceDTO.builder()
                                .id(absence.getId())
                                .date(absence.getDate())
                                .reason(absence.getReason())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}