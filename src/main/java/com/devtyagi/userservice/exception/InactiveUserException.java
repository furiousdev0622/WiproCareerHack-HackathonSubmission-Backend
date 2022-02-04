package com.devtyagi.userservice.exception;

import org.springframework.http.HttpStatus;

public class InactiveUserException extends BaseException{
    public InactiveUserException() {
        super("The user account has not been activated yet!", HttpStatus.BAD_REQUEST);
    }
}
