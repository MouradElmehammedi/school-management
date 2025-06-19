package com.mourad.school_management.dto;

import com.mourad.school_management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String address;
    private List<Role> roles;
}

