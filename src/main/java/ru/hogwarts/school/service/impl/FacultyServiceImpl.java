package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;


@Service
@Transactional(readOnly = true)
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    @Transactional
    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id).orElseThrow(
                () -> new FacultyNotFoundException("Данный факультет не найден")
        );
    }

    @Override
    public Collection<Faculty> getFaculties() {
        return facultyRepository.findAll();
    }

    @Override
    @Transactional
    public Faculty updateFaculty(Faculty faculty) {
        getFacultyById(faculty.getId());
        return facultyRepository.save(faculty);
    }

    @Override
    @Transactional
    public Faculty deleteFacultyById(Long id) {
        Faculty faculty = getFacultyById(id);
        facultyRepository.deleteById(id);
        return faculty;
    }

    @Override
    public Collection<Faculty> getFacultiesByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }

}
