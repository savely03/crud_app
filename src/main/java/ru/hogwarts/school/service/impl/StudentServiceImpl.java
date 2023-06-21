package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;


@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Override
    @Transactional
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(
                () -> new StudentNotFoundException("Данный студент не найден")
        );
    }

    @Override
    public Collection<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional
    public Student updateStudent(Student student) {
        getStudentById(student.getId());
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public Student deleteStudentById(Long id) {
        Student student = getStudentById(id);
        studentRepository.deleteById(id);
        return student;
    }

    @Override
    public Collection<Student> getStudentsByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

}
