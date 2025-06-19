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
public class TeacherDTO {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private String phoneNumber;
    private String address;
    private String specialization;
    private List<Long> subjectIds;
}
