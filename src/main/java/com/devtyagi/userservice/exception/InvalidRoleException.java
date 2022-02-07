package com.devtyagi.userservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidRoleException extends BaseException{

    public InvalidRoleException() {
        super("Invalid Role", HttpStatus.BAD_REQUEST);
    }

}
