package ru.hogwarts.school.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.hogwarts.school.exceptions.model.CustomError;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public void handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
                                                     HttpServletResponse response) throws IOException {
        log.error("{}: {}", ex.getClass().getName(), ex.getMessage());
        response.sendError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(IllegalArgumentException ex,
                                               HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException ex,
                                                   HttpServletResponse response) throws IOException {
        log.error("{}: {}", ex.getClass().getName(), ex.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(EntryNotFoundException.class)
    public void handleEntryNotFoundException(EntryNotFoundException ex,
                                             HttpServletResponse response) throws IOException {
        log.error("{}: {}", ex.getClass().getName(), ex.getLogMessage());
        response.sendError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                             WebRequest request) {
        log.error("{}: {}", ex.getClass().getName(), ex.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(createCustomBindError(ex, request, status));
    }

    @ExceptionHandler(SQLException.class)
    public void handleSQLException(SQLException ex,
                                   HttpServletResponse response) throws IOException {
        log.error("{}: {}", ex.getClass().getName(), ex.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    private CustomError createCustomBindError(BindException ex,
                                              WebRequest request, HttpStatus status) {
        CustomError error = new CustomError();
        error.setStatus(status.value());
        error.setErrorMessage(status.getReasonPhrase());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String message = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        error.setMessage(message);

        error.setPath(((ServletWebRequest) request).getRequest().getServletPath());

        return error;
    }
}
