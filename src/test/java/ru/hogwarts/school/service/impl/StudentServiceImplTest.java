package ru.hogwarts.school.service.impl;

import org.junit.jupiter.api.Test;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.service.StudentService;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static ru.hogwarts.school.constants.FacultyConstants.FACULTY1;
import static ru.hogwarts.school.constants.StudentConstants.*;

class StudentServiceImplTest {

    private final StudentService out = new StudentServiceImpl();

    @Test
    void createFacultyTest() {
        int initialSize = out.getStudents().size();

        assertThat(out.createStudent(STUDENT1)).isEqualTo(STUDENT1);
        assertThat(out.getStudents()).hasSize(initialSize + 1);
    }

    @Test
    void getFacultyByIdTest() {
        out.createStudent(STUDENT1);

        assertThat(out.getStudentById(STUDENT1.getId())).isEqualTo(STUDENT1);
    }

    @Test
    void getFacultyByIdWhenFacultyDoesNotExistTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(
                () -> out.getStudentById(FACULTY1.getId())
        );
    }

    @Test
    void getFacultiesTest() {
        out.createStudent(STUDENT1);

        assertThat(out.getStudents()).containsOnly(STUDENT1).hasSize(1);
    }

    @Test
    void updateFacultyTest() {
        out.createStudent(STUDENT1);

        assertThat(out.updateStudent(STUDENT2)).isEqualTo(STUDENT2);
        assertThat(out.getStudents()).contains(STUDENT2);
    }

    @Test
    void updateFacultyWhenFacultyDoesNotExistTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(
                () -> out.updateStudent(STUDENT1)
        );
    }

    @Test
    void deleteFacultyByIdTest() {
        out.createStudent(STUDENT1);
        int initialSize = out.getStudents().size();

        assertThat(out.deleteStudentById(STUDENT1.getId())).isEqualTo(STUDENT1);
        assertThat(out.getStudents()).hasSize(initialSize - 1);
    }

    @Test
    void deleteFacultyByIdWhenFacultyDoesNotExistTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(
                () -> out.deleteStudentById(STUDENT1.getId())
        );
    }

    @Test
    void getStudentsByAge() {
        out.createStudent(STUDENT1);
        out.createStudent(STUDENT2);

        assertThat(out.getStudentsByAge(STUDENT1.getAge())).hasSize(1).containsOnly(STUDENT1);
        assertThat(out.getStudentsByAge(STUDENT2.getAge())).hasSize(1).containsOnly(STUDENT2);
    }
}