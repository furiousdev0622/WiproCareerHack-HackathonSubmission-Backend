package com.devtyagi.userservice.repository;

import com.devtyagi.userservice.dao.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(String roleName);

}
