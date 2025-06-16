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
public class ParentResponseDTO {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String address;
    private List<StudentDTO> children;
}
