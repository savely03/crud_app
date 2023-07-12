package ru.hogwarts.school.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.hogwarts.school.dto.ErrorResponse;
import ru.hogwarts.school.exception.TypicalException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GeneralExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    @ExceptionHandler(TypicalException.class)
    public ResponseEntity<ErrorResponse> handleStudentNotFoundException(TypicalException e) {
        logger.error(e.getMessage());
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                e.getFieldErrors().stream()
                        .map(fieldError -> new ErrorResponse(fieldError.getDefaultMessage()))
                        .collect(Collectors.toList())
        );
    }
}
