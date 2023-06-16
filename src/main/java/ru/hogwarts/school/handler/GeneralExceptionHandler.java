package ru.hogwarts.school.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.hogwarts.school.dto.ResponseDto;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(FacultyNotFoundException.class)
    public ResponseEntity<ResponseDto> handleFacultyNotFoundException(FacultyNotFoundException e) {
        ResponseDto response = new ResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ResponseDto> handleStudentNotFoundException(StudentNotFoundException e) {
        ResponseDto response = new ResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
