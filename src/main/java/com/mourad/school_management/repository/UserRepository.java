package com.mourad.school_management.repository;

import com.mourad.school_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByEnabled(Boolean enabled);
    List<User> findByRolesName(com.mourad.school_management.entity.Role.RoleName roleName);
}
