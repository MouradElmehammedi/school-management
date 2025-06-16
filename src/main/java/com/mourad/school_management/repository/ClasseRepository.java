package com.mourad.school_management.repository;

import com.mourad.school_management.entity.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {
    Optional<Classe> findByName(String name);
    List<Classe> findByLevel(String level);
    List<Classe> findByTeacherId(Long teacherId);
    List<Classe> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
}
