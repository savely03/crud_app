package ru.hogwarts.school.service;

import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;

import java.util.Collection;

public interface StudentService {

    StudentDtoOut createStudent(StudentDtoIn studentDtoIn);

    StudentDtoOut getStudentById(Long id);

    Collection<StudentDtoOut> getStudents();

    StudentDtoOut updateStudent(Long id, StudentDtoIn studentDtoIn);

    StudentDtoOut deleteStudentById(Long id);

    Collection<StudentDtoOut> getStudentsByAge(int age);

    Collection<StudentDtoOut> getStudentsByAgeBetween(int minAge, int maxAge);

    FacultyDtoOut getFacultyByStudentId(Long id);
}
