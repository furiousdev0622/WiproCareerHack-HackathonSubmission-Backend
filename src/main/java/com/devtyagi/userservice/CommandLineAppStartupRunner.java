package com.devtyagi.userservice;

import com.devtyagi.userservice.dao.User;
import com.devtyagi.userservice.enums.UserStatus;
import com.devtyagi.userservice.service.RoleService;
import com.devtyagi.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final UserService userService;

    private final RoleService roleService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if(userService.adminExists(adminEmail)) return;
        val role = roleService.getRoleByRoleName("LEVEL3");
        val user = User.builder()
                .username("admin")
                .emailAddress(adminEmail)
                .fullName("Admin User")
                .role(role)
                .password(passwordEncoder.encode(adminPassword))
                .status(UserStatus.ACTIVE)
                .build();
        userService.save(user);
    }

}
