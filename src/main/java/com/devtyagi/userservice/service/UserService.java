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

    /**
     * This method created a new user account and saves into Database.
     * All fields required for user creation are passed in CreateUserRequestDTO.
     * Once the account is saved, it triggers the Send Invite flow.
     *
     * @throws UserAlreadyExistsException if a user with given email already exists.
     * @return The saved user's profile.
     */
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

    /**
     * This method tries to log in with the provided credentials and
     * returns JWT and other details if login is successful.
     * @param loginRequest A DTO containing Email and Password.
     * @return A DTO containing JWT along with other user details.
     */
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

    /**
     * This method is used to update the user's profile.
     * 1. Sets a new Password for the User
     * 2. Change status from 'PENDING' to 'ACTIVE'.
     * @throws InvalidInvitationException if the provided InviteCode is invalid.
     * @param activateRequest A DTO containing the new password and invitation code.
     * @return A DTO containing JWT along with other user details.
     */
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

    /**
     * This method is used to Delete a user's account by ID.
     * @param userId ID of the user to be deleted.
     */
    public BaseResponseDTO deleteUser(String userId) {
        invitationService.deleteInvite(userId);
        userRepository.deleteById(userId);
        return BaseResponseDTO.builder()
                .message("User Deleted Successfully!")
                .build();
    }

    /**
     * This method fetches all users that exist in the database and returns as a list.
     * The resultant user list is sorted on the basis of the provided pramaters.
     * @param sortOn The field on which Sorting has to be applied.
     * @param order The direction of sorting (Ascending / Descening)
     * @return A List of Users.
     */
    public List<User> getAllUsers(String sortOn, String order) {
        val sortField = validateSortOn(sortOn);
        val sortOrder = order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return userRepository.findAll(Sort.by(sortOrder, sortField));
    }

    /**
     * This method is used to validate the name of the field on which
     * sorting has to be applied.
     *
     * If the provided sort field name is invalid, it returns "fullName" so that
     * the user list will be sorted on basis of Full Name.
     *
     * @param sort Name of the field
     * @return
     */
    private String validateSortOn(String sort) {
        if(Set.of("fullName", "username", "emailAddress", "status", "role").contains(sort)) {
            return sort;
        }
        return "fullName";
    }

    /**
     * This method saves a user into the database.
     * @param user User to be saved.
     * @return Saved User's profile.
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * This method checks if the user with Admin Email exists.
     * @param adminEmail The email of admin user.
     * @return A boolean value indicating if admin user exists.
     */
    public boolean adminExists(String adminEmail) {
        val user = userRepository.findUserByEmailAddress(adminEmail);
        return user != null;
    }
}
