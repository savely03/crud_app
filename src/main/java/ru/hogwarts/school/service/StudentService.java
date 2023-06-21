package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {

    Student createStudent(Student student);

    Student getStudentById(Long id);

    Collection<Student> getStudents();

    Student updateStudent(Student student);

    Student deleteStudentById(Long id);

    Collection<Student> getStudentsByAge(int age);
}
