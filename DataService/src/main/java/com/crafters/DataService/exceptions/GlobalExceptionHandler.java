package com.crafters.DataService.exceptions;

import com.crafters.DataService.dtos.ErrorResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ValidationFailureException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(Exception ex) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .type(getCurrentEndpoint())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ex.getMessage())
                .date(new Date())
                .build();

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(Exception ex) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .type(getCurrentEndpoint())
                .status(HttpStatus.NOT_FOUND.value())
                .error(ex.getMessage())
                .date(new Date())
                .build();

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .type(getCurrentEndpoint())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(ex.getMessage())
                .date(new Date())
                .build();

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({SignatureException.class, ExpiredJwtException.class, AccessDeniedExceptionHandle.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponseDTO> handleJWTExpired(Exception ex) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .type(getCurrentEndpoint())
                .status(HttpStatus.FORBIDDEN.value())
                .error(ex.getMessage())
                .date(new Date())
                .build();

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.FORBIDDEN);
    }

    private String getCurrentEndpoint() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String endpoint = request.getRequestURL().toString();
        return endpoint;
    }
}
