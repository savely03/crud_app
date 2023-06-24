package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.exception.FacultyAlreadyAddedException;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public FacultyDtoOut createFaculty(FacultyDtoIn facultyDtoIn) {
        if (facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(facultyDtoIn.getName(), facultyDtoIn.getColor()).isPresent()) {
            throw new FacultyAlreadyAddedException();
        }
        return facultyMapper.toDto(facultyRepository.save(facultyMapper.toEntity(facultyDtoIn)));
    }

    @Override
    public FacultyDtoOut getFacultyById(Long id) {
        return facultyMapper.toDto(facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new));
    }

    @Override
    public Collection<FacultyDtoOut> getFaculties() {
        return facultyRepository.findAll().stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FacultyDtoOut updateFaculty(Long id, FacultyDtoIn facultyDtoIn) {
        return facultyRepository.findById(id).map(
                        f -> {
                            f.setName(facultyDtoIn.getName());
                            f.setColor(facultyDtoIn.getColor());
                            return facultyMapper.toDto(facultyRepository.save(f));
                        })
                .orElseThrow(FacultyNotFoundException::new);
    }

    @Override
    @Transactional
    public FacultyDtoOut deleteFacultyById(Long id) {
        FacultyDtoOut facultyDtoOut = getFacultyById(id);
        facultyRepository.deleteById(id);
        return facultyDtoOut;
    }

    @Override
    public FacultyDtoOut getFacultyByNameOrColor(String name, String color) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color)
                .map(facultyMapper::toDto)
                .orElseThrow(FacultyNotFoundException::new);
    }

    @Override
    public Collection<StudentDtoOut> getStudentsByFacultyId(Long id) {
        return facultyRepository.findById(id).map(
                        faculty -> faculty.getStudents().stream()
                                .map(studentMapper::toDto)
                                .collect(Collectors.toList()))
                .orElseThrow(FacultyNotFoundException::new);
    }
}
