package com.mourad.school_management.repository;

import com.mourad.school_management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByMatricule(String matricule);
    List<Student> findByClasseId(Long classeId);
    List<Student> findByParentId(Long parentId);
    Optional<Student> findByUserId(Long userId);
}
