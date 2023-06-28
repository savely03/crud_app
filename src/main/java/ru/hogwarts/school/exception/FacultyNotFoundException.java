package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;

public class FacultyNotFoundException extends TypicalException {
    public FacultyNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
    @Override
    public String getMessage() {
        return "Данный факультет не найден";
    }

}
