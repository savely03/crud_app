package ru.hogwarts.school.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;
import ru.hogwarts.school.test_util.DbTest;

import static org.assertj.core.api.Assertions.*;


@DbTest
@ContextConfiguration(classes = StudentContextConfig.class)
class StudentServiceImplTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private Student studentOne;
    private Student studentTwo;
    private StudentDto studentDto;
    private Faculty faculty;


    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
        faculty = facultyRepository.save(Faculty.builder().name("faculty").color("red").build());
        studentOne = Student.builder().id(0L).name("student1").age(20).faculty(faculty).build();
        studentTwo = Student.builder().id(0L).name("student2").age(25).faculty(faculty).build();
        studentDto = StudentDto.builder().id(0L).name("studentDto").age(26).facultyId(faculty.getId()).build();
    }

    @Test
    void createStudentTest() {
        Student student = studentService.createStudent(studentDto);

        assertThat(student.getAge()).isEqualTo(studentDto.getAge());
        assertThat(student.getName()).isEqualTo(studentDto.getName());
        assertThat(student.getFaculty()).isEqualTo(faculty);
        assertThat(studentRepository.count()).isOne();
    }

    @Test
    void createStudentWhenFacultyDoesNotExistTest() {
        studentDto.setFacultyId(0);

        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> studentService.createStudent(studentDto)
        );
    }

    @Test
    void getStudentByIdTest() {
        studentRepository.save(studentOne);

        assertThat(studentService.getStudentById(studentOne.getId())).isEqualTo(studentOne);
    }

    @Test
    void getStudentByIdWhenStudentDoesNotExistTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(
                () -> studentService.getStudentById(studentOne.getId())
        );
    }

    @Test
    void getStudentsTest() {
        studentRepository.save(studentOne);
        studentRepository.save(studentTwo);

        assertThat(studentService.getStudents()).contains(studentOne, studentTwo);
    }

    @Test
    void updateStudentTest() {
        studentRepository.save(studentOne);
        studentDto.setId(studentOne.getId());

        Student student = studentService.updateStudent(studentDto);
        assertThat(student).usingRecursiveComparison().ignoringFields("faculty").isEqualTo(studentDto);
        assertThat(student.getFaculty().getId()).isEqualTo(studentDto.getFacultyId());
        assertThat(studentService.getFacultyByStudentId(studentOne.getId())).isNotEqualTo(studentOne);
    }

    @Test
    void deleteStudentByIdTest() {
        studentRepository.save(studentOne);

        assertThat(studentService.deleteStudentById(studentOne.getId())).isEqualTo(studentOne);
        assertThat(studentRepository.count()).isZero();
    }

    @Test
    void deleteStudentByIdWhenStudentDoesNotExistTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(
                () -> studentService.deleteStudentById(studentOne.getId())
        );
    }

    @Test
    void getStudentsByAgeTest() {
        studentRepository.save(studentOne);
        studentRepository.save(studentTwo);

        assertThat(studentService.getStudentsByAge(studentOne.getAge())).contains(studentOne);
        assertThat(studentService.getStudentsByAge(studentTwo.getAge())).contains(studentTwo);
    }

    @Test
    void getStudentsByAgeBetweenTest() {
        studentRepository.save(studentOne);
        studentRepository.save(studentTwo);

        assertThat(studentService.getStudentsByAgeBetween(studentOne.getAge(), studentTwo.getAge()))
                .containsOnly(studentOne, studentTwo);
    }

    @Test
    void getFacultyByStudentIdTest() {
        studentRepository.save(studentOne);

        assertThat(studentService.getFacultyByStudentId(studentOne.getId())).isEqualTo(faculty);
    }
}