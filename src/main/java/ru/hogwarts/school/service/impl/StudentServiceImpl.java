package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.dto.StudentDto;
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
    public StudentDto createStudent(StudentDto studentDto) {
        return facultyRepository.findById(studentDto.getFacultyId()).map(
                        f -> {
                            Student student = studentMapper.toEntity(studentDto);
                            student.setId(0L);
                            student.setFaculty(f);
                            return studentMapper.toDto(studentRepository.save(student));
                        })
                .orElseThrow(FacultyNotFoundException::new);
    }

    @Override
    public StudentDto getStudentById(Long id) {
        return studentMapper.toDto(studentRepository.findById(id).orElseThrow(StudentNotFoundException::new));
    }

    @Override
    public Collection<StudentDto> getStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        return studentRepository.findById(id)
                .map(s -> {
                    s.setName(studentDto.getName());
                    s.setAge(studentDto.getAge());
                    s.setFaculty(facultyRepository.findById(studentDto.getFacultyId())
                            .orElseThrow(FacultyNotFoundException::new));
                    return studentMapper.toDto(studentRepository.save(s));
                })
                .orElseThrow(StudentNotFoundException::new);
    }

    @Override
    @Transactional
    public StudentDto deleteStudentById(Long id) {
        StudentDto studentDto = getStudentById(id);
        studentRepository.deleteById(id);
        return studentDto;
    }

    @Override
    public Collection<StudentDto> getStudentsByAge(int age) {
        return studentRepository.findAllByAge(age).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<StudentDto> getStudentsByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findAllByAgeBetween(minAge, maxAge).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FacultyDto getFacultyByStudentId(Long id) {
        return facultyMapper.toDto(studentRepository.findById(id)
                .map(Student::getFaculty)
                .orElseThrow(StudentNotFoundException::new));
    }
}
