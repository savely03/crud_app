package ru.hogwarts.school.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.exception.FacultyAlreadyAddedException;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;
import ru.hogwarts.school.test_util.DbTest;



import static org.assertj.core.api.Assertions.*;


@DbTest
@SpringBootTest
class FacultyServiceImplTest {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    private FacultyDtoIn facultyDtoIn;
    private FacultyDtoIn facultyDtoInSecond;
    private StudentDtoIn studentDtoIn;

    @BeforeEach
    void setUp() {
        facultyRepository.deleteAll();
        studentRepository.deleteAll();
        facultyDtoIn = FacultyDtoIn.builder().name("name1").color("color1").build();
        facultyDtoInSecond = FacultyDtoIn.builder().name("name2").color("color2").build();
        studentDtoIn = StudentDtoIn.builder().name("student").age(21).build();
    }

    @Test
    void createFacultyTest() {
        FacultyDtoOut facultyDtoOut = facultyService.createFaculty(facultyDtoIn);

        assertThat(facultyDtoOut)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(facultyDtoIn);
        assertThat(facultyRepository.count()).isOne();
    }

    @Test
    void createFacultyWhenFacultyAlreadyAdded() {
        facultyService.createFaculty(facultyDtoIn);

        assertThatExceptionOfType(FacultyAlreadyAddedException.class).isThrownBy(
                () -> facultyService.createFaculty(facultyDtoIn)
        );
    }

    @Test
    void getFacultyByIdTest() {
        FacultyDtoOut facultyDtoOut = facultyService.createFaculty(facultyDtoIn);

        assertThat(facultyService.getFacultyById(facultyDtoOut.getId())).isEqualTo(facultyDtoOut);
    }

    @Test
    void getFacultyByIdWhenFacultyDoesNotExist() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> facultyService.getFacultyById(1L)
        );
    }

    @Test
    void getFacultiesTest() {
        FacultyDtoOut facultyDtoOut = facultyService.createFaculty(facultyDtoIn);

        assertThat(facultyService.getFaculties()).containsOnly(facultyDtoOut);
    }

    @Test
    void updateFacultyTest() {
        FacultyDtoOut facultyDtoOut = facultyService.createFaculty(facultyDtoIn);
        FacultyDtoOut facultyDtoOutSecond = facultyService.updateFaculty(facultyDtoOut.getId(), facultyDtoInSecond);

        assertThat(facultyDtoOutSecond.getId()).isEqualTo(facultyDtoOut.getId());
        assertThat(facultyDtoOutSecond)
                .usingRecursiveComparison()
                .ignoringFields("students", "id")
                .isEqualTo(facultyDtoInSecond);
    }

    @Test
    void updateFacultyWhenFacultyDoesNotExist() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> facultyService.deleteFacultyById(1L)
        );
    }

    @Test
    void deleteFacultyByIdTest() {
        FacultyDtoOut facultyDtoOut = facultyService.createFaculty(facultyDtoIn);

        assertThat(facultyService.deleteFacultyById(facultyDtoOut.getId())).isEqualTo(facultyDtoOut);
        assertThat(facultyRepository.count()).isZero();

    }

    @Test
    void deleteFacultyByIdWhenFacultyDoesNotExist() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> facultyService.deleteFacultyById(1L)
        );
    }

    @Test
    void getFacultyByNameOrColorTest() {
        FacultyDtoOut facultyDtoOut = facultyService.createFaculty(facultyDtoIn);

        assertThat(facultyService.getFacultyByNameOrColor(facultyDtoOut.getName(), facultyDtoOut.getColor()))
                .isEqualTo(facultyDtoOut);
    }

    @Test
    void getFacultyByNameOrColorWhenFacultyDoesNotExistTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> facultyService.getFacultyByNameOrColor(facultyDtoIn.getName(), facultyDtoIn.getColor())
        );
    }

    @Test
    void getStudentsByFacultyId() {
        FacultyDtoOut facultyDtoOut = facultyService.createFaculty(facultyDtoIn);
        studentDtoIn.setFacultyId(facultyDtoOut.getId());
        StudentDtoOut studentDtoOut = studentService.createStudent(studentDtoIn);

        assertThat(facultyService.getStudentsByFacultyId(facultyDtoOut.getId())).containsOnly(studentDtoOut);
    }
}