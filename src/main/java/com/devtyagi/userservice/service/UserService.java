package com.devtyagi.userservice.service;

import com.devtyagi.userservice.dao.Invitation;
import com.devtyagi.userservice.dao.User;
import com.devtyagi.userservice.dto.request.ActivateUserRequestDTO;
import com.devtyagi.userservice.dto.request.CreateUserRequestDTO;
import com.devtyagi.userservice.dto.request.LoginUserRequestDTO;
import com.devtyagi.userservice.dto.response.BaseResponseDTO;
import com.devtyagi.userservice.dto.response.CreateUserResponseDTO;
import com.devtyagi.userservice.dto.response.LoginUserResponseDTO;
import com.devtyagi.userservice.enums.UserStatus;
import com.devtyagi.userservice.exception.InactiveUserException;
import com.devtyagi.userservice.exception.InvalidCredentialsException;
import com.devtyagi.userservice.exception.InvalidInvitationException;
import com.devtyagi.userservice.repository.UserRepository;
import com.devtyagi.userservice.util.CustomUserDetails;
import com.devtyagi.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final AuthenticationManager authenticationManager;

    private final JwtUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    private final InvitationService invitationService;

    private final BCryptPasswordEncoder passwordEncoder;

    public CreateUserResponseDTO createUser(CreateUserRequestDTO createUserRequest) {
        val role = roleService.getRoleByRoleName(createUserRequest.getRole());

        val user = User.builder()
                .username(createUserRequest.getUsername())
                .fullName(createUserRequest.getFullName())
                .emailAddress(createUserRequest.getEmailAddress())
                .role(role)
                .status(UserStatus.PENDING)
                .build();

        val savedUser = save(user);

        invitationService.sendInvitationToUser(savedUser);

        return CreateUserResponseDTO.builder()
                .userId(savedUser.getUserId())
                .emailAddress(savedUser.getEmailAddress())
                .username(savedUser.getUsername())
                .build();
    }

    public LoginUserResponseDTO loginUser(LoginUserRequestDTO loginRequest) {
        val user = userRepository.findUserByEmailAddress(loginRequest.getEmailAddress());
        if(user.getStatus().equals(UserStatus.PENDING)) throw new InactiveUserException();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmailAddress(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException exception) {
            throw new InvalidCredentialsException();
        }
        val userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(loginRequest.getEmailAddress());
        val accessToken = jwtUtil.generateToken(userDetails);
        return LoginUserResponseDTO.builder()
                .userId(userDetails.getUserId())
                .accessToken(accessToken)
                .username(userDetails.getUserUsername())
                .emailAddress(userDetails.getUsername())
                .build();
    }

    public BaseResponseDTO activateAccount(ActivateUserRequestDTO activateRequest) {
        val invitation = invitationService.getInvitationById(activateRequest.getInvitationCode());
        if(invitation.getUsed()) {
            throw new InvalidInvitationException();
        }
        val user = invitation.getUser();
        user.setPassword(passwordEncoder.encode(activateRequest.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        invitationService.setInviteUsed(invitation);
        userRepository.save(user);
        return BaseResponseDTO.builder()
                .message("User activated successfully!")
                .build();
    }

    public BaseResponseDTO deleteUser(String userId) {
        invitationService.deleteInvite(userId);
        userRepository.deleteById(userId);
        return BaseResponseDTO.builder()
                .message("User Deleted Successfully!")
                .build();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean adminExists(String adminEmail) {
        val user = userRepository.findUserByEmailAddress(adminEmail);
        return user != null;
    }
}
