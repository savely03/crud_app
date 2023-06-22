package ru.hogwarts.school.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.test_util.DbTest;


import java.util.Collection;

import static org.assertj.core.api.Assertions.*;


@DbTest
@ContextConfiguration(classes = FacultyContextConfig.class)
class FacultyServiceImplTest {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultyService facultyService;

    private Faculty facultyOne;
    private Faculty facultyTwo;

    @BeforeEach
    void setUp() {
        facultyRepository.deleteAll();
        facultyOne = Faculty.builder().id(0L).name("name1").color("color1").build();
        facultyTwo = Faculty.builder().id(0L).name("name2").color("color2").build();
    }

    @Test
    void createFacultyTest() {
        assertThat(facultyService.createFaculty(facultyOne))
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(facultyOne);
        assertThat(facultyRepository.count()).isOne();
    }

    @Test
    void getFacultyByIdTest() {
        facultyService.createFaculty(facultyOne);

        assertThat(facultyService.getFacultyById(facultyOne.getId())).isEqualTo(facultyOne);
    }

    @Test
    void getFacultyByIdWhenFacultyDoesNotExist() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> facultyService.getFacultyById(facultyOne.getId())
        );
    }

    @Test
    void getFacultiesTest() {
        facultyRepository.save(facultyOne);
        facultyRepository.save(facultyTwo);

        assertThat(facultyService.getFaculties()).contains(facultyOne, facultyTwo);
    }

    @Test
    void updateFacultyTest() {
        facultyRepository.save(facultyOne);

        facultyTwo.setId(facultyOne.getId());

        assertThat(facultyService.updateFaculty(facultyTwo)).isEqualTo(facultyTwo);
        assertThat(facultyService.getFacultyById(facultyTwo.getId())).isNotEqualTo(facultyOne);
    }

    @Test
    void updateFacultyWhenFacultyDoesNotExist() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> facultyService.deleteFacultyById(facultyOne.getId())
        );
    }

    @Test
    void deleteFacultyByIdTest() {
        facultyRepository.save(facultyOne);

        assertThat(facultyService.deleteFacultyById(facultyOne.getId())).isEqualTo(facultyOne);
        assertThat(facultyRepository.count()).isZero();

    }

    @Test
    void deleteFacultyByIdWhenFacultyDoesNotExist() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> facultyService.deleteFacultyById(facultyOne.getId())
        );
    }

    @Test
    void getFacultyByNameOrColorTest() {
        Faculty faculty = facultyRepository.save(facultyOne);

        assertThat(facultyService.getFacultyByNameOrColor(facultyOne.getName(), facultyOne.getColor())).isEqualTo(faculty);
    }

    @Test
    void getFacultyByNameOrColorWhenFacultyDoesNotExistTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> facultyService.getFacultyByNameOrColor(facultyOne.getName(), facultyTwo.getColor())
        );
    }

    @Test
    @Disabled
    void getStudentsByFacultyId() {
        Student student = Student.builder().id(0L).name("student").age(20).build();
        facultyOne.addStudent(student);
        Faculty faculty = facultyRepository.save(facultyOne);

        Collection<Student> students = facultyService.getStudentsByFacultyId(faculty.getId());
        assertThat(students).containsOnly(student);
    }
}