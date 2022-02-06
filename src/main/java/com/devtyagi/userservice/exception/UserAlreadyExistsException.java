package com.devtyagi.userservice.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BaseException{
    public UserAlreadyExistsException() {
        super("User with given email already exists!", HttpStatus.BAD_REQUEST);
    }
}
