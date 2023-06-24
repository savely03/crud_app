package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;
    private final FacultyRepository facultyRepository;


    @Override
    @Transactional
    public StudentDtoOut createStudent(StudentDtoIn studentDtoIn) {
        return facultyRepository.findById(studentDtoIn.getFacultyId()).map(
                        f -> {
                            Student student = studentMapper.toEntity(studentDtoIn);
                            student.setFaculty(f);
                            return studentMapper.toDto(studentRepository.save(student));
                        })
                .orElseThrow(FacultyNotFoundException::new);
    }

    @Override
    public StudentDtoOut getStudentById(Long id) {
        return studentMapper.toDto(studentRepository.findById(id).orElseThrow(StudentNotFoundException::new));
    }

    @Override
    public Collection<StudentDtoOut> getStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentDtoOut updateStudent(Long id, StudentDtoIn studentDtoIn) {
        return studentRepository.findById(id)
                .map(s -> {
                    s.setName(studentDtoIn.getName());
                    s.setAge(studentDtoIn.getAge());
                    s.setFaculty(facultyRepository.findById(studentDtoIn.getFacultyId())
                            .orElseThrow(FacultyNotFoundException::new));
                    return studentMapper.toDto(studentRepository.save(s));
                })
                .orElseThrow(StudentNotFoundException::new);
    }

    @Override
    @Transactional
    public StudentDtoOut deleteStudentById(Long id) {
        StudentDtoOut studentDtoOut = getStudentById(id);
        studentRepository.deleteById(id);
        return studentDtoOut;
    }

    @Override
    public Collection<StudentDtoOut> getStudentsByAge(int age) {
        return studentRepository.findAllByAge(age).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<StudentDtoOut> getStudentsByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findAllByAgeBetween(minAge, maxAge).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FacultyDtoOut getFacultyByStudentId(Long id) {
        return facultyMapper.toDto(studentRepository.findById(id)
                .map(Student::getFaculty)
                .orElseThrow(StudentNotFoundException::new));
    }
}
