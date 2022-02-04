package com.devtyagi.userservice.config;

import com.devtyagi.userservice.dto.response.BaseResponseDTO;
import com.devtyagi.userservice.exception.BaseException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

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

}
