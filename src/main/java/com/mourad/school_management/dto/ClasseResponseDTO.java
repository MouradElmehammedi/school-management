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
public class ClasseResponseDTO {
    private Long id;
    private String name;
    private String level;
    private TeacherDTO mainTeacher;
    private List<StudentDTO> students;
    private List<ScheduleDTO> schedules;
}
