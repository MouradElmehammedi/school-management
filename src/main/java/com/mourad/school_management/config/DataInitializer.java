package com.mourad.school_management.config;

import com.mourad.school_management.entity.Role;
import com.mourad.school_management.entity.User;
import com.mourad.school_management.repository.RoleRepository;
import com.mourad.school_management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Créer les rôles par défaut en premier
        createDefaultRolesIfNotExist();
        
        // Créer les utilisateurs par défaut
        createDefaultUsersIfNotExist();
    }

    private void createDefaultRolesIfNotExist() {
        // Créer ROLE_ADMIN
        if (roleRepository.findByName(Role.RoleName.ROLE_ADMIN).isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName(Role.RoleName.ROLE_ADMIN);
            roleRepository.save(adminRole);
            System.out.println("Role ROLE_ADMIN created successfully");
        }

        // Créer ROLE_USER
        if (roleRepository.findByName(Role.RoleName.ROLE_USER).isEmpty()) {
            Role userRole = new Role();
            userRole.setName(Role.RoleName.ROLE_USER);
            roleRepository.save(userRole);
            System.out.println("Role ROLE_USER created successfully");
        }

        // Créer ROLE_TEACHER
        if (roleRepository.findByName(Role.RoleName.ROLE_TEACHER).isEmpty()) {
            Role teacherRole = new Role();
            teacherRole.setName(Role.RoleName.ROLE_TEACHER);
            roleRepository.save(teacherRole);
            System.out.println("Role ROLE_TEACHER created successfully");
        }

        // Créer ROLE_STUDENT
        if (roleRepository.findByName(Role.RoleName.ROLE_STUDENT).isEmpty()) {
            Role studentRole = new Role();
            studentRole.setName(Role.RoleName.ROLE_STUDENT);
            roleRepository.save(studentRole);
            System.out.println("Role ROLE_STUDENT created successfully");
        }

        // Créer ROLE_PARENT
        if (roleRepository.findByName(Role.RoleName.ROLE_PARENT).isEmpty()) {
            Role parentRole = new Role();
            parentRole.setName(Role.RoleName.ROLE_PARENT);
            roleRepository.save(parentRole);
            System.out.println("Role ROLE_PARENT created successfully");
        }
    }

    private void createDefaultUsersIfNotExist() {
        // Créer l'administrateur
        if (userRepository.findByEmail("admin@school.com").isEmpty()) {
            List<Role> roles = new ArrayList<>();
            List<Role> adminRoles = roleRepository.findByName(Role.RoleName.ROLE_ADMIN);
            if (adminRoles.isEmpty()) {
                throw new EntityNotFoundException("Role ADMIN not found");
            }
            roles.add(adminRoles.get(0));
            
            User admin = new User();
            admin.setFirstname("Admin");
            admin.setLastname("System");
            admin.setEmail("admin@school.com");
            admin.setPhoneNumber("123456789");
            admin.setAddress("Address City");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            admin.setRoles(roles);

            userRepository.save(admin);
            System.out.println("Admin user created successfully");
        }

        // Créer l'étudiant
        if (userRepository.findByEmail("student@school.com").isEmpty()) {
            List<Role> roles = new ArrayList<>();
            List<Role> studentRoles = roleRepository.findByName(Role.RoleName.ROLE_STUDENT);
            if (studentRoles.isEmpty()) {
                throw new EntityNotFoundException("Role STUDENT not found");
            }
            roles.add(studentRoles.get(0));
            
            User student = new User();
            student.setFirstname("John");
            student.setLastname("Student");
            student.setEmail("student@school.com");
            student.setPhoneNumber("123456789");
            student.setAddress("Address City");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setEnabled(true);
            student.setRoles(roles);

            userRepository.save(student);
            System.out.println("Student user created successfully");
        }

        // Créer le parent
        if (userRepository.findByEmail("parent@school.com").isEmpty()) {
            List<Role> roles = new ArrayList<>();
            List<Role> parentRoles = roleRepository.findByName(Role.RoleName.ROLE_PARENT);
            if (parentRoles.isEmpty()) {
                throw new EntityNotFoundException("Role PARENT not found");
            }
            roles.add(parentRoles.get(0));
            
            User parent = new User();
            parent.setFirstname("Jane");
            parent.setLastname("Parent");
            parent.setEmail("parent@school.com");
            parent.setPhoneNumber("123456789");
            parent.setAddress("Address City");
            parent.setPassword(passwordEncoder.encode("parent123"));
            parent.setEnabled(true);
            parent.setRoles(roles);

            userRepository.save(parent);
            System.out.println("Parent user created successfully");
        }

        // Créer l'enseignant
        if (userRepository.findByEmail("teacher@school.com").isEmpty()) {
            List<Role> roles = new ArrayList<>();
            List<Role> teacherRoles = roleRepository.findByName(Role.RoleName.ROLE_TEACHER);
            if (teacherRoles.isEmpty()) {
                throw new EntityNotFoundException("Role TEACHER not found");
            }
            roles.add(teacherRoles.get(0));
            
            User teacher = new User();
            teacher.setFirstname("Robert");
            teacher.setLastname("Teacher");
            teacher.setEmail("teacher@school.com");
            teacher.setPhoneNumber("123456789");
            teacher.setAddress("Address City");
            teacher.setPassword(passwordEncoder.encode("teacher123"));
            teacher.setEnabled(true);
            teacher.setRoles(roles);

            userRepository.save(teacher);
            System.out.println("Teacher user created successfully");
        }

        // Créer un utilisateur standard
        if (userRepository.findByEmail("user@school.com").isEmpty()) {
            List<Role> roles = new ArrayList<>();
            List<Role> userRoles = roleRepository.findByName(Role.RoleName.ROLE_USER);
            if (userRoles.isEmpty()) {
                throw new EntityNotFoundException("Role USER not found");
            }
            roles.add(userRoles.get(0));
            
            User user = new User();
            user.setFirstname("Standard");
            user.setLastname("User");
            user.setEmail("user@school.com");
            user.setPhoneNumber("123456789");
            user.setAddress("Address City");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEnabled(true);
            user.setRoles(roles);

            userRepository.save(user);
            System.out.println("Standard user created successfully");
        }
    }
}
