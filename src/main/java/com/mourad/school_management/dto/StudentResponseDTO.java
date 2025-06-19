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
public class StudentResponseDTO {
    private Long id;
    private String matricule;
    private String email;
    private String firstname;
    private String lastname;
    private String classeName;
    private String parentName;
    private String phoneNumber;
    private String address;
    private List<NoteDTO> notes;
    private List<AbsenceDTO> absences;
}
