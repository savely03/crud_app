package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.dto.FacultyDto;
import ru.hogwarts.school.dto.StudentDto;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.exception.FacultyAlreadyAddedException;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;
    private final StudentMapper studentMapper;

    private final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    @Override
    @Transactional
    public FacultyDto createFaculty(FacultyDto facultyDto) {
        logger.info("Was invoked method for creating faculty");
        if (facultyRepository.findByNameIgnoreCaseAndColorIgnoreCase(facultyDto.getName(), facultyDto.getColor()).isPresent()) {
            logger.warn("Faculty with name - {} and color - {} already added", facultyDto.getName(), facultyDto.getColor());
            throw new FacultyAlreadyAddedException();
        }
        facultyDto.setId(0L);
        return facultyMapper.toDto(facultyRepository.save(facultyMapper.toEntity(facultyDto)));
    }

    @Override
    public FacultyDto getFacultyById(Long id) {
        logger.info("Was invoked method for getting faculty by id");
        return facultyMapper.toDto(facultyRepository.findById(id).orElseThrow(() -> {
            logger.warn("Faculty with id - {} doesn't exist", id);
            return new FacultyNotFoundException();
        }));
    }

    @Override
    public Collection<FacultyDto> getFaculties() {
        logger.info("Was invoked method for getting faculty by id");
        return facultyRepository.findAll().stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FacultyDto updateFaculty(Long id, FacultyDto facultyDto) {
        logger.info("Was invoked method for updating faculty");
        return facultyRepository.findById(id).map(
                        f -> {
                            f.setName(facultyDto.getName());
                            f.setColor(facultyDto.getColor());
                            return facultyMapper.toDto(facultyRepository.save(f));
                        })
                .orElseThrow(() -> {
                    logger.warn("Faculty with id - {} doesn't exist", id);
                    return new FacultyNotFoundException();
                });
    }

    @Override
    @Transactional
    public FacultyDto deleteFacultyById(Long id) {
        logger.info("Was invoked method for deleting faculty");
        FacultyDto facultyDto = getFacultyById(id);
        facultyRepository.deleteById(id);
        return facultyDto;
    }

    @Override
    public FacultyDto getFacultyByNameAndColor(String name, String color) {
        logger.info("Was invoked method for getting faculty by name and color");
        return facultyRepository.findByNameIgnoreCaseAndColorIgnoreCase(name, color)
                .map(facultyMapper::toDto)
                .orElseThrow(() -> {
                    logger.warn("Faculty with name - {} and color - {} doesn't exist", name, color);
                    return new FacultyNotFoundException();
                });
    }

    @Override
    public Collection<StudentDto> getStudentsByFacultyId(Long id) {
        logger.info("Was invoked method for getting students by faculty id");
        return facultyRepository.findById(id).map(
                        faculty -> faculty.getStudents().stream()
                                .map(studentMapper::toDto)
                                .collect(Collectors.toList()))
                .orElseThrow(() -> {
                    logger.warn("Faculty with id - {} doesn't exist", id);
                    return new FacultyNotFoundException();
                });
    }

    @Override
    public String findLongestFacultyName() {
        logger.info("Was invoked method for getting longest faculty name");
        return facultyRepository.findAll()
                .parallelStream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
    }
}
