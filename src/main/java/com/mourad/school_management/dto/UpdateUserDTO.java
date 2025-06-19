package com.mourad.school_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String address;
}
