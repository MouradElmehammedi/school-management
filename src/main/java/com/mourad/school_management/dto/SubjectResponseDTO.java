package com.mourad.school_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponseDTO {
    private Long id;
    private String name;
    private Double coefficient;
    private List<TeacherDTO> teachers;
    private List<ScheduleDTO> schedules;
    private List<NoteDTO> notes;
}
