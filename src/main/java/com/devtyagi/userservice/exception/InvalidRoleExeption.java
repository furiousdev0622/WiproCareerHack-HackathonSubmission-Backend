package com.devtyagi.userservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidRoleExeption extends BaseException{

    public InvalidRoleExeption() {
        super("Invalid Role", HttpStatus.BAD_REQUEST);
    }

}
