package ru.hogwarts.school.service;

import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.dto.StudentDto;

import java.util.Collection;

public interface StudentService {

    StudentDto createStudent(StudentDto studentDto);

    StudentDto getStudentById(Long id);

    Collection<StudentDto> getStudents();

    StudentDto updateStudent(Long id, StudentDto studentDto);

    StudentDto deleteStudentById(Long id);

    Collection<StudentDto> getStudentsByAge(int age);

    Collection<StudentDto> getStudentsByAgeBetween(int minAge, int maxAge);

    FacultyDto getFacultyByStudentId(Long id);

}
