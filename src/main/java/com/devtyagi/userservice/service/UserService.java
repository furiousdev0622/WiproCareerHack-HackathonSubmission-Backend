package com.devtyagi.userservice.service;

import com.devtyagi.userservice.dao.User;
import com.devtyagi.userservice.dto.request.ActivateUserRequestDTO;
import com.devtyagi.userservice.dto.request.CreateUserRequestDTO;
import com.devtyagi.userservice.dto.request.LoginUserRequestDTO;
import com.devtyagi.userservice.dto.response.BaseResponseDTO;
import com.devtyagi.userservice.dto.response.LoginUserResponseDTO;
import com.devtyagi.userservice.enums.UserStatus;
import com.devtyagi.userservice.exception.InactiveUserException;
import com.devtyagi.userservice.exception.InvalidCredentialsException;
import com.devtyagi.userservice.exception.InvalidInvitationException;
import com.devtyagi.userservice.exception.UserAlreadyExistsException;
import com.devtyagi.userservice.repository.UserRepository;
import com.devtyagi.userservice.util.CustomUserDetails;
import com.devtyagi.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    public User createUser(CreateUserRequestDTO createUserRequest) {
        val userEmail = createUserRequest.getEmailAddress();
        val userCheckInDB = userRepository.findUserByEmailAddress(userEmail);
        if(userCheckInDB != null) {
            throw new UserAlreadyExistsException();
        }
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

        return savedUser;
    }

    public LoginUserResponseDTO loginUser(LoginUserRequestDTO loginRequest) {
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
        if(userDetails.getUserStatus().equals(UserStatus.PENDING)) throw new InactiveUserException();
        val accessToken = jwtUtil.generateToken(userDetails);
        return LoginUserResponseDTO.builder()
                .userId(userDetails.getUserId())
                .accessToken(accessToken)
                .username(userDetails.getUserUsername())
                .emailAddress(userDetails.getUsername())
                .role(userDetails.getRole())
                .build();
    }

    public LoginUserResponseDTO activateAccount(ActivateUserRequestDTO activateRequest) {
        val invitation = invitationService.getInvitationById(activateRequest.getInvitationCode());
        if(invitation.getUsed()) {
            throw new InvalidInvitationException();
        }
        val user = invitation.getUser();
        user.setPassword(passwordEncoder.encode(activateRequest.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        invitationService.setInviteUsed(invitation);
        userRepository.save(user);
        return loginUser(
                LoginUserRequestDTO.builder()
                .emailAddress(user.getEmailAddress())
                .password(activateRequest.getPassword())
                .build()
        );
    }

    public BaseResponseDTO deleteUser(String userId) {
        invitationService.deleteInvite(userId);
        userRepository.deleteById(userId);
        return BaseResponseDTO.builder()
                .message("User Deleted Successfully!")
                .build();
    }

    public List<User> getAllUsers(String sortOn, String order) {
        val sortField = validateSortOn(sortOn);
        val sortOrder = order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return userRepository.findAll(Sort.by(sortOrder, sortField));
    }

    private String validateSortOn(String sort) {
        if(Set.of("fullName", "username", "emailAddress", "status", "role").contains(sort)) {
            return sort;
        }
        return "fullName";
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean adminExists(String adminEmail) {
        val user = userRepository.findUserByEmailAddress(adminEmail);
        return user != null;
    }
}
