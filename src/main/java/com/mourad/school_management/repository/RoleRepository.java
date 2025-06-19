package com.mourad.school_management.repository;

import com.mourad.school_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByName(Role.RoleName name);
}
