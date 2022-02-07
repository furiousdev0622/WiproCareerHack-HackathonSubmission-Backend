package com.devtyagi.userservice.service;

import com.devtyagi.userservice.dao.Role;
import com.devtyagi.userservice.exception.InvalidRoleException;
import com.devtyagi.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * This method is used to fetch the Role from database by passing the Role ID.
     * @param id Role ID
     * @return Role
     */
    public Role getRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(InvalidRoleException::new);
    }

    /**
     * This method is used to fetch the Role from database by passing the Role Name.
     * @param roleName Role Name
     * @return Role
     */
    public Role getRoleByRoleName(String roleName) {
        val role = roleRepository.findByRoleName(roleName);
        if(role == null) throw new InvalidRoleException();
        return role;
    }

}
