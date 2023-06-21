package ru.hogwarts.school.service.impl;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.repository.FacultyRepository;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.hogwarts.school.constants.FacultyConstants.*;

@ExtendWith(MockitoExtension.class)
class FacultyServiceImplTest {

    @Mock
    private FacultyRepository facultyRepository;
    @InjectMocks
    private FacultyServiceImpl out;

    @Test
    void createFacultyTest() {
        when(facultyRepository.save(FACULTY1)).thenReturn(FACULTY1);

        assertThat(out.createFaculty(FACULTY1)).isEqualTo(FACULTY1);
    }

    @Test
    void getFacultyByIdTest() {
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(FACULTY1));

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
        when(facultyRepository.findAll()).thenReturn(List.of(FACULTY1, FACULTY2));

        assertThat(out.getFaculties()).containsExactly(FACULTY1, FACULTY2).hasSize(2);
    }

    @Test
    void updateFacultyTest() {
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(FACULTY1));
        when(facultyRepository.save(FACULTY2)).thenReturn(FACULTY2);

        assertThat(out.updateFaculty(FACULTY2)).isEqualTo(FACULTY2);
    }

    @Test
    void updateFacultyWhenFacultyDoesNotExistTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> out.updateFaculty(FACULTY1)
        );

        verify(facultyRepository, times(0)).save(FACULTY1);
    }

    @Test
    void deleteFacultyByIdTest() {
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(FACULTY1));

        assertThat(out.deleteFacultyById(FACULTY1.getId())).isEqualTo(FACULTY1);

        verify(facultyRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteFacultyByIdWhenFacultyDoesNotExistTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(
                () -> out.deleteFacultyById(FACULTY1.getId())
        );

        verify(facultyRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void getFacultiesByColorTest() {
        when(facultyRepository.findAllByColor(FACULTY1.getColor())).thenReturn(Collections.singletonList(FACULTY1));
        when(facultyRepository.findAllByColor(FACULTY2.getColor())).thenReturn(Collections.singletonList(FACULTY2));

        assertThat(out.getFacultiesByColor(FACULTY1.getColor())).hasSize(1).containsOnly(FACULTY1);
        assertThat(out.getFacultiesByColor(FACULTY2.getColor())).hasSize(1).containsOnly(FACULTY2);
    }
}