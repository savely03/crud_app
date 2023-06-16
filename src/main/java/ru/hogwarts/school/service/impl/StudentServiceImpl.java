package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class StudentServiceImpl implements StudentService {

    private final Map<Long, Student> students;


    private long cntId;

    public StudentServiceImpl() {
        students = new HashMap<>();
    }

    private void checkStudentById(Long id) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException("Данный студент не найден");
        }
    }

    @Override
    public Student createStudent(Student student) {
        student.setId(++cntId);
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public Student getStudentById(Long id) {
        checkStudentById(id);
        return students.get(id);
    }

    @Override
    public Collection<Student> getStudents() {
        return students.values();
    }

    @Override
    public Student updateStudent(Student student) {
        checkStudentById(student.getId());
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public Student deleteStudentById(Long id) {
        checkStudentById(id);
        return students.remove(id);
    }

    @Override
    public Collection<Student> getStudentsByAge(int age) {
        return getStudents()
                .stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

}
