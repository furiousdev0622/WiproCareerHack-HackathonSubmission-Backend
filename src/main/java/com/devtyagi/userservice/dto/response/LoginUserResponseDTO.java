package com.devtyagi.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserResponseDTO {

    private String userId;
    private String username;
    private String emailAddress;
    private String accessToken;

}
