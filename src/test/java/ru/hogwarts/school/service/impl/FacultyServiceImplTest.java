package ru.hogwarts.school.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.dto.StudentDto;
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

    private FacultyDto facultyDto;
    private FacultyDto facultyDtoSecond;
    private StudentDto studentDto;

    @BeforeEach
    void setUp() {
        facultyRepository.deleteAll();
        studentRepository.deleteAll();
        facultyDto = FacultyDto.builder().name("name1").color("color1").build();
        facultyDtoSecond = FacultyDto.builder().name("name2").color("color2").build();
        studentDto = StudentDto.builder().name("student").age(21).build();
    }

    @Test
    void createFacultyTest() {
        FacultyDto facultyDtoOut = facultyService.createFaculty(facultyDto);

        assertThat(facultyDtoOut)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(facultyDto);
        assertThat(facultyRepository.count()).isOne();
    }

    @Test
    void createFacultyWhenFacultyAlreadyAdded() {
        facultyService.createFaculty(facultyDto);

        assertThatExceptionOfType(FacultyAlreadyAddedException.class).isThrownBy(
                () -> facultyService.createFaculty(facultyDto)
        );
    }

    @Test
    void getFacultyByIdTest() {
        FacultyDto facultyDtoOut = facultyService.createFaculty(facultyDto);

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
        FacultyDto facultyDtoOut = facultyService.createFaculty(facultyDto);

        assertThat(facultyService.getFaculties()).containsOnly(facultyDtoOut);
    }

    @Test
    void updateFacultyTest() {
        FacultyDto facultyDtoOut = facultyService.createFaculty(facultyDto);
        FacultyDto facultyDtoOutSecond = facultyService.updateFaculty(facultyDtoOut.getId(), facultyDtoSecond);

        assertThat(facultyDtoOutSecond.getId()).isEqualTo(facultyDtoOut.getId());
        assertThat(facultyDtoOutSecond)
                .usingRecursiveComparison()
                .ignoringFields("students", "id")
                .isEqualTo(facultyDtoSecond);
    }

    @Test
    void updateFacultyWhenFacultyDoesNotExist() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> facultyService.deleteFacultyById(1L)
        );
    }

    @Test
    void deleteFacultyByIdTest() {
        FacultyDto facultyDtoOut = facultyService.createFaculty(facultyDto);

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
        FacultyDto facultyDtoOut = facultyService.createFaculty(facultyDto);

        assertThat(facultyService.getFacultyByNameOrColor(facultyDtoOut.getName(), facultyDtoOut.getColor()))
                .isEqualTo(facultyDtoOut);
    }

    @Test
    void getFacultyByNameOrColorWhenFacultyDoesNotExistTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> facultyService.getFacultyByNameOrColor(facultyDto.getName(), facultyDto.getColor())
        );
    }

    @Test
    void getStudentsByFacultyId() {
        FacultyDto facultyDtoOut = facultyService.createFaculty(facultyDto);
        studentDto.setFacultyId(facultyDtoOut.getId());
        StudentDto studentDtoOut = studentService.createStudent(studentDto);

        assertThat(facultyService.getStudentsByFacultyId(facultyDtoOut.getId())).containsOnly(studentDtoOut);
    }
}