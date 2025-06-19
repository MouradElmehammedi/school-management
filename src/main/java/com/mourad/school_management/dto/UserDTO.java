package com.mourad.school_management.dto;

import com.mourad.school_management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String address;
    private Boolean enabled;
    private List<Role.RoleName> roles;
}