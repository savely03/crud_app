package ru.hogwarts.school.service;

import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {

    Student createStudent(StudentDto studentDto);

    Student getStudentById(Long id);

    Collection<Student> getStudents();

    Student updateStudent(StudentDto studentDto);

    Student deleteStudentById(Long id);

    Collection<Student> getStudentsByAge(int age);

    Collection<Student> getStudentsByAgeBetween(int minAge, int maxAge);

    Faculty getFacultyByStudentId(Long id);
}
