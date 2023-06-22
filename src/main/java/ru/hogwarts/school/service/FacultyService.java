package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface FacultyService {

    Faculty createFaculty(Faculty faculty);

    Faculty getFacultyById(Long id);

    Collection<Faculty> getFaculties();

    Faculty updateFaculty(Faculty faculty);

    Faculty deleteFacultyById(Long id);

    Faculty getFacultyByNameOrColor(String name, String color);

    Collection<Student> getStudentsByFacultyId(Long id);

}
