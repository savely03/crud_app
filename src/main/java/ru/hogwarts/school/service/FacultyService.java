package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyService {

    Faculty createFaculty(Faculty faculty);

    Faculty getFacultyById(Long id);

    Collection<Faculty> getFaculties();

    Faculty updateFaculty(Faculty faculty);

    Faculty deleteFacultyById(Long id);

    Collection<Faculty> getFacultiesByColor(String color);

}
