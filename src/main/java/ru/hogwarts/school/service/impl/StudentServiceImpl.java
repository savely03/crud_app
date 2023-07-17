package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);


    @Override
    @Transactional
    public StudentDto createStudent(StudentDto studentDto) {
        logger.info("Was invoked method for creating student");
        return facultyRepository.findById(studentDto.getFacultyId()).map(
                        f -> {
                            Student student = studentMapper.toEntity(studentDto);
                            student.setId(0L);
                            student.setFaculty(f);
                            return studentMapper.toDto(studentRepository.save(student));
                        })
                .orElseThrow(() -> {
                    logger.warn("Faculty with id - {} doesn't exist", studentDto.getFacultyId());
                    return new FacultyNotFoundException();
                });
    }

    @Override
    public StudentDto getStudentById(Long id) {
        logger.info("Was invoked method for getting student by id");
        return studentMapper.toDto(studentRepository.findById(id).orElseThrow(() -> {
            logger.warn("Student with id - {} doesn't exist", id);
            return new StudentNotFoundException();
        }));
    }

    @Override
    public Collection<StudentDto> getStudents() {
        logger.info("Was invoked method for getting students");
        return studentRepository.findAll().stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        logger.info("Was invoked method for updating student");
        return studentRepository.findById(id)
                .map(s -> {
                    s.setName(studentDto.getName());
                    s.setAge(studentDto.getAge());
                    s.setFaculty(facultyRepository.findById(studentDto.getFacultyId())
                            .orElseThrow(FacultyNotFoundException::new));
                    return studentMapper.toDto(studentRepository.save(s));
                })
                .orElseThrow(() -> {
                    logger.warn("Student with id - {} doesn't exist", id);
                    return new StudentNotFoundException();
                });
    }

    @Override
    @Transactional
    public StudentDto deleteStudentById(Long id) {
        logger.info("Was invoked method for deleting student");
        StudentDto studentDto = getStudentById(id);
        studentRepository.deleteById(id);
        return studentDto;
    }

    @Override
    public Collection<StudentDto> getStudentsByAge(int age) {
        logger.info("Was invoked method for getting student by age");
        return studentRepository.findAllByAge(age).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<StudentDto> getStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for getting student by age between {} and {}", minAge, maxAge);
        return studentRepository.findAllByAgeBetween(minAge, maxAge).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FacultyDto getFacultyByStudentId(Long id) {
        logger.info("Was invoked method for getting faculty by student id");
        return facultyMapper.toDto(studentRepository.findById(id)
                .map(Student::getFaculty)
                .orElseThrow(() -> {
                    logger.warn("Student with id - {} doesn't exist", id);
                    return new StudentNotFoundException();
                }));
    }

    @Override
    public int getCountOfStudents() {
        logger.info("Was invoked method for getting count of students");
        return studentRepository.getCountOfStudents();
    }

    @Override
    public double getAvgAgeOfStudents() {
        logger.info("Was invoked method for getting average age of students");
        return studentRepository.getAvgAgeOfStudents();
    }

    @Override
    public Collection<StudentDto> getLastFiveStudents() {
        logger.info("Was invoked method for getting last five students");
        return studentRepository.getLastFiveStudents()
                .stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }
}
