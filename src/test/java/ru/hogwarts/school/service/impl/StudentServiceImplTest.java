package ru.hogwarts.school.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;
import ru.hogwarts.school.test_util.DbTest;

import static org.assertj.core.api.Assertions.*;


@DbTest
@SpringBootTest
class StudentServiceImplTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private StudentDtoIn studentDtoIn;
    private StudentDtoIn studentDtoInSecond;
    private Faculty faculty;


    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
        faculty = facultyRepository.save(Faculty.builder().name("faculty").color("red").build());
        studentDtoIn = StudentDtoIn.builder().name("studentIn").age(20).facultyId(faculty.getId()).build();
        studentDtoInSecond = StudentDtoIn.builder().name("studentInS").age(21).facultyId(faculty.getId()).build();
    }

    @Test
    void createStudentTest() {
        StudentDtoOut studentDtoOut = studentService.createStudent(studentDtoIn);

        assertThat(studentDtoOut)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(studentDtoIn);
        assertThat(studentRepository.count()).isOne();
    }

    @Test
    void createStudentWhenFacultyDoesNotExistTest() {
        studentDtoIn.setFacultyId(0);

        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> studentService.createStudent(studentDtoIn)
        );
    }

    @Test
    void getStudentByIdTest() {
        StudentDtoOut studentDtoOut = studentService.createStudent(studentDtoIn);

        assertThat(studentService.getStudentById(studentDtoOut.getId())).isEqualTo(studentDtoOut);
    }

    @Test
    void getStudentByIdWhenStudentDoesNotExistTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(
                () -> studentService.getStudentById(1L)
        );
    }

    @Test
    void getStudentsTest() {
        StudentDtoOut studentDtoOut = studentService.createStudent(studentDtoIn);

        assertThat(studentService.getStudents()).containsOnly(studentDtoOut);
    }

    @Test
    void updateStudentTest() {
        StudentDtoOut studentDtoOut = studentService.createStudent(studentDtoIn);
        StudentDtoOut studentDtoOutSecond = studentService.updateStudent(studentDtoOut.getId(), studentDtoInSecond);

        assertThat(studentDtoOutSecond.getId()).isEqualTo(studentDtoOut.getId());
        assertThat(studentDtoOutSecond)
                .usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(studentDtoInSecond);
    }

    @Test
    void deleteStudentByIdTest() {
        StudentDtoOut studentDtoOut = studentService.createStudent(studentDtoIn);

        assertThat(studentService.deleteStudentById(studentDtoOut.getId())).isEqualTo(studentDtoOut);
        assertThat(studentRepository.count()).isZero();
    }

    @Test
    void deleteStudentByIdWhenStudentDoesNotExistTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(
                () -> studentService.deleteStudentById(1L)
        );
    }

    @Test
    void getStudentsByAgeTest() {
        StudentDtoOut studentDtoOut = studentService.createStudent(studentDtoIn);
        StudentDtoOut studentDtoOutSecond = studentService.createStudent(studentDtoInSecond);

        assertThat(studentService.getStudentsByAge(studentDtoIn.getAge())).containsOnly(studentDtoOut);
        assertThat(studentService.getStudentsByAge(studentDtoInSecond.getAge())).containsOnly(studentDtoOutSecond);
    }

    @Test
    void getStudentsByAgeBetweenTest() {
        StudentDtoOut studentDtoOut = studentService.createStudent(studentDtoIn);
        StudentDtoOut studentDtoOutSecond = studentService.createStudent(studentDtoInSecond);

        assertThat(studentService.getStudentsByAgeBetween(studentDtoIn.getAge(), studentDtoInSecond.getAge()))
                .containsOnly(studentDtoOut, studentDtoOutSecond);

    }

    @Test
    void getFacultyByStudentIdTest() {
        StudentDtoOut studentDtoOut = studentService.createStudent(studentDtoIn);

        assertThat(studentService.getFacultyByStudentId(studentDtoOut.getId()))
                .usingRecursiveComparison()
                .ignoringFields("students")
                .isEqualTo(faculty);
    }
}