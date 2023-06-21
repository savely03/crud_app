package ru.hogwarts.school.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.repository.StudentRepository;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;
import static ru.hogwarts.school.constants.FacultyConstants.FACULTY1;
import static ru.hogwarts.school.constants.StudentConstants.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentServiceImpl out;

    @Test
    void createStudentTest() {
        when(studentRepository.save(STUDENT1)).thenReturn(STUDENT1);

        assertThat(out.createStudent(STUDENT1)).isEqualTo(STUDENT1);
    }

    @Test
    void getStudentByIdTest() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(STUDENT1));

        assertThat(out.getStudentById(STUDENT1.getId())).isEqualTo(STUDENT1);
    }

    @Test
    void getStudentByIdWhenStudentDoesNotExistTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(
                () -> out.getStudentById(FACULTY1.getId())
        );
    }

    @Test
    void getStudentsTest() {
        when(studentRepository.findAll()).thenReturn(List.of(STUDENT1, STUDENT2));

        assertThat(out.getStudents()).containsExactly(STUDENT1, STUDENT2).hasSize(2);
    }

    @Test
    void updateStudentTest() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(STUDENT1));
        when(studentRepository.save(STUDENT2)).thenReturn(STUDENT2);

        assertThat(out.updateStudent(STUDENT2)).isEqualTo(STUDENT2);
    }

    @Test
    void updateStudentWhenStudentDoesNotExistTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(
                () -> out.updateStudent(STUDENT1)
        );

        verify(studentRepository, times(0)).save(STUDENT1);
    }

    @Test
    void deleteStudentByIdTest() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(STUDENT1));

        assertThat(out.deleteStudentById(STUDENT1.getId())).isEqualTo(STUDENT1);
        verify(studentRepository, times(1)).deleteById(STUDENT1.getId());
    }

    @Test
    void deleteStudentByIdWhenStudentDoesNotExistTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(
                () -> out.deleteStudentById(STUDENT1.getId())
        );

        verify(studentRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void getStudentsByAge() {
        when(studentRepository.findAllByAge(STUDENT1.getAge())).thenReturn(List.of(STUDENT1));
        when(studentRepository.findAllByAge(STUDENT2.getAge())).thenReturn(List.of(STUDENT2));

        assertThat(out.getStudentsByAge(STUDENT1.getAge())).hasSize(1).containsOnly(STUDENT1);
        assertThat(out.getStudentsByAge(STUDENT2.getAge())).hasSize(1).containsOnly(STUDENT2);
    }
}