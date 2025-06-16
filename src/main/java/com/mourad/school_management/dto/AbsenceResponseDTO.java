package com.mourad.school_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbsenceResponseDTO {
    private Long id;
    private String studentName;
    private String studentMatricule;
    private String classeName;
    private LocalDateTime date;
    private String reason;
    private String createdBy;
    private LocalDateTime createdAt;
}
