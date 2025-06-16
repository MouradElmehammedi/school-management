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
public class NoteDTO {
    private Long id;
    private Double value;
    private String subjectName;
    private Term term;
}