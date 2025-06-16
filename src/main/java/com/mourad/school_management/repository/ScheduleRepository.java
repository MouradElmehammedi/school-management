package com.mourad.school_management.repository;

import com.mourad.school_management.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByClasseId(Long classeId);
    List<Schedule> findByTeacherId(Long teacherId);
    List<Schedule> findByClasseIdAndDayOfWeek(Long classeId, DayOfWeek dayOfWeek);
    List<Schedule> findByTeacherIdAndDayOfWeek(Long teacherId, DayOfWeek dayOfWeek);
}
