package com.mourad.school_management.dto;

import com.mourad.school_management.entity.Term;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponseDTO {
    private Long id;
    private String studentName;
    private String studentMatricule;
    private String subjectName;
    private Double value;
    private Term term;
    private String comment;
    private Double coefficient;
}
