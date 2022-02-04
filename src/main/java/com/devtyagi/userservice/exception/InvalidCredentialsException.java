package com.devtyagi.userservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BaseException {

    public InvalidCredentialsException() {
        super("Invalid Credentials!", HttpStatus.UNAUTHORIZED);
    }

}
