package com.devtyagi.userservice.config;

import com.devtyagi.userservice.dto.response.BaseResponseDTO;
import com.devtyagi.userservice.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * Handles all custom raised exceptions that are children of BaseException.
     * The following type of exceptions are handled by this method
     * 1. {@link InactiveUserException}
     * 2. {@link InvalidCredentialsException}
     * 3. {@link InvalidInvitationException}
     * 4. {@link InvalidRoleException}
     * 5. {@link UserAlreadyExistsException}
     * @param exception Any exception that is an instance of BaseException or a sub-class of BaseException.
     * @return A response entity with the Exception Message and HTTP Status.
     */
    @ExceptionHandler({ BaseException.class })
    public ResponseEntity<BaseResponseDTO> exceptionHandler(final BaseException exception) {
        val response = BaseResponseDTO.builder()
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, exception.getResponseHttpStatus());
    }

    /**
     * This method handles the exception thrown a User tries to log in but does not exists in the Database.
     * @param usernameNotFoundException Exception class provided by Spring Security.
     * @return A response entity with the Exception Message and 404 HTTP Status.
     */
    @ExceptionHandler({ UsernameNotFoundException.class })
    public ResponseEntity<BaseResponseDTO> userNotFoundExceptionHandler(final UsernameNotFoundException usernameNotFoundException) {
        val response = BaseResponseDTO.builder()
                .message(usernameNotFoundException.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * This method handles the exception that is thrown if a protected API endpoint request is made
     * with an expired JWT Token.
     * @param expiredJwtException Exception class provided by io.jsonwebtoken
     * @return A response entity with the Exception Message and 401 HTTP Status.
     */
    @ExceptionHandler({ ExpiredJwtException.class })
    public ResponseEntity<BaseResponseDTO> expiredJwtExceptionHandler(final ExpiredJwtException expiredJwtException) {
        val response = BaseResponseDTO.builder()
                .message("token_expired")
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * This method handles the exception that is thrown if a protected API endpoint request is made
     * and the JWT signature does not match locally computed signature.
     * @param signatureException Exception class provided by io.jsonwebtoken
     * @return A response entity with the Exception Message and 401 HTTP Status.
     */
    @ExceptionHandler({ SignatureException.class })
    public ResponseEntity<BaseResponseDTO> invalidTokenException(final SignatureException signatureException) {
        val response = BaseResponseDTO.builder()
                .message(signatureException.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * This method handles the exception that is thrown if a protected API endpoint request is made
     * with an Invalid JWT Token.
     * @param malformedJwtException Exception class provided by io.jsonwebtoken
     * @return A response entity with the Exception Message and 401 HTTP Status.
     */
    @ExceptionHandler({ MalformedJwtException.class })
    public ResponseEntity<BaseResponseDTO> malformedJwtExceptionHandler(final MalformedJwtException malformedJwtException) {
        val response = BaseResponseDTO.builder()
                .message("invalid_token")
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * This method handles the API Validation Errors as per configuration.
     * @return A JSON Object with the fields that failed Validations and the error message associated with them.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
