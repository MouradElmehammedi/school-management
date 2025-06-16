package com.mourad.school_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String matricule;
    private String email;
    private String firstname;
    private String lastname;
    private Long classeId;
    private Long parentId;
}