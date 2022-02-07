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

    @PostMapping(Endpoints.UserAPI.CREATE_USER)
    public User createUser(@RequestBody @Valid CreateUserRequestDTO createUserRequest) {
        return userService.createUser(createUserRequest);
    }

    @PostMapping(Endpoints.AuthAPI.LOGIN_USER)
    public LoginUserResponseDTO loginUser(@RequestBody @Valid LoginUserRequestDTO loginRequest) {
        return userService.loginUser(loginRequest);
    }

    @PutMapping(Endpoints.UserAPI.ACTIVATE_USER)
    public LoginUserResponseDTO activateUser(@RequestBody ActivateUserRequestDTO activateUserRequestDTO) {
        return userService.activateAccount(activateUserRequestDTO);
    }

    @DeleteMapping(Endpoints.UserAPI.DELETE_USER)
    public BaseResponseDTO deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }

    @GetMapping(Endpoints.UserAPI.GET_USERS)
    public List<User> getAllUsers(
            @RequestParam(defaultValue = "fullName") String sort,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return userService.getAllUsers(sort, order);
    }

}
