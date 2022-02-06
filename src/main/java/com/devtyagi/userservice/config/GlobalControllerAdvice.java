package com.devtyagi.userservice.config;

import com.devtyagi.userservice.dto.response.BaseResponseDTO;
import com.devtyagi.userservice.exception.BaseException;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ BaseException.class })
    public ResponseEntity<BaseResponseDTO> exceptionHandler(final BaseException exception) {
        val response = BaseResponseDTO.builder()
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, exception.getResponseHttpStatus());
    }

    @ExceptionHandler({ UsernameNotFoundException.class })
    public ResponseEntity<BaseResponseDTO> userNotFoundExceptionHandler(final UsernameNotFoundException usernameNotFoundException) {
        val response = BaseResponseDTO.builder()
                .message(usernameNotFoundException.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ ExpiredJwtException.class })
    public ResponseEntity<BaseResponseDTO> expiredJwtExceptionHandler(final ExpiredJwtException expiredJwtException) {
        val response = BaseResponseDTO.builder()
                .message("token_expired")
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ SignatureException.class })
    public ResponseEntity<BaseResponseDTO> invalidTokenException(final SignatureException signatureException) {
        val response = BaseResponseDTO.builder()
                .message(signatureException.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ MalformedJwtException.class })
    public ResponseEntity<BaseResponseDTO> malformedJwtExceptionHandler(final MalformedJwtException malformedJwtException) {
        val response = BaseResponseDTO.builder()
                .message("invalid_token")
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

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
