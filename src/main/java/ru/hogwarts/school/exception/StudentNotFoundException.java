package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;

public class StudentNotFoundException extends TypicalException {
    public StudentNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
    @Override
    public String getMessage() {
        return "Данный студент не существует";
    }

}
