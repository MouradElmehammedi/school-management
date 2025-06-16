package com.mourad.school_management.repository;

import com.mourad.school_management.entity.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findByStudentId(Long studentId);
    List<Absence> findByStudentIdAndDateBetween(Long studentId, LocalDateTime startDate, LocalDateTime endDate);
}
