package ru.hogwarts.school.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.hogwarts.school.dto.ErrorResponse;
import ru.hogwarts.school.exception.TypicalException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler(TypicalException.class)
    public ResponseEntity<ErrorResponse> handleStudentNotFoundException(TypicalException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                fieldErrors.stream()
                        .map(fieldError -> new ErrorResponse(fieldError.getDefaultMessage()))
                        .collect(Collectors.toList())
        );
    }
}
