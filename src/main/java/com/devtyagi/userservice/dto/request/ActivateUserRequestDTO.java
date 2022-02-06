package com.devtyagi.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateUserRequestDTO {

    @NotBlank(message = "Invite Code can not be empty")
    private String invitationCode;

    @NotBlank(message = "Password can not be empty")
    private String password;

}
