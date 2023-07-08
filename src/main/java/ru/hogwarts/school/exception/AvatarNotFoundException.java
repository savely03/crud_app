package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;

public class AvatarNotFoundException extends TypicalException {
    public AvatarNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "Данный аватар не найден";
    }
}
