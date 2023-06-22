package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final FacultyService facultyService;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public Student createStudent(StudentDto studentDto) {
        Faculty faculty = facultyService.getFacultyById(studentDto.getFacultyId());
        Student student = studentMapper.toEntity(studentDto);
        student.setFaculty(faculty);
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
    public Student updateStudent(StudentDto studentDto) {
        getStudentById(studentDto.getId());
        Faculty faculty = facultyService.getFacultyById(studentDto.getFacultyId());
        Student student = studentMapper.toEntity(studentDto);
        student.setFaculty(faculty);
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

    @Override
    public Collection<Student> getStudentsByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findAllByAgeBetween(minAge, maxAge);

    }

    @Override
    public Faculty getFacultyByStudentId(Long id) {
        return getStudentById(id).getFaculty();
    }
}
