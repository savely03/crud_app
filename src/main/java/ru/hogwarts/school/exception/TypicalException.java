package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;

public abstract class TypicalException extends RuntimeException {
    private final HttpStatus httpStatus;

    protected TypicalException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
