package com.mourad.school_management.config;


import com.mourad.school_management.entity.Role;
import com.mourad.school_management.entity.User;
import com.mourad.school_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Créer les utilisateurs par défaut s'ils n'existent pas
        createDefaultUsersIfNotExist();
    }

    private void createDefaultUsersIfNotExist() {
        // Créer l'administrateur
        if (userRepository.findByEmail("admin@school.com").isEmpty()) {
            User admin = new User();
            admin.setFirstname("Admin");
            admin.setLastname("System");
            admin.setEmail("admin@school.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
        }

        // Créer l'étudiant
        if (userRepository.findByEmail("student@school.com").isEmpty()) {
            User student = new User();
            student.setFirstname("John");
            student.setLastname("Student");
            student.setEmail("student@school.com");
            student.setPassword(passwordEncoder.encode("student123"));

            student.setEnabled(true);
            student.setRole(Role.STUDENT);

            userRepository.save(student);
        }

        // Créer le parent
        if (userRepository.findByEmail("parent@school.com").isEmpty()) {
            User parent = new User();
            parent.setFirstname("Jane");
            parent.setLastname("Parent");
            parent.setEmail("parent@school.com");
            parent.setPassword(passwordEncoder.encode("parent123"));

            parent.setEnabled(true);
            parent.setRole(Role.PARENT);

            userRepository.save(parent);
        }

        // Créer l'enseignant
        if (userRepository.findByEmail("teacher@school.com").isEmpty()) {
            User teacher = new User();
            teacher.setFirstname("Robert");
            teacher.setLastname("Teacher");
            teacher.setEmail("teacher@school.com");
            teacher.setPassword(passwordEncoder.encode("teacher123"));

            teacher.setEnabled(true);
            teacher.setRole(Role.TEACHER);

            userRepository.save(teacher);
        }
    }
}
