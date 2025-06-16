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
public class TeacherResponseDTO {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private List<SubjectDTO> subjects;
    private List<ScheduleDTO> schedules;
    private List<ClasseDTO> mainClasses;
}