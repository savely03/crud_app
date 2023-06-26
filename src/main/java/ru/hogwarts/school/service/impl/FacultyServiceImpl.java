package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.dto.StudentDto;
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
    public FacultyDto createFaculty(FacultyDto facultyDto) {
        if (facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(facultyDto.getName(), facultyDto.getColor()).isPresent()) {
            throw new FacultyAlreadyAddedException();
        }
        facultyDto.setId(0L);
        return facultyMapper.toDto(facultyRepository.save(facultyMapper.toEntity(facultyDto)));
    }

    @Override
    public FacultyDto getFacultyById(Long id) {
        return facultyMapper.toDto(facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new));
    }

    @Override
    public Collection<FacultyDto> getFaculties() {
        return facultyRepository.findAll().stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FacultyDto updateFaculty(Long id, FacultyDto facultyDto) {
        return facultyRepository.findById(id).map(
                        f -> {
                            f.setName(facultyDto.getName());
                            f.setColor(facultyDto.getColor());
                            return facultyMapper.toDto(facultyRepository.save(f));
                        })
                .orElseThrow(FacultyNotFoundException::new);
    }

    @Override
    @Transactional
    public FacultyDto deleteFacultyById(Long id) {
        FacultyDto facultyDto = getFacultyById(id);
        facultyRepository.deleteById(id);
        return facultyDto;
    }

    @Override
    public FacultyDto getFacultyByNameOrColor(String name, String color) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color)
                .map(facultyMapper::toDto)
                .orElseThrow(FacultyNotFoundException::new);
    }

    @Override
    public Collection<StudentDto> getStudentsByFacultyId(Long id) {
        return facultyRepository.findById(id).map(
                        faculty -> faculty.getStudents().stream()
                                .map(studentMapper::toDto)
                                .collect(Collectors.toList()))
                .orElseThrow(FacultyNotFoundException::new);
    }
}
