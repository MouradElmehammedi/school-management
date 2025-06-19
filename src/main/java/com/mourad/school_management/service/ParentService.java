package com.mourad.school_management.service;


import com.mourad.school_management.dto.ParentDTO;
import com.mourad.school_management.dto.ParentResponseDTO;
import com.mourad.school_management.dto.StudentDTO;
import com.mourad.school_management.entity.Parent;
import com.mourad.school_management.entity.Role;
import com.mourad.school_management.entity.User;
import com.mourad.school_management.repository.ParentRepository;
import com.mourad.school_management.repository.RoleRepository;
import com.mourad.school_management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ParentService {
    private final ParentRepository parentRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public ParentResponseDTO createParent(ParentDTO parentDTO) {
        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(parentDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        List<Role> roles = new ArrayList<>();
        List<Role> teacherRoles = roleRepository.findByName(Role.RoleName.ROLE_PARENT);
        if (teacherRoles.isEmpty()) {
            throw new EntityNotFoundException("Role PARENT not found");
        }
        roles.add(teacherRoles.get(0));

        // Créer l'utilisateur
        User user = User.builder()
                .email(parentDTO.getEmail())
                .firstname(parentDTO.getFirstname())
                .lastname(parentDTO.getLastname())
                .phoneNumber(parentDTO.getPhoneNumber())
                .address(parentDTO.getAddress())
                .roles(roles)
                .build();
        user = userRepository.save(user);

        // Créer le parent
        Parent parent = Parent.builder()
                .user(user)
                .build();

        parent = parentRepository.save(parent);
        return mapToResponseDTO(parent);
    }

    public ParentResponseDTO getParent(Long id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));
        return mapToResponseDTO(parent);
    }

    public ParentResponseDTO getParentByUserId(Long userId) {
        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));
        return mapToResponseDTO(parent);
    }

    public List<ParentResponseDTO> getAllParents() {
        return parentRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ParentResponseDTO updateParent(Long id, ParentDTO parentDTO) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        // Mettre à jour les informations de l'utilisateur
        User user = parent.getUser();
        user.setEmail(parentDTO.getEmail());
        user.setFirstname(parentDTO.getFirstname());
        user.setLastname(parentDTO.getLastname());
        user.setPhoneNumber(parentDTO.getPhoneNumber());
        user.setAddress(parentDTO.getAddress());
        userRepository.save(user);

        parent = parentRepository.save(parent);
        return mapToResponseDTO(parent);
    }

    public void deleteParent(Long id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parent not found"));

        // Vérifier si le parent a des enfants
        if (!parent.getChildren().isEmpty()) {
            throw new IllegalStateException("Cannot delete parent with associated children");
        }

        parentRepository.delete(parent);
        userRepository.delete(parent.getUser());
    }

    private ParentResponseDTO mapToResponseDTO(Parent parent) {
        return ParentResponseDTO.builder()
                .id(parent.getId())
                .email(parent.getUser().getEmail())
                .firstname(parent.getUser().getFirstname())
                .lastname(parent.getUser().getLastname())
                .phoneNumber(parent.getUser().getPhoneNumber())
                .address(parent.getUser().getAddress())
                .children(parent.getChildren().stream()
                        .map(student -> StudentDTO.builder()
                                .id(student.getId())
                                .matricule(student.getMatricule())
                                .email(student.getUser().getEmail())
                                .firstname(student.getUser().getFirstname())
                                .lastname(student.getUser().getLastname())
                                .classeId(student.getClasse().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
