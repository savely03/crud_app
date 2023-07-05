package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;

public class PaginationException extends TypicalException {
    public PaginationException() {
        super(HttpStatus.BAD_REQUEST);
    }

    @Override
    public String getMessage() {
        return "Номер и размер страницы должны быть больше 0";
    }

}
