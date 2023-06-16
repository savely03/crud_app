package ru.hogwarts.school.service.impl;


import org.junit.jupiter.api.Test;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.service.FacultyService;


import static org.assertj.core.api.Assertions.*;
import static ru.hogwarts.school.constants.FacultyConstants.*;

class FacultyServiceImplTest {

    private final FacultyService out = new FacultyServiceImpl();

    @Test
    void createFacultyTest() {
        int initialSize = out.getFaculties().size();

        assertThat(out.createFaculty(FACULTY1)).isEqualTo(FACULTY1);
        assertThat(out.getFaculties()).hasSize(initialSize + 1);
    }

    @Test
    void getFacultyByIdTest() {
        out.createFaculty(FACULTY1);

        assertThat(out.getFacultyById(FACULTY1.getId())).isEqualTo(FACULTY1);
    }

    @Test
    void getFacultyByIdWhenFacultyDoesNotExistTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> out.getFacultyById(FACULTY1.getId())
        );
    }

    @Test
    void getFacultiesTest() {
        out.createFaculty(FACULTY1);

        assertThat(out.getFaculties()).containsOnly(FACULTY1).hasSize(1);
    }

    @Test
    void updateFacultyTest() {
        out.createFaculty(FACULTY1);

        assertThat(out.updateFaculty(FACULTY2)).isEqualTo(FACULTY2);
        assertThat(out.getFaculties()).contains(FACULTY2);
    }

    @Test
    void updateFacultyWhenFacultyDoesNotExistTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> out.updateFaculty(FACULTY1)
        );
    }

    @Test
    void deleteFacultyByIdTest() {
        out.createFaculty(FACULTY1);
        int initialSize = out.getFaculties().size();

        assertThat(out.deleteFacultyById(FACULTY1.getId())).isEqualTo(FACULTY1);
        assertThat(out.getFaculties()).hasSize(initialSize - 1);
    }

    @Test
    void deleteFacultyByIdWhenFacultyDoesNotExistTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> out.deleteFacultyById(FACULTY1.getId())
        );
    }

    @Test
    void getFacultiesByColorTest() {
        out.createFaculty(FACULTY1);
        out.createFaculty(FACULTY2);

        assertThat(out.getFacultiesByColor(FACULTY1.getColor())).hasSize(1).containsOnly(FACULTY1);
        assertThat(out.getFacultiesByColor(FACULTY2.getColor())).hasSize(1).containsOnly(FACULTY2);
    }
}