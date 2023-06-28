package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;

public class FacultyAlreadyAddedException extends TypicalException {
    public FacultyAlreadyAddedException() {
        super(HttpStatus.BAD_REQUEST);
    }
    @Override
    public String getMessage() {
        return "Данный факультет уже добавлен";
    }
}
