package ru.hogwarts.school.service;

import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoOut;

import java.util.Collection;

public interface FacultyService {

    FacultyDtoOut createFaculty(FacultyDtoIn facultyDtoIn);

    FacultyDtoOut getFacultyById(Long id);

    Collection<FacultyDtoOut> getFaculties();

    FacultyDtoOut updateFaculty(Long id, FacultyDtoIn facultyDtoIn);

    FacultyDtoOut deleteFacultyById(Long id);

    FacultyDtoOut getFacultyByNameOrColor(String name, String color);

    Collection<StudentDtoOut> getStudentsByFacultyId(Long id);

}
