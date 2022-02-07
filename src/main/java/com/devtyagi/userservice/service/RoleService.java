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

    public Role getRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(InvalidRoleException::new);
    }

    public Role getRoleByRoleName(String roleName) {
        val role = roleRepository.findByRoleName(roleName);
        if(role == null) throw new InvalidRoleException();
        return role;
    }

}
