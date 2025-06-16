package com.mourad.school_management.service;


import com.mourad.school_management.dto.ScheduleDTO;
import com.mourad.school_management.dto.ScheduleResponseDTO;
import com.mourad.school_management.entity.Classe;
import com.mourad.school_management.entity.Schedule;
import com.mourad.school_management.entity.Subject;
import com.mourad.school_management.entity.Teacher;
import com.mourad.school_management.repository.ClasseRepository;
import com.mourad.school_management.repository.ScheduleRepository;
import com.mourad.school_management.repository.SubjectRepository;
import com.mourad.school_management.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ClasseRepository classeRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    public ScheduleResponseDTO createSchedule(ScheduleDTO scheduleDTO) {
        // Vérifier si la classe existe
        Classe classe = classeRepository.findById(scheduleDTO.getClasseId())
                .orElseThrow(() -> new EntityNotFoundException("Classe not found"));

        // Vérifier si la matière existe
        Subject subject = subjectRepository.findById(scheduleDTO.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));

        // Vérifier si le professeur existe
        Teacher teacher = teacherRepository.findById(scheduleDTO.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        // Vérifier les conflits d'horaires
        if (hasScheduleConflict(classe, scheduleDTO.getDayOfWeek(),
                scheduleDTO.getStartTime(), scheduleDTO.getEndTime())) {
            throw new IllegalArgumentException("Schedule conflict for the class");
        }

        if (hasTeacherConflict(teacher, scheduleDTO.getDayOfWeek(),
                scheduleDTO.getStartTime(), scheduleDTO.getEndTime())) {
            throw new IllegalArgumentException("Schedule conflict for the teacher");
        }

        // Créer l'emploi du temps
        Schedule schedule = Schedule.builder()
                .classe(classe)
                .subject(subject)
                .teacher(teacher)
                .dayOfWeek(scheduleDTO.getDayOfWeek())
                .startTime(scheduleDTO.getStartTime())
                .endTime(scheduleDTO.getEndTime())
                .room(scheduleDTO.getRoom())
                .build();

        schedule = scheduleRepository.save(schedule);
        return mapToResponseDTO(schedule);
    }

    public ScheduleResponseDTO getSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        return mapToResponseDTO(schedule);
    }

    public List<ScheduleResponseDTO> getSchedulesByClasse(Long classeId) {
        return scheduleRepository.findByClasseId(classeId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleResponseDTO> getSchedulesByTeacher(Long teacherId) {
        return scheduleRepository.findByTeacherId(teacherId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleResponseDTO> getSchedulesByClasseAndDay(Long classeId, DayOfWeek dayOfWeek) {
        return scheduleRepository.findByClasseIdAndDayOfWeek(classeId, dayOfWeek).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ScheduleResponseDTO updateSchedule(Long id, ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));

        // Vérifier les conflits d'horaires
        if (hasScheduleConflict(schedule.getClasse(), scheduleDTO.getDayOfWeek(),
                scheduleDTO.getStartTime(), scheduleDTO.getEndTime(), id)) {
            throw new IllegalArgumentException("Schedule conflict for the class");
        }

        if (hasTeacherConflict(schedule.getTeacher(), scheduleDTO.getDayOfWeek(),
                scheduleDTO.getStartTime(), scheduleDTO.getEndTime(), id)) {
            throw new IllegalArgumentException("Schedule conflict for the teacher");
        }

        // Mettre à jour l'emploi du temps
        schedule.setDayOfWeek(scheduleDTO.getDayOfWeek());
        schedule.setStartTime(scheduleDTO.getStartTime());
        schedule.setEndTime(scheduleDTO.getEndTime());
        schedule.setRoom(scheduleDTO.getRoom());

        schedule = scheduleRepository.save(schedule);
        return mapToResponseDTO(schedule);
    }

    public void deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        scheduleRepository.delete(schedule);
    }

    private boolean hasScheduleConflict(Classe classe, DayOfWeek dayOfWeek,
                                        LocalTime startTime, LocalTime endTime) {
        return hasScheduleConflict(classe, dayOfWeek, startTime, endTime, null);
    }

    private boolean hasScheduleConflict(Classe classe, DayOfWeek dayOfWeek,
                                        LocalTime startTime, LocalTime endTime, Long excludeScheduleId) {
        List<Schedule> existingSchedules = scheduleRepository.findByClasseIdAndDayOfWeek(
                classe.getId(), dayOfWeek);

        return existingSchedules.stream()
                .filter(schedule -> !schedule.getId().equals(excludeScheduleId))
                .anyMatch(schedule ->
                        (startTime.isBefore(schedule.getEndTime()) &&
                                endTime.isAfter(schedule.getStartTime())));
    }

    private boolean hasTeacherConflict(Teacher teacher, DayOfWeek dayOfWeek,
                                       LocalTime startTime, LocalTime endTime) {
        return hasTeacherConflict(teacher, dayOfWeek, startTime, endTime, null);
    }

    private boolean hasTeacherConflict(Teacher teacher, DayOfWeek dayOfWeek,
                                       LocalTime startTime, LocalTime endTime, Long excludeScheduleId) {
        List<Schedule> existingSchedules = scheduleRepository.findByTeacherIdAndDayOfWeek(
                teacher.getId(), dayOfWeek);

        return existingSchedules.stream()
                .filter(schedule -> !schedule.getId().equals(excludeScheduleId))
                .anyMatch(schedule ->
                        (startTime.isBefore(schedule.getEndTime()) &&
                                endTime.isAfter(schedule.getStartTime())));
    }

    private ScheduleResponseDTO mapToResponseDTO(Schedule schedule) {
        return ScheduleResponseDTO.builder()
                .id(schedule.getId())
                .classeName(schedule.getClasse().getName())
                .subjectName(schedule.getSubject().getName())
                .teacherName(schedule.getTeacher().getUser().getFirstname() + " " +
                        schedule.getTeacher().getUser().getLastname())
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .room(schedule.getRoom())
                .build();
    }
}
