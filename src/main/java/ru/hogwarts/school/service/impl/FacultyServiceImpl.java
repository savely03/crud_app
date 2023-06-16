package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class FacultyServiceImpl implements FacultyService {

    private final Map<Long, Faculty> faculties;
    private long cntId;

    public FacultyServiceImpl() {
        faculties = new HashMap<>();
    }


    private void checkFacultyById(Long id) {
        if (!faculties.containsKey(id)) {
            throw new FacultyNotFoundException("Данный факультет не найден");
        }
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++cntId);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public Faculty getFacultyById(Long id) {
        checkFacultyById(id);
        return faculties.get(id);
    }

    @Override
    public Collection<Faculty> getFaculties() {
        return faculties.values();
    }

    @Override
    public Faculty updateFaculty(Faculty faculty) {
        checkFacultyById(faculty.getId());
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public Faculty deleteFacultyById(Long id) {
        checkFacultyById(id);
        return faculties.remove(id);
    }

    @Override
    public Collection<Faculty> getFacultiesByColor(String color) {
        return getFaculties()
                .stream()
                .filter(faculty -> Objects.equals(faculty.getColor(), color))
                .collect(Collectors.toList());
    }

}
