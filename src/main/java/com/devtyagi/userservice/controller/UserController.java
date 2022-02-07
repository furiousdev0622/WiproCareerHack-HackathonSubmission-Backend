package com.devtyagi.userservice.controller;

import com.devtyagi.userservice.constants.Endpoints;
import com.devtyagi.userservice.dao.User;
import com.devtyagi.userservice.dto.request.ActivateUserRequestDTO;
import com.devtyagi.userservice.dto.request.CreateUserRequestDTO;
import com.devtyagi.userservice.dto.request.LoginUserRequestDTO;
import com.devtyagi.userservice.dto.response.BaseResponseDTO;
import com.devtyagi.userservice.dto.response.LoginUserResponseDTO;
import com.devtyagi.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(Endpoints.BASE_URL)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * This method serves as an Endpoint to create  a new User.
     * By default, the user's status will be "PENDING" and the new user will receive a unique Invitation Code on the provided email.
     *
     * Only LEVEL2 or LEVEL3 role users can create a new user account!
     *
     * @param createUserRequest contains the required fields for User Creation.
     * @return The newly created User Profile.
     */
    @PostMapping(Endpoints.UserAPI.CREATE_USER)
    public User createUser(@RequestBody @Valid CreateUserRequestDTO createUserRequest) {
        return userService.createUser(createUserRequest);
    }

    /**
     * This method is used to log in to the application and create a JWT token for further access.
     * @param loginRequest contains email and password of the User requesting login.
     * @return A DTO that contains the Logged In User's profile along with the JWT token.
     */
    @PostMapping(Endpoints.AuthAPI.LOGIN_USER)
    public LoginUserResponseDTO loginUser(@RequestBody @Valid LoginUserRequestDTO loginRequest) {
        return userService.loginUser(loginRequest);
    }

    /**
     * This method is used to activate a new account, it changes the status from 'PENDING' to 'ACTIVE'
     * and sets the user's password for the first time.
     *
     * Only a LEVEL3 user can delete any other user profile!
     *
     * @param activateUserRequestDTO contains the unique invite code and the new password for the user.
     * @return A DTO that contains the activated User's profile along with the JWT token.
     */
    @PutMapping(Endpoints.UserAPI.ACTIVATE_USER)
    public LoginUserResponseDTO activateUser(@RequestBody ActivateUserRequestDTO activateUserRequestDTO) {
        return userService.activateAccount(activateUserRequestDTO);
    }

    /**
     * This method is used to permanently delete a user's account.
     *
     * @param id The USER's ID that is to be deleted.
     * @return A success message.
     */
    @DeleteMapping(Endpoints.UserAPI.DELETE_USER)
    public BaseResponseDTO deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }

    /**
     * This method is used to fetch all users from the database.
     * The users can be sorted based on either of the following:
     * ["fullName", "username", "emailAddress", "status", "role"].
     * By default the results are sorted in Ascending order of fullName.
     * @param sort A String specifying the field on which the data is to be sorted.
     * @param order Can be either "asc" or "desc", specifies the order of sorting.
     * @return
     */
    @GetMapping(Endpoints.UserAPI.GET_USERS)
    public List<User> getAllUsers(
            @RequestParam(defaultValue = "fullName") String sort,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return userService.getAllUsers(sort, order);
    }

}
