package ru.hogwarts.school.service;

import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.dto.StudentDto;

import java.util.Collection;

public interface FacultyService {

    FacultyDto createFaculty(FacultyDto facultyDto);

    FacultyDto getFacultyById(Long id);

    Collection<FacultyDto> getFaculties();

    FacultyDto updateFaculty(Long id, FacultyDto facultyDto);

    FacultyDto deleteFacultyById(Long id);

    FacultyDto getFacultyByNameAndColor(String name, String color);

    Collection<StudentDto> getStudentsByFacultyId(Long id);

    String findLongestFacultyName();

}
