package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.exception.FacultyAlreadyAddedException;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    @Override
    @Transactional
    public Faculty createFaculty(Faculty faculty) {
        if (facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(faculty.getName(), faculty.getColor()).isPresent()) {
            throw new FacultyAlreadyAddedException("Факультет с таким именем или цветом уже добавлен");
        }
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
    public Faculty getFacultyByNameOrColor(String name, String color) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color).orElseThrow(
                () -> new FacultyNotFoundException("Данный факультет не найден")
        );
    }

    @Override
    public Collection<Student> getStudentsByFacultyId(Long id) {
        return getFacultyById(id).getStudents();
    }

}
