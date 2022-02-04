package com.devtyagi.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDTO {

    private String username;
    private String fullName;
    private String emailAddress;
    private String role;

}
