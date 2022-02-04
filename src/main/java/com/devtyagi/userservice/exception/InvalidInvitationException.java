package com.devtyagi.userservice.exception;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class InvalidInvitationException extends BaseException {
    public InvalidInvitationException() {
        super("Invalid Invite Code", HttpStatus.BAD_REQUEST);
    }
}
