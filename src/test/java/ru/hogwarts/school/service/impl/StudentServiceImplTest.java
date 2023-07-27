package ru.hogwarts.school.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;
import ru.hogwarts.school.test_util.DbTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


@DbTest
@SpringBootTest
class StudentServiceImplTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;


    private StudentDto studentDto;
    private StudentDto studentDtoSecond;
    private Faculty faculty;


    @BeforeEach
    void setUp() {
        faculty = facultyRepository.save(Faculty.builder().name("faculty").color("red").build());
        studentDto = StudentDto.builder().name("studentIn").age(20).facultyId(faculty.getId()).build();
        studentDtoSecond = StudentDto.builder().name("studentInS").age(21).facultyId(faculty.getId()).build();
    }

    @Test
    void createStudentTest() {
        StudentDto studentDtoOut = studentService.createStudent(studentDto);

        assertThat(studentDtoOut)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(studentDto);
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
        StudentDto studentDtoOut = studentService.createStudent(studentDto);

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
        StudentDto studentDtoOut = studentService.createStudent(studentDto);

        assertThat(studentService.getStudents()).containsOnly(studentDtoOut);
    }

    @Test
    void updateStudentTest() {
        StudentDto studentDtoOut = studentService.createStudent(studentDto);
        StudentDto studentDtoOutSecond = studentService.updateStudent(studentDtoOut.getId(), studentDtoSecond);

        assertThat(studentDtoOutSecond.getId()).isEqualTo(studentDtoOut.getId());
        assertThat(studentDtoOutSecond)
                .usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(studentDtoSecond);
    }

    @Test
    void deleteStudentByIdTest() {
        StudentDto studentDtoOut = studentService.createStudent(studentDto);

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
        StudentDto studentDtoOut = studentService.createStudent(studentDto);
        StudentDto studentDtoOutSecond = studentService.createStudent(studentDtoSecond);

        assertThat(studentService.getStudentsByAge(studentDto.getAge())).containsOnly(studentDtoOut);
        assertThat(studentService.getStudentsByAge(studentDtoSecond.getAge())).containsOnly(studentDtoOutSecond);
    }

    @Test
    void getStudentsByAgeBetweenTest() {
        StudentDto studentDtoOut = studentService.createStudent(studentDto);
        StudentDto studentDtoOutSecond = studentService.createStudent(studentDtoSecond);

        assertThat(studentService.getStudentsByAgeBetween(studentDto.getAge(), studentDtoSecond.getAge()))
                .containsOnly(studentDtoOut, studentDtoOutSecond);

    }

    @Test
    void getFacultyByStudentIdTest() {
        StudentDto studentDtoOut = studentService.createStudent(studentDto);

        assertThat(studentService.getFacultyByStudentId(studentDtoOut.getId()))
                .usingRecursiveComparison()
                .ignoringFields("students")
                .isEqualTo(faculty);
    }
    @AfterEach
    void cleanUp() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }
}